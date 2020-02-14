package ygf.deps.example;

import com.dep.service.impl.api.HelloService;
import ygf.deps.isolation.DepContext;

/**
 * example
 *
 * @author yanggaofeng
 * @date 20200214
 */
public class ExampleMain {

    public static void main(String[] args) throws Exception{
        Class<?>  service1 = DepContext.getClass(
                "com.dep.service.impl.HelloServiceImpl", "service1-1.0-SNAPSHOT.jar");

        Class<?> service2 = DepContext.getClass(
                "com.dep.service.impl.HelloServiceImpl", "service2-1.0-SNAPSHOT.jar");

        HelloService first = (HelloService) service1.getConstructor().newInstance();
        HelloService second = (HelloService) service2.getConstructor().newInstance();

        first.sayHello();
        second.sayHello();
    }
}
