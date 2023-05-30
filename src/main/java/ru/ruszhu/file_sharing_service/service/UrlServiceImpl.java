package ru.ruszhu.file_sharing_service.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ruszhu.file_sharing_service.model.Url;
import ru.ruszhu.file_sharing_service.repository.UrlRepository;
import ru.ruszhu.file_sharing_service.service.exception.ShortLinkNotFound;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {
    private final Logger LOGGER = LoggerFactory.getLogger(LoggerFactory.class);
    private final UrlRepository urlRepository;

    @Override
    public Url getShortLinkFromFull(String host) {
        String fullLink = host + UUID.randomUUID();
        String shortLink = linkShortener(fullLink);
        Url newUrl = Url.builder()
                        .originalUrl(fullLink)
                        .shortUrl(shortLink)
                        .build();
        urlRepository.save(newUrl);
        LOGGER.info("Short link was saved");
        return newUrl;
    }



    @Override
    public Url getFullLinkFromShort(String shortLink) {
        Optional<Url> optional = urlRepository.findUrlByShortUrl(shortLink);
        if (optional.isEmpty()) {
            LOGGER.info(shortLink + " not found");
            throw new ShortLinkNotFound(shortLink + " not found! Check the link");
        }
        return optional.get();
    }

    private String linkShortener(String link) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int len = 10;
        for (int i = 0; i < len; i++) {
            int num = random.nextInt(62);
            String alphSeq = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            sb.append(alphSeq.charAt(num));
        }
        LOGGER.info("Short link from " + link + " generated to -> " + sb);
        return sb.toString();
    }
}
