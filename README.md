# Dep-helper
Dep-helper is a lib which can be used to resolve dependency conflicts. 

# Features
* simple and light
* maven plugin support

# How to build
## build deps-isolation
```
    cd deps-isolation
    mvn clean install
```

## build deps maven plugin
```
    cd deps-helper-maven-plugin
    mvn clean install
```

# How to use
## add deps-isolation to pom
```
    <dependency>
        <groupId>ygf.deps</groupId>
        <artifactId>deps-isolation</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```

## add deps-mevn-plugin to pom
```
    <plugin>
        <groupId>ygf.deps</groupId>
        <artifactId>dep-helper-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <dependencies>
                <dependency>com.dep.service:service1:1.0-SNAPSHOT</dependency>
                <dependency>com.dep.service:service2:1.0-SNAPSHOT</dependency>
            </dependencies>
        </configuration>
        <executions>
            <execution>
                <id>dep</id>
                <phase>compile</phase>
                <goals>
                    <goal>build</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
```

the goal of this plugin is `build`, and you can add dependencies to dependency tag which you want to isolate.

## get target class
```
    Class<?>  service1 = DepContext.getClass(
                "com.dep.service.impl.HelloServiceImpl", "service1-1.0-SNAPSHOT.jar");
    HelloService first = (HelloService) service1.getConstructor().newInstance();
    first.sayHello();
```

`DepContext.getClass()` has two parameters:
1. the full name of target `class`
2. the name of `jar` which belongs 


## do something with the target class
