package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.ChatMessage;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 聊天消息Mapper
 */
@Mapper
public interface ChatMessageMapper {
    
    @Insert("INSERT INTO chat_messages (chat_id, sender_id, sender_nickname, content, msg_type, file_url, status, create_time) " +
            "VALUES (#{chatId}, #{senderId}, #{senderNickname}, #{content}, #{msgType}, #{fileUrl}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ChatMessage message);
    
    @Select("SELECT id, chat_id AS chatId, sender_id AS senderId, sender_nickname AS senderNickname, " +
            "content, msg_type AS msgType, file_url AS fileUrl, status, create_time AS createTime " +
            "FROM chat_messages WHERE chat_id = #{chatId} ORDER BY create_time ASC")
    List<ChatMessage> selectByChatId(@Param("chatId") Long chatId);
    
    @Select("SELECT id, chat_id AS chatId, sender_id AS senderId, sender_nickname AS senderNickname, " +
            "content, msg_type AS msgType, file_url AS fileUrl, status, create_time AS createTime " +
            "FROM chat_messages WHERE id = #{id}")
    ChatMessage selectById(@Param("id") Long id);
    
    @Update("UPDATE chat_messages SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    @Delete("DELETE FROM chat_messages WHERE chat_id = #{chatId}")
    int deleteByChatId(@Param("chatId") Long chatId);
}