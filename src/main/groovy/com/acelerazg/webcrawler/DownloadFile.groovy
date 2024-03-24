package com.acelerazg.webcrawler

import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download

class DownloadFile {

    protected void downloadFile(String url, String path = "./downloads", String fileName) {

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
