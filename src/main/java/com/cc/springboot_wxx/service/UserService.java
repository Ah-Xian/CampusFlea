package com.cc.springboot_wxx.service;

import com.cc.springboot_wxx.entity.User;
import com.cc.springboot_wxx.entity.UserBan;
import com.cc.springboot_wxx.mapper.UserMapper;
import com.cc.springboot_wxx.mapper.UserBanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户业务逻辑服务
 * 处理用户相关的所有业务逻辑
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserBanMapper userBanMapper;
    
    /**
     * 用户注册
     * @param user 用户信息（包含account, password, nickname等）
     * @return 保存后的用户
     */
    public User register(User user) {
        User existingUser = userMapper.selectByAccount(user.getAccount());
        if (existingUser != null) {
            throw new RuntimeException("账号已存在");
        }
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        userMapper.insert(user);
        return user;
    }
    
    /**
     * 用户登录验证
     * @param account 账号
     * @param password 密码
     * @return 用户信息
     */
    public User login(String account, String password) {
        User user = userMapper.selectByAccount(account);
        if (user == null) {
            throw new RuntimeException("账号或密码错误");
        }
        if (!user.getPasswordHash().equals(password)) {
            throw new RuntimeException("账号或密码错误");
        }
        if (user.getStatus() == User.Status.BANNED) {
            throw new RuntimeException("账号已被封禁");
        }
        return user;
    }
    
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }
    
    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param userInfo 新用户信息（部分字段）
     * @return 更新后的用户
     */
    public User updateUserInfo(Long userId, User userInfo) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (userInfo.getNickname() != null) {
            user.setNickname(userInfo.getNickname());
        }
        if (userInfo.getCollege() != null) {
            user.setCollege(userInfo.getCollege());
        }
        if (userInfo.getGrade() != null) {
            user.setGrade(userInfo.getGrade());
        }
        if (userInfo.getPhone() != null) {
            user.setPhone(userInfo.getPhone());
        }
        if (userInfo.getWechat() != null) {
            user.setWechat(userInfo.getWechat());
        }
        user.setUpdatedAt(new Date());
        userMapper.update(user);
        return user;
    }
    
    /**
     * 管理员获取所有用户列表
     * @return 所有用户列表
     */
    public List<User> adminList() {
        return userMapper.selectAll();
    }
    
    /**
     * 管理员封禁用户
     * @param userId 被封禁用户ID
     * @param adminId 管理员ID
     * @param reason 封禁原因
     * @param endAt 封禁结束时间（null表示永久）
     */
    public void banUser(Long userId, Long adminId, String reason, Date endAt) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(User.Status.BANNED);
        user.setUpdatedAt(new Date());
        userMapper.update(user);
        
        UserBan userBan = new UserBan();
        userBan.setUserId(userId);
        userBan.setAdminId(adminId);
        userBan.setReason(reason);
        userBan.setStartAt(new Date());
        userBan.setEndAt(endAt);
        userBan.setCreatedAt(new Date());
userBanMapper.insert(userBan);
    }
}