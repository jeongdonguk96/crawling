package com.goods.crawler.crawling_service.api_crawling

import com.goods.crawler.product.vo.Product

interface APICrawlingService {
    val siteName: String
    fun crawl()
    fun parseProductList(json: String) : List<Product>
}