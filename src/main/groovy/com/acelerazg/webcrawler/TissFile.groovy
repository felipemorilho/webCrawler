package com.acelerazg.webcrawler

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TissFile {

    GetPages crawler = new GetPages()
    DownloadFile file = new DownloadFile()

    protected void tissDocument() {

        try {

            String link = crawler.linkTiss()
            Document page = crawler.getLink(link)
            Element pageElement = page.getElementsByClass('internal-link').first()
            String nextPageLink = pageElement.attr('href')

            Document nextPage = crawler.getLink(nextPageLink)
            String finalLink = nextPage.select('tbody tr:first-child a').attr('href')

            file.downloadFile(finalLink, "DocumentoTiss.pdf")

        } catch (Exception e) {

            e.printStackTrace("\n\nErro ao fazer download do documento TISS.\nErro: ${e.message} \n")
        }
    }

}
