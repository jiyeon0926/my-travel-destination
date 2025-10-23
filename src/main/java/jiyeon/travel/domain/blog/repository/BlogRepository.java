package jiyeon.travel.domain.blog.repository;

import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.repository.custom.CustomBlogRepository;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, CustomBlogRepository {

    @Query("select b from Blog b inner join fetch b.user u where b.id = :id and u.email = :email")
    Optional<Blog> findByIdAndEmail(Long id, String email);

    default Blog findByIdAndEmailOrElseThrow(Long id, String email) {
        return this.findByIdAndEmail(id, email)
                .orElseThrow(() -> new CustomException(ErrorCode.BLOG_NOT_FOUND));
    }
}
