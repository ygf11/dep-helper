package ygf.deps.service.impl;

import ygf.deps.service.api.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("in service 2");
    }
}
