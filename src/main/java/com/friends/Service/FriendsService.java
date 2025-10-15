package com.friends.Service;

import com.friends.Entity.Friend;
import com.friends.Entity.FriendsLstResponse;
import com.friends.Repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendsService {
    private final FriendRepository friendRepository;

    /**
     * 친구 목록 조회
     * @param page
     * @param maxSize
     * @param sort
     * @return
     */
    public FriendsLstResponse getFriendsLst(String myId, Integer page, Integer maxSize, String sort) {
        String[] sortParams = sort.split(",");
        if (sortParams.length != 2 || !sortParams[0].equals("approvedAt") ||
                (!sortParams[1].equalsIgnoreCase("asc") && !sortParams[1].equalsIgnoreCase("desc"))) {
            throw new IllegalArgumentException("sort는 'approvedAt,asc' 또는 'approvedAt,desc' 형태여야 합니다.");
        }

        String sortField = sortParams[0];
        String sortDirection = sortParams[1];
        Sort sorting = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, maxSize, sorting);
        Page<Friend> result = friendRepository.findByIdFromUserIdOrIdToUserId(myId, myId, pageable);

        FriendsLstResponse response = new FriendsLstResponse();
        response.setTotalPages(result.getTotalPages());
        response.setTotalCount((int) result.getTotalElements());
        response.setItems(result.getContent());

        return response;
    }
}
