package com.goods.crawler.scheduler

import com.goods.crawler.constant.CrawlingProperties
import com.goods.crawler.service.CrawlerService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CrawlingScheduler(
    private val crawlerServices: List<CrawlerService>,
    private val crawlingProperties: CrawlingProperties
) {

    @Scheduled(initialDelay = 1000)
    fun crawlingAnimate() {
        crawlingProperties.crawling!!
            .forEach { (site, targetUrls) ->
                val crawlerService = crawlerServices
                    .find { it.siteName == site }
                    ?: return@forEach

                targetUrls.url!!
                    .forEach { url ->
                        crawlerService.crawl(url)
                    }
            }
    }

}