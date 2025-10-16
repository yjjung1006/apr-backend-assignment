package com.friends.Service;

import com.friends.Entity.FriendId;
import com.friends.Entity.Request.FriendEntity;
import com.friends.Entity.FriendReq;
import com.friends.Entity.Request.FriendRequestEntity;
import com.friends.Entity.Response.FriendsLstResponse;
import com.friends.Entity.Response.FriendsReqLstResponse;
import com.friends.Repository.FriendRepository;
import com.friends.Repository.FriendReqRepository;
import com.friends.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendsService {
    private final FriendRepository friendRepository;
    private final FriendReqRepository friendReqRepository;
    private final UsersRepository usersRepository;

    private static final String myId = "jyj"; // "나" 고정 ID
    private final Map<String, List<Long>> requestTimes = new ConcurrentHashMap<>();

    /**
     * 친구 목록 조회
     * @param page
     * @param maxSize
     * @param sort
     * @return
     */
    public FriendsLstResponse getFriendsLst(String requestUserId, Integer page, Integer maxSize, String sort) {
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
        Page<FriendEntity> result = friendRepository.findByIdFromUserIdOrIdToUserId(requestUserId, requestUserId, pageable);

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
    public FriendsReqLstResponse getReqFriendsLst(String requestUserId, Integer maxSize, String window, String sort) {
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
                friendReqRepository.findByTargetUserIdAndRequestedAtAfterOrderByRequestedAtDesc(requestUserId, startTime, pageable);

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

    /**
     * 친구 신청
     * @param requestUserId
     * @param targetUserId
     */
    @Transactional
    public void reqFriends(String requestUserId, String targetUserId) {
        //1. 자기 자신에게 요청
        if (requestUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("자기 자신에게는 요청할 수 없습니다.");
        }

        //2. 존재하지 않는 사용자
        if (!usersRepository.existsById(targetUserId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        //3. 이미 보낸 요청(거절x)
        boolean exists = friendReqRepository.existsByRequestUserIdAndTargetUserIdAndRejectYn(requestUserId, targetUserId, "N");
        if (exists) {
            throw new IllegalStateException("이미 요청한 친구입니다.");
        }

        //4. 사용자별 초당 10회 요청 제한
        this.checkClickCnt(requestUserId);

        //5. insert
        FriendRequestEntity entity = new FriendRequestEntity();
        entity.setRequestId(UUID.randomUUID().toString());
        entity.setRequestUserId(requestUserId);
        entity.setTargetUserId(targetUserId);
        entity.setProcYn("N");
        entity.setRequestedAt(new Date());
        friendReqRepository.save(entity);
    }

    /**
     * 친구 수락
     * @param requestUserId
     * @param targetUserId
     */
    @Transactional
    public void acptFriends(String requestUserId, String targetUserId) {
        //1. 자기 자신에게 요청
        if (requestUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("자기 자신에게는 요청할 수 없습니다.");
        }

        //2. 존재하지 않는 사용자
        if (!usersRepository.existsById(targetUserId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        //3. 이미 목록관계 존재
        boolean relExists = friendRepository.existsByIdFromUserIdAndIdToUserId(requestUserId, requestUserId, targetUserId);
        if (relExists) {
            throw new IllegalStateException("이미 요청한 친구입니다.");
        }

        //4. 이미 procyn = y
        boolean exists = friendReqRepository.existsByRequestUserIdAndTargetUserIdAndProcYn(requestUserId, targetUserId, "Y");
        if (exists) {
            throw new IllegalStateException("이미 친구 관계입니다.");
        }

        //5. update
        int updateCnt = 0;
        updateCnt = friendReqRepository.approveFriendRequest(requestUserId, targetUserId, new Date());
        if( updateCnt < 1) {
            //throw 예외처리
        }

        //6. insert
        FriendId idEntity = new FriendId();
        idEntity.setFromUserId(requestUserId);
        idEntity.setToUserId(targetUserId);

        FriendEntity entity = new  FriendEntity();
        entity.setId(idEntity);
        entity.setApprovedAt(new Date());
        friendRepository.save(entity);

    }

    public void checkClickCnt(String userId) {
        long now = System.currentTimeMillis();
        requestTimes.putIfAbsent(userId, new ArrayList<>());
        List<Long> times = requestTimes.get(userId);

        // 1초 이전 요청 제거
        times.removeIf(t -> t < now - 1000);

        if (times.size() >= 10) {
            //throw new TooManyRequestsException("초당 10회 요청 제한을 초과했습니다.");
        }

        times.add(now);
    }
}
