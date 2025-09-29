package jiyeon.travel.domain.partner.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.partner.dto.*;
import jiyeon.travel.domain.partner.service.PartnerAdminCommandService;
import jiyeon.travel.domain.partner.service.PartnerAdminQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/partners")
@RequiredArgsConstructor
public class PartnerAdminController {

    private final PartnerAdminCommandService partnerAdminCommandService;
    private final PartnerAdminQueryService partnerAdminQueryService;

    @PostMapping
    public ResponseEntity<PartnerSignupResDto> signupPartner(@Valid @RequestBody PartnerSignupReqDto partnerSignupReqDto) {
        PartnerSignupResDto partnerSignupResDto = partnerAdminCommandService.signupPartner(
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
        PartnerProfileResDto partnerProfileResDto = partnerAdminCommandService.updatePartnerById(partnerId, partnerUpdateReqDto.getName(), partnerUpdateReqDto.getPhone(), partnerUpdateReqDto.getAddress());

        return new ResponseEntity<>(partnerProfileResDto, HttpStatus.OK);
    }

    @DeleteMapping("/{partnerId}")
    public ResponseEntity<Void> deletePartnerById(@PathVariable Long partnerId) {
        partnerAdminCommandService.deletePartnerById(partnerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{partnerId}")
    public ResponseEntity<PartnerProfileResDto> findPartnerById(@PathVariable Long partnerId) {
        PartnerProfileResDto partnerProfileResDto = partnerAdminQueryService.findPartnerById(partnerId);

        return new ResponseEntity<>(partnerProfileResDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PartnerSimpleResDto>> searchPartners(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(required = false) String name) {
        List<PartnerSimpleResDto> partnerSimpleResDtos = partnerAdminQueryService.searchPartners(page, size, name);

        return new ResponseEntity<>(partnerSimpleResDtos, HttpStatus.OK);
    }
}
