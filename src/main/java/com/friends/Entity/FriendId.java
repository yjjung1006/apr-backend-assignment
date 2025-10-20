package com.friends.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class FriendId implements Serializable {
    @Column(name = "FROM_USER_ID")
    private String fromUserId;

    @Column(name = "TO_USER_ID")
    private String toUserId;

    public FriendId() {}

    public FriendId(String fromUserId, String toUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }
}
