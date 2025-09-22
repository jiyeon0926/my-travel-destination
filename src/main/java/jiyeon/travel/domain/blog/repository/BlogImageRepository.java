package jiyeon.travel.domain.blog.repository;

import jiyeon.travel.domain.blog.entity.BlogImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogImageRepository extends JpaRepository<BlogImage, Long>, CustomBlogImageRepository {

    Optional<BlogImage> findByBlogIdAndIsMainTrue(Long blogId);

    List<BlogImage> findAllByBlogId(Long blogId);

    int countByBlogId(Long id);
}
