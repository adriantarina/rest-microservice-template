package com.example.demo.user.dao;

import com.example.demo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Page<User> findByIdIn(List<Long> ids, Pageable pageable);
}
