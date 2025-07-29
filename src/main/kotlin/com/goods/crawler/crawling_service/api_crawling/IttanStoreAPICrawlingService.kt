package com.goods.crawler.crawling_service.api_crawling

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.goods.crawler.enums.WebSite
import com.goods.crawler.product.vo.Product
import com.goods.crawler.product.service.ProductService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class IttanStoreAPICrawlingService(
    private val productService: ProductService
) : APICrawlingService {

    override val siteName = WebSite.ITTANSTORE.name.lowercase()
    private val webClient = WebClient.create("https://m.ittanstore.com")


    override fun crawl() {
        println("사이트명: $siteName")

        val cateNo = 232
        val supplierCode = "S0000000"
        val bInitMore = "F"
        val count = "15"
        var page = 1

        while (true) {
            val responseBody = webClient.get()
                .uri("/exec/front/Product/ApiProductNormal?cate_no=$cateNo&supplier_code=$supplierCode&bInitMore=$bInitMore&count=$count&page=$page")
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: break

            val productList = parseProductList(responseBody)
            if (productList.isEmpty() || productList.size < count.toInt()) break

            println("크롤링 페이지: $page, 아이템 개수: ${productList.size}")
            productService.insertProduct(productList)

            page++
            Thread.sleep(2000L)
            if (page > 5) break
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