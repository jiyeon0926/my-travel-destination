package jiyeon.travel.domain.partner.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.partner.dto.PartnerProfileResDto;
import jiyeon.travel.domain.partner.dto.PartnerSignupReqDto;
import jiyeon.travel.domain.partner.dto.PartnerSignupResDto;
import jiyeon.travel.domain.partner.dto.PartnerUpdateReqDto;
import jiyeon.travel.domain.partner.service.PartnerAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/partners")
@RequiredArgsConstructor
public class PartnerAdminController {

    private final PartnerAdminService partnerAdminService;

    @PostMapping
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

    @PatchMapping("/{partnerId}")
    public ResponseEntity<PartnerProfileResDto> updatePartnerById(@PathVariable Long partnerId,
                                                                  @RequestBody PartnerUpdateReqDto partnerUpdateReqDto) {
        PartnerProfileResDto partnerProfileResDto = partnerAdminService.updatePartnerById(partnerId, partnerUpdateReqDto.getName(), partnerUpdateReqDto.getPhone(), partnerUpdateReqDto.getAddress());

        return new ResponseEntity<>(partnerProfileResDto, HttpStatus.OK);
    }

    @GetMapping("/{partnerId}")
    public ResponseEntity<PartnerProfileResDto> findPartnerById(@PathVariable Long partnerId) {
        PartnerProfileResDto partnerProfileResDto = partnerAdminService.findPartnerById(partnerId);

        return new ResponseEntity<>(partnerProfileResDto, HttpStatus.OK);
    }

    @DeleteMapping("/{partnerId}")
    public ResponseEntity<Void> deletePartnerById(@PathVariable Long partnerId) {
        partnerAdminService.deletePartnerById(partnerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
