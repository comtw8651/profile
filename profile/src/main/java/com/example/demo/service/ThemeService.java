package com.example.demo.service;

import com.example.demo.entity.Theme;
import com.example.demo.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    /**
     * 獲取所有可用的主題。
     * @return 所有 Theme 物件的列表。
     */
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    /**
     * 根據主題 ID 獲取特定主題。
     * @param themeId 主題的唯一標識符。
     * @return 包含 Theme 物件的 Optional，如果找不到則為 Optional.empty()。
     */
    public Optional<Theme> getThemeById(Long themeId) {
        return themeRepository.findById(themeId);
    }

    /**
     * 根據主題名稱獲取特定主題。
     * @param themeName 主題的名稱。
     * @return 包含 Theme 物件的 Optional，如果找不到則為 Optional.empty()。
     */
    public Optional<Theme> getThemeByName(String themeName) {
        return themeRepository.findByThemeName(themeName);
    }

    // 您可以根據需要添加其他方法，例如：
    // public Theme createTheme(Theme theme) { return themeRepository.save(theme); }
    // public Theme updateTheme(Theme theme) { return themeRepository.save(theme); }
    // public void deleteTheme(Long themeId) { themeRepository.deleteById(themeId); }
}