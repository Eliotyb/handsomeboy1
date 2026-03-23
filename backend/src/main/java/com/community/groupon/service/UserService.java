package com.community.groupon.service;

import com.community.groupon.entity.User;
import com.community.groupon.repository.UserRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.selectList(null);
    }

    public User findById(Long id) {
        return userRepository.selectById(id);
    }

    public User findByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userRepository.selectOne(wrapper);
    }

    public User save(User user) {
        if (user.getId() != null) {
            userRepository.updateById(user);
        } else {
            userRepository.insert(user);
        }
        return user;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}