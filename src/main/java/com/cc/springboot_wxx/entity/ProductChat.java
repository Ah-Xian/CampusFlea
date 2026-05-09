package com.cc.springboot_wxx.entity;

import java.util.Date;

/**
 * 商品聊天会话实体类
 * 核心：一个商品 = 一个独立聊天场景
 * 角色：卖家(seller) vs 买家(buyer)
 */
public class ProductChat {
    private Long id;
    private Long productId;
    private Long sellerId;
    private Long buyerId;
    private String lastMessage;
    private Date lastMessageTime;
    private Integer unreadCount;
    private Integer status;
    private Date createTime;
    
    // 关联查询用
    private String productTitle;
    private String sellerNickname;
    private String buyerNickname;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Long getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    
    public Long getBuyerId() {
        return buyerId;
    }
    
    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }
    
    public String getLastMessage() {
        return lastMessage;
    }
    
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    
    public Date getLastMessageTime() {
        return lastMessageTime;
    }
    
    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
    
    public Integer getUnreadCount() {
        return unreadCount;
    }
    
    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public String getProductTitle() {
        return productTitle;
    }
    
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    
    public String getSellerNickname() {
        return sellerNickname;
    }
    
    public void setSellerNickname(String sellerNickname) {
        this.sellerNickname = sellerNickname;
    }
    
    public String getBuyerNickname() {
        return buyerNickname;
    }
    
    public void setBuyerNickname(String buyerNickname) {
        this.buyerNickname = buyerNickname;
    }
}