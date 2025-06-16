package com.betafore.evoting.UserManagement;

import com.betafore.evoting.GiftManagement.Gift;
import com.betafore.evoting.SeminarManagement.Seminar;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserConfirmationDto {
    private Set<Long> seminarList = new HashSet<>();
    private Set<Long> giftList = new HashSet<>();
    private String isInvolvedWithCeramic;
    private String designation;
    private String workAddress;
}
