package com.goods.crawler.scheduler

import com.goods.crawler.constant.HtmlCrawlingProperties
import com.goods.crawler.crawling_service.api_crawling.APICrawlingService
import com.goods.crawler.crawling_service.html_crawling.HTMLCrawlingService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CrawlingScheduler(
    private val apiCrawlingServices: List<APICrawlingService>,
    private val htmlCrawlingServices: List<HTMLCrawlingService>,
    private val htmlCrawlingProperties: HtmlCrawlingProperties,
) {

    @Scheduled(initialDelay = 1000)
    fun crawlingHTMLWebSite() {
        htmlCrawlingProperties.targets!!
            .forEach { (site, targetUrls) ->
                val crawlingService = htmlCrawlingServices
                    .find { it.siteName == site }
                    ?: return@forEach

                targetUrls.url!!
                    .forEach { url ->
                        crawlingService.crawl(url)
                    }
            }
    }

    @Scheduled(initialDelay = 1000)
    fun crawlingAPIServer() {
        apiCrawlingServices.forEach {
            it.crawl()
        }
    }

}