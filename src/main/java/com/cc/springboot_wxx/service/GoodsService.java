package com.cc.springboot_wxx.service;

import com.cc.springboot_wxx.entity.Category;
import com.cc.springboot_wxx.entity.Goods;
import com.cc.springboot_wxx.entity.GoodsImage;
import com.cc.springboot_wxx.entity.GoodsReport;
import com.cc.springboot_wxx.mapper.CategoryMapper;
import com.cc.springboot_wxx.mapper.GoodsMapper;
import com.cc.springboot_wxx.mapper.GoodsImageMapper;
import com.cc.springboot_wxx.mapper.GoodsReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品业务逻辑服务
 * 处理商品相关的所有业务逻辑
 */
@Service
public class GoodsService {
    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);
    @Autowired
    private GoodsMapper goodsMapper;
    
    @Autowired
    private GoodsImageMapper goodsImageMapper;
    
    @Autowired
    private GoodsReportMapper goodsReportMapper;
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    /**
     * 获取所有商品分类列表
     * @return 分类列表（按sortOrder排序）
     */
    public List<Category> categories() {
        return categoryMapper.selectAllOrderBySortOrder();
    }
    
    /**
     * 发布商品
     * @param goods 商品信息
     * @return 保存后的商品
     */
    public Goods publish(Goods goods) {
        logger.debug("publish商品对象: {}", goods);
        logger.debug("title={}, desc={}, price={}, catId={}, sellerId={}", 
            goods.getTitle(), goods.getDescription(), goods.getPrice(), 
            goods.getCategoryId(), goods.getSellerId());
        logger.debug("status={}, dealType={}, meetPlace={}", 
            goods.getStatus(), goods.getDealType(), goods.getMeetPlace());
        goods.setCreatedAt(new Date());
        goods.setUpdatedAt(new Date());
        try {
            goodsMapper.insert(goods);
        } catch (Exception e) {
            logger.error("insert失败: " + e.getMessage(), e);
            throw e;
        }
        return goods;
    }
    
    /**
     * 更新商品信息
     * @param goodsId 商品ID
     * @param goods 新商品信息（部分字段）
     * @return 更新后的商品
     */
    public Goods update(Long goodsId, Goods goods) {
        Goods existingGoods = goodsMapper.selectById(goodsId);
        if (existingGoods == null) {
            throw new RuntimeException("商品不存在");
        }
        if (goods.getTitle() != null) {
            existingGoods.setTitle(goods.getTitle());
        }
        if (goods.getDescription() != null) {
            existingGoods.setDescription(goods.getDescription());
        }
        if (goods.getPrice() != null) {
            existingGoods.setPrice(goods.getPrice());
        }
        if (goods.getCategoryId() != null) {
            existingGoods.setCategoryId(goods.getCategoryId());
        }
        if (goods.getDealType() != null) {
            existingGoods.setDealType(goods.getDealType());
        }
        if (goods.getMeetPlace() != null) {
            existingGoods.setMeetPlace(goods.getMeetPlace());
        }
        existingGoods.setUpdatedAt(new Date());
        goodsMapper.update(existingGoods);
        return existingGoods;
    }
    
    /**
     * 上传商品图片
     * @param goodsImage 图片信息
     * @return 保存后的图片
     */
    public GoodsImage uploadImage(GoodsImage goodsImage) {
        goodsImage.setCreatedAt(new Date());
        goodsImageMapper.insert(goodsImage);
        return goodsImage;
    }
    
    /**
     * 更新商品状态
     * @param goodsId 商品ID
     * @param status 新状态
     * @return 更新后的商品
     */
    public Goods updateStatus(Long goodsId, Goods.Status status) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            throw new RuntimeException("商品不存在");
        }
        goods.setStatus(status);
        goods.setUpdatedAt(new Date());
        goodsMapper.update(goods);
        return goods;
    }
    
    /**
     * 举报商品
     * @param goodsId 商品ID
     * @param reporterId 举报人ID
     * @param reason 举报原因
     * @return 保存的举报记录
     */
    public GoodsReport report(Long goodsId, Long reporterId, String reason) {
        GoodsReport report = new GoodsReport();
        report.setGoodsId(goodsId);
        report.setReporterId(reporterId);
        report.setReason(reason);
        report.setStatus(GoodsReport.Status.PENDING);
        report.setCreatedAt(new Date());
        goodsReportMapper.insert(report);
        return report;
    }
    
    /**
     * 获取在售商品列表
     * @return 在售商品列表
     */
    public List<Goods> list() {
        return goodsMapper.selectByStatus(Goods.Status.ON_SALE.name());
    }
    
    /**
     * 搜索商品
     * @param keyword 关键词（标题或描述包含关键词）
     * @return 商品列表
     */
    public List<Goods> search(String keyword) {
        return goodsMapper.search(keyword, Goods.Status.ON_SALE.name());
    }
    
    /**
     * 获取商品详情
     * @param goodsId 商品ID
     * @return 商品详情（浏览量+1）
     */
    public Goods detail(Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        logger.debug("商品查询: id={}, sellerId={}, sellerNickname={}", goods.getId(), goods.getSellerId(), goods.getSellerNickname());
        if (goods == null) {
            throw new RuntimeException("商品不存在");
        }
        goods.setViewCount(goods.getViewCount() + 1);
        goodsMapper.updateViewCount(goodsId);
        return goods;
    }
    
    /**
     * 获取商品图片列表
     * @param goodsId 商品ID
     * @return 图片列表
     */
    public List<GoodsImage> getImages(Long goodsId) {
        return goodsImageMapper.selectByGoodsId(goodsId);
    }
    
    /**
     * 获取商品的第一张图片
     * @param goodsId 商品ID
     * @return 第一张图片，未找到返回null
     */
    public GoodsImage getFirstImage(Long goodsId) {
        return goodsImageMapper.selectFirstByGoodsId(goodsId);
    }
    
    /**
     * 删除商品图片
     * @param imageId 图片ID
     * @param userId 用户ID（验证所有权）
     * @return 是否删除成功
     */
    public boolean deleteImage(Long imageId, Long userId) {
        GoodsImage image = goodsImageMapper.selectById(imageId);
        if (image == null) {
            return false;
        }
        Goods goods = goodsMapper.selectById(image.getGoodsId());
        if (goods == null || !goods.getSellerId().equals(userId)) {
            return false;
        }
        return goodsImageMapper.delete(imageId) > 0;
    }
    
    /**
     * 获取用户发布的商品列表
     * @param userId 用户ID
     * @return 商品列表
     */
    public List<Goods> myGoods(Long userId) {
        return goodsMapper.selectBySellerId(userId);
    }
    
    /**
     * 管理员获取所有商品列表
     * @return 所有商品列表
     */
    public List<Goods> adminList() {
        return goodsMapper.selectAll();
    }
}