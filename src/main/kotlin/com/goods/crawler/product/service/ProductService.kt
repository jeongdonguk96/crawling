package com.goods.crawler.product.service

import com.goods.crawler.product.dao.ProductDao
import com.goods.crawler.product.vo.Product
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productDao: ProductDao
) {

    fun insertProducts(
        products: List<Product>
    ) {
        productDao.insertProducts(products)
    }

}