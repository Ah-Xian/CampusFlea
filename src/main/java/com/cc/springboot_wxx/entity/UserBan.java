package com.cc.springboot_wxx.entity;

import java.util.Date;

/**
 * 用户封禁记录实体类
 */
public class UserBan {
    private Long id;
    private Long userId;
    private Long adminId;
    private String reason;
    private Date startAt;
    private Date endAt;
    private Date createdAt;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getAdminId() {
        return adminId;
    }
    
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public Date getStartAt() {
        return startAt;
    }
    
    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }
    
    public Date getEndAt() {
        return endAt;
    }
    
    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}