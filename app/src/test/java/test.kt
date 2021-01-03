

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


fun main(){

    sendEmail()
    }
/*
    //Old way only required API 16
    val dateToday = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    val formattedDate = dateFormat.format(dateToday.time)
    dateToday.add(Calendar.DATE, 66)
    val formattedDate2 = dateFormat.format(dateToday.time)
    val readDate = dateFormat.parse(formattedDate)
    val readDate2 = dateFormat.parse(formattedDate2)
    val diff = readDate2.time - readDate.time
    val daysDiff = (TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS))
    print(daysDiff)

 */

    /*//New Way requiring API 26
    val dateToday = LocalDate.now()
    val formattedDate = dateToday.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    println(formattedDate)
    val dateTom = dateToday.plusDays( 3)
    val formattedDate2 = dateTom.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    println(formattedDate2)
    val readDate = LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    val readDate2 = LocalDate.parse(formattedDate2, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    val daysDiff = Period.between(readDate,readDate2).days
    print(daysDiff)

     */
    fun sendEmail(){
        val username = ""
        val password = ""

        val prop = Properties()
        prop["mail.smtp.host"] = "smtp.gmail.com"
        prop["mail.smtp.port"] = "587"
        prop["mail.smtp.auth"] = "true"
        prop["mail.smtp.starttls.enable"] = "true" //TLS


        val session = Session.getInstance(prop,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password)
                }
            })

        try {
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress("from@gmail.com"))
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("to_username_a@gmail.com, to_username_b@yahoo.com")
            )
            message.subject = "Testing Gmail TLS"
            message.setText(
                """Dear Mail Crawler,

 Please do not spam my email!"""
            )
            Transport.send(message)
            println("Done")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

