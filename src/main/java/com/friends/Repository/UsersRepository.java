package com.friends.Repository;

import com.friends.Entity.Request.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, String> {

   boolean existsById(String userId);

}
