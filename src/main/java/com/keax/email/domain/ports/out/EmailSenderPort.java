package com.keax.email.domain.ports.out;

import com.keax.email.domain.model.Contact;

public interface EmailSenderPort {

    void sendContactEmail(Contact contact);

}
