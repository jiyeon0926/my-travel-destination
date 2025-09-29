package jiyeon.travel.domain.partner.controller;

import jiyeon.travel.domain.partner.dto.PartnerProfileResDto;
import jiyeon.travel.domain.partner.service.PartnerQueryService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerQueryService partnerQueryService;

    @GetMapping("/me")
    public ResponseEntity<PartnerProfileResDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        PartnerProfileResDto partnerProfileResDto = partnerQueryService.getProfile(email);

        return new ResponseEntity<>(partnerProfileResDto, HttpStatus.OK);
    }
}
