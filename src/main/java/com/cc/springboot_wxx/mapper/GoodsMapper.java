package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.Goods;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface GoodsMapper {
    
    /**
     * 根据ID查询商品（关联分类和卖家信息）
     * @param id 商品ID
     * @return 商品对象
     */
    @Select("SELECT g.id, g.title, g.description, g.price, g.category_id, gc.name AS category, " +
            "g.seller_id, u.nickname AS sellerNickname, g.status, g.deal_type, g.meet_place, " +
            "g.view_count, g.created_at, g.updated_at " +
            "FROM goods g " +
            "LEFT JOIN goods_categories gc ON g.category_id = gc.id " +
            "LEFT JOIN users u ON g.seller_id = u.id " +
            "WHERE g.id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "description", column = "description"),
        @Result(property = "price", column = "price"),
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "category", column = "category"),
        @Result(property = "sellerId", column = "seller_id"),
        @Result(property = "sellerNickname", column = "sellerNickname"),
        @Result(property = "status", column = "status"),
        @Result(property = "dealType", column = "deal_type"),
        @Result(property = "meetPlace", column = "meet_place"),
        @Result(property = "viewCount", column = "view_count"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    Goods selectById(Long id);
    
    /**
     * 根据卖家ID查询商品列表（关联分类和卖家信息）
     * @param sellerId 卖家ID
     * @return 商品列表
     */
    @Select("SELECT g.id, g.title, g.description, g.price, g.category_id AS categoryId, gc.name AS category, " +
            "g.seller_id AS sellerId, u.nickname AS sellerNickname, g.status AS status, g.deal_type AS dealType, g.meet_place AS meetPlace, " +
            "g.view_count, g.created_at AS createdAt, g.updated_at AS updatedAt " +
            "FROM goods g " +
            "LEFT JOIN goods_categories gc ON g.category_id = gc.id " +
            "LEFT JOIN users u ON g.seller_id = u.id " +
            "WHERE g.seller_id = #{sellerId}")
    List<Goods> selectBySellerId(Long sellerId);
    
    /**
     * 根据状态查询商品列表（关联分类和卖家信息）
     * @param status 商品状态
     * @return 商品列表
     */
    @Select("SELECT g.id, g.title, g.description, g.price, g.category_id AS categoryId, gc.name AS category, " +
            "g.seller_id AS sellerId, u.nickname AS sellerNickname, g.status AS status, g.deal_type AS dealType, g.meet_place AS meetPlace, " +
            "g.view_count, g.created_at AS createdAt, g.updated_at AS updatedAt " +
            "FROM goods g " +
            "LEFT JOIN goods_categories gc ON g.category_id = gc.id " +
            "LEFT JOIN users u ON g.seller_id = u.id " +
            "WHERE g.status = #{status}")
    List<Goods> selectByStatus(String status);
    
    /**
     * 搜索商品（模糊查询标题和描述，关联分类和卖家信息）
     * @param keyword 搜索关键词
     * @param status 商品状态
     * @return 商品列表
     */
    @Select("SELECT g.id, g.title, g.description, g.price, g.category_id AS categoryId, gc.name AS category, " +
            "g.seller_id AS sellerId, u.nickname AS sellerNickname, g.status AS status, g.deal_type AS dealType, g.meet_place AS meetPlace, " +
            "g.view_count, g.created_at AS createdAt, g.updated_at AS updatedAt " +
            "FROM goods g " +
            "LEFT JOIN goods_categories gc ON g.category_id = gc.id " +
            "LEFT JOIN users u ON g.seller_id = u.id " +
            "WHERE g.status = #{status} AND (g.title LIKE CONCAT('%', #{keyword}, '%') OR g.description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Goods> search(String keyword, String status);
    
    /**
     * 查询所有商品（关联分类和卖家信息）
     * @return 商品列表
     */
    @Select("SELECT g.id, g.title, g.description, g.price, g.category_id AS categoryId, gc.name AS category, " +
            "g.seller_id AS sellerId, u.nickname AS sellerNickname, g.status AS status, g.deal_type AS dealType, g.meet_place AS meetPlace, " +
            "g.view_count, g.created_at AS createdAt, g.updated_at AS updatedAt " +
            "FROM goods g " +
            "LEFT JOIN goods_categories gc ON g.category_id = gc.id " +
            "LEFT JOIN users u ON g.seller_id = u.id")
    List<Goods> selectAll();
    
    /**
     * 插入商品
     * @param goods 商品对象
     * @return 影响行数
     */
    @Insert("INSERT INTO goods(title, description, price, category_id, seller_id, status, deal_type, meet_place, view_count, created_at, updated_at) " +
            "VALUES(#{title}, #{description}, #{price}, #{categoryId}, #{sellerId}, #{status}, #{dealType}, #{meetPlace}, #{viewCount}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Goods goods);
    
    /**
     * 更新商品
     * @param goods 商品对象
     * @return 影响行数
     */
    @Update("UPDATE goods SET title=#{title}, description=#{description}, price=#{price}, category_id=#{categoryId}, " +
            "status=#{status}, deal_type=#{dealType}, meet_place=#{meetPlace}, view_count=#{viewCount}, updated_at=#{updatedAt} " +
            "WHERE id=#{id}")
    int update(Goods goods);
    
    /**
     * 只更新浏览量
     * @param id 商品ID
     * @return 影响行数
     */
    @Update("UPDATE goods SET view_count = view_count + 1 WHERE id = #{id}")
    int updateViewCount(Long id);
    
    /**
     * 删除商品
     * @param id 商品ID
     * @return 影响行数
     */
    @Delete("DELETE FROM goods WHERE id = #{id}")
    int delete(Long id);
}