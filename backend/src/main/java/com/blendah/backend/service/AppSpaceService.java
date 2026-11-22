package com.blendah.backend.service;

import com.blendah.backend.dto.AppSpaceItemResponse;
import com.blendah.backend.dto.DashboardResponse;
import com.blendah.backend.dto.MiniAppResponse;
import com.blendah.backend.model.AppSpaceEntry;
import com.blendah.backend.model.MiniApp;
import com.blendah.backend.model.User;
import com.blendah.backend.repository.AppSpaceRepository;
import com.blendah.backend.repository.MiniAppRepository;
import com.blendah.backend.repository.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Personal App Space + dashboard (Member 2). */
@Service
public class AppSpaceService {

    private static final int MAX_SUGGESTIONS = 5;

    private final AppSpaceRepository appSpaceRepository;
    private final MiniAppRepository miniAppRepository;
    private final UserRepository userRepository;

    public AppSpaceService(AppSpaceRepository appSpaceRepository,
                           MiniAppRepository miniAppRepository,
                           UserRepository userRepository) {
        this.appSpaceRepository = appSpaceRepository;
        this.miniAppRepository = miniAppRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<AppSpaceItemResponse> getAppSpace(UUID userId) {
        List<AppSpaceEntry> entries =
                appSpaceRepository.findByUserIdOrderByAddedAtDesc(userId);

        Map<Long, MiniApp> apps = miniAppRepository
                .findAllById(entries.stream().map(AppSpaceEntry::getAppId).toList())
                .stream()
                .collect(Collectors.toMap(MiniApp::getAppId, Function.identity()));

        return entries.stream()
                .filter(e -> apps.containsKey(e.getAppId()))
                .map(e -> toItem(e, apps.get(e.getAppId())))
                .toList();
    }

    @Transactional
    public AppSpaceItemResponse addApp(UUID userId, Long appId) {
        MiniApp app = miniAppRepository.findById(appId)
                .filter(a -> StoreService.PUBLISHED.equalsIgnoreCase(a.getStatus()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mini App not found"));

        if (appSpaceRepository.existsByUserIdAndAppId(userId, appId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Mini App is already in your App Space");
        }

        AppSpaceEntry entry = new AppSpaceEntry();
        entry.setUserId(userId);
        entry.setAppId(appId);

        try {
            return toItem(appSpaceRepository.saveAndFlush(entry), app);
        } catch (DataIntegrityViolationException e) {
            // two quick taps: the unique (user_id, app_id) constraint caught it
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Mini App is already in your App Space");
        }
    }

    @Transactional
    public void removeApp(UUID userId, Long appId) {
        if (appSpaceRepository.deleteByUserIdAndAppId(userId, appId) == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Mini App is not in your App Space");
        }
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        List<AppSpaceItemResponse> items = getAppSpace(userId);
        Set<Long> owned = items.stream()
                .map(AppSpaceItemResponse::appId)
                .collect(Collectors.toSet());

        List<MiniAppResponse> suggestions = miniAppRepository
                .findByStatusIgnoreCase(StoreService.PUBLISHED)
                .stream()
                .filter(a -> !owned.contains(a.getAppId()))
                .limit(MAX_SUGGESTIONS)
                .map(StoreService::toResponse)
                .toList();

        int unavailable = (int) items.stream().filter(i -> !i.available()).count();

        return new DashboardResponse(
                user.getFullName(), items.size(), unavailable, items, suggestions);
    }

    private AppSpaceItemResponse toItem(AppSpaceEntry entry, MiniApp app) {
        return new AppSpaceItemResponse(
                app.getAppId(),
                app.getAppName(),
                app.getDescription(),
                app.getIconUrl(),
                app.getAppUrl(),
                app.getStatus(),
                StoreService.PUBLISHED.equalsIgnoreCase(app.getStatus()),
                entry.getAddedAt()
        );
    }
}
