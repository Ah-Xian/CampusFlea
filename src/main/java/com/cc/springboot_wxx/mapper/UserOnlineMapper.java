package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.UserOnline;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户在线设备 Mapper
 */
@Mapper
public interface UserOnlineMapper {
    
    /**
     * 根据用户ID查询所有在线设备
     * @param userId 用户ID
     * @return 在线设备列表
     */
    @Select("SELECT id, user_id AS userId, token, device_type AS deviceType, login_time AS loginTime, ip FROM user_online WHERE user_id = #{userId}")
    List<UserOnline> selectByUserId(Long userId);
    
    /**
     * 根据token查询在线设备
     * @param token token
     * @return 在线设备
     */
    @Select("SELECT id, user_id AS userId, token, device_type AS deviceType, login_time AS loginTime, ip FROM user_online WHERE token = #{token}")
    UserOnline selectByToken(String token);
    
    /**
     * 根据用户ID删除所有在线设备
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM user_online WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
    
    /**
     * 插入在线设备
     * @param userOnline 在线设备
     * @return 影响行数
     */
    @Insert("INSERT INTO user_online(user_id, token, device_type, login_time, ip) VALUES(#{userId}, #{token}, #{deviceType}, #{loginTime}, #{ip})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserOnline userOnline);
    
    /**
     * 删除指定token的设备
     * @param token token
     * @return 影响行数
     */
    @Delete("DELETE FROM user_online WHERE token = #{token}")
    int deleteByToken(String token);
}