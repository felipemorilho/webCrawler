package com.acelerazg.webcrawler

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class TissVersionHistory {

    GetPages crawler = new GetPages()

    protected void tissVersionHistory() {

        try {

            String link = crawler.linkTiss()
            Document page = crawler.getLink(link)
            Element pageElement = page.getElementsByClass('external-link').first()
            String nextPageLink = pageElement.attr('href')

            Document nextPage = crawler.getLink(nextPageLink)
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

}
