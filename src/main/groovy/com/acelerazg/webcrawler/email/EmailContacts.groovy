package com.acelerazg.webcrawler.email

import com.opencsv.CSVReader
import com.opencsv.CSVWriter

class EmailContacts {

    void writeToCSV(List<String[]> data) {

        try {


            CSVWriter writer = new CSVWriter(new FileWriter("./emailList/emails.csv"))
            writer.writeAll(data)
            writer.close()

        } catch (Exception e) {

            e.printStackTrace()
        }
    }

    List<String[]> readEmails() {

        try {

            List<String[]> emails = []
            new CSVReader(new FileReader("./emailList/emails.csv")).each { row ->
                emails.add(row)
            }

            return emails

        } catch (Exception e) {

           return e.printStackTrace()
        }
    }

    void addEmail(String name, String email) {

        try {

            List<String[]> emails = readEmails()

            boolean emailExists = emails.any {row -> row[1] == email}

            if(!emailExists) {

                emails.add([name, email] as String[])
                writeToCSV(emails)

            } else  {

                throw new IllegalArgumentException("Email já cadastrado.");
            }

        } catch (Exception e) {

                e.printStackTrace()
        }
    }

    void updateEmail(String email, String name, String newEmail) {

        try {

            List<String[]> emails = readEmails()

            emails.each { row ->

                if (row[1] == email) {

                    row[0] = name == "" ? row[0] : name
                    row[1] = newEmail

                    return
                }
            }

            writeToCSV(emails)

        } catch (Exception e) {

            e.printStackTrace()
        }
    }

    void deleteEmail(String email) {

        try {

            List<String[]> emails = readEmails()

            emails.removeIf { it[1] == email }

            writeToCSV(emails)

        } catch (Exception e) {

            e.printStackTrace()
        }
    }


}
