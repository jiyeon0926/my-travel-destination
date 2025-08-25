package jiyeon.travel.domain.user.repository;

import jiyeon.travel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDisplayName(String displayName);

    Optional<User> findByEmail(String email);
}
