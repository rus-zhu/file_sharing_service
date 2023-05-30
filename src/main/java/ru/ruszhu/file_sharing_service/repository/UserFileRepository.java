package ru.ruszhu.file_sharing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ruszhu.file_sharing_service.model.UserFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFileRepository extends JpaRepository<UserFile, UUID> {
    Optional<UserFile> findUserFileByUrlShortUrl(String shortUrl);

    List<UserFile> findAllByUrlShortUrl(String shortUrl);
}
