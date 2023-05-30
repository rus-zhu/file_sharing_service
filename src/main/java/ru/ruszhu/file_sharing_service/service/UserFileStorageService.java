package ru.ruszhu.file_sharing_service.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ruszhu.file_sharing_service.model.Url;
import ru.ruszhu.file_sharing_service.model.UserFile;
import ru.ruszhu.file_sharing_service.repository.UserFileRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFileStorageService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserFileStorageService.class);
    private final UserFileRepository userFileRepository;

    public UserFile saveFile(MultipartFile file, Url newUrl) {
        String fileName = file.getOriginalFilename();
        try {
            UserFile userFile = UserFile.builder()
                                        .fileName(fileName)
                                        .fileType(file.getContentType())
                                        .data(file.getBytes())
                                        .size(file.getSize())
                                        .url(newUrl)
                                        .build();
            LOGGER.info("File \"" + file.getOriginalFilename() + "\" was saved");
            return userFileRepository.save(userFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<UserFile> getFile(UUID id) {
        return userFileRepository.findById(id);
    }

    public List<UserFile> getFiles(String shortLink) {
        return userFileRepository.findAllByUrlShortUrl(shortLink);
    }
}
