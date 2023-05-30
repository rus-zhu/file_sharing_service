package ru.ruszhu.file_sharing_service.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.ruszhu.file_sharing_service.model.Url;
import ru.ruszhu.file_sharing_service.model.UserFile;
import ru.ruszhu.file_sharing_service.service.UrlService;
import ru.ruszhu.file_sharing_service.service.UserFileStorageService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserFileController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserFileController.class);
    private final UserFileStorageService userFileStorageService;
    private final UrlService urlService;
    private final String HOST = "http://localhost:9090/";

    @GetMapping("/")
    public String get() {
        return "upload_files";
    }

    @PostMapping("/upload_files")
    public String uploadFiles(@RequestParam("files") MultipartFile[] files, Model model) {
        Url newUrl = urlService.getShortLinkFromFull(HOST);
        LOGGER.info("New URL was created - " + HOST + newUrl.getShortUrl());
        for (MultipartFile file : files) {
            userFileStorageService.saveFile(file, newUrl);
        }
        model.addAttribute("link", HOST + newUrl.getShortUrl());
        return "link";
    }

    @GetMapping("/{shortUrl}")
    public String downloadFilePage(@PathVariable(name = "shortUrl") String shortUrl, Model model) {
        List<UserFile> userFiles = userFileStorageService.getFiles(shortUrl);
        model.addAttribute("user_files", userFiles);
        return "download_files";
    }

    @GetMapping("/download_file/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "fileId") String fileId) {
        UserFile userFile = userFileStorageService.getFile(UUID.fromString(fileId)).get();
        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(userFile.getFileType()))
                             .header(
                                     HttpHeaders.CONTENT_DISPOSITION,
                                     "attachment:filename=\"" + userFile.getFileName() + "\"")
                             .body(new ByteArrayResource(userFile.getData()));
    }
}
