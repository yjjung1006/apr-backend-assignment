package com.friends.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class FriendReq implements Serializable {
    @Id
    @Column(name = "REQUEST_ID")
    @JsonProperty("request_id")
    private String requestId;

    @Column(name = "REQUEST_USER_ID")
    @JsonProperty("request_user_id")
    private String requestUserId;

    @Column(name = "REQUESTED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("requestedAt")
    private Date requestedAt;

}
