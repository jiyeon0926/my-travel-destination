package jiyeon.travel.domain.partner.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.partner.dto.PartnerSignupReqDto;
import jiyeon.travel.domain.partner.dto.PartnerSignupResDto;
import jiyeon.travel.domain.partner.service.PartnerAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class PartnerAdminController {

    private final PartnerAdminService partnerAdminService;

    @PostMapping("/partners")
    public ResponseEntity<PartnerSignupResDto> signupPartner(@Valid @RequestBody PartnerSignupReqDto partnerSignupReqDto) {
        PartnerSignupResDto partnerSignupResDto = partnerAdminService.signupPartner(
                partnerSignupReqDto.getEmail(),
                partnerSignupReqDto.getPassword(),
                partnerSignupReqDto.getName(),
                partnerSignupReqDto.getBusinessNumber(),
                partnerSignupReqDto.getAddress(),
                partnerSignupReqDto.getPhone()
        );

        return new ResponseEntity<>(partnerSignupResDto, HttpStatus.CREATED);
    }
}
