package ru.ruszhu.file_sharing_service.service;

import ru.ruszhu.file_sharing_service.model.Url;

public interface UrlService {
    Url getShortLinkFromFull(String link);

    Url getFullLinkFromShort(String shortLink);
}
