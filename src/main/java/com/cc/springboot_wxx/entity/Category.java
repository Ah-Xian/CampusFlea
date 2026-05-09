package com.cc.springboot_wxx.entity;

/**
 * 商品分类实体类
 * 用于表示商品的分类信息，如"电子产品"、"书籍"、"生活用品"等
 */
public class Category {
    private Integer id;
    private String name;
    private Integer sortOrder = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
