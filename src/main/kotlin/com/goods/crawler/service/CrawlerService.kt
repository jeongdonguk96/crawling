package com.goods.crawler.service

interface CrawlerService {
    val siteName: String
    fun crawl(url: String)
}