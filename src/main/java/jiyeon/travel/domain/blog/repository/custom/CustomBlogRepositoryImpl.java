package jiyeon.travel.domain.blog.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.blog.dto.BlogPreviewResDto;
import jiyeon.travel.domain.blog.dto.MyBlogListResDto;
import jiyeon.travel.domain.blog.dto.QBlogPreviewResDto;
import jiyeon.travel.domain.blog.entity.QBlog;
import jiyeon.travel.domain.blog.entity.QBlogImage;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomBlogRepositoryImpl implements CustomBlogRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public MyBlogListResDto findAllMyBlogs(String email) {
        QBlog blog = QBlog.blog;
        QBlogImage blogImage = QBlogImage.blogImage;
        QUser user = QUser.user;

        Long total = Optional.ofNullable(
                        jpaQueryFactory
                                .select(blog.count())
                                .from(blog)
                                .innerJoin(user).on(blog.user.id.eq(user.id))
                                .where(user.email.eq(email))
                                .fetchOne())
                .orElse(0L);

        List<BlogPreviewResDto> blogs = jpaQueryFactory
                .select(new QBlogPreviewResDto(
                        blog.id,
                        blog.title,
                        blogImage.imageUrl,
                        blogImage.fileName,
                        blogImage.isMain
                ))
                .from(blog)
                .leftJoin(blogImage).on(blog.id.eq(blogImage.blog.id).and(blogImage.isMain.eq(true)))
                .innerJoin(user).on(blog.user.id.eq(user.id))
                .where(user.email.eq(email))
                .fetch();

        return new MyBlogListResDto(total, blogs);
    }
}
