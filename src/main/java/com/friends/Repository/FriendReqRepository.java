package com.friends.Repository;

import com.friends.Entity.Request.FriendRequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FriendReqRepository extends JpaRepository<FriendRequestEntity, String> {
    List<FriendRequestEntity> findByTargetUserIdAndRequestedAtAfterOrderByRequestedAtDesc(
            String targetUserId, LocalDateTime requestedAt, Pageable pageable
    );
}
