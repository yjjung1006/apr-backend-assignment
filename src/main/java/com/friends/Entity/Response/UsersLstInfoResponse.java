package com.friends.Entity.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "사용자 리스트 조회 상세 응답 정보")
public class UsersLstInfoResponse {
    private String user_id;
    private String user_name;
    private String email;
    private Date createdAt;
}
