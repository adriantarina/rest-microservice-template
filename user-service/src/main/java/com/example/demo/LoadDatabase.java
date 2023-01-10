package com.example.demo;

import com.example.demo.user.Position;
import com.example.demo.user.User;
import com.example.demo.user.dao.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("username1", Position.OPERATIONS));
            userRepository.save(new User("username2", Position.MANAGER));
        };
    }
}
