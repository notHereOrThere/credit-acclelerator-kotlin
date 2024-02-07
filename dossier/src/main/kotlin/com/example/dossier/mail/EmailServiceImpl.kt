package com.example.dossier.mail

import com.example.dossier.dto.Application
import com.example.dossier.dto.EmailDto
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import javax.mail.MessagingException

@Service
@RequiredArgsConstructor
class EmailServiceImpl (private val mailSender: JavaMailSender) : EmailService {

    var mailMessage = SimpleMailMessage()

    @Value("\${spring.mail.username}")
    private val username: String? = null

    @Throws(MessagingException::class)
    override fun sendApplicationDenied(event: EmailDto?) {
        sendSimpleTextMessage(event, "Отказ в предоставлении кредита")
    }

    override fun sendFinishRegistration(event: EmailDto?) {
        sendSimpleTextMessage(event, "Завершение регистрации")
    }

    override fun sendCreateDocuments(event: EmailDto?) {
        sendSimpleTextMessage(event, "Выгрузка документов")
    }

    override fun sendSendSes(event: EmailDto?) {
        sendSimpleTextMessage(event, "СЭС код")
    }

    override fun sendCreditIssued(event: EmailDto?) {
        sendSimpleTextMessage(event, "Выдача кредита")
    }

    @Throws(MessagingException::class, IOException::class)
    override fun sendSendDocuments(event: Application?) {
        sendEmailWithAttachment(event)
    }

    @Throws(MessagingException::class)
    private fun sendEmailWithAttachment(event: Application?) {
        val message = mailSender!!.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        event?.client?.email?.let {
            helper.setTo(it)
        }
        helper.setFrom(username!!)
        helper.setSubject("Документы по кредиту")

        // Текст сообщения
        helper.setText(event.toString())

        // Генерация файла
        try {
            ByteArrayOutputStream().use { outputStream ->
                outputStream.write(event.toString().toByteArray(StandardCharsets.UTF_8))
                var byteArrayResource = ByteArrayResource(outputStream.toByteArray())
                // Добавление вложения
                helper.addAttachment("Заявка.txt", byteArrayResource)
                outputStream.write(
                    event?.credit?.paymentSchedule.toString().toByteArray()
                )
                byteArrayResource = ByteArrayResource(outputStream.toByteArray())
                helper.addAttachment("График_платежей.txt", byteArrayResource)
                outputStream.write(buildCreditAct(event)!!.toByteArray(StandardCharsets.UTF_8))
                byteArrayResource = ByteArrayResource(outputStream.toByteArray())
                helper.addAttachment("Кредитный_договор.txt", byteArrayResource)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        mailSender.send(message)
    }

    private fun buildCreditAct(application: Application?): String? {
        return try {
            val resource = ClassPathResource("Credit_act_template.txt")
            val template = BufferedReader(
                InputStreamReader(
                    resource.inputStream, StandardCharsets.UTF_8
                )
            )
                .lines().collect(Collectors.joining("\n"))
            val fio: String = application?.client?.lastName + " " + application?.client?.firstName
            template.replace("{name}", fio)
                .replace("{amount}", application?.credit?.amount.toString())
                .replace("{term}", application?.credit?.term.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun sendSimpleTextMessage(event: EmailDto?, subject: String) {
        mailMessage.from = username
        mailMessage.setTo(event!!.email)
        mailMessage.subject = subject
        val str = StringBuilder()
        str.append("Уважаемый ").append(event.fio).append("!\n")
            .append(event.emailText)
        mailMessage.text = str.toString()
        mailSender!!.send(mailMessage)
    }
}