package jiyeon.travel.domain.user.repository;

import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findByDisplayName(String displayName);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    default User findActiveByEmailOrElseThrow(String email) {
        return this.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
