package com.goods.crawler.util

class ThreadUtil {
    companion object {
        fun sleepRandomly() {
            try {
                Thread.sleep((1000L..4000L).random())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}