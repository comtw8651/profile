package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 標記為 Spring 的組件，使其可被 Spring 掃描和管理
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository<T, ID>
    // T: 實體類型 (User)
    // ID: 實體主鍵的類型 (Long)

    // Spring Data JPA 會根據方法名自動生成查詢
    // 查找使用者名稱
    Optional<User> findByUsername(String username);

    // 查找電子郵件
    Optional<User> findByEmail(String email);

    // 檢查使用者名稱是否存在
    boolean existsByUsername(String username);

    // 檢查電子郵件是否存在
    boolean existsByEmail(String email);
}