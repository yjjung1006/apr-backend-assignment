package com.friends.Service;

import com.friends.Entity.Friend;
import com.friends.Entity.FriendReq;
import com.friends.Entity.Request.FriendRequestEntity;
import com.friends.Entity.Response.FriendsLstResponse;
import com.friends.Entity.Response.FriendsReqLstResponse;
import com.friends.Repository.FriendRepository;
import com.friends.Repository.FriendReqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendsService {
    private final FriendRepository friendRepository;
    private final FriendReqRepository friendReqRepository;
    private static final String myId = "jyj"; // "나" 고정 ID

    /**
     * 친구 목록 조회
     * @param page
     * @param maxSize
     * @param sort
     * @return
     */
    public FriendsLstResponse getFriendsLst(Integer page, Integer maxSize, String sort) {
        //정렬 유효성체크
        String[] sortParams = sort.split(",");
        if (sortParams.length != 2 || !sortParams[0].equals("approvedAt") ||
                (!sortParams[1].equalsIgnoreCase("asc") && !sortParams[1].equalsIgnoreCase("desc"))) {
            throw new IllegalArgumentException("sort는 'approvedAt,asc' 또는 'approvedAt,desc' 형태여야 합니다.");
        }
        String sortField = sortParams[0];
        String sortDirection = sortParams[1];
        Sort sorting = Sort.by(Sort.Direction.fromString(sortDirection), sortField);

        // 페이징 세팅
        Pageable pageable = PageRequest.of(page, maxSize, sorting);

        // 조회
        Page<Friend> result = friendRepository.findByIdFromUserIdOrIdToUserId(myId, myId, pageable);

        // 결과 세팅
        FriendsLstResponse response = new FriendsLstResponse();
        response.setTotalPages(result.getTotalPages());
        response.setTotalCount((int) result.getTotalElements());
        response.setItems(result.getContent());

        return response;
    }

    /**
     * 받은 친구 신청 목록 조회
     * @param maxSize
     * @param window
     * @param sort
     * @return
     */
    public FriendsReqLstResponse getReqFriendsLst(Integer maxSize, String window, String sort) {
        // 정렬 유효성체크
        String[] sortParams = sort.split(",");
        if (sortParams.length != 2 || !sortParams[0].equals("requestedAt") ||
                (!sortParams[1].equalsIgnoreCase("asc") && !sortParams[1].equalsIgnoreCase("desc"))) {
            throw new IllegalArgumentException("sort는 'requestedAt,asc' 또는 'requestedAt,desc' 형태여야 합니다.");
        }
        String sortField = sortParams[0];
        String sortDirection = sortParams[1];
        Sort sorting = Sort.by(Sort.Direction.fromString(sortDirection), sortField);

        // 조건 날짜 설정
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = switch (window) {
            case "1d" -> now.minusDays(1);
            case "7d" -> now.minusDays(7);
            case "30d" -> now.minusDays(30);
            case "90d" -> now.minusDays(90);
            case "over" -> LocalDateTime.MIN; // 전체
            default -> throw new IllegalArgumentException("Invalid window: " + window);
        };

        // 페이징 세팅
        Pageable pageable = PageRequest.of(0, maxSize);

        // 조회
        List<FriendRequestEntity> result =
                friendReqRepository.findByTargetUserIdAndRequestedAtAfterOrderByRequestedAtDesc(myId, startTime, pageable);

        // 변환
        List<FriendReq> items = result.stream()
                .map(e -> new FriendReq(e.getRequestId(), e.getRequestUserId(), e.getRequestedAt()))
                .collect(Collectors.toList());

        // 결과값세팅
        FriendsReqLstResponse response = new FriendsReqLstResponse();
        response.setWindow(window);
        response.setTotalCount(maxSize);
        response.setItems(items);

        return response;
    }
}
