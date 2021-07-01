package com.example.restfulwebservice.user;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    private static int userCount = 3;
    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User(1, "gwise", new Date(), "pass1", "770530"));
        users.add(new User(2, "hongkidong", new Date(), "pass2", "770531"));
        users.add(new User(3, "eric", new Date(), "pass3", "770529"));
    }

    public List<User> findAll() {
        return this.users;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++userCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(Integer id) {
        for(User user : users) {
            if(user.getId() == id) {
                return user;
            }
        }
        return null;

        //return users.stream().filter(t-> t.getId() == id).findFirst().orElseGet(null);
    }

    public User deleteById(int id) {
        Iterator<User> iterator =  users.iterator();

        while(iterator.hasNext()) {
            User user = iterator.next();
            if(user.getId() == id) {
                iterator.remove();
                return user;
            }
        }
        return null;
    }

    //delete, insert
    public User update(User user, int id) {
        User deleteUser = deleteById(id);
        User saveUser = new User();

        if(deleteUser == null) {
            return null;
        } else {
            saveUser = save(user);
        }

        return saveUser;
    }
}
