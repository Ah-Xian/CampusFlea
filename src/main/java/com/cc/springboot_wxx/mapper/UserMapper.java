package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    @Select("SELECT id, account, password_hash AS passwordHash, nickname, college, grade, phone, wechat, role, status, created_at AS createdAt, updated_at AS updatedAt FROM users WHERE id = #{id}")
    User selectById(Long id);
    
    /**
     * 根据账号查询用户
     * @param account 账号
     * @return 用户对象
     */
    @Select("SELECT id, account, password_hash AS passwordHash, nickname, college, grade, phone, wechat, role, status, created_at AS createdAt, updated_at AS updatedAt FROM users WHERE account = #{account}")
    User selectByAccount(String account);
    
    /**
     * 查询所有用户
     * @return 用户列表
     */
    @Select("SELECT id, account, password_hash AS passwordHash, nickname, college, grade, phone, wechat, role, status, created_at AS createdAt, updated_at AS updatedAt FROM users")
    List<User> selectAll();
    
    /**
     * 插入用户
     * @param user 用户对象
     * @return 影响行数
     */
    @Insert("INSERT INTO users(account, password_hash, nickname, college, grade, phone, wechat, role, status, created_at, updated_at) " +
            "VALUES(#{account}, #{passwordHash}, #{nickname}, #{college}, #{grade}, #{phone}, #{wechat}, #{role}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    /**
     * 更新用户
     * @param user 用户对象
     * @return 影响行数
     */
    @Update("UPDATE users SET account=#{account}, password_hash=#{passwordHash}, nickname=#{nickname}, " +
            "college=#{college}, grade=#{grade}, phone=#{phone}, wechat=#{wechat}, role=#{role}, status=#{status}, updated_at=#{updatedAt} " +
            "WHERE id=#{id}")
    int update(User user);
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM users WHERE id = #{id}")
    int delete(Long id);
}