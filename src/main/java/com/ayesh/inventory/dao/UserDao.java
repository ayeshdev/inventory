package com.ayesh.inventory.dao;

import com.ayesh.inventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User> {
}
