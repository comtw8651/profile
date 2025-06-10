package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "themes") // 對應資料庫中的 themes 表
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "theme_name", unique = true, nullable = false, length = 100)
    private String themeName;

    @Column(name = "description", columnDefinition = "TEXT") // 使用 TEXT 類型儲存較長的描述
    private String description;

    @Column(name = "default_background_image_url", length = 255)
    private String defaultBackgroundImageUrl;

    @Column(name = "default_avatar_url", length = 255)
    private String defaultAvatarUrl;

    // 注意：這裡將 button_style_config 儲存為 TEXT，可以存放 JSON 字串或 CSS 類名
    @Column(name = "default_button_style_config", columnDefinition = "TEXT")
    private String defaultButtonStyleConfig;

    @Column(name = "default_text_color", length = 20)
    private String defaultTextColor;

    @Column(name = "default_background_color", length = 20)
    private String defaultBackgroundColor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaultBackgroundImageUrl() {
		return defaultBackgroundImageUrl;
	}

	public void setDefaultBackgroundImageUrl(String defaultBackgroundImageUrl) {
		this.defaultBackgroundImageUrl = defaultBackgroundImageUrl;
	}

	public String getDefaultAvatarUrl() {
		return defaultAvatarUrl;
	}

	public void setDefaultAvatarUrl(String defaultAvatarUrl) {
		this.defaultAvatarUrl = defaultAvatarUrl;
	}

	public String getDefaultButtonStyleConfig() {
		return defaultButtonStyleConfig;
	}

	public void setDefaultButtonStyleConfig(String defaultButtonStyleConfig) {
		this.defaultButtonStyleConfig = defaultButtonStyleConfig;
	}

	public String getDefaultTextColor() {
		return defaultTextColor;
	}

	public void setDefaultTextColor(String defaultTextColor) {
		this.defaultTextColor = defaultTextColor;
	}

	public String getDefaultBackgroundColor() {
		return defaultBackgroundColor;
	}

	public void setDefaultBackgroundColor(String defaultBackgroundColor) {
		this.defaultBackgroundColor = defaultBackgroundColor;
	}
    
    

    // 如果還有其他預設樣式，可以繼續添加字段
}