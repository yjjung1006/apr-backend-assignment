package com.friends.Repository;

import com.friends.Entity.Request.FriendEntity;
import com.friends.Entity.FriendId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, FriendId> {
    Page<FriendEntity> findByIdFromUserIdOrIdToUserId(String fromUserId, String toUserId, Pageable pageable);

    boolean existsByIdFromUserIdAndIdToUserId(String fromUserId, String toUserId, String userId);
}
