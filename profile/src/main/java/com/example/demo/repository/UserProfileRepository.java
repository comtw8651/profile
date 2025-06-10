package com.example.demo.repository;

import com.example.demo.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // 這裡的主鍵是 Long 類型的 userId

    // 因為 UserProfile 的主鍵就是 userId，所以 findById(userId) 就能找到對應的 UserProfile
    // 您也可以明確定義一個方法來基於 userId 查找
    Optional<UserProfile> findByUserId(Long userId);
}