package com.blendah.backend.service;

import com.blendah.backend.dto.MiniAppResponse;
import com.blendah.backend.model.MiniApp;
import com.blendah.backend.repository.MiniAppRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import com.blendah.backend.dto.MiniAppCreateRequest;
import java.time.OffsetDateTime;

@Service
public class MiniAppService {

    private final MiniAppRepository repository;

    public MiniAppService(MiniAppRepository repository) {
        this.repository = repository;
    }

    public List<MiniAppResponse> getMiniApps(String search) {

        String query = search == null
                ? ""
                : search.trim().toLowerCase(Locale.ROOT);

        return repository.findAll()
                .stream()
                .filter(app ->
                    query.isEmpty()
                    || app.getAppName().toLowerCase(Locale.ROOT)
                        .contains(query)
                    || (app.getDescription() != null
                        && app.getDescription()
                            .toLowerCase(Locale.ROOT)
                            .contains(query))
                )
                .map(this::toResponse)
                .toList();
    }


public MiniAppResponse createMiniApp(MiniAppCreateRequest request) {

    MiniApp app = new MiniApp();

    app.setAppName(request.appName().trim());

    app.setDescription(request.description());

    app.setIconUrl(request.iconUrl());

    app.setAppUrl(request.appUrl().trim());

    // New mini-apps must start as unpublished.
    app.setStatus("UNPUBLISHED");

    // Set timestamps explicitly.
    OffsetDateTime now = OffsetDateTime.now();
    app.setCreatedAt(now);
    app.setUpdatedAt(now);

    MiniApp savedApp = repository.save(app);

    return toResponse(savedApp);
}


    private MiniAppResponse toResponse(MiniApp app) {
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