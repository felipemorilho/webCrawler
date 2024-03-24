package com.acelerazg.webcrawler

import groovyx.net.http.HttpBuilder
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class GetPages {

    protected Document getLink(String url) {

        try {

            return HttpBuilder.configure {

                request.uri = url

            }.get() as Document

        } catch (Exception e) {

            e.printStackTrace("\n\nErro ao configurar página.\nErro: ${e.message} \n")
            return null
        }
    }

    protected String linkTiss() {

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

}
