package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.GoodsReport;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface GoodsReportMapper {
    
    /**
     * 根据ID查询举报记录
     * @param id 举报ID
     * @return 举报对象
     */
    @Select("SELECT id, goods_id AS goodsId, reporter_id AS reporterId, reason, status, admin_id AS adminId, result_comment AS resultComment, created_at AS createdAt, processed_at AS processedAt FROM goods_reports WHERE id = #{id}")
    GoodsReport selectById(Long id);
    
    /**
     * 查询所有举报记录
     * @return 举报列表
     */
    @Select("SELECT id, goods_id AS goodsId, reporter_id AS reporterId, reason, status, admin_id AS adminId, result_comment AS resultComment, created_at AS createdAt, processed_at AS processedAt FROM goods_reports")
    List<GoodsReport> selectAll();
    
    /**
     * 插入举报记录
     * @param goodsReport 举报对象
     * @return 影响行数
     */
    @Insert("INSERT INTO goods_reports(goods_id, reporter_id, reason, status, created_at) " +
            "VALUES(#{goodsId}, #{reporterId}, #{reason}, #{status}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(GoodsReport goodsReport);
    
    /**
     * 更新举报状态
     * @param goodsReport 举报对象
     * @return 影响行数
     */
    @Update("UPDATE goods_reports SET status=#{status}, processed_at=#{processedAt} WHERE id=#{id}")
    int update(GoodsReport goodsReport);
    
    /**
     * 删除举报记录
     * @param id 举报ID
     * @return 影响行数
     */
    @Delete("DELETE FROM goods_reports WHERE id = #{id}")
    int delete(Long id);
}