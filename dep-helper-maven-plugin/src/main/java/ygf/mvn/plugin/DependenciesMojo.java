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
import ygf.mvn.plugin.Exception.DepFormatErrorException;
import ygf.mvn.plugin.Exception.ResolveDepErrorException;
import ygf.mvn.plugin.Exception.SameDependencyException;
import ygf.mvn.plugin.Exception.WriteFileFailedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

@Mojo(name = "build")
public class DependenciesMojo extends AbstractMojo {

    @Parameter(property = "deps", required = true)
    private List<String> deps;

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
        System.out.println("in deps helper.");
        deps.forEach(System.out::println);
    }

    private void checkConfigedDeps(List<String> dependencyList) {
        Set<String> depSet = new HashSet<>();
        for (String dependency : dependencyList) {
            String[] posArray = dependency.split(":");
            if (posArray.length != 3) {
                throw new DepFormatErrorException("err dependency format: " + dependency);
            }

            if (depSet.contains(dependency)){
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
            throw new ResolveDepErrorException("dependency:" + dependency +
                    " is not present in any of the given repositories");
        }

        getLog().info("Resolving artifact: " + dependency + " from "
                + remoteRepositories + " success");

        return result.getArtifact();
    }

    private void zipConfigedDeps(List<Artifact> artifactList) {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes()
                .put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try {
            JarOutputStream jarOutputStream = new JarOutputStream(
                    new FileOutputStream("src/main/resources/deps.jar"), manifest);

            for (Artifact artifact : artifactList) {
                File file = artifact.getFile();
                FileInputStream inputStream = new FileInputStream(file);
                writeToJar(inputStream, jarOutputStream, file.getName());
            }

        } catch (IOException e) {
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
    }
}
