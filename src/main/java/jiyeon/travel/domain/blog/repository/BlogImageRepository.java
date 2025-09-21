package jiyeon.travel.domain.blog.repository;

import jiyeon.travel.domain.blog.entity.BlogImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogImageRepository extends JpaRepository<BlogImage, Long> {

    int countByBlogId(Long id);
}
