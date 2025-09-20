package jiyeon.travel.domain.blog.repository;

import jiyeon.travel.domain.blog.entity.BlogTicketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogTicketItemRepository extends JpaRepository<BlogTicketItem, Long> {
}
