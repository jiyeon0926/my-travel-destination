package jiyeon.travel.domain.blog.repository.custom;

import jiyeon.travel.domain.blog.entity.BlogImage;

import java.util.Optional;

public interface CustomBlogImageRepository {

    Optional<BlogImage> findByIdAndBlogIdAndEmail(Long id, Long blogId, String email);
}
