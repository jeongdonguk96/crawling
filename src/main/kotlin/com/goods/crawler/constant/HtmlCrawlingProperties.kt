package com.goods.crawler.constant

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "html-crawling")
class HtmlCrawlingProperties {
    var targets: Map<String, TargetUrls>? = null

    class TargetUrls {
        var url: List<String>? = null
    }
}