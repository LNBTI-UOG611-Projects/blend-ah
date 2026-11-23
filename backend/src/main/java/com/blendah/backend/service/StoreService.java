package com.blendah.backend.service;

import com.blendah.backend.dto.MiniAppResponse;
import com.blendah.backend.model.MiniApp;
import com.blendah.backend.repository.MiniAppRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** Mini-App Store: read-only, PUBLISHED apps only (Member 2). */
@Service
public class StoreService {

    public static final String PUBLISHED = "PUBLISHED";

    private final MiniAppRepository miniAppRepository;

    public StoreService(MiniAppRepository miniAppRepository) {
        this.miniAppRepository = miniAppRepository;
    }

    public List<MiniAppResponse> getPublishedApps(String search) {
        String query = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);

        return miniAppRepository.findByStatusIgnoreCase(PUBLISHED)
                .stream()
                .filter(app -> query.isEmpty()
                        || app.getAppName().toLowerCase(Locale.ROOT).contains(query)
                        || (app.getDescription() != null
                            && app.getDescription().toLowerCase(Locale.ROOT).contains(query)))
                .sorted(Comparator.comparing(
                        (MiniApp a) -> a.getAppName().toLowerCase(Locale.ROOT)))
                .map(StoreService::toResponse)
                .toList();
    }

    public MiniAppResponse getPublishedApp(Long id) {
        MiniApp app = miniAppRepository.findById(id)
                .filter(a -> PUBLISHED.equalsIgnoreCase(a.getStatus()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mini App not found"));
        return toResponse(app);
    }

    public static MiniAppResponse toResponse(MiniApp app) {
        return new MiniAppResponse(
                app.getAppId(),
                app.getAppName(),
                app.getDescription(),
                app.getIconUrl(),
                app.getAppUrl(),
                app.getStatus(),
                app.getCreatedAt()
        );
    }
}
