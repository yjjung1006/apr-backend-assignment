package com.friends.Entity.Response;

import com.friends.Entity.FriendReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "받은 친구 신청 목록 조회 응답 정보")
public class FriendsReqLstResponse {
    private String window;
    private Integer totalCount;
    private List<FriendReq> items;
}
