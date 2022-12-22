package com.example.demo;

import com.example.demo.user.Position;
import com.example.demo.user.User;
import com.example.demo.user.dao.UserDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UserDao userDao) {
        return args -> {
            userDao.save(new User("username1", Position.OPERATIONS));
            userDao.save(new User("username2", Position.MANAGER));
        };
    }
}
