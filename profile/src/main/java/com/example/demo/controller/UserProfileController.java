package com.example.demo.controller;

import com.example.demo.entity.Theme;
import com.example.demo.entity.UserProfile;
import com.example.demo.service.UserProfileService;
import com.example.demo.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map; // 用於接收簡單的 JSON 請求體
import java.util.Optional;

@RestController // 標記為 RESTful Controller，自動將返回值序列化為 JSON
@RequestMapping("/api/profile") // 設定基礎路徑為 /api/profile
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final ThemeService themeService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService, ThemeService themeService) {
        this.userProfileService = userProfileService;
        this.themeService = themeService;
    }

    /**
     * 獲取指定使用者的個人檔案資訊。
     * 實際應用中，userId 應該從 JWT 或安全上下文中獲取，而不是直接從路徑變數。
     * 這裡為了範例方便，暫時從路徑變數獲取。
     *
     * @param userId 使用者 ID
     * @return UserProfile 物件或 404 Not Found
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        // 在實際應用中，這裡會是從 Spring Security 上下文獲取當前使用者 ID 的地方
        // Long authenticatedUserId = getAuthenticatedUserId(); // 假設有這樣的方法
        // if (!userId.equals(authenticatedUserId)) {
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 禁止訪問非自己的個人檔案
        // }

        Optional<UserProfile> userProfile = userProfileService.getUserProfile(userId);
        return userProfile.map(ResponseEntity::ok) // 如果找到，返回 200 OK 和 UserProfile
                          .orElseGet(() -> ResponseEntity.notFound().build()); // 否則返回 404 Not Found
    }

    /**
     * 上傳並更新使用者的背景圖片。
     *
     * @param userId 使用者 ID
     * @param file 背景圖片檔案
     * @return 新圖片的 URL
     */
    @PostMapping("/{userId}/background-image")
    public ResponseEntity<String> updateBackgroundImage(@PathVariable Long userId,
                                                        @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = userProfileService.updateBackgroundImage(userId, file);
            return ResponseEntity.ok(imageUrl);
        } catch (RuntimeException e) {
            // 實際應用中，應返回更具體的錯誤碼和訊息，例如：
            // ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新背景圖片失敗: " + e.getMessage());
        }
    }

    /**
     * 上傳並更新使用者的頭像。
     *
     * @param userId 使用者 ID
     * @param file 頭像檔案
     * @return 新圖片的 URL
     */
    @PostMapping("/{userId}/avatar")
    public ResponseEntity<String> updateAvatar(@PathVariable Long userId,
                                                @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = userProfileService.updateAvatar(userId, file);
            return ResponseEntity.ok(imageUrl);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新頭像失敗: " + e.getMessage());
        }
    }

    /**
     * 更新使用者的按鈕樣式配置。
     *
     * @param userId 使用者 ID
     * @param payload 包含 buttonStyleConfig 鍵值對的 JSON 物件
     * @return 更新後的 UserProfile 物件
     */
    @PutMapping("/{userId}/button-style")
    public ResponseEntity<UserProfile> updateButtonStyle(@PathVariable Long userId,
                                                        @RequestBody Map<String, String> payload) {
        String buttonStyleConfig = payload.get("buttonStyleConfig");
        if (buttonStyleConfig == null) {
            return ResponseEntity.badRequest().build(); // 請求體中缺少必要參數
        }
        try {
            UserProfile updatedProfile = userProfileService.updateButtonStyle(userId, buttonStyleConfig);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 找不到使用者檔案
        }
    }

    /**
     * 更新使用者當前選擇的主題。
     *
     * @param userId 使用者 ID
     * @param payload 包含 themeId 鍵值對的 JSON 物件
     * @return 更新後的 UserProfile 物件
     */
    @PutMapping("/{userId}/theme")
    public ResponseEntity<UserProfile> updateUserTheme(@PathVariable Long userId,
                                                      @RequestBody Map<String, Long> payload) {
        Long themeId = payload.get("themeId");
        if (themeId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            UserProfile updatedProfile = userProfileService.updateUserTheme(userId, themeId);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 例如，主題不存在
        }
    }

    /**
     * 獲取所有可用的主題列表。
     *
     * @return 所有 Theme 物件的列表
     */
    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> getAllThemes() {
        List<Theme> themes = themeService.getAllThemes();
        return ResponseEntity.ok(themes);
    }

    /**
     * 根據 ID 獲取特定主題的詳細資訊。
     *
     * @param themeId 主題 ID
     * @return Theme 物件或 404 Not Found
     */
    @GetMapping("/themes/{themeId}")
    public ResponseEntity<Theme> getThemeById(@PathVariable Long themeId) {
        Optional<Theme> theme = themeService.getThemeById(themeId);
        return theme.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 您可能還需要一個端點來處理預設個人檔案的建立
    // 例如：當新使用者註冊後，可以呼叫此 API 來初始化其個人檔案
    @PostMapping("/{userId}/initialize-profile")
    public ResponseEntity<UserProfile> initializeUserProfile(@PathVariable Long userId) {
        try {
            UserProfile defaultProfile = userProfileService.createDefaultUserProfile(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(defaultProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}