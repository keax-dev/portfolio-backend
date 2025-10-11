package com.keax.application.services.Implementation;

import com.keax.application.services.Interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Email.ContactEmailUseCase;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Contact;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private ContactEmailUseCase contactEmailUseCase;

    @Override
    public Contact sendContactEmail(Contact contact) {
        return contactEmailUseCase.sendContactEmail(contact);
    }

}
