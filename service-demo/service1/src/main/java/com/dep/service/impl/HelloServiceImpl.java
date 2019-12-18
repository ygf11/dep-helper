package com.dep.service.impl;

import com.dep.service.impl.api.HelloService;

public class HelloServiceImpl  implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("in service 1");
    }
}
