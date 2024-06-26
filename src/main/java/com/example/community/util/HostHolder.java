package com.example.community.util;

import com.example.community.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，用于代替session
 */

@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUsers(User user){
        users.set(user);
    }
    public User getUsers(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}
