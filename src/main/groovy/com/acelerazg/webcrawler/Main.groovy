package com.acelerazg.webcrawler

class Main {

    static void main(String[] args) {

        TissFile tissFile = new TissFile()
        TissVersionHistory tissVersionHistory = new TissVersionHistory()
        ErrorTableAns errorTableAns = new ErrorTableAns()

        tissFile.tissDocument()
        tissVersionHistory.tissVersionHistory()
        errorTableAns.errorTable()
    }
}
