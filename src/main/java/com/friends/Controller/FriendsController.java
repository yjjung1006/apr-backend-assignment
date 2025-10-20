package com.friends.Controller;

import com.friends.Entity.Response.CommonResponse;
import com.friends.Entity.Response.FriendsLstResponse;
import com.friends.Entity.Response.FriendsReqLstResponse;
import com.friends.Entity.Response.UsersLstResponse;
import com.friends.Service.FriendsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Validated
@Tag(name = "친구 맺기", description = "에이지알 서비스에 > 친구 맺기 기능 API")
public class FriendsController {

    private final FriendsService friendsService;

    /**
     *  친구 목록 조회
     * @param maxSize
     * @return
     */
    @GetMapping("/api/users")
    @Operation(summary = "신청 가능한 사용자 목록 조회",
            description = "신청 가능한 사용자 목록을 조회합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "1. 정상 처리\n2. ERR-0001 : 친구 목록 조회 실패")})
    public CommonResponse<UsersLstResponse> findUsersNotFriendWith(@RequestHeader("X-user-Id") String myUserId,
                                                        @RequestParam @NotNull @Min(value = 0, message = "페이지는 0 이상이어야 합니다") Integer page,
                                                        @RequestParam @NotNull @Min(value = 1, message = "maxSize는 1 이상이어야 합니다") Integer maxSize,
                                                        @RequestParam(defaultValue = "username,desc") @Pattern(
                                                                    regexp = "^username,(asc|desc)$",
                                                                    message = "sort 값은 username,asc 또는 username,desc 여야 합니다.") String sort) {
        UsersLstResponse result = new UsersLstResponse();
        result = friendsService.findUsersNotFriendWith(myUserId, page, maxSize, sort);

        return CommonResponse.success(result);
    }

    /**
     *  친구 목록 조회
     * @param maxSize
     * @return
     */
    @GetMapping("/api/friends")
    @Operation(summary = "친구 목록 조회",
                description = "나를 기준으로 현재 맺어진 친구 목록을 조회합니다.",
                responses = {@ApiResponse(responseCode = "200", description = "1. 정상 처리\n2. ERR-0001 : 친구 목록 조회 실패")})
    public CommonResponse<FriendsLstResponse> getFriendsLst(@RequestHeader("X-user-Id") String myUserId,
                                            @RequestParam @NotNull @Min(value = 0, message = "페이지는 0 이상이어야 합니다") Integer page,
                                            @RequestParam @NotNull @Min(value = 1, message = "maxSize는 1 이상이어야 합니다") Integer maxSize,
                                            @RequestParam(defaultValue = "approvedAt,desc") @Pattern(
                                                    regexp = "^approvedAt,(asc|desc)$",
                                                    message = "sort 값은 approvedAt,asc 또는 approvedAt,desc 여야 합니다.") String sort) {
        FriendsLstResponse result = new FriendsLstResponse();
        result = friendsService.getFriendsLst(myUserId, page, maxSize, sort);

        return CommonResponse.success(result);
    }

    /**
     * 받은 친구 신청 목록 조회
     * @param maxSize
     * @param window
     * @param sort
     * @return
     */
    @GetMapping("/api/friends/requests")
    @Operation(summary = "받은 친구 신청 목록 조회",
            description = "나를 기준으로 받은 친구 신청 목록을 조회합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "1. 정상 처리\n2. ERR-0002 : 받은 친구 신청 목록 조회 실패")})
    public CommonResponse<FriendsReqLstResponse> getReqFriendsLst( @RequestHeader("X-user-Id") String myUserId,
                                                                   @RequestParam @NotNull @Min(value = 1, message = "maxSize는 1 이상이어야 합니다") Integer maxSize,
                                                                   @RequestParam(defaultValue = "1d") @Pattern(regexp = "^(1d|7d|30d|90d|over)$",
                                                                          message = "window 값은 1d, 7d, 30d, 90d, over 중 하나여야 합니다.") String window,
                                                                   @RequestParam(defaultValue = "requestedAt,desc") @Pattern(
                                                                          regexp = "^requestedAt,(asc|desc)$",
                                                                          message = "sort 값은 requestedAt,asc 또는 requestedAt,desc 여야 합니다.") String sort) {
        FriendsReqLstResponse result = new FriendsReqLstResponse();
        result = friendsService.getReqFriendsLst(myUserId, maxSize, window, sort);

        return CommonResponse.success(result);
    }

    /**
     * 친구 신청
     * @param targetUserId
     * @return
     */
    @Operation(summary = "친구 신청",
            description = "친구를 신청합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "1. 정상 처리\n2. ERR-0003 : 친구 신청 실패")})
    @PostMapping("/api/friends/request")
    public CommonResponse<String> reqFriends(@RequestHeader("X-user-Id") String myUserId,
                             @Valid @RequestBody @NotNull(message = "targetUserId는 필수입니다.") String targetUserId) {

        friendsService.reqFriends(myUserId, targetUserId);

        return CommonResponse.success("친구 신청했습니다.");
    }

    /**
     *   친구 수락
     * @param myUserId
     * @return
     */
    @Operation(summary = "친구 수락",
            description = "친구를 수락합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "1. 정상 처리\n2. ERR-0004 : 친구 수락 실패")})
    @PostMapping("/api/friends/accept")
    public CommonResponse<String> acptFriends(@RequestHeader("X-user-Id") String myUserId,
                              @Valid @RequestBody @NotNull(message = "requestUserId는 필수입니다.") String requestUserId) {

        friendsService.acptFriends(requestUserId, myUserId);

        return CommonResponse.success("친구수락 완료했습니다.");
    }

    /**
     *   친구 거절
     * @param myUserId
     * @return
     */
    @Operation(summary = "친구 거절",
            description = "친구를 거절합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "1. 정상 처리\n2. ERR-0004 : 친구 수락 실패")})
    @PostMapping("/api/friends/reject")
    public CommonResponse<String> rjctFriends(@RequestHeader("X-user-Id") String myUserId,
                              @Valid @RequestBody @NotNull(message = "targetUserId는 필수입니다.") String requestUserId) {

        friendsService.rejectFriends(requestUserId, myUserId);

        return CommonResponse.success("친구신청을 거절했습니다.");
    }
}
