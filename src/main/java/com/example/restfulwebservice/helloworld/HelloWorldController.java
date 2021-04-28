package com.example.restfulwebservice.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController //@Controller + @ResponseBody, View를 갖지 않는 Rest Data(JSON/XML)를 반환
public class HelloWorldController {

    @Autowired
    private MessageSource messageSource;

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

    @GetMapping(path="hello-world-internationalized")
    public String helloWorldInternationalized(@RequestHeader(name="Accept-Language", required = false) Locale locale) {
        return messageSource.getMessage("greeting.message", null, locale);
    }
}
