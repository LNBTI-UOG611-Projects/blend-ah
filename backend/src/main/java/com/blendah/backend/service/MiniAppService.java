package com.blendah.backend.service;

import com.blendah.backend.dto.MiniAppResponse;
import com.blendah.backend.dto.MiniAppUpdateRequest;
import com.blendah.backend.model.MiniApp;
import com.blendah.backend.repository.MiniAppRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

import com.blendah.backend.dto.MiniAppCreateRequest;
import java.time.OffsetDateTime;


import com.blendah.backend.dto.MiniAppUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


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

public MiniAppResponse updateMiniApp(
        Long id,
        MiniAppUpdateRequest request) {

    MiniApp app = repository.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Mini App not found"
                )
            );

    app.setAppName(request.appName().trim());
    app.setDescription(request.description());
    app.setIconUrl(request.iconUrl());
    app.setAppUrl(request.appUrl().trim());

    app.setUpdatedAt(OffsetDateTime.now());

    MiniApp updatedApp = repository.save(app);

    return toResponse(updatedApp);
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