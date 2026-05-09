package com.cc.springboot_wxx.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体类
 * 表示校园二手交易平台中的商品信息
 */
public class Goods {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer categoryId;
    private String category;
    private Long sellerId;
    private String sellerNickname;
    private Status status = Status.ON_SALE;
    private DealType dealType = DealType.MEET;
    private String meetPlace;
    private Integer viewCount = 0;
    private Date createdAt;
    private Date updatedAt;
    
    public enum Status {
        ON_SALE, SOLD, OFF_SHELF, PENDING_REVIEW
    }
    
    public enum DealType {
        MEET, DELIVERY, BOTH
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Long getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    
    public String getSellerNickname() {
        return sellerNickname;
    }
    
    public void setSellerNickname(String sellerNickname) {
        this.sellerNickname = sellerNickname;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public DealType getDealType() {
        return dealType;
    }
    
    public void setDealType(DealType dealType) {
        this.dealType = dealType;
    }
    
    public String getMeetPlace() {
        return meetPlace;
    }
    
    public void setMeetPlace(String meetPlace) {
        this.meetPlace = meetPlace;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}