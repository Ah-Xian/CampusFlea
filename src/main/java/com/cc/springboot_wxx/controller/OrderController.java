package com.cc.springboot_wxx.controller;

import com.cc.springboot_wxx.entity.Order;
import com.cc.springboot_wxx.entity.User;
import com.cc.springboot_wxx.service.OrderService;
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
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单控制器
 * RESTful API：
 * - GET  /order             - 获取当前用户的订单列表
 * - GET  /order/{id}        - 获取订单详情
 * - GET  /order/admin/list  - 管理员获取所有订单
 * - POST /order             - 创建订单
 */
@RestController
@RequestMapping("/order")
@Tag(name = "订单接口", description = "订单创建、查询、管理相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    /**
     * 获取当前用户的订单列表
     */
    @GetMapping
    @Operation(summary = "获取我的订单", description = "获取当前登录用户的订单列表")
    public void myOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            List<Order> orders = orderService.myOrders(userId);
            ResponseUtil.success(response, orders);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情", description = "根据ID获取订单详情")
    public void detail(
            @Parameter(description = "订单ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        try {
            Order order = orderService.getById(id);
            ResponseUtil.success(response, order);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 管理员获取所有订单列表
     */
    @GetMapping("/admin/list")
    @Operation(summary = "管理员获取订单列表", description = "获取所有订单列表（需管理员权限）")
    public void adminList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            User.Role userRole = (User.Role) session.getAttribute("userRole");
            if (userRole == null || userRole != User.Role.ADMIN) {
                ResponseUtil.error(response, 403, "权限不足");
                return;
            }
            List<Order> orders = orderService.allOrders();
            ResponseUtil.success(response, orders);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 创建订单
     */
    @PostMapping
    @Operation(summary = "创建订单", description = "创建新订单")
    public void create(
            HttpServletRequest request,
            @Parameter(description = "商品ID") @RequestParam Long goodsId,
            @Parameter(description = "卖家ID") @RequestParam Long sellerId,
            @Parameter(description = "订单金额") @RequestParam BigDecimal amount,
            @Parameter(description = "备注") @RequestParam(required = false) String remark,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            Order order = new Order();
            order.setGoodsId(goodsId);
            order.setBuyerId(userId);
            order.setSellerId(sellerId);
            order.setAmount(amount);
            order.setRemark(remark);
            
            Order savedOrder = orderService.create(order);
            ResponseUtil.success(response, savedOrder);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
}