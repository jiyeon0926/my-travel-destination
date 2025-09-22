package jiyeon.travel.domain.blog.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.blog.entity.BlogImage;
import jiyeon.travel.domain.blog.entity.QBlog;
import jiyeon.travel.domain.blog.entity.QBlogImage;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomBlogImageRepositoryImpl implements CustomBlogImageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<BlogImage> findByIdAndBlogIdAndEmail(Long id, Long blogId, String email) {
        QUser user = QUser.user;
        QBlog blog = QBlog.blog;
        QBlogImage blogImage = QBlogImage.blogImage;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(blogImage.id.eq(id));
        conditions.and(blog.id.eq(blogId));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(blogImage)
                .innerJoin(blogImage.blog, blog).fetchJoin()
                .innerJoin(blog.user, user).fetchJoin()
                .where(conditions)
                .fetchOne());
    }
}
