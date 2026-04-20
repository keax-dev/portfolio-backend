package com.keax.email.infrastructure.in.web.mapper;

import com.keax.email.infrastructure.in.web.dto.ContactDTO;
import com.keax.email.domain.model.Contact;

public final class ContactWebMapper {

    public static Contact toDomain(ContactDTO dto) {
        return new Contact(
                dto.getName(),
                dto.getEmail(),
                dto.getMessage()
        );
    }

    public static ContactDTO fromDomain(Contact contact) {
        return new ContactDTO(
                contact.getName(),
                contact.getEmail(),
                contact.getMessage()
        );
    }

}
