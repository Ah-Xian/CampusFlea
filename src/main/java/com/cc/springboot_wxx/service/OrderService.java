package com.cc.springboot_wxx.service;

import com.cc.springboot_wxx.entity.Order;
import com.cc.springboot_wxx.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 订单业务逻辑服务
 * 处理订单相关的所有业务逻辑
 */
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    
    /**
     * 创建订单
     * @param order 订单信息
     * @return 保存后的订单（自动生成订单号）
     */
    public Order create(Order order) {
        String orderNo = "ORDER" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderNo(orderNo);
        order.setStatus(Order.Status.PENDING);
        order.setCreatedAt(new Date());
        orderMapper.insert(order);
        return order;
    }
    
    /**
     * 获取用户的订单列表（包含作为买家和卖家的订单）
     * @param userId 用户ID
     * @return 订单列表
     */
    public List<Order> myOrders(Long userId) {
        List<Order> buyerOrders = orderMapper.selectByBuyerId(userId);
        List<Order> sellerOrders = orderMapper.selectBySellerId(userId);
        buyerOrders.addAll(sellerOrders);
        return buyerOrders;
    }
    
    /**
     * 管理员获取所有订单列表
     * @return 所有订单列表
     */
    public List<Order> allOrders() {
        return orderMapper.selectAll();
    }
    
    /**
     * 根据ID获取订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    public Order getById(Long id) {
        return orderMapper.selectById(id);
    }
}