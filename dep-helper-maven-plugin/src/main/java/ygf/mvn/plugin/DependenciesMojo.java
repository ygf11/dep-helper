package ygf.mvn.plugin;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import ygf.mvn.plugin.Exception.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

@Mojo(name = "build")
public class DependenciesMojo extends AbstractMojo {

    /**
     * dependency dir
     */
    private static final String DEP_DIR = "dep-helper";

    @Parameter(property = "dependencies", required = true)
    private List<String> dependencies;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject mavenProject;

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession mavenSession;

    @Component
    private RepositorySystem repositorySystem;

    @Parameter(defaultValue = "${project.remotePluginRepositories}", readonly = true, required = true)
    private List<RemoteRepository> remoteRepositories;

    @Parameter(defaultValue = "${repositorySystemSession}")
    private RepositorySystemSession repositorySystemSession;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (dependencies.isEmpty()) {
            getLog().info("dependencies config is empty");
            return;
        }

        getLog().info("Checking dependencies config");

        checkConfig(dependencies);

        List<Artifact> artifacts = new ArrayList<>();
        for (String dependency : dependencies) {
            Artifact artifact = getDependencyFile(dependency);
            artifacts.add(artifact);
        }

        zipConfigDeps(artifacts);
    }

    private void checkConfig(List<String> dependencyList) {
        Set<String> depSet = new HashSet<>();
        for (String dependency : dependencyList) {
            String[] posArray = dependency.split(":");
            if (posArray.length != 3) {
                throw new DepFormatErrorException("err dependency format: " + dependency);
            }

            if (depSet.contains(dependency)) {
                throw new SameDependencyException("same dependency in config:" + dependency);
            }

            depSet.add(dependency);
        }
    }

    private Artifact getDependencyFile(String dependency) {
        Artifact artifact = new DefaultArtifact(dependency);
        ArtifactRequest request = new ArtifactRequest();
        request.setArtifact(artifact);
        request.setRepositories(remoteRepositories);


        getLog().info("Resolving artifact: " + dependency + " from "
                + remoteRepositories);

        ArtifactResult result;
        try {
            result = repositorySystem.resolveArtifact(repositorySystemSession, request);
        } catch (ArtifactResolutionException e) {
            throw new ResolveDepErrorException("get dependency file from repository failed: ", e);
        }

        if (result.isMissing()) {
            getLog().error("dependency:" + dependency +
                    " is not present in any of the given repositories");
            throw new ResolveDepErrorException("dependency:" + dependency +
                    " is not present in any of the given repositories");
        }

        getLog().info("Resolving artifact: " + dependency + " from "
                + remoteRepositories + " success");

        return result.getArtifact();
    }

    private void zipConfigDeps(List<Artifact> artifactList) {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes()
                .put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try {
            createDepDir();
            JarOutputStream jarOutputStream = new JarOutputStream(
                    new FileOutputStream("dep-helper/deps.jar"), manifest);

            for (Artifact artifact : artifactList) {
                File file = artifact.getFile();
                FileInputStream inputStream = new FileInputStream(file);
                writeToJar(inputStream, jarOutputStream, file.getName());
                inputStream.close();
            }

            jarOutputStream.finish();
            jarOutputStream.close();
        } catch (IOException e) {
            getLog().error("write file to deps.jar failed", e);
            throw new WriteFileFailedException("write file to deps.jar failed:", e);
        }
    }

    private void writeToJar(FileInputStream fileInputStream,
                            JarOutputStream jarOutputStream, String fileName) throws IOException {
        int bytesRead;
        byte[] buffer = new byte[1024];

        JarEntry entry = new JarEntry(fileName);
        jarOutputStream.putNextEntry(entry);

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            jarOutputStream.write(buffer, 0, bytesRead);
        }

        jarOutputStream.flush();
        fileInputStream.close();
    }

    private void createDepDir() {
        File dir = new File(DEP_DIR);

        if (!dir.exists()) {
            boolean success = dir.mkdir();
            if (!success) {
                throw new CreateDepDirException(
                        "create dep-helper dir fail");
            }
        }

    }

}
