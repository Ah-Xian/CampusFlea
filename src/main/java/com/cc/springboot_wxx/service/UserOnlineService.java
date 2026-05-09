package com.cc.springboot_wxx.service;

import com.cc.springboot_wxx.entity.UserOnline;
import com.cc.springboot_wxx.mapper.UserOnlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户在线设备服务
 * 实现登录互顶功能
 */
@Service
public class UserOnlineService {
    @Autowired
    private UserOnlineMapper userOnlineMapper;
    
    /**
     * 用户登录时记录在线设备
     * @param userId 用户ID
     * @param deviceType 设备类型
     * @param ip 登录IP
     * @return token
     */
    public String login(Long userId, String deviceType, String ip) {
        // 生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // 先踢掉该用户的其他设备（实现互顶）
        userOnlineMapper.deleteByUserId(userId);
        
        // 记录新设备
        UserOnline userOnline = new UserOnline();
        userOnline.setUserId(userId);
        userOnline.setToken(token);
        userOnline.setDeviceType(deviceType);
        userOnline.setLoginTime(new Date());
        userOnline.setIp(ip);
        userOnlineMapper.insert(userOnline);
        
        return token;
    }
    
    /**
     * 验证token是否有效
     * @param token token
     * @return 用户ID，无效返回null
     */
    public Long verifyToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        UserOnline userOnline = userOnlineMapper.selectByToken(token);
        if (userOnline == null) {
            return null;
        }
        return userOnline.getUserId();
    }
    
    /**
     * 获取用户所有在线设备
     * @param userId 用户ID
     * @return 在线设备列表
     */
    public List<UserOnline> getUserDevices(Long userId) {
        return userOnlineMapper.selectByUserId(userId);
    }
    
    /**
     * 登出，删除设备记录
     * @param token token
     */
    public void logout(String token) {
        if (token != null) {
            userOnlineMapper.deleteByToken(token);
        }
    }
    
    /**
     * 踢掉其他设备，只保留当前token
     * @param userId 用户ID
     * @param currentToken 当前token
     */
    public void kickOtherDevices(Long userId, String currentToken) {
        // 先获取当前设备信息
        UserOnline currentDevice = userOnlineMapper.selectByToken(currentToken);
        
        // 删除用户所有设备
        userOnlineMapper.deleteByUserId(userId);
        
        // 重新插入当前设备
        if (currentDevice != null) {
            userOnlineMapper.insert(currentDevice);
        }
    }
}