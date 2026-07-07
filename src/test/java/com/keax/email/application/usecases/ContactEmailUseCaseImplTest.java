package com.keax.email.application.usecases;

import com.keax.email.domain.model.Contact;
import com.keax.email.domain.ports.out.EmailSenderPort;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Verifica que el caso de uso de contacto delegue el envío y traduzca fallos
 * del proveedor sin perder su causa técnica.
 */
class ContactEmailUseCaseImplTest {

    @Test
    void returnsContactAfterSuccessfulDelivery() {
        // Arrange: se inyecta un puerto de correo exitoso.
        EmailSenderPort sender = mock(EmailSenderPort.class);
        ContactEmailUseCaseImpl useCase = useCase(sender);
        Contact contact = new Contact("Keax", "keax@example.com", "Hello");

        // Act: se envía el mensaje.
        Contact result = useCase.sendContactEmail(contact);

        // Assert: se delega exactamente una vez y se conserva el modelo.
        assertSame(contact, result);
        verify(sender).sendContactEmail(contact);
    }

    @Test
    void wrapsProviderFailureWithOriginalCause() {
        // Arrange: el proveedor SMTP falla.
        EmailSenderPort sender = mock(EmailSenderPort.class);
        IllegalStateException cause = new IllegalStateException("smtp unavailable");
        Contact contact = new Contact("Keax", "keax@example.com", "Hello");
        doThrow(cause).when(sender).sendContactEmail(contact);
        ContactEmailUseCaseImpl useCase = useCase(sender);

        // Act: se ejecuta el caso de uso.
        ExternalServiceException exception = assertThrows(
                ExternalServiceException.class,
                () -> useCase.sendContactEmail(contact)
        );

        // Assert: la API recibe un error estable y logs conservan la causa.
        assertSame(cause, exception.getCause());
        assertEquals("There was an error sending the email, try again", exception.getMessage());
    }

    private ContactEmailUseCaseImpl useCase(EmailSenderPort sender) {
        // Inyecta el puerto en la implementación legada.
        ContactEmailUseCaseImpl useCase = new ContactEmailUseCaseImpl();
        ReflectionTestUtils.setField(useCase, "emailSenderPort", sender);
        return useCase;
    }

}
