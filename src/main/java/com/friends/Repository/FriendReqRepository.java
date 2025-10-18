package com.friends.Repository;

import com.friends.Entity.Request.FriendRequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface FriendReqRepository extends JpaRepository<FriendRequestEntity, String> {
    List<FriendRequestEntity> findByTargetUserIdAndRequestedAtAfterOrderByRequestedAtDesc(
            String targetUserId, LocalDateTime requestedAt, Pageable pageable
    );

    boolean existsByRequestUserIdAndTargetUserId(String requestUserId, String targetUserId);

    boolean existsByRequestUserIdAndTargetUserIdAndProcYn(String requestUserId, String targetUserId, String procYn);

    @Modifying
    @Transactional
    @Query("UPDATE FriendRequestEntity f " +
            "SET f.procYn = 'Y', f.respondedAt = :respondedAt " +
            "WHERE f.requestUserId = :requestUserId " +
            "AND f.targetUserId = :targetUserId " +
            "AND f.procYn = 'N' ")
    int approveFriendRequest(@Param("requestUserId") String requestUserId,
                             @Param("targetUserId") String targetUserId,
                             @Param("respondedAt") Date respondedAt);

    @Modifying
    @Transactional
    @Query("DELETE FROM FriendRequestEntity f " +
            "WHERE f.requestUserId = :requestUserId " +
            "AND f.targetUserId = :targetUserId "  +
            "AND f.procYn = :procYn "  +
            "AND f.respondedAt IS NULL")
    int deletePendingRequestsByTargetUserId(@Param("requestUserId") String requestUserId,
                                            @Param("targetUserId") String targetUserId,
                                            @Param("procYn") String procYn);
}
