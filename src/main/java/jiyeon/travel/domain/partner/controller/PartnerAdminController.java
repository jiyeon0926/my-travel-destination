package jiyeon.travel.domain.partner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Partner Management",
        description = "관리자가 업체 정보를 등록, 수정, 삭제, 조회 및 검색할 수 있는 기능을 제공합니다."
)
public class PartnerAdminController {

    private final PartnerAdminCommandService partnerAdminCommandService;
    private final PartnerAdminQueryService partnerAdminQueryService;

    @PostMapping
    @Operation(summary = "업체 등록", description = "관리자가 직접 업체 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartnerSignupResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "관리자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일 또는 사업장번호 입니다.", content = @Content(mediaType = "application/json"))
    })
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
    @Operation(summary = "업체 수정", description = "관리자가 직접 업체 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartnerProfileResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "관리자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "업체를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<PartnerProfileResDto> updatePartnerById(@PathVariable Long partnerId,
                                                                  @RequestBody PartnerUpdateReqDto partnerUpdateReqDto) {
        PartnerProfileResDto partnerProfileResDto = partnerAdminCommandService.updatePartnerById(partnerId, partnerUpdateReqDto.getName(), partnerUpdateReqDto.getPhone(), partnerUpdateReqDto.getAddress());

        return new ResponseEntity<>(partnerProfileResDto, HttpStatus.OK);
    }

    @DeleteMapping("/{partnerId}")
    @Operation(summary = "업체 삭제", description = "관리자가 직접 업체 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "관리자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "업체를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deletePartnerById(@PathVariable Long partnerId) {
        partnerAdminCommandService.deletePartnerById(partnerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{partnerId}")
    @Operation(summary = "업체 상세 조회", description = "관리자가 업체 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartnerProfileResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "관리자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "업체를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<PartnerProfileResDto> findPartnerById(@PathVariable Long partnerId) {
        PartnerProfileResDto partnerProfileResDto = partnerAdminQueryService.findPartnerById(partnerId);

        return new ResponseEntity<>(partnerProfileResDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "업체 검색", description = "관리자는 업체명을 기준으로 업체를 검색할 수 있습니다.")
    @Parameter(name = "name", description = "업체명", example = "컴퍼니")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartnerProfileResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "관리자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "업체를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<PartnerSimpleResDto>> searchPartners(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(required = false) String name) {
        List<PartnerSimpleResDto> partnerSimpleResDtoList = partnerAdminQueryService.searchPartners(page, size, name);

        return new ResponseEntity<>(partnerSimpleResDtoList, HttpStatus.OK);
    }
}
