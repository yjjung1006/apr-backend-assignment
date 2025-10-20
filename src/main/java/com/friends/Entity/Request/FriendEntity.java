package com.friends.Entity.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.friends.Entity.FriendId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "FRND_LST_MST")
@Getter
@Setter
public class FriendEntity {
    @EmbeddedId
    private FriendId id;

    @Column(name = "APPROVED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("approvedAt")
    private Date approvedAt;

    @JsonProperty("from_user_id")
    public String getFromUserId() {
        return id.getFromUserId();
    }

    @JsonProperty("to_user_id")
    public String getToUserId() {
        return id.getToUserId();
    }

    @JsonProperty("user_id")
    public String getUserId() {
        // 여기서는 임의로 fromUserId 또는 toUserId 중 하나 반환 가능
        return getToUserId();
    }
}
