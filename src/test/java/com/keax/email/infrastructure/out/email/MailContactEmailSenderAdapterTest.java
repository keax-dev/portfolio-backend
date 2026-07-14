package com.keax.email.infrastructure.out.email;

import com.keax.email.domain.model.Contact;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica el adaptador SMTP sin red: plantilla, escape HTML, cabeceras del
 * mensaje y entrega final al JavaMailSender.
 */
class MailContactEmailSenderAdapterTest {

    @Test
    void buildsEscapedMultipartEmailAndSendsIt() throws Exception {
        // Arrange: se usa un MimeMessage real en memoria y colaboradores simulados.
        JavaMailSender mailSender = mock(JavaMailSender.class);
        SpringTemplateEngine templateEngine = mock(SpringTemplateEngine.class);
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("contact-email"), org.mockito.ArgumentMatchers.any(Context.class)))
                .thenReturn("<html>safe</html>");
        MailContactEmailSenderAdapter adapter = new MailContactEmailSenderAdapter(
                mailSender,
                templateEngine,
                "sender@example.com",
                "owner@example.com"
        );
        Contact contact = new Contact(
                "Keax",
                "keax@example.com",
                "<script>alert('x')</script>\nsecond line"
        );

        // Act: se construye y envía el correo.
        adapter.sendContactEmail(contact);

        // Assert: la plantilla recibe HTML escapado y el mensaje tiene destinatario/tema.
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq("contact-email"), contextCaptor.capture());
        assertEquals(
                "&lt;script&gt;alert(&#39;x&#39;)&lt;/script&gt;<br>second line",
                contextCaptor.getValue().getVariable("messageHtml")
        );
        assertEquals("New Contact me", mimeMessage.getSubject());
        assertEquals("owner@example.com", mimeMessage.getAllRecipients()[0].toString());
        verify(mailSender).send(mimeMessage);
    }

}
