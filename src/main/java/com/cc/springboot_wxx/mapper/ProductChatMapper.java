package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.ProductChat;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 商品聊天会话Mapper
 */
@Mapper
public interface ProductChatMapper {
    
    @Insert("INSERT INTO product_chats (product_id, seller_id, buyer_id, last_message, last_message_time, unread_count, status, create_time) " +
            "VALUES (#{productId}, #{sellerId}, #{buyerId}, #{lastMessage}, #{lastMessageTime}, #{unreadCount}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ProductChat chat);
    
    @Update("UPDATE product_chats SET last_message = #{lastMessage}, last_message_time = #{lastMessageTime}, " +
            "unread_count = #{unreadCount}, status = #{status} WHERE id = #{id}")
    int update(ProductChat chat);
    
    @Select("SELECT id, product_id AS productId, seller_id AS sellerId, buyer_id AS buyerId, " +
            "last_message AS lastMessage, last_message_time AS lastMessageTime, " +
            "unread_count AS unreadCount, status, create_time AS createTime " +
            "FROM product_chats WHERE id = #{id}")
    ProductChat selectById(@Param("id") Long id);
    
    @Select("SELECT id, product_id AS productId, seller_id AS sellerId, buyer_id AS buyerId, " +
            "last_message AS lastMessage, last_message_time AS lastMessageTime, " +
            "unread_count AS unreadCount, status, create_time AS createTime " +
            "FROM product_chats WHERE product_id = #{productId} AND ((seller_id = #{userId} AND buyer_id = #{otherId}) OR (seller_id = #{otherId} AND buyer_id = #{userId}))")
    ProductChat selectByProductAndUsers(@Param("productId") Long productId, @Param("userId") Long userId, @Param("otherId") Long otherId);
    
    @Select("SELECT id, product_id AS productId, seller_id AS sellerId, buyer_id AS buyerId, " +
            "last_message AS lastMessage, last_message_time AS lastMessageTime, " +
            "unread_count AS unreadCount, status, create_time AS createTime " +
            "FROM product_chats WHERE seller_id = #{userId} OR buyer_id = #{userId} ORDER BY last_message_time DESC")
    List<ProductChat> selectByUserId(@Param("userId") Long userId);
    
    @Select("SELECT pc.id, pc.product_id AS productId, pc.seller_id AS sellerId, pc.buyer_id AS buyerId, " +
            "pc.last_message AS lastMessage, pc.last_message_time AS lastMessageTime, " +
            "pc.unread_count AS unreadCount, pc.status, pc.create_time AS createTime, " +
            "g.title AS productTitle " +
            "FROM product_chats pc LEFT JOIN goods g ON pc.product_id = g.id " +
            "WHERE (pc.seller_id = #{userId} OR pc.buyer_id = #{userId}) " +
            "ORDER BY pc.last_message_time DESC")
    List<ProductChat> selectByUserIdWithGoods(@Param("userId") Long userId);
    
    @Delete("DELETE FROM product_chats WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
    
    @Update("UPDATE product_chats SET unread_count = 0 WHERE id = #{id}")
    int markAsRead(@Param("id") Long id);
}