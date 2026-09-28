package com.example.MaintainIt.service;

import com.example.MaintainIt.dto.TechnicianRequest;
import com.example.MaintainIt.exception.BusinessRuleException;
import com.example.MaintainIt.exception.ResourceNotFoundException;
import com.example.MaintainIt.model.Technician;
import com.example.MaintainIt.repository.TechnicianRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class TechnicianService {

    /*
     * Gmail local-part rule (Google's own rule):
     * only letters, numbers and single dots between
     * characters. Dots are ignored by Gmail, so we
     * strip them before checking the length.
     */
    private static final Pattern GMAIL_LOCAL_PART =
            Pattern.compile("^[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*$");

    private final TechnicianRepository technicianRepository;

    public TechnicianService(
            TechnicianRepository technicianRepository) {

        this.technicianRepository =
                technicianRepository;
    }


    @Transactional
    public Technician createTechnician(
            TechnicianRequest request) {

        String name = request.name().trim();

        String email = request.email().trim();

        /*
         * BUSINESS RULE:
         *
         * A technician's name cannot be only numbers
         * or contain digits. This is already blocked
         * by the DTO's @Pattern, but we defend again
         * here in case this method is ever called
         * from somewhere that skips bean validation.
         */
        if (name.chars().anyMatch(Character::isDigit)) {

            throw new BusinessRuleException(
                    "Technician name cannot contain numbers."
            );
        }


        /*
         * BUSINESS RULE:
         *
         * Two technicians cannot share the same
         * email address (case-insensitive).
         */
        if (technicianRepository
                .existsByEmailIgnoreCase(email)) {

            throw new BusinessRuleException(
                    "A technician with email '"
                            + email
                            + "' already exists."
            );
        }


        validateGmailAddressIfApplicable(email);


        Technician technician = new Technician();

        technician.setName(name);

        technician.setEmail(email);

        if (request.phone() != null
                && !request.phone().isBlank()) {

            technician.setPhone(
                    request.phone().trim()
            );

        } else {

            technician.setPhone(null);
        }

        return technicianRepository.save(technician);
    }


    public List<Technician> getAllTechnicians() {

        return technicianRepository.findAll();
    }


    public Technician getTechnician(Long id) {

        return technicianRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician with ID "
                                        + id
                                        + " not found."
                        )
                );
    }


    /*
     * Extra checks specifically for Gmail / Googlemail
     * addresses, since Gmail has stricter rules than
     * the generic email format:
     *
     *  - the part before '@' (ignoring dots) must be
     *    between 6 and 30 characters
     *  - only letters, numbers and single dots between
     *    characters are allowed (no leading/trailing or
     *    consecutive dots, no other symbols)
     */
    private void validateGmailAddressIfApplicable(
            String email) {

        int atIndex = email.lastIndexOf('@');

        if (atIndex <= 0) {
            // Already caught by @Email / @Pattern, but guard anyway.
            return;
        }

        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1).toLowerCase();

        boolean isGmail =
                domain.equals("gmail.com")
                        || domain.equals("googlemail.com");

        if (!isGmail) {
            return;
        }

        if (!GMAIL_LOCAL_PART.matcher(localPart).matches()) {

            throw new BusinessRuleException(
                    "Gmail addresses can only contain letters, numbers, "
                            + "and single dots between characters "
                            + "(no starting/ending or consecutive dots, no other symbols)."
            );
        }

        String withoutDots =
                localPart.replace(".", "");

        if (withoutDots.length() < 6
                || withoutDots.length() > 30) {

            throw new BusinessRuleException(
                    "Gmail addresses must have between 6 and 30 characters "
                            + "before the '@' symbol (dots are not counted)."
            );
        }
    }
}
