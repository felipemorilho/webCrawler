package com.acelerazg.webcrawler

import com.acelerazg.webcrawler.email.EmailContacts
import com.acelerazg.webcrawler.email.SendEmail

class Main {

    static void main(String[] args) {

        TissFile tissFile = new TissFile()
        TissVersionHistory tissVersionHistory = new TissVersionHistory()
        ErrorTableAns errorTableAns = new ErrorTableAns()
        EmailContacts emailContacts = new EmailContacts()
        SendEmail sendEmail = new SendEmail()

        tissFile.tissDocument()
        tissVersionHistory.tissVersionHistory()
        errorTableAns.errorTable()

        emailContacts.addEmail("Felipe", "felipemc@gmail.com")
        emailContacts.updateEmail("felipemc@gmail.com", "", "felipemdecastro@gmail.com")
        emailContacts.deleteEmail("teste@gmail.com")

//        sendEmail.sendEmail()
    }
}
