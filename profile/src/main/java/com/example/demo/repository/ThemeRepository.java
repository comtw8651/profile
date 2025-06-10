package com.example.demo.repository;

import com.example.demo.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    // 查找主題名稱
    Optional<Theme> findByThemeName(String themeName);

    // 檢查主題名稱是否存在
    boolean existsByThemeName(String themeName);
}