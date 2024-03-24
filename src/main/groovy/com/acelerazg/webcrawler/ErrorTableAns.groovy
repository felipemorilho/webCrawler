package com.acelerazg.webcrawler

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class ErrorTableAns {

    GetPages crawler = new GetPages()
    DownloadFile file = new DownloadFile()

    protected void errorTable() {

        try {

            String link = crawler.linkTiss()
            Document page = crawler.getLink(link)
            Element pageElement = page.getElementsByClass('alert-link internal-link').first()
            String nextPageLink = pageElement.attr('href')

            Document nextPage = crawler.getLink(nextPageLink)
            Element nextPageElement = nextPage.getElementsByClass('internal-link').first()
            String finalLink = nextPageElement.attr('href')

            file.downloadFile(finalLink, "TabelaErrosDeEnvioANS.xlsx")

        } catch (Exception e) {

            e.printStackTrace("\n\nErro ao baixar tabela de erros.\nErro: ${e.message} \n")
        }
    }

}
