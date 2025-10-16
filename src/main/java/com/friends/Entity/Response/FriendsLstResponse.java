package com.friends.Entity.Response;

import com.friends.Entity.Request.FriendEntity;
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
@Schema(description = "친구 리스트 조회 응답 정보")
public class FriendsLstResponse {
    private Integer totalPages;
    private Integer totalCount;
    private List<FriendEntity> items;
}
