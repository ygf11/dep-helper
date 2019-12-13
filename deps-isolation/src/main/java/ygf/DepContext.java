package ygf;

import com.dep.service.impl.api.HelloService;

public class DepContext {
    HelloService helloService = new HelloService() {
        @Override
        public void sayHello() {
            System.out.println("test");
        }
    };

    public DepContext(){
        helloService.sayHello();
    }

    public static void main(String[] args){
        DepContext context = new DepContext();
    }
}
