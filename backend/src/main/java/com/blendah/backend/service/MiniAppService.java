
package com.blendah.backend.service;

import com.blendah.backend.dto.MiniAppCreateRequest;
import com.blendah.backend.dto.MiniAppResponse;
import com.blendah.backend.dto.MiniAppUpdateRequest;
import com.blendah.backend.model.MiniApp;
import com.blendah.backend.repository.MiniAppRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class MiniAppService {

    private final MiniAppRepository repository;

    public MiniAppService(MiniAppRepository repository) {
        this.repository = repository;
    }

    // View and Search Available Mini Apps
public List<MiniAppResponse> getMiniApps(String search) {

    String query = search == null
            ? ""
            : search.trim().toLowerCase(Locale.ROOT);

    return repository.findByStatusIgnoreCase("PUBLISHED")
            .stream()
            .filter(app ->
                query.isEmpty()
                || app.getAppName()
                    .toLowerCase(Locale.ROOT)
                    .contains(query)
                || (app.getDescription() != null
                    && app.getDescription()
                        .toLowerCase(Locale.ROOT)
                        .contains(query))
            )
            .map(this::toResponse)
            .toList();
}

    // Add New Mini App
    public MiniAppResponse createMiniApp(
            MiniAppCreateRequest request) {

        MiniApp app = new MiniApp();

        app.setAppName(request.appName().trim());
        app.setDescription(request.description());
        app.setIconUrl(request.iconUrl());
        app.setAppUrl(request.appUrl().trim());
        app.setStatus("UNPUBLISHED");

        OffsetDateTime now = OffsetDateTime.now();

        app.setCreatedAt(now);
        app.setUpdatedAt(now);

        MiniApp savedApp = repository.save(app);

        return toResponse(savedApp);
    }

    // Modify Existing Mini App
    public MiniAppResponse updateMiniApp(
            Long id,
            MiniAppUpdateRequest request) {

        MiniApp app = findMiniApp(id);

        app.setAppName(request.appName().trim());
        app.setDescription(request.description());
        app.setIconUrl(request.iconUrl());
        app.setAppUrl(request.appUrl().trim());
        app.setUpdatedAt(OffsetDateTime.now());

        return toResponse(repository.save(app));
    }

    // Publish Mini App
    public MiniAppResponse publishMiniApp(Long id) {

        MiniApp app = findMiniApp(id);

        if (!"UNPUBLISHED".equals(app.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Only unpublished Mini Apps can be published"
            );
        }

        app.setStatus("PUBLISHED");
        app.setUpdatedAt(OffsetDateTime.now());

        return toResponse(repository.save(app));
    }

    // Deactivate Mini App
    public MiniAppResponse deactivateMiniApp(Long id) {

        MiniApp app = findMiniApp(id);

        if (!"PUBLISHED".equals(app.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Only published Mini Apps can be deactivated"
            );
        }

        app.setStatus("INACTIVE");
        app.setUpdatedAt(OffsetDateTime.now());

        return toResponse(repository.save(app));
    }


    // Activate an inactive Mini App
    public MiniAppResponse activateMiniApp(Long id) {

        MiniApp app = findMiniApp(id);

        if (!"INACTIVE".equals(app.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Only inactive Mini Apps can be activated"
            );
        }

        app.setStatus("PUBLISHED");
        app.setUpdatedAt(OffsetDateTime.now());

        return toResponse(repository.save(app));
    }


    // Find Mini App by ID
    private MiniApp findMiniApp(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mini App not found"
                    )
                );
    }

    // Convert Entity to Response DTO
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
