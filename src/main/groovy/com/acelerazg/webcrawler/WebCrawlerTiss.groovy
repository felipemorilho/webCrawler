package com.acelerazg.webcrawler

import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class WebCrawlerTiss {

    private static Document getLink(String url) {

        try {

            return HttpBuilder.configure {

                request.uri = url

            }.get() as Document

        } catch (Exception e) {

            e.printStackTrace("\n\nErro ao configurar página.\nErro: ${e.message} \n")
            return null
        }
    }

    static String linkTiss() {

        try {

            Document page = getLink('https://www.gov.br/ans/pt-br')
            Element pageElement = page.select('a[href$=/assuntos/prestadores]').first()
            String link = pageElement.attr('href')

            Document nextPage = getLink(link)
            Element nextPageElement = nextPage.select('a[href$=tiss]').first()
            String nextLink = nextPageElement.attr('href')

            return nextLink

        } catch (Exception e) {

            e.printStackTrace("\n\nErro ao acessar página.\nErro: ${e.message} \n")
            return null
        }
    }

    static void tissDocument() {

        try {

            String link = linkTiss()
            Document page = getLink(link)
            Element pageElement = page.getElementsByClass('internal-link').first()
            String nextPageLink = pageElement.attr('href')

            Document nextPage = getLink(nextPageLink)
            String finalLink = nextPage.select('tbody tr:first-child a').attr('href')

            downloadFile(finalLink, "DocumentoTiss")

        } catch (Exception e) {

            e.printStackTrace("\n\nErro ao fazer download do documento TISS.\nErro: ${e.message} \n")
        }
    }

    static void tissVersionHistory() {

        try {

            String link = linkTiss()
            Document page = getLink(link)
            Element pageElement = page.getElementsByClass('external-link').first()
            String nextPageLink = pageElement.attr('href')

            Document nextPage = getLink(nextPageLink)
            Element table = nextPage.select("#content-core table").first()

            Element thead = table.select('thead').first()
            Element tbody = table.select('tbody').first()

            Elements theadElements = thead.select('th:lt(3)')
            List<String> headers = []
            theadElements.each { th ->
                headers.add(th.text())
            }

            if (headers.size() < 3 || !headers.containsAll(["Competência", "Publicação", "Início de Vigência"])) {
                throw new Exception("Required headers not found in the table.")
            }

            Elements tbodyElements = tbody.select('tr')
            List<List<String>> tableValues = []

            boolean stopGetValues = false

            for (Element tr : tbodyElements) {

                if (!stopGetValues) {

                    List<String> rowValues = []
                    Elements tdElements = tr.select('td')
                    String competence = tdElements.get(0).text()
                    String publication = tdElements.get(1).text()
                    String validityBeginning = tdElements.get(2).text()
                    rowValues.add(competence)
                    rowValues.add(publication)
                    rowValues.add(validityBeginning)

                    tableValues.add(rowValues)

                    stopGetValues = competence == "Jan/2016"

                    if (stopGetValues) break
                }
            }

            String csvPath = "./downloads/tissVersion.csv"
            FileWriter csvWriter = new FileWriter(csvPath)

            csvWriter.append("Competência,Publicação,Início de Vigência")
            csvWriter.append('\n')

            tableValues.each { row ->

                csvWriter.append(row.join(','))
                csvWriter.append('\n')

            }

            csvWriter.flush()
            csvWriter.close()

        } catch (Exception e) {

            e.printStackTrace("\n\nErro ao capturar as versões.\nErro: ${e.message} \n")
        }
    }

    static downloadFile(String url, String path = "./download", String fileName) {

        try {

            File pathToSave = new File(path, fileName)

            HttpBuilder.configure {

                request.uri = url

            }.get {

                Download.toFile(delegate, pathToSave)

            }

        } catch (Exception e) {

            e.printStackTrace("\n\nErro ao fazer download.\nErro: ${e.message} \n")
        }
    }
}
