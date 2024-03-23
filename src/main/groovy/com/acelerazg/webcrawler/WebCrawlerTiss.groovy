package com.acelerazg.webcrawler

import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class WebCrawlerTiss {

    private static Document getLink(String url) {

        return HttpBuilder.configure {
            request.uri = url
        }.get() as Document

    }

    static String linkTiss() {

        Document page = getLink('https://www.gov.br/ans/pt-br')
        Element pageElement = page.select('a[href$=/assuntos/prestadores]').first()
        String link = pageElement.attr('href')

        Document nextPage = getLink(link)
        Element nextPageElement = nextPage.select('a[href$=tiss]').first()
        String nextLink = nextPageElement.attr('href')

        return nextLink
    }

    static void tissDocument() {

        String link = linkTiss()
        Document page = getLink(link)
        Element pageElement = page.getElementsByClass('internal-link').first()
        String nextPageLink = pageElement.attr('href')

        Document nextPage = getLink(nextPageLink)
        String finalLink = nextPage.select('tbody tr:first-child a').attr('href')

        downloadFile(finalLink, "DocumentoTiss")
    }

    static void tissVersionHistory() {

        String link = linkTiss()
        Document page = getLink(link)
        Element pageElement = page.getElementsByClass('external-link').first()
        String nextPageLink = pageElement.attr('href')

        Document nextPage = getLink(nextPageLink)
        Element table = nextPage.select("#content-core table").first()

        Element thead = table.select('thead').first()
        Element tbody = table.select('tbody').first()

        Elements theadElements = thead.select('th:lt(3)')
        List<String> headers = theadElements.each {it.text() }

        if (headers.size() < 3 || !headers.containsAll(["Competência", "Publicação", "Início de Vigência"])) {
            throw new Exception("Required headers not found in the table.")
        }

        Elements tbodyElements = tbody.select('tr')
        List<List<String>> tableValues = []

        boolean stopGetValues = false

        tbodyElements.each{tr ->

            while(!stopGetValues) {

                List<String> rowValues = []
                Elements tdElements = tbodyElements.select('td')
                String competence = tdElements.get(0).text()
                String publication = tdElements.get(1).text()
                String validityBeginning = tdElements.get(2).text()
                rowValues.add(competence)
                rowValues.add(publication)
                rowValues.add(validityBeginning)

                tableValues.add(rowValues)

            }

        }

        print(nextPageLink)
    }

    static downloadFile(String url, String path = "./downloads", String fileName) {

        File pathToSave = new File(path, fileName)
        pathToSave.parentFile.mkdirs()

        HttpBuilder.configure {

            request.uri = url

        }.get {

            Download.toFile(delegate, pathToSave)

        }
    }
}
