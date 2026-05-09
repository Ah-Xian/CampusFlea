package com.cc.springboot_wxx.controller;

import com.cc.springboot_wxx.entity.ProductChat;
import com.cc.springboot_wxx.entity.ChatMessage;
import com.cc.springboot_wxx.service.MessageService;
import com.cc.springboot_wxx.service.ProductChatService;
import com.cc.springboot_wxx.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 消息控制器（简化版：只使用商品聊天 ProductChat + ChatMessage）
 * RESTful API：
 * - GET  /messages/chats          - 获取聊天会话列表
 * - POST /messages               - 发送消息（创建聊天会话）
 * - GET  /messages/chats/{id}   - 获取聊天会话详情
 * - GET  /messages/chats/{id}/messages - 获取聊天消息列表
 * - POST /messages/chats/{id}/messages - 发送聊天消息
 * - POST /messages/chats/{id}/read     - 标记已读
 */
@RestController
@RequestMapping("/messages")
@Tag(name = "消息接口", description = "商品聊天相关接口")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private ProductChatService productChatService;
    
    /**
     * 获取聊天会话列表
     */
    @GetMapping("/chats")
    @Operation(summary = "获取聊天会话列表", description = "获取当前用户的所有商品聊天会话")
    public void getChats(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            List<ProductChat> chats = messageService.getChats(userId);
            ResponseUtil.success(response, chats);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 获取聊天会话详情
     */
    @GetMapping("/chats/{id}")
    @Operation(summary = "获取聊天会话详情", description = "获取指定聊天会话详情")
    public void getChatDetail(
            HttpServletRequest request,
            @Parameter(description = "聊天会话ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            ProductChat chat = productChatService.getChatById(id);
            if (chat == null) {
                ResponseUtil.error(response, 404, "会话不存在");
                return;
            }
            if (chat.getSellerId() == null || chat.getBuyerId() == null ||
                (!chat.getSellerId().equals(userId) && !chat.getBuyerId().equals(userId))) {
                ResponseUtil.error(response, 403, "无权限访问");
                return;
            }
            ResponseUtil.success(response, chat);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 发送消息（创建或更新聊天会话）
     * 买家联系卖家
     */
    @PostMapping
    @Operation(summary = "发送消息", description = "买家联系卖家，同时创建/更新商品聊天会话")
    public void send(
            HttpServletRequest request,
            @Parameter(description = "卖家ID") @RequestParam Long sellerId,
            @Parameter(description = "商品ID") @RequestParam Long goodsId,
            @Parameter(description = "消息内容") @RequestParam String content,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long fromUserId = (Long) session.getAttribute("userId");
            if (fromUserId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            if (content == null || content.trim().isEmpty()) {
                ResponseUtil.error(response, 400, "消息内容不能为空");
                return;
            }
            
            if (fromUserId.equals(sellerId)) {
                ResponseUtil.error(response, 400, "不能与自己聊天");
                return;
            }
            
            ProductChat chat = messageService.sendMessage(fromUserId, sellerId, goodsId, content);
            ResponseUtil.success(response, chat);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 获取聊天消息列表
     */
    @GetMapping("/chats/{id}/messages")
    @Operation(summary = "获取聊天消息", description = "获取指定聊天会话的所有消息")
    public void getChatMessages(
            HttpServletRequest request,
            @Parameter(description = "聊天会话ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            ProductChat chat = productChatService.getChatById(id);
            if (chat == null) {
                ResponseUtil.error(response, 404, "会话不存在");
                return;
            }
            if (chat.getSellerId() == null || chat.getBuyerId() == null ||
                (!chat.getSellerId().equals(userId) && !chat.getBuyerId().equals(userId))) {
                ResponseUtil.error(response, 403, "无权限访问");
                return;
            }
            List<ChatMessage> messages = productChatService.getMessagesByChatId(id);
            ResponseUtil.success(response, messages);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 发送聊天消息
     */
    @PostMapping("/chats/{id}/messages")
    @Operation(summary = "发送聊天消息", description = "向指定聊天会话发送消息")
    public void sendChatMessage(
            HttpServletRequest request,
            @Parameter(description = "聊天会话ID") @PathVariable Long id,
            @Parameter(description = "消息内容") @RequestParam String content,
            @Parameter(description = "消息类型") @RequestParam(required = false, defaultValue = "1") Integer msgType,
            @Parameter(description = "文件URL") @RequestParam(required = false) String fileUrl,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            if (content == null || content.trim().isEmpty()) {
                ResponseUtil.error(response, 400, "消息内容不能为空");
                return;
            }
            ProductChat chat = productChatService.getChatById(id);
            if (chat == null) {
                ResponseUtil.error(response, 404, "会话不存在");
                return;
            }
            if (chat.getSellerId() == null || chat.getBuyerId() == null ||
                (!chat.getSellerId().equals(userId) && !chat.getBuyerId().equals(userId))) {
                ResponseUtil.error(response, 403, "无权限访问");
                return;
            }
            ChatMessage message = productChatService.sendMessage(id, userId, content, msgType, fileUrl);
            ResponseUtil.success(response, message);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 标记会话已读
     */
    @PostMapping("/chats/{id}/read")
    @Operation(summary = "标记已读", description = "标记聊天会话为已读")
    public void markRead(
            HttpServletRequest request,
            @Parameter(description = "聊天会话ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            ProductChat chat = productChatService.getChatById(id);
            if (chat == null) {
                ResponseUtil.error(response, 404, "会话不存在");
                return;
            }
            if (chat.getSellerId() == null || chat.getBuyerId() == null ||
                (!chat.getSellerId().equals(userId) && !chat.getBuyerId().equals(userId))) {
                ResponseUtil.error(response, 403, "无权限访问");
                return;
            }
            productChatService.markAsRead(id);
            ResponseUtil.success(response, null);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
}