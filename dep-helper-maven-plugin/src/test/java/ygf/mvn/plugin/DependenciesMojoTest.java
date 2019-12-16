package ygf.mvn.plugin;

import org.junit.Before;
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
    }
}
