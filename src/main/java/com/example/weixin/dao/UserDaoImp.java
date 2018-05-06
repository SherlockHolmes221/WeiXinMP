package com.example.weixin.dao;

import com.example.weixin.model.WeChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDaoImp extends JpaRepository<WeChatUser,Integer> {
}
