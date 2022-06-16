package com.api.authorization.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.authorization.entity.Users;

@Repository
public interface UsersDAO extends JpaRepository<Users, String> {

}
