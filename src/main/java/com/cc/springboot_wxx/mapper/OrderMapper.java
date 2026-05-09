package com.cc.springboot_wxx.mapper;

import com.cc.springboot_wxx.entity.Order;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface OrderMapper {
    
    /**
     * 根据ID查询订单
     * @param id 订单ID
     * @return 订单对象
     */
    @Select("SELECT id, order_no AS orderNo, goods_id AS goodsId, buyer_id AS buyerId, seller_id AS sellerId, amount, status, remark, created_at AS createdAt, completed_at AS completedAt, cancelled_at AS cancelledAt FROM orders WHERE id = #{id}")
    Order selectById(Long id);
    
    /**
     * 根据买家ID查询订单
     * @param buyerId 买家ID
     * @return 订单列表
     */
    @Select("SELECT id, order_no AS orderNo, goods_id AS goodsId, buyer_id AS buyerId, seller_id AS sellerId, amount, status, remark, created_at AS createdAt, completed_at AS completedAt, cancelled_at AS cancelledAt FROM orders WHERE buyer_id = #{buyerId}")
    List<Order> selectByBuyerId(Long buyerId);
    
    /**
     * 根据卖家ID查询订单
     * @param sellerId 卖家ID
     * @return 订单列表
     */
    @Select("SELECT id, order_no AS orderNo, goods_id AS goodsId, buyer_id AS buyerId, seller_id AS sellerId, amount, status, remark, created_at AS createdAt, completed_at AS completedAt, cancelled_at AS cancelledAt FROM orders WHERE seller_id = #{sellerId}")
    List<Order> selectBySellerId(Long sellerId);
    
    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单对象
     */
    @Select("SELECT id, order_no AS orderNo, goods_id AS goodsId, buyer_id AS buyerId, seller_id AS sellerId, amount, status, remark, created_at AS createdAt, completed_at AS completedAt, cancelled_at AS cancelledAt FROM orders WHERE order_no = #{orderNo}")
    Order selectByOrderNo(String orderNo);
    
    /**
     * 查询所有订单
     * @return 订单列表
     */
    @Select("SELECT id, order_no AS orderNo, goods_id AS goodsId, buyer_id AS buyerId, seller_id AS sellerId, amount, status, remark, created_at AS createdAt, completed_at AS completedAt, cancelled_at AS cancelledAt FROM orders")
    List<Order> selectAll();
    
    /**
     * 插入订单
     * @param order 订单对象
     * @return 影响行数
     */
    @Insert("INSERT INTO orders(order_no, goods_id, buyer_id, seller_id, amount, status, remark, created_at) " +
            "VALUES(#{orderNo}, #{goodsId}, #{buyerId}, #{sellerId}, #{amount}, #{status}, #{remark}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);
    
    /**
     * 更新订单
     * @param order 订单对象
     * @return 影响行数
     */
    @Update("UPDATE orders SET order_no=#{orderNo}, goods_id=#{goodsId}, buyer_id=#{buyerId}, seller_id=#{sellerId}, " +
            "amount=#{amount}, status=#{status}, remark=#{remark}, completed_at=#{completedAt}, cancelled_at=#{cancelledAt} " +
            "WHERE id=#{id}")
    int update(Order order);
    
    /**
     * 删除订单
     * @param id 订单ID
     * @return 影响行数
     */
    @Delete("DELETE FROM orders WHERE id = #{id}")
    int delete(Long id);
}