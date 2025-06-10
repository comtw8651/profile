package com.example.demo.entity;

import jakarta.persistence.*; // 使用 jakarta.persistence 作為 JPA 規範的命名空間
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users") // 對應資料庫中的 users 表
@Data // Lombok 註解，自動生成 getter, setter, equals, hashCode, toString
@NoArgsConstructor // Lombok 註解，生成無參構造函數
@AllArgsConstructor // Lombok 註解，生成包含所有字段的構造函數
public class User {

    @Id // 標記為主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增長主鍵
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password; // 實際應用中請務必加密儲存密碼！

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "created_at", updatable = false) // updatable = false 表示不允許更新此字段
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	// JPA 生命周期回調方法，用於自動設定創建和更新時間
    @PrePersist // 在物件持久化（首次保存）之前執行
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // 在物件更新之前執行
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}