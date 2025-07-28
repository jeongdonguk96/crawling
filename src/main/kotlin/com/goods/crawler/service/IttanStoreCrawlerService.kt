package com.goods.crawler.service

import com.goods.crawler.enums.WebSite
import org.jsoup.Jsoup
import org.springframework.stereotype.Component

@Component
class IttanStoreCrawlerService : CrawlerService {
    override val siteName = WebSite.ITTANSTORE.name.lowercase()

    override fun crawl(url: String) {
        val docs = Jsoup.connect(url).get()
        println("사이트명: $siteName")

        val items = docs.select("li.xans-record-")
        println("크롤링 URL: $url, 아이템 개수: ${items.size}")

        for (item in items) {
            val productImageUrl = item.selectFirst(".thumbnail a img")?.attr("src")?.let {
                if (it.startsWith("http")) it else "https:$it"
            } ?: ""
            val productUrlRelative = item.selectFirst(".thumbnail a")?.attr("href") ?: ""
            val productUrl = "https://m.ittanstore.com$productUrlRelative"
            val productName = item.selectFirst(".description .name a")?.text()?.trim() ?: ""
            val productPrice = item.selectFirst(".spec li.price")?.text()?.replace("\\s+".toRegex(), "") ?: "0"

            println(
                """
                📌 상품명: $productName
                🔗 상품 URL: $productUrl
                🖼️ 상품 이미지: $productImageUrl
                💰 상품 가격: $productPrice
                """.trimIndent()
            )
            println("------------")
        }
    }
}