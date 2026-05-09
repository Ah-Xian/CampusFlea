package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.Category;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CategoryMapper {
    
    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return 分类对象
     */
    @Select("SELECT id, name, sort_order AS sortOrder FROM goods_categories WHERE id = #{id}")
    Category selectById(Integer id);
    
    /**
     * 查询所有分类（按排序字段升序）
     * @return 分类列表
     */
    @Select("SELECT id, name, sort_order AS sortOrder FROM goods_categories ORDER BY sort_order ASC")
    List<Category> selectAllOrderBySortOrder();
    
    /**
     * 查询所有分类
     * @return 分类列表
     */
    @Select("SELECT id, name, sort_order AS sortOrder FROM goods_categories")
    List<Category> selectAll();
    
    /**
     * 插入分类
     * @param category 分类对象
     * @return 影响行数
     */
    @Insert("INSERT INTO goods_categories(name, sort_order) VALUES(#{name}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);
    
    /**
     * 更新分类
     * @param category 分类对象
     * @return 影响行数
     */
    @Update("UPDATE goods_categories SET name=#{name}, sort_order=#{sortOrder} WHERE id=#{id}")
    int update(Category category);
    
    /**
     * 删除分类
     * @param id 分类ID
     * @return 影响行数
     */
    @Delete("DELETE FROM goods_categories WHERE id = #{id}")
    int delete(Integer id);
}