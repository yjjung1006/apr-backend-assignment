package com.friends.Entity.Response;

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
@Schema(description = "사용자 리스트 조회 응답 정보")
public class UsersLstResponse {
    private Integer totalPages;
    private Integer totalCount;
    private List<UsersLstInfoResponse> items;
}
