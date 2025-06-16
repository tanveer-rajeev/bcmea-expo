package com.betafore.evoting.Exception;

import com.betafore.evoting.OrganizerManagement.Organizer;
import com.betafore.evoting.OrganizerManagement.OrganizerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {

    @Autowired
    private OrganizerRepository organizerRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Organizer> organizerByEmail = organizerRepository.findOrganizerByEmail(email);
        return organizerByEmail.isEmpty();
    }
}
