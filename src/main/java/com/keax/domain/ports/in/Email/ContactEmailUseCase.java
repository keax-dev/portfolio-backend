package com.keax.domain.ports.in.Email;

import com.keax.domain.models.Contact;

public interface ContactEmailUseCase {

    Contact sendContactEmail(Contact contact);

}
