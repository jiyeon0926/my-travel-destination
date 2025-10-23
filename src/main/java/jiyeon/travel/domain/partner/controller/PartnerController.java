package jiyeon.travel.domain.partner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Partner",
        description = "업체가 본인의 프로필을 조회할 수 있는 기능을 제공합니다."
)
public class PartnerController {

    private final PartnerQueryService partnerQueryService;

    @GetMapping("/me")
    @Operation(summary = "업체 프로필 조회", description = "업체는 관리자에 의해 등록된 본인의 프로필을 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartnerProfileResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "업체를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<PartnerProfileResDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        PartnerProfileResDto partnerProfileResDto = partnerQueryService.getProfile(email);

        return new ResponseEntity<>(partnerProfileResDto, HttpStatus.OK);
    }
}
