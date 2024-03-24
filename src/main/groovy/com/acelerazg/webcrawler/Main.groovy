package com.acelerazg.webcrawler

import com.acelerazg.webcrawler.WebCrawlerTiss

class Main {

    static void main(String[] args) {

        WebCrawlerTiss webCrawlerTiss = new WebCrawlerTiss()

        webCrawlerTiss.tissDocument()
        webCrawlerTiss.tissVersionHistory()
        webCrawlerTiss.errorTable()
    }
}
