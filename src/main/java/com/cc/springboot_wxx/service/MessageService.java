package com.cc.springboot_wxx.service;

import com.cc.springboot_wxx.entity.ProductChat;
import com.cc.springboot_wxx.entity.ChatMessage;
import com.cc.springboot_wxx.mapper.ProductChatMapper;
import com.cc.springboot_wxx.mapper.ChatMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息服务（简化版：只使用商品聊天 ProductChat + ChatMessage）
 */
@Service
public class MessageService {
    @Autowired
    private ProductChatService productChatService;
    
    /**
     * 发送消息（创建或更新聊天会话）
     * @param buyerId 买家ID
     * @param sellerId 卖家ID
     * @param goodsId 商品ID
     * @param content 消息内容
     * @return 聊天会话
     */
    public ProductChat sendMessage(Long buyerId, Long sellerId, Long goodsId, String content) {
        ProductChat chat = productChatService.getOrCreateChat(goodsId, buyerId, sellerId);
        productChatService.sendMessage(chat.getId(), buyerId, content, 1, null);
        return chat;
    }
    
    /**
     * 获取未读消息数量（从聊天会话统计）
     * @param userId 用户ID
     * @return 未读数量
     */
    public int getUnreadCount(Long userId) {
        List<ProductChat> chats = productChatService.getChatsByUserId(userId);
        int count = 0;
        for (ProductChat chat : chats) {
            count += chat.getUnreadCount();
        }
        return count;
    }
    
    /**
     * 获取用户的聊天会话列表
     * @param userId 用户ID
     * @return 聊天会话列表
     */
    public List<ProductChat> getChats(Long userId) {
        return productChatService.getChatsByUserId(userId);
    }
}