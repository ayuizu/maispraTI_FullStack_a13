package com.example.api_user.repository;

import com.example.api_user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    //Dá pra personalizar
    //Optional<User> findByUsername(String username);

    Optional<User> findByUsername(String username);

}
