package com.cc.springboot_wxx.controller;

import com.cc.springboot_wxx.entity.Category;
import com.cc.springboot_wxx.entity.Goods;
import com.cc.springboot_wxx.entity.GoodsImage;
import com.cc.springboot_wxx.entity.User;
import com.cc.springboot_wxx.service.GoodsService;
import com.cc.springboot_wxx.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * 商品控制器
 * RESTful API：
 * - GET    /goods              - 获取在售商品列表
 * - GET    /goods/categories   - 获取商品分类列表
 * - GET    /goods/search       - 搜索商品
 * - GET    /goods/my            - 获取当前用户的商品
 * - GET    /goods/admin/list    - 管理员获取所有商品
 * - GET    /goods/{id}          - 获取商品详情
 * - GET    /goods/{id}/images   - 获取商品图片列表
 * - POST   /goods               - 发布商品
 * - POST   /goods/upload        - 上传文件
 * - POST   /goods/{id}/images   - 添加商品图片
 * - POST   /goods/{id}/status   - 更新商品状态
 * - POST   /goods/{id}/report   - 举报商品
 * - PUT    /goods/{id}          - 更新商品信息
 */
@RestController
@RequestMapping("/goods")
@Tag(name = "商品接口", description = "商品发布、查询、管理相关接口")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);
    
    @Autowired
    private GoodsService goodsService;
    
    @Value("${upload.path}")
    private String uploadPath;
    
    @Value("${upload.base-url}")
    private String baseUrl;
    
    /**
     * 获取在售商品列表
     */
    @GetMapping
    @Operation(summary = "获取商品列表", description = "获取所有在售商品列表")
    public void list(HttpServletResponse response) throws IOException {
        try {
            List<Goods> goodsList = goodsService.list();
            ResponseUtil.success(response, goodsList);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 搜索商品
     */
    @GetMapping("/search")
    @Operation(summary = "搜索商品", description = "根据关键词搜索商品")
    public void search(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            HttpServletResponse response) throws IOException {
        try {
            List<Goods> goodsList = goodsService.search(keyword);
            ResponseUtil.success(response, goodsList);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 获取商品详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据ID获取商品详情，同时增加浏览量")
    public void detail(
            @Parameter(description = "商品ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        try {
            Goods goods = goodsService.detail(id);
            ResponseUtil.success(response, goods);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 获取商品图片列表
     */
    @GetMapping("/{id}/images")
    @Operation(summary = "获取商品图片", description = "获取指定商品的所有图片")
    public void getImages(
            @Parameter(description = "商品ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        try {
            List<GoodsImage> images = goodsService.getImages(id);
            ResponseUtil.success(response, images);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 获取商品第一张图片
     */
    @GetMapping("/{id}/image")
    @Operation(summary = "获取商品首图", description = "获取指定商品的第一张图片")
    public void getFirstImage(
            @Parameter(description = "商品ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        try {
            GoodsImage image = goodsService.getFirstImage(id);
            ResponseUtil.success(response, image);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 获取商品分类列表
     */
    @GetMapping("/categories")
    @Operation(summary = "获取分类列表", description = "获取所有商品分类")
    public void categories(HttpServletResponse response) throws IOException {
        try {
            List<Category> categories = goodsService.categories();
            ResponseUtil.success(response, categories);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 获取当前用户的商品列表
     */
    @GetMapping("/my")
    @Operation(summary = "获取我的商品", description = "获取当前用户发布的商品列表")
    public void myGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            List<Goods> goodsList = goodsService.myGoods(userId);
            ResponseUtil.success(response, goodsList);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 管理员获取所有商品列表
     */
    @GetMapping("/admin/list")
    @Operation(summary = "管理员获取商品列表", description = "管理员获取所有商品（含下架）")
    public void adminList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            User.Role userRole = (User.Role) session.getAttribute("userRole");
            if (userRole == null || userRole != User.Role.ADMIN) {
                ResponseUtil.error(response, 403, "权限不足");
                return;
            }
            List<Goods> goodsList = goodsService.adminList();
            ResponseUtil.success(response, goodsList);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 发布商品
     */
    @PostMapping
    @Operation(summary = "发布商品", description = "发布新商品到平台")
    public void publish(
            HttpServletRequest request,
            @Parameter(description = "商品标题") @RequestParam String title,
            @Parameter(description = "商品描述") @RequestParam(required = false) String description,
            @Parameter(description = "价格") @RequestParam String price,
            @Parameter(description = "分类ID") @RequestParam Integer categoryId,
            @Parameter(description = "交易方式") @RequestParam(required = false) String dealType,
            @Parameter(description = "交易地点") @RequestParam(required = false) String meetPlace,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            logger.debug("发布商品: title={}, price={}, categoryId={}", title, price, categoryId);
            
            Goods goods = new Goods();
            goods.setTitle(title);
            goods.setDescription(description);
            goods.setPrice(new BigDecimal(price));
            goods.setCategoryId(categoryId);
            goods.setSellerId(userId);
            if (dealType != null) {
                goods.setDealType(Goods.DealType.valueOf(dealType));
            }
            goods.setMeetPlace(meetPlace);
            
            Goods savedGoods = goodsService.publish(goods);
            ResponseUtil.success(response, savedGoods);
        } catch (NumberFormatException e) {
            ResponseUtil.error(response, 400, "价格格式错误");
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null || msg.isEmpty()) {
                msg = "服务器内部错误";
            }
            logger.error("发布商品失败: " + msg, e);
            ResponseUtil.error(response, 500, msg);
        }
    }
    
    /**
     * 更新商品���息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新商品", description = "更新商品信息")
    public void update(
            HttpServletRequest request,
            @Parameter(description = "商品ID") @PathVariable Long id,
            @Parameter(description = "商品标题") @RequestParam(required = false) String title,
            @Parameter(description = "商品描述") @RequestParam(required = false) String description,
            @Parameter(description = "价格") @RequestParam(required = false) String price,
            @Parameter(description = "分类ID") @RequestParam(required = false) Integer categoryId,
            @Parameter(description = "交易方式") @RequestParam(required = false) String dealType,
            @Parameter(description = "交易地点") @RequestParam(required = false) String meetPlace,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            Goods goods = new Goods();
            goods.setTitle(title);
            goods.setDescription(description);
            if (price != null) {
                goods.setPrice(new BigDecimal(price));
            }
            goods.setCategoryId(categoryId);
            if (dealType != null) {
                goods.setDealType(Goods.DealType.valueOf(dealType));
            }
            goods.setMeetPlace(meetPlace);
            
            Goods updatedGoods = goodsService.update(id, goods);
            ResponseUtil.success(response, updatedGoods);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件，返回文件访问URL")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            org.springframework.web.multipart.MultipartHttpServletRequest multipartRequest = 
                (org.springframework.web.multipart.MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            
            if (file == null || file.isEmpty()) {
                ResponseUtil.error(response, 400, "请选择文件");
                return;
            }
            
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString().replace("-", "") + ext;
            
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            File destFile = new File(uploadPath + "/" + newFileName);
            file.transferTo(destFile);
            
            String fileUrl = baseUrl + "/" + newFileName;
            ResponseUtil.success(response, fileUrl);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, "上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加商品图片
     */
    @PostMapping("/{id}/images")
    @Operation(summary = "添加商品图片", description = "为商品添加图片")
    public void uploadImage(
            HttpServletRequest request,
            @Parameter(description = "商品ID") @PathVariable Long id,
            @Parameter(description = "图片URL") @RequestParam String imageUrl,
            @Parameter(description = "排序") @RequestParam(required = false) Integer sortOrder,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            GoodsImage goodsImage = new GoodsImage();
            goodsImage.setGoodsId(id);
            goodsImage.setImageUrl(imageUrl);
            goodsImage.setSortOrder(sortOrder);
            
            GoodsImage savedImage = goodsService.uploadImage(goodsImage);
            ResponseUtil.success(response, savedImage);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 删除商品图片
     */
    @DeleteMapping("/images/{imageId}")
    @Operation(summary = "删除商品图片", description = "删除指定商品图片（需商品所有者）")
    public void deleteImage(
            HttpServletRequest request,
            @Parameter(description = "图片ID") @PathVariable Long imageId,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            boolean success = goodsService.deleteImage(imageId, userId);
            if (success) {
                ResponseUtil.success(response, null);
            } else {
                ResponseUtil.error(response, 400, "删除失败");
            }
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 更新商品状态
     */
    @PostMapping("/{id}/status")
    @Operation(summary = "更新商品状态", description = "更新商品状态（上下架、售出）")
    public void updateStatus(
            HttpServletRequest request,
            @Parameter(description = "商品ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam String status,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            Goods.Status goodsStatus = Goods.Status.valueOf(status);
            Goods updatedGoods = goodsService.updateStatus(id, goodsStatus);
            ResponseUtil.success(response, updatedGoods);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
    
    /**
     * 举报商品
     */
    @PostMapping("/{id}/report")
    @Operation(summary = "举报商品", description = "举报违规商品")
    public void report(
            HttpServletRequest request,
            @Parameter(description = "商品ID") @PathVariable Long id,
            @Parameter(description = "举报原因") @RequestParam String reason,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                ResponseUtil.error(response, 401, "未登录");
                return;
            }
            
            goodsService.report(id, userId, reason);
            ResponseUtil.success(response, null);
        } catch (Exception e) {
            ResponseUtil.error(response, 500, e.getMessage());
        }
    }
}