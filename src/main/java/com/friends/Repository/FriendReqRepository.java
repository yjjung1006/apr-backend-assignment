package com.friends.Repository;

import com.friends.Entity.Request.FriendRequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface FriendReqRepository extends JpaRepository<FriendRequestEntity, String> {
    List<FriendRequestEntity> findByTargetUserIdAndRequestedAtAfterOrderByRequestedAtDesc(
            String targetUserId, LocalDateTime requestedAt, Pageable pageable
    );

    /*
     * SELECT * FROM REQUEST_FRND WHERE TARGET_USER_ID =TARG AND REQUEST_USER_ID = REQUESTED AND REJECE_YN = 'N'
     * */
    boolean existsByRequestUserIdAndTargetUserIdAndRejectYn(String requestUserId, String targetUserId, String rejectYn);

    /*
     * SELECT * FROM REQUEST_FRND WHERE TARGET_USER_ID =TARG AND REQUEST_USER_ID = REQUESTED AND proc_yn  = 'y'
     * */
    boolean existsByRequestUserIdAndTargetUserIdAndProcYn(String requestUserId, String targetUserId, String procYn);

    @Modifying
    @Query("UPDATE FriendRequestEntity f " +
            "SET f.procYn = 'Y', f.respondedAt = :respondedAt " +
            "WHERE f.requestUserId = :requestUserId " +
            "AND f.targetUserId = :targetUserId " +
            "AND f.procYn = 'N' " +
            "AND f.rejectYn = 'N'")
    int approveFriendRequest(@Param("requestUserId") String requestUserId,
                             @Param("targetUserId") String targetUserId,
                             @Param("respondedAt") Date respondedAt);
}
