package ru.ruszhu.file_sharing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ruszhu.file_sharing_service.model.Url;

import java.util.Optional;
import java.util.UUID;

public interface UrlRepository extends JpaRepository<Url, UUID> {
    Optional<Url> findUrlByShortUrl(String shortUlr);
}
