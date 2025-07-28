package com.goods.crawler.constant

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "")
class CrawlingProperties {
    var crawling: Map<String, TargetUrls>? = null

    class TargetUrls {
        var url: List<String>? = null
    }
}