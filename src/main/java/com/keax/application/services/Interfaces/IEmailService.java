package com.keax.application.services.Interfaces;

import com.keax.domain.models.Contact;

public interface IEmailService {

    Contact sendContactEmail(Contact contact);

}
