package com.example.restfulwebservice.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController //@Controller + @ResponseBody, View를 갖지 않는 Rest Data(JSON/XML)를 반환
public class HelloWorldController {
    //GET
    //hello-world(endpoint)
    //Requestmapping(method=RequestMethod,GET, path="") 예전 방식
    @GetMapping(path="/hello-world")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping(path="/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello World");
    }

    @GetMapping(path="/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable(value="name") String name) {
        return new HelloWorldBean(String.format("Hello World, %s ", name));
    }
}