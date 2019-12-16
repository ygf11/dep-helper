package ygf.mvn.plugin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class DependenciesMojoTest {

    /**
     * mojo
     */
    private DependenciesMojo mojo;

    @Before
    public void setup() {
        mojo = new DependenciesMojo();
        String str = "java.jar";

    }

    @Test
    public void test(){
        String str = "java.jar|.class";
        Assert.assertTrue(str.endsWith(".class"));
    }
}
