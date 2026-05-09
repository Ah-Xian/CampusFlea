package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.UserBan;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserBanMapper {
    
    /**
     * 根据ID查询封禁记录
     * @param id 记录ID
     * @return 封禁对象
     */
    @Select("SELECT id, user_id AS userId, admin_id AS adminId, reason, start_at AS startAt, end_at AS endAt, created_at AS createdAt FROM user_bans WHERE id = #{id}")
    UserBan selectById(Long id);
    
    /**
     * 根据用户ID查询封禁记录
     * @param userId 用户ID
     * @return 封禁对象
     */
    @Select("SELECT id, user_id AS userId, admin_id AS adminId, reason, start_at AS startAt, end_at AS endAt, created_at AS createdAt FROM user_bans WHERE user_id = #{userId}")
    UserBan selectByUserId(Long userId);
    
    /**
     * 查询所有封禁记录
     * @return 封禁列表
     */
    @Select("SELECT id, user_id AS userId, admin_id AS adminId, reason, start_at AS startAt, end_at AS endAt, created_at AS createdAt FROM user_bans")
    List<UserBan> selectAll();
    
    /**
     * 插入封禁记录
     * @param userBan 封禁对象
     * @return 影响行数
     */
    @Insert("INSERT INTO user_bans(user_id, admin_id, reason, start_at, end_at, created_at) " +
            "VALUES(#{userId}, #{adminId}, #{reason}, #{startAt}, #{endAt}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserBan userBan);
    
    /**
     * 删除封禁记录
     * @param id 记录ID
     * @return 影响行数
     */
    @Delete("DELETE FROM user_bans WHERE id = #{id}")
    int delete(Long id);
}