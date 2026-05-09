package com.cc.springboot_wxx.entity;

import java.util.Date;

/**
 * 商品举报实体类
 */
public class GoodsReport {
    private Long id;
    private Long goodsId;
    private Long reporterId;
    private String reason;
    private Status status = Status.PENDING;
    private Long adminId;
    private String resultComment;
    private Date createdAt;
    private Date processedAt;
    
    public enum Status {
        PENDING, PROCESSED
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getGoodsId() {
        return goodsId;
    }
    
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    
    public Long getReporterId() {
        return reporterId;
    }
    
    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Long getAdminId() {
        return adminId;
    }
    
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
    
    public String getResultComment() {
        return resultComment;
    }
    
    public void setResultComment(String resultComment) {
        this.resultComment = resultComment;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getProcessedAt() {
        return processedAt;
    }
    
    public void setProcessedAt(Date processedAt) {
        this.processedAt = processedAt;
    }
}