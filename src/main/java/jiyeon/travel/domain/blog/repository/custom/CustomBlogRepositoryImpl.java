package jiyeon.travel.domain.blog.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.blog.dto.BlogListResDto;
import jiyeon.travel.domain.blog.dto.BlogPreviewResDto;
import jiyeon.travel.domain.blog.dto.QBlogPreviewResDto;
import jiyeon.travel.domain.blog.entity.QBlog;
import jiyeon.travel.domain.blog.entity.QBlogImage;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomBlogRepositoryImpl implements CustomBlogRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public BlogListResDto findAllMyBlogs(String email) {
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

        return new BlogListResDto(total, blogs);
    }

    @Override
    public BlogListResDto searchBlogs(Pageable pageable, String title, LocalDate travelStartDate, LocalDate travelEndDate, Integer totalExpense) {
        QBlog blog = QBlog.blog;
        QBlogImage blogImage = QBlogImage.blogImage;

        BooleanBuilder conditions = new BooleanBuilder();
        if (title != null) conditions.and(blog.title.contains(title));
        if (travelStartDate != null) conditions.and(blog.travelStartDate.eq(travelStartDate));
        if (travelEndDate != null) conditions.and(blog.travelEndDate.eq(travelEndDate));
        if (totalExpense != null) conditions.and(blog.totalExpense.eq(totalExpense));

        Long total = Optional.ofNullable(
                        jpaQueryFactory
                                .select(blog.count())
                                .from(blog)
                                .where(conditions)
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
                .where(conditions)
                .orderBy(blog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new BlogListResDto(total, blogs);
    }
}
