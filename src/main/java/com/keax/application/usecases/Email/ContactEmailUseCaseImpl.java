package com.keax.application.usecases.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.keax.domain.ports.in.Email.ContactEmailUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import jakarta.mail.internet.MimeMessage;
import com.keax.domain.models.Contact;
import org.thymeleaf.context.Context;

@Component
public class ContactEmailUseCaseImpl implements ContactEmailUseCase {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.to}")
    private String to;

    public Contact sendContactEmail(Contact contact){

        try{
            Context ctx = new Context();
            ctx.setVariable("name", contact.getName());
            ctx.setVariable("email", contact.getEmail());

            String messageHtml = nl2br(escape(contact.getMessage()));
            ctx.setVariable("messageHtml", messageHtml);

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
        }catch (Exception ex){
            throw new ExceptionAlert("There was an error sending the email, try again");
        }

        return contact;
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
