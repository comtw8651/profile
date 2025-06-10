package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "user_profiles") // 對應資料庫中的 user_profiles 表
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    // 這裡的主鍵應該同時是外鍵，參考 users 表的 id
    @Id
    @Column(name = "user_id")
    private Long userId;

    // 與 User 實體建立一對一關係
    // mappedBy 指向 User 實體中擁有 UserProfile 的字段，表示 UserProfile 由 User 管理
    @OneToOne
    @MapsId // 使用 User 的主鍵作為 UserProfile 的主鍵
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "background_image_url", length = 255)
    private String backgroundImageUrl;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "button_style_config", columnDefinition = "TEXT")
    private String buttonStyleConfig; // 使用者自訂的按鈕樣式配置
    
    

    public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public String getBackgroundImageUrl() {
		return backgroundImageUrl;
	}



	public void setBackgroundImageUrl(String backgroundImageUrl) {
		this.backgroundImageUrl = backgroundImageUrl;
	}



	public String getAvatarUrl() {
		return avatarUrl;
	}



	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}



	public String getButtonStyleConfig() {
		return buttonStyleConfig;
	}



	public void setButtonStyleConfig(String buttonStyleConfig) {
		this.buttonStyleConfig = buttonStyleConfig;
	}



	public Theme getCurrentTheme() {
		return currentTheme;
	}



	public void setCurrentTheme(Theme currentTheme) {
		this.currentTheme = currentTheme;
	}



	// 與 Theme 實體建立多對一關係
    // 一個 Theme 可以被多個 UserProfile 使用，一個 UserProfile 只能有一個 Theme
    @ManyToOne(fetch = FetchType.LAZY) // 延遲加載 Theme 數據，提高性能
    @JoinColumn(name = "current_theme_id", referencedColumnName = "id") // 外鍵 current_theme_id 參考 Theme 的 id
    private Theme currentTheme;

    // 注意：您可能不需要在 UserProfile 中再儲存一個 current_theme_id 字段，
    // 因為 JPA 會透過 currentTheme 物件來管理這個關係。
    // 但是，如果您想在查詢或某些特定情況下直接使用 ID，可以保留它並標記為 @Transient 或僅用於某些場景。
    // 在我的設計中，直接透過 `currentTheme` 物件來存取主題資訊。
}