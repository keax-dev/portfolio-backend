package com.keax.email.infrastructure.out.email;

import com.keax.email.domain.ports.out.EmailSenderPort;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.stereotype.Component;
import com.keax.email.domain.model.Contact;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;

@Component
public class MailContactEmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final String from;
    private final String to;

    public MailContactEmailSenderAdapter(
            JavaMailSender mailSender,
            SpringTemplateEngine templateEngine,
            @Value("${spring.mail.username}") String from,
            @Value("${spring.mail.to}") String to
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.from = from;
        this.to = to;
    }

    @Override
    public void sendContactEmail(Contact contact) {
        try {
            Context ctx = new Context();
            ctx.setVariable("name", contact.getName());
            ctx.setVariable("email", contact.getEmail());
            ctx.setVariable("messageHtml", nl2br(escape(contact.getMessage())));

            String html = templateEngine.process("contact-email", ctx);
            String plain = "Name: " + safe(contact.getName()) +
                    "\nEmail: " + safe(contact.getEmail()) +
                    "\nMessage: " + safe(contact.getMessage());

            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("New Contact me");
            helper.setText(plain, html);

            mailSender.send(mime);
        } catch (Exception ex) {
            throw new ExternalServiceException("Email service is not available", ex);
        }
    }

    private static String escape(String s){
        if (s == null) return "";

        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                .replace("\"","&quot;").replace("'","&#39;");
    }

    private static String nl2br(String s){
        return s.replace("\n", "<br>");
    }

    private static String safe(String s){
        return s == null ? "" : s;
    }

}
