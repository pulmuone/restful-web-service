package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService service;

    @Autowired
    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    /*
    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }
     */

    @GetMapping("/users")
    public ResponseEntity<List<User>> retrieveAllUsers(){

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", "MyValue");
        var result = service.findAll();
        return new ResponseEntity<List<User>>(result, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable(value="id") int id) {
        User user = service.findOne(id);
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        return user;
    }

    /*
    @PostMapping("/users")
    // @RequestBody : Client에서 json이나 object으로 받을 경우 사용함.
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        System.out.println("location = " + location);
        
        return ResponseEntity.created(location).build();
    }
     */

    @PostMapping("/users")
    // @RequestBody : Client에서 json이나 object으로 받을 경우 사용함.
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        responseHeaders.set("MyResponseHeader", "MyValue");

        return  ResponseEntity.created(location).headers(responseHeaders).body("OK");
        //return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }

    //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html
    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable int id) {
        User savedUser = service.update(user, id);

        if(savedUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
