package jiyeon.travel.domain.blog.repository.custom;

import jiyeon.travel.domain.blog.dto.MyBlogListResDto;

public interface CustomBlogRepository {

    MyBlogListResDto findAllMyBlogs(String email);
}
