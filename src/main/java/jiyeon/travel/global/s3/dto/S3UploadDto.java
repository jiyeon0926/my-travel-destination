package jiyeon.travel.global.s3.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class S3UploadDto {

    private final String url;
    private final String key;
}
