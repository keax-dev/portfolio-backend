package com.keax.email.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.email.domain.ports.in.ContactEmailUseCase;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.email.domain.ports.out.EmailSenderPort;
import org.springframework.stereotype.Service;
import com.keax.email.domain.model.Contact;

@Service
@RequiredArgsConstructor
public class ContactEmailUseCaseImpl implements ContactEmailUseCase {
    private final EmailSenderPort emailSenderPort;
    public Contact sendContactEmail(Contact contact){

        try{
            emailSenderPort.sendContactEmail(contact);
        }catch (Exception ex){
            throw new ExternalServiceException("There was an error sending the email, try again", ex);
        }

        return contact;
    }

}
