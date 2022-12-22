package com.example.demo.api;

import com.example.demo.user.User;
import com.example.demo.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/v1")
public class UserController {

    @Autowired
    private PagedResourcesAssembler<User> pagedResourcesAssembler;

    @Autowired
    private UserAssembler userAssembler;

    @GetMapping(value = "/users")
    public ResponseEntity<CollectionModel<EntityModel<User>>> all(@RequestParam(required = false) List<Long> userIds,
                                                                  Pageable pageable) {

        Page<User> users = getUsers(userIds, pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(users, userAssembler));
    }

    private Page<User> getUsers(List<Long> userIds, Pageable pageable) {
        if (userIds == null) {
            return userDao.findAll(pageable);
        } else {
            return userDao.findByIdIn(userIds, pageable);
        }
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<EntityModel<User>> get(@PathVariable Long id) {
        User user = userDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return ResponseEntity.ok(userAssembler.toModel(user));
    }

    @Autowired
    private UserDao userDao;
}
