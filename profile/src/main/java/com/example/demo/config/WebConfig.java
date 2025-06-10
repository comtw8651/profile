package com.example.demo.config; // 請根據您的實際套件名稱調整

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 標記為 Spring 配置類
public class WebConfig implements WebMvcConfigurer {

    // 從 application.properties 讀取 'file.upload.dir' 的值
    @Value("${file.upload.dir}")
    private String fileUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 配置上傳圖片的訪問路徑
        // 當瀏覽器請求 /uploads/** 時，Spring 會到 fileUploadDir 指定的真實檔案系統路徑去尋找檔案。
        // 請確保 fileUploadDir 的值是 "C:/Users/User/eclipse-workspace/profile/uploads/" 且以斜線結尾。
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + fileUploadDir); // 注意 "file:" 前綴

        // 2. 配置其他靜態資源 (例如您的 index.html) 的訪問路徑
        // 當瀏覽器請求根路徑 / 或其他未匹配的路徑時，Spring 會到 classpath:/static/ 去尋找檔案。
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}