package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.GoodsImage;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface GoodsImageMapper {
    
    /**
     * 根据ID查询商品图片
     * @param id 图片ID
     * @return 图片对象
     */
    @Select("SELECT id, goods_id AS goodsId, image_url AS imageUrl, sort_order AS sortOrder, created_at AS createdAt FROM goods_images WHERE id = #{id}")
    GoodsImage selectById(Long id);
    
    /**
     * 根据商品ID查询图片列表
     * @param goodsId 商品ID
     * @return 图片列表
     */
    @Select("SELECT id, goods_id AS goodsId, image_url AS imageUrl, sort_order AS sortOrder, created_at AS createdAt FROM goods_images WHERE goods_id = #{goodsId} ORDER BY sort_order ASC, id ASC")
    List<GoodsImage> selectByGoodsId(Long goodsId);
    
    /**
     * 查询商品的第一张图片
     * @param goodsId 商品ID
     * @return 第一张图片，未找到返回null
     */
    @Select("SELECT id, goods_id AS goodsId, image_url AS imageUrl, sort_order AS sortOrder, created_at AS createdAt " +
            "FROM goods_images WHERE goods_id = #{goodsId} ORDER BY IFNULL(sort_order, 0), id LIMIT 1")
    GoodsImage selectFirstByGoodsId(Long goodsId);
    
    /**
     * 插入商品图片
     * @param goodsImage 图片对象
     * @return 影响行数
     */
    @Insert("INSERT INTO goods_images(goods_id, image_url, sort_order, created_at) " +
            "VALUES(#{goodsId}, #{imageUrl}, #{sortOrder}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(GoodsImage goodsImage);
    
    /**
     * 删除商品图片
     * @param id 图片ID
     * @return 影响行数
     */
    @Delete("DELETE FROM goods_images WHERE id = #{id}")
    int delete(Long id);
}