package com.cc.springboot_wxx.service;

import com.cc.springboot_wxx.entity.ProductChat;
import com.cc.springboot_wxx.entity.ChatMessage;
import com.cc.springboot_wxx.mapper.ProductChatMapper;
import com.cc.springboot_wxx.mapper.ChatMessageMapper;
import com.cc.springboot_wxx.mapper.UserMapper;
import com.cc.springboot_wxx.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 商品聊天服务
 */
@Service
public class ProductChatService {
    @Autowired
    private ProductChatMapper productChatMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    
    /**
     * 获取用户的聊天会话列表
     * @param userId 用户ID
     * @return 聊天会话列表
     */
    public List<ProductChat> getChatsByUserId(Long userId) {
        List<ProductChat> chats = productChatMapper.selectByUserIdWithGoods(userId);
        for (ProductChat chat : chats) {
            if (chat.getSellerId() != null && chat.getBuyerId() != null) {
                if (chat.getSellerId().equals(userId)) {
                    chat.setSellerNickname(userMapper.selectById(chat.getSellerId()).getNickname());
                    chat.setBuyerNickname(userMapper.selectById(chat.getBuyerId()).getNickname());
                } else {
                    chat.setSellerNickname(userMapper.selectById(chat.getSellerId()).getNickname());
                    chat.setBuyerNickname(userMapper.selectById(chat.getBuyerId()).getNickname());
                }
            }
        }
        return chats;
    }
    
    /**
     * 获取特定商品的聊天会话
     * @param productId 商品ID
     * @param userId 用户ID
     * @return 聊天会话
     */
    public ProductChat getChatByProductAndUser(Long productId, Long userId, Long otherUserId) {
        return productChatMapper.selectByProductAndUsers(productId, userId, otherUserId);
    }
    
    /**
     * 创建或获取聊天会话
     * @param productId 商品ID
     * @param buyerId 买家ID
     * @param sellerId 卖家ID
     * @return 聊天会话
     */
    @Transactional
    public ProductChat getOrCreateChat(Long productId, Long buyerId, Long sellerId) {
        ProductChat chat = productChatMapper.selectByProductAndUsers(productId, buyerId, sellerId);
        if (chat == null) {
            chat = new ProductChat();
            chat.setProductId(productId);
            chat.setSellerId(sellerId);
            chat.setBuyerId(buyerId);
            chat.setLastMessage("");
            chat.setLastMessageTime(new Date());
            chat.setUnreadCount(0);
            chat.setStatus(1);
            chat.setCreateTime(new Date());
            productChatMapper.insert(chat);
        }
        return chat;
    }
    
    /**
     * 发送聊天消息
     * @param chatId 聊天会话ID
     * @param senderId 发送者ID（可能是卖家也可能是买家）
     * @param content 消息内容
     * @param msgType 消息类型
     * @param fileUrl 文件URL
     * @return 消息
     */
    @Transactional
    public ChatMessage sendMessage(Long chatId, Long senderId, String content, Integer msgType, String fileUrl) {
        ChatMessage message = new ChatMessage();
        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setSenderNickname(userMapper.selectById(senderId).getNickname());
        message.setContent(content);
        message.setMsgType(msgType != null ? msgType : 1);
        message.setFileUrl(fileUrl);
        message.setStatus(1);
        message.setCreateTime(new Date());
        chatMessageMapper.insert(message);
        
        ProductChat chat = productChatMapper.selectById(chatId);
        chat.setLastMessage(content);
        chat.setLastMessageTime(new Date());
        // 判断接收者
        Long receiverId;
        if (chat.getSellerId() != null && chat.getSellerId().equals(senderId)) {
            receiverId = chat.getBuyerId();
        } else {
            receiverId = chat.getSellerId();
        }
        chat.setUnreadCount(chat.getUnreadCount() + 1);
        productChatMapper.update(chat);
        
        return message;
    }
    
    /**
     * 获取聊天消息列表
     * @param chatId 聊天会话ID
     * @return 消息列表
     */
    public List<ChatMessage> getMessagesByChatId(Long chatId) {
        return chatMessageMapper.selectByChatId(chatId);
    }
    
    /**
     * 标记聊天会话为已读
     * @param chatId 聊天会话ID
     */
    public void markAsRead(Long chatId) {
        productChatMapper.markAsRead(chatId);
    }
    
    /**
     * 获取聊天会话详情
     * @param chatId 聊天会话ID
     * @return 聊天会话
     */
    public ProductChat getChatById(Long chatId) {
        return productChatMapper.selectById(chatId);
    }
}