package jiyeon.travel.domain.blog.service;

import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import jiyeon.travel.domain.blog.repository.BlogImageRepository;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import jiyeon.travel.global.s3.dto.S3UploadDto;
import jiyeon.travel.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BlogImageService {

    private final BlogImageRepository blogImageRepository;
    private final S3Service s3Service;

    @Transactional
    public List<BlogImage> saveImages(Blog blog, List<MultipartFile> files) {
        boolean isImage = files.stream()
                .allMatch(file -> Objects.requireNonNull(file.getContentType()).startsWith("image"));
        if (!isImage) {
            throw new CustomException(ErrorCode.IMAGE_ONLY_ALLOWED);
        }

        return IntStream.range(0, files.size())
                .mapToObj(i -> {
                    MultipartFile file = files.get(i);

                    try {
                        String fileName = file.getOriginalFilename();
                        String folderName = "blog/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                        S3UploadDto s3UploadDto = s3Service.uploadFileToFolder(file, folderName);

                        boolean isMain = i == 0;
                        BlogImage image = new BlogImage(blog, s3UploadDto.getUrl(), s3UploadDto.getKey(), fileName, isMain);

                        return blogImageRepository.save(image);
                    } catch (IOException e) {
                        throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
                    }
                }).toList();
    }
}
