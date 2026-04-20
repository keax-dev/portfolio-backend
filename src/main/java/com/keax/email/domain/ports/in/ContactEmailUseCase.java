package com.keax.email.domain.ports.in;

import com.keax.email.domain.model.Contact;

public interface ContactEmailUseCase {

    Contact sendContactEmail(Contact contact);

}
