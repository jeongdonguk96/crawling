package com.goods.crawler

import com.goods.crawler.constant.CrawlingProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@EnableConfigurationProperties(CrawlingProperties::class)
class CrawlerApplication

fun main(args: Array<String>) {
	runApplication<CrawlerApplication>(*args)
}
