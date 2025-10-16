package com.friends.Entity.Request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "FRND_REQUEST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestEntity implements Serializable {
    @Id
    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "REQUEST_USER_ID")
    private String requestUserId;

    @Column(name = "TARGET_USER_ID")
    private String targetUserId;

    @Column(name = "PROC_YN")
    private String procYn;

    @Column(name = "REJECT_YN")
    private String rejectYn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REQUESTED_AT")
    private Date requestedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RESPONDED_AT")
    private Date respondedAt;

}
