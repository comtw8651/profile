package com.example.demo.service;

import com.example.demo.entity.User; // 如果需要
import com.example.demo.entity.UserProfile;
import com.example.demo.entity.Theme;
import com.example.demo.repository.UserProfileRepository;
import com.example.demo.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // 引入 MultipartFile

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final ThemeRepository themeRepository;
    private final FileStorageService fileStorageService; // 引入 FileStorageService

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository,
                              ThemeRepository themeRepository,
                              FileStorageService fileStorageService) { // 注入 FileStorageService
        this.userProfileRepository = userProfileRepository;
        this.themeRepository = themeRepository;
        this.fileStorageService = fileStorageService;
    }

    // 假設使用者 ID 會透過認證機制傳遞過來
    public Optional<UserProfile> getUserProfile(Long userId) {
        return userProfileRepository.findById(userId);
    }

    // 範例：為新註冊的使用者建立預設個人檔案 (如果您的登入專案會調用此專案的 API 來完成)
    @Transactional
    public UserProfile createDefaultUserProfile(Long userId) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        // 如果這個專案不管理 User 實體，那麼 userProfile.setUser(user); 就不需要了
        // 或者您需要從另一個服務獲取 User 實體並設定
        // userProfile.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));

        Optional<Theme> defaultTheme = themeRepository.findByThemeName("Default");
        defaultTheme.ifPresent(userProfile::setCurrentTheme);

        return userProfileRepository.save(userProfile);
    }

    // 更新使用者的背景圖片
    @Transactional
    public String updateBackgroundImage(Long userId, MultipartFile file) {
        // 先儲存新檔案
        String imageUrl = fileStorageService.storeFile(file);

        return userProfileRepository.findById(userId)
                .map(profile -> {
                    // 如果有舊的背景圖片，考慮刪除它以節省空間
                    if (profile.getBackgroundImageUrl() != null && !profile.getBackgroundImageUrl().isEmpty()) {
                        fileStorageService.deleteFile(profile.getBackgroundImageUrl());
                    }
                    profile.setBackgroundImageUrl(imageUrl);
                    userProfileRepository.save(profile); // 保存更新
                    return imageUrl; // 返回新的圖片URL
                })
                .orElseThrow(() -> new RuntimeException("User profile not found for ID: " + userId));
    }

    // 更新使用者的頭像
    @Transactional
    public String updateAvatar(Long userId, MultipartFile file) {
        String imageUrl = fileStorageService.storeFile(file);

        return userProfileRepository.findById(userId)
                .map(profile -> {
                    if (profile.getAvatarUrl() != null && !profile.getAvatarUrl().isEmpty()) {
                        fileStorageService.deleteFile(profile.getAvatarUrl());
                    }
                    profile.setAvatarUrl(imageUrl);
                    userProfileRepository.save(profile);
                    return imageUrl;
                })
                .orElseThrow(() -> new RuntimeException("User profile not found for ID: " + userId));
    }

    // 更新使用者的按鈕樣式配置
    @Transactional
    public UserProfile updateButtonStyle(Long userId, String buttonStyleConfig) {
        return userProfileRepository.findById(userId)
                .map(profile -> {
                    profile.setButtonStyleConfig(buttonStyleConfig);
                    return userProfileRepository.save(profile);
                })
                .orElseThrow(() -> new RuntimeException("User profile not found for ID: " + userId));
    }

    // 更新使用者當前選中的主題
    @Transactional
    public UserProfile updateUserTheme(Long userId, Long themeId) {
        Optional<Theme> themeOptional = themeRepository.findById(themeId);
        if (themeOptional.isEmpty()) {
            throw new RuntimeException("Theme with ID " + themeId + " not found.");
        }
        Theme selectedTheme = themeOptional.get();

        return userProfileRepository.findById(userId)
                .map(profile -> {
                    profile.setCurrentTheme(selectedTheme);
                    return userProfileRepository.save(profile);
                })
                .orElseThrow(() -> new RuntimeException("User profile not found for ID: " + userId));
    }

    // 其他可能的更新方法 (例如文字顏色等)
    // ...
}