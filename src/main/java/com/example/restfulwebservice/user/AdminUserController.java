package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
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
    public MappingJacksonValue retrieveAllUsers(){

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(" MyResponseHeader", "MyValue");
        var result = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(result);
        mapping.setFilters(filters);

        return mapping;
    }

    //@GetMapping(value="/users/{id}", headers = "X-API-VERISON=1")
    @GetMapping(value="/users/{id}", produces = "application/vnd.company.appv1+json")
    //@GetMapping(value="/users/{id}/", params = "version=1")
    public MappingJacksonValue retrieveUser(@PathVariable(value="id") int id) {
        User user = service.findOne(id);
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping(value="/users/{id}", produces = "application/vnd.company.appv2+json")
    //@GetMapping(value="/users/{id}", headers = "X-API-VERISON=2")
    //@GetMapping(value="/users/{id}/", params="version=2") //http://localhost:8088/admin/users/1/?version=2
    public MappingJacksonValue retrieveUserV2(@PathVariable(value="id") int id) {
        User user = service.findOne(id);

        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        //User -> User2
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2); //id, name, joinDate, password, ssn
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
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
