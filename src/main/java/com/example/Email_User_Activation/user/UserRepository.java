package com.example.Email_User_Activation.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByEmail(String email);

}
