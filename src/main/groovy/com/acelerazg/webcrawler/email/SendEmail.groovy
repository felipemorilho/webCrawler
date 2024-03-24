package com.acelerazg.webcrawler.email

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.*

class SendEmail {

    String host = "smtp.gmail.com"
    int port = 587
    String username = "felipe.testsend@gmail.com"
    String password = "Asw11Asw@"

    EmailContacts emailContacts = new EmailContacts()

    void sendEmail() {

        Properties props = new Properties()
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.smtp.host", host)
        props.put("mail.smtp.port", port)

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password)
            }
        })

        try {

            List<String[]> emailList = emailContacts.readEmails()

            emailList.each { row ->

                MimeMessage message = new MimeMessage(session)

                message.setFrom(new InternetAddress(username))

                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(row[1]))

                message.setSubject("Relatório TISS")

                Multipart multipart = new MimeMultipart()

                MimeBodyPart textPart = new MimeBodyPart()

                textPart.setText("Olá ${row[0]}, \nSegue arquivos TISS em anexo.")

                multipart.addBodyPart(textPart)

                List<File> attachmentFiles = getAttachments()

                attachmentFiles.each { file ->

                    MimeBodyPart attachmentPart = new MimeBodyPart()
                    DataSource source = new FileDataSource(file)
                    attachmentPart.setDataHandler(new DataHandler(source))
                    attachmentPart.setFileName(file.getName())
                    multipart.addBodyPart(attachmentPart)
                }

                message.setContent(multipart)

                message.setText("Olá ${row[0]}, \nSegue arquivos TISS em anexo.")

                Transport.send(message)

            }
        } catch (MessagingException e) {

            e.printStackTrace()
        }
    }

    List<File> getAttachments() {

        File path = new File("./doenloads")
        return path.listFiles()
    }

}