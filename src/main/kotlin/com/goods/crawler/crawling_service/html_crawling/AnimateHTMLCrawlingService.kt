package com.goods.crawler.crawling_service.html_crawling

import com.goods.crawler.enums.WebSite
import com.goods.crawler.product.service.ProductService
import com.goods.crawler.product.vo.Product
import com.goods.crawler.util.ThreadUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.stereotype.Component

@Component
class AnimateHTMLCrawlingService(
    private val productService: ProductService
) : HTMLCrawlingService {

    override val siteName = WebSite.ANIMATE.name.lowercase()


    override fun crawl(
        baseUrl: String
    ) {
        val firstPageUrl = "$baseUrl&page=1"
        val firstPageDoc = Jsoup.connect(firstPageUrl).get()
        val lastPageNumber = getLastPageNumber(firstPageDoc)
        println("마지막 페이지 번호: $lastPageNumber")

        for (page in 1..lastPageNumber) {
            val url = "$baseUrl&page=$page"
            println("📄 페이지 $page 크롤링 중: $url")

            val docs = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/115.0.0.0 Safari/537.36")
                .header("Accept-Language", "ko-KR,ko;q=0.9")
                .header("Referer", "https://www.google.com/")
                .get()
            val items = docs.select("li[class^=goodsitem]")
            val productList = parseProductList(items)

            println("크롤링 URL: $url, 아이템 개수: ${productList.size}")
            productService.insertProducts(productList)

            ThreadUtil.sleepRandomly()
            if (page > 4) break
        }
    }


    override fun parseProductList(
        items: Elements
    ): List<Product> {
        val productList = mutableListOf<Product>()

        for (item in items) {
            val productUrlRelative = item.selectFirst(".item_tit_box a")?.attr("href") ?: ""

            productList.add(
                Product(
                    name = item.selectFirst(".item_name")?.text()?.trim() ?: "",
                    url = "https://www.animate-onlineshop.co.kr/goods/${productUrlRelative.removePrefix("../goods/")}",
                    imageUrl = item.selectFirst(".item_photo_box a img")?.attr("src") ?: "",
                    price = item.selectFirst(".item_price span")?.text()?.replace("\\s+".toRegex(), "") ?: "0"
                )
            )
        }

        return productList
    }


    private fun getLastPageNumber(
        doc: Document
    ): Int {
        val lastPageHref = doc.selectFirst("li.btn_page_last a")
            ?.attr("href") ?: return 1

        val match = Regex("page=(\\d+)").find(lastPageHref)
        return match?.groupValues?.get(1)?.toIntOrNull() ?: 1
    }

}