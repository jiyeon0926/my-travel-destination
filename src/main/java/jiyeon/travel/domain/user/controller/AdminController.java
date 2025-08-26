package jiyeon.travel.domain.user.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.user.dto.PartnerSignupReqDto;
import jiyeon.travel.domain.user.dto.PartnerSignupResDto;
import jiyeon.travel.domain.user.service.AdminService;
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
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/partners")
    public ResponseEntity<PartnerSignupResDto> signupPartner(@Valid @RequestBody PartnerSignupReqDto partnerSignupReqDto) {
        PartnerSignupResDto partnerSignupResDto = adminService.signupPartner(
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
