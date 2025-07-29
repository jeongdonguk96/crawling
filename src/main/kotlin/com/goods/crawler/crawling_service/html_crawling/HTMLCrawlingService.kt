package com.goods.crawler.crawling_service.html_crawling

import com.goods.crawler.product.vo.Product
import org.jsoup.select.Elements

interface HTMLCrawlingService {
    val siteName: String
    fun crawl(baseUrl: String)
    fun parseProductList(items: Elements): List<Product>
}