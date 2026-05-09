package com.cc.springboot_wxx.controller;

import com.cc.springboot_wxx.entity.User;
import com.cc.springboot_wxx.service.UserService;
import com.cc.springboot_wxx.service.UserOnlineService;
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
import java.util.Date;
import java.util.List;

/**
 * 用户控制器
 * RESTful API：
 * - GET  /user              - 获取当前用户信息
 * - GET  /user/logout       - 退出登录
 * - GET  /user/admin/list   - 管理员获取用户列表
 * - POST /user/register     - 用户注册
 * - POST /user/login        - 用户登录
 * - POST /user/logout       - 退出登录
 * - POST /user/update       - 更新用户信息
 * - POST /user/{id}/ban     - 管理员封禁用户
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口", description = "用户注册、登录、信息管理相关接口")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserOnlineService userOnlineService;
    
    /**
     * 获取当前登录用户信息
     */
    @GetMapping
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的信息")
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            User user = userService.getUserInfo(userId);
            ResponseUtil.success(response, user);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 退出登录
     */
    @GetMapping("/logout")
    @Operation(summary = "退出登录", description = "使当前会话失效并删除设备记录")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            String token = (String) session.getAttribute("token");
            userOnlineService.logout(token);
            session.invalidate();
            ResponseUtil.success(response, null);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 管理员获取所有用户列表
     */
    @GetMapping("/admin/list")
    @Operation(summary = "管理员获取用户列表", description = "获取所有用户列表（需管理员权限）")
    public void adminList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            User.Role userRole = (User.Role) session.getAttribute("userRole");
            if (userRole == null || userRole != User.Role.ADMIN) {
                ResponseUtil.error(response, 403, "权限不足");
                return;
            }
            List<User> users = userService.adminList();
            ResponseUtil.success(response, users);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户")
    public void register(
            @Parameter(description = "账号") @RequestParam String account,
            @Parameter(description = "密码") @RequestParam String password,
            @Parameter(description = "昵称") @RequestParam String nickname,
            @Parameter(description = "学院") @RequestParam(required = false) String college,
            @Parameter(description = "年级") @RequestParam(required = false) String grade,
            @Parameter(description = "电话") @RequestParam(required = false) String phone,
            @Parameter(description = "微信") @RequestParam(required = false) String wechat,
            HttpServletResponse response) throws IOException {
        try {
            User user = new User();
            user.setAccount(account);
            user.setPasswordHash(password);
            user.setNickname(nickname);
            user.setCollege(college);
            user.setGrade(grade);
            user.setPhone(phone);
            user.setWechat(wechat);
            
            User savedUser = userService.register(user);
            ResponseUtil.success(response, savedUser);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录，返回用户信息并创建Session，实现登录互顶")
    public void login(
            @Parameter(description = "账号") @RequestParam String account,
            @Parameter(description = "密码") @RequestParam String password,
            @Parameter(description = "设备类型") @RequestParam(defaultValue = "PC") String deviceType,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            User user = userService.login(account, password);
            
            // 获取客户端IP
            String ip = request.getRemoteAddr();
            
            // 生成token并记录在线设备（踢掉其他设备）
            String token = userOnlineService.login(user.getId(), deviceType, ip);
            
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("token", token);
            
            // 返回token给前端
            user.setPasswordHash(null);
            ResponseUtil.success(response, java.util.Map.of("user", user, "token", token));
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    @Operation(summary = "更新用户信息", description = "更新当前登录用户的信息")
    public void updateUserInfo(
            HttpServletRequest request,
            @Parameter(description = "昵称") @RequestParam(required = false) String nickname,
            @Parameter(description = "学院") @RequestParam(required = false) String college,
            @Parameter(description = "年级") @RequestParam(required = false) String grade,
            @Parameter(description = "电话") @RequestParam(required = false) String phone,
            @Parameter(description = "微信") @RequestParam(required = false) String wechat,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            User userInfo = new User();
            userInfo.setNickname(nickname);
            userInfo.setCollege(college);
            userInfo.setGrade(grade);
            userInfo.setPhone(phone);
            userInfo.setWechat(wechat);
            
            User updatedUser = userService.updateUserInfo(userId, userInfo);
            ResponseUtil.success(response, updatedUser);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 获取当前用户的在线设备列表
     */
    @GetMapping("/devices")
    @Operation(summary = "获取在线设备", description = "获取当前用户的所有在线设备")
    public void myDevices(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            var devices = userOnlineService.getUserDevices(userId);
            ResponseUtil.success(response, devices);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 踢掉其他设备
     */
    @PostMapping("/kick-devices")
    @Operation(summary = "踢掉其他设备", description = "踢掉当前用户的其他登录设备，只保留当前设备")
    public void kickDevices(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            String token = (String) session.getAttribute("token");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            userOnlineService.kickOtherDevices(userId, token);
            ResponseUtil.success(response, null);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 管理员封禁用户
     */
    @PostMapping("/{id}/ban")
    @Operation(summary = "封禁用户", description = "管理员封禁指定用户")
    public void banUser(
            HttpServletRequest request,
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "封禁原因") @RequestParam String reason,
            @Parameter(description = "结束时间戳") @RequestParam(required = false) Long endAt,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            User.Role userRole = (User.Role) session.getAttribute("userRole");
            Long adminId = (Long) session.getAttribute("userId");
            if (userRole == null || userRole != User.Role.ADMIN || adminId == null) {
                ResponseUtil.error(response, 403, "权限不足");
                return;
            }
            
            Date endAtDate = endAt != null ? new Date(endAt) : null;
            userService.banUser(id, adminId, reason, endAtDate);
            ResponseUtil.success(response, null);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
}