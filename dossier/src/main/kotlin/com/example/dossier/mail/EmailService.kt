package com.example.dossier.mail

import com.example.dossier.dto.Application
import com.example.dossier.dto.EmailDto
import java.io.IOException
import javax.mail.MessagingException

interface EmailService {
    @Throws(MessagingException::class)
    fun sendApplicationDenied(event: EmailDto?)
    fun sendFinishRegistration(event: EmailDto?)
    fun sendCreateDocuments(event: EmailDto?)

    @Throws(MessagingException::class, IOException::class)
    fun sendSendDocuments(event: Application?)
    fun sendSendSes(event: EmailDto?)
    fun sendCreditIssued(event: EmailDto?)
}
