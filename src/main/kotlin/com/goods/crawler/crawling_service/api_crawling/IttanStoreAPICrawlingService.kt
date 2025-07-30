package com.goods.crawler.crawling_service.api_crawling

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.goods.crawler.enums.WebSite
import com.goods.crawler.product.service.ProductService
import com.goods.crawler.product.vo.Product
import com.goods.crawler.util.ThreadUtil
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class IttanStoreAPICrawlingService(
    private val productService: ProductService,
    private val webClient: WebClient
) : APICrawlingService {

    companion object {
        private const val URL = "https://m.ittanstore.com/exec/front/Product/ApiProductNormal"
    }

    override val siteName = WebSite.ITTANSTORE.name.lowercase()

    override fun crawl() {
        println("사이트명: $siteName")

        val cateNo = 232
        val supplierCode = "S0000000"
        val bInitMore = "F"
        val count = "15"
        var page = 1

        while (true) {
            val responseBody = webClient.get()
                .uri("$URL?cate_no=$cateNo&supplier_code=$supplierCode&bInitMore=$bInitMore&count=$count&page=$page")
                .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/115.0.0.0 Safari/537.36")
                .header(HttpHeaders.REFERER, "https://m.ittanstore.com/")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "ko-KR,ko;q=0.9")
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: break

            val productList = parseProductList(responseBody)
            if (productList.isEmpty() || productList.size < count.toInt()) break

            println("크롤링 페이지: $page, 아이템 개수: ${productList.size}")
            productService.insertProducts(productList)

            if (page > 4) break
            page++
            ThreadUtil.sleepRandomly()
        }
    }


    override fun parseProductList(
        json: String
    ): List<Product> {
        val mapper = jacksonObjectMapper()
        val node = mapper.readTree(json)
        val dataNode = node["rtn_data"] ?: return emptyList()
        val listNode = dataNode["data"] ?: return emptyList()

        return listNode.map {
            Product(
                name = it["product_name"]?.asText() ?: "",
                url = "https://m.ittanstore.com/product/detail.html?product_no=${it["product_no"]?.asText()}",
                imageUrl = "https:" + (it["image_medium"]?.asText() ?: ""),
                price = it["product_price"]?.asText() ?: "0"
            )
        }
    }

}