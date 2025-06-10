package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    // 從 application.properties 讀取檔案上傳目錄
    @Value("${file.upload.dir}")
    private String fileUploadDir;

    private Path fileStorageLocation;

    // 構造函數會在服務啟動時初始化儲存位置
    public FileStorageService(@Value("${file.upload.dir}") String fileUploadDir) {
        this.fileUploadDir = fileUploadDir;
        this.fileStorageLocation = Paths.get(fileUploadDir).toAbsolutePath().normalize();

        try {
            // 檢查目錄是否存在，如果不存在則建立
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("無法建立檔案儲存目錄！", ex);
        }
    }

    /**
     * 儲存上傳的檔案。
     * @param file 要儲存的 MultipartFile 物件。
     * @return 儲存後檔案的相對路徑 (URL)。
     * @throws RuntimeException 如果檔案儲存失敗。
     */
    public String storeFile(MultipartFile file) {
        // 正規化檔案名，防止目錄遍歷攻擊
        String fileName = UUID.randomUUID().toString() + "_" +
                          Objects.requireNonNull(file.getOriginalFilename()).replaceAll("[^a-zA-Z0-9.-]", "_"); // 清理檔案名，替換特殊字符

        try {
            // 檢查檔案名是否包含無效字符
            if (fileName.contains("..")) {
                throw new RuntimeException("檔案名包含無效的路徑序列 " + fileName);
            }

            // 將檔案複製到目標位置 (覆蓋同名檔案)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 返回檔案的相對 URL。在生產環境中，您可能需要返回一個可公開訪問的絕對 URL
            // 例如：http://yourdomain.com/uploads/your_file_name.jpg
            return "/uploads/" + fileName; // 這裡返回一個簡單的相對路徑，需要前端或Controller層配合處理
        } catch (IOException ex) {
            throw new RuntimeException("無法儲存檔案 " + fileName + "。請重試！", ex);
        }
    }

    /**
     * 從儲存位置載入檔案。
     * @param fileName 檔案的名稱。
     * @return 檔案的 Path 物件。
     */
    public Path loadFileAsResource(String fileName) {
        return this.fileStorageLocation.resolve(fileName).normalize();
    }

    /**
     * 刪除指定路徑的檔案。
     * 注意：這個方法需要謹慎使用，確保有適當的權限檢查。
     * @param filePath 檔案的相對路徑，例如 "/uploads/your_file_name.jpg"
     * @return 如果成功刪除則為 true，否則為 false。
     */
    public boolean deleteFile(String filePath) {
        if (filePath == null || !filePath.startsWith("/uploads/")) {
            // 確保只處理我們管理的檔案路徑
            return false;
        }
        String fileName = filePath.substring("/uploads/".length());
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName).normalize();
            // 確保要刪除的檔案確實位於預期的儲存目錄內
            if (!targetLocation.startsWith(this.fileStorageLocation)) {
                throw new RuntimeException("嘗試刪除的檔案不在允許的目錄範圍內！");
            }
            return Files.deleteIfExists(targetLocation);
        } catch (IOException | RuntimeException ex) {
            System.err.println("無法刪除檔案: " + fileName + ". 錯誤: " + ex.getMessage());
            return false;
        }
    }

    // 您可能還需要一個方法來獲取檔案的 URL，這取決於您的部署方式
    // 對於本地儲存，通常會透過 Spring Boot 靜態資源映射來提供訪問
    public String getFileUrl(String fileName) {
        // 如果您的應用程式在 example.com 上運行，並且將 /uploads 映射到此服務的檔案，
        // 則返回完整的 URL：
        // return "http://yourdomain.com/uploads/" + fileName;
        // 這裡暫時返回相對路徑，具體根據前端如何訪問這些檔案來調整
        return "/uploads/" + fileName;
    }
}