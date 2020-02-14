package ygf.isolation;

import ygf.deps.service.api.HelloService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ygf.deps.isolation.DepClassLoader;

@RunWith(JUnit4.class)
public class LoadClassTest {

    @Test
    public void loadClassSuccessTest1() throws Exception{
        DepClassLoader classLoader = new DepClassLoader("service1-1.0-SNAPSHOT.jar");
        Class<?> cz = classLoader.loadClass("ygf.deps.service.impl.HelloServiceImpl");

        Assert.assertNotNull(cz);

        HelloService service = (HelloService) cz.newInstance();
        service.sayHello();
    }

    @Test
    public void loadClassSuccessTest2() throws Exception{
        DepClassLoader classLoader = new DepClassLoader("service2-1.0-SNAPSHOT.jar");
        Class<?> cz = classLoader.loadClass("ygf.deps.service.impl.HelloServiceImpl");

        Assert.assertNotNull(cz);

        HelloService service = (HelloService) cz.newInstance();
        service.sayHello();


        DepClassLoader classLoader1 = new DepClassLoader("service1-1.0-SNAPSHOT.jar");
        Class<?> cz1 = classLoader1.loadClass("ygf.deps.service.impl.HelloServiceImpl");

        Assert.assertNotNull(cz1);

        HelloService service1 = (HelloService) cz1.newInstance();
        service1.sayHello();
    }

}