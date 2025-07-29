package com.goods.crawler.product.dao

import com.goods.crawler.product.vo.Product
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ProductDao {
    fun insertProducts(products: List<Product>)
}