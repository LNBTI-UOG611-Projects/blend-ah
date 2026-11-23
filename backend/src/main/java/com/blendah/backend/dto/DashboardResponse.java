package com.blendah.backend.dto;

import java.util.List;

public record DashboardResponse(
    String fullName,
    int totalApps,
    int unavailableApps,
    List<AppSpaceItemResponse> apps,
    List<MiniAppResponse> suggestions
) {}
