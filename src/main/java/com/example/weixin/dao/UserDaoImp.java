package com.example.weixin.dao;

import com.example.weixin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDaoImp extends JpaRepository<User,Integer> {
}
