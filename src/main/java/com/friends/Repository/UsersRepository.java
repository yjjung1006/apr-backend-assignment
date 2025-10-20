package com.friends.Repository;

import com.friends.Entity.Request.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, String> {

//    @Query("""
//        SELECT u
//        FROM UserEntity u
//        WHERE u.userId <> :myUserId
//          AND NOT EXISTS (
//              SELECT f
//              FROM FriendEntity f
//              WHERE (f.id.fromUserId = :myUserId AND f.id.toUserId = u.userId)
//                 OR (f.id.toUserId = :myUserId AND f.id.fromUserId = u.userId)
//          )
//    """)
//    Page<UserEntity> findUsersNotFriendWith(@Param("myUserId") String myUserId, Pageable pageable);

    boolean existsById(String userId);

    Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);
}
