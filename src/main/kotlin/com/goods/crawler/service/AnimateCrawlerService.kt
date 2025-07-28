package com.goods.crawler.service

import com.goods.crawler.enums.WebSite
import org.jsoup.Jsoup
import org.springframework.stereotype.Component

@Component
class AnimateCrawlerService : CrawlerService {
    override val siteName = WebSite.ANIMATE.name.lowercase()

    override fun crawl(url: String) {
        val docs = Jsoup.connect(url).get()
        println("사이트명: $siteName")

        val items = docs.select("li[class^=goodsitem]")
        println("크롤링 URL: $url, 아이템 개수: ${items.size}")

        for (item in items) {
            val productImageUrl = item.selectFirst(".item_photo_box a img")?.attr("src") ?: ""
            val productUrlRelative = item.selectFirst(".item_tit_box a")?.attr("href") ?: ""
            val productUrl = "https://www.animate-onlineshop.co.kr/goods/${productUrlRelative.removePrefix("../goods/")}"
            val productName = item.selectFirst(".item_name")?.text()?.trim() ?: ""
            val productPrice = item.selectFirst(".item_price span")?.text()?.replace("\\s+".toRegex(), "") ?: "0"

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