
package com.blendah.backend.repository;

import com.blendah.backend.model.MiniApp;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MiniAppRepository {

    private final List<MiniApp> miniApps = new ArrayList<>();

    public MiniAppRepository() {

        MiniApp app1 = new MiniApp();
        app1.setAppId(UUID.randomUUID());
        app1.setAppName("Food Delivery");
        app1.setDescription("Order your favourite food online");
        app1.setCategory("Food");
        app1.setIconUrl("food.png");
        app1.setStatus(MiniApp.AppStatus.ACTIVE);
        app1.setCreatedAt(OffsetDateTime.now());

        MiniApp app2 = new MiniApp();
        app2.setAppId(UUID.randomUUID());
        app2.setAppName("Online Shopping");
        app2.setDescription("Shop your favourite products");
        app2.setCategory("Shopping");
        app2.setIconUrl("shopping.png");
        app2.setStatus(MiniApp.AppStatus.ACTIVE);
        app2.setCreatedAt(OffsetDateTime.now());

        miniApps.add(app1);
        miniApps.add(app2);
    }

    public List<MiniApp> findAllActive() {
        return miniApps.stream()
                .filter(app -> app.getStatus() == MiniApp.AppStatus.ACTIVE)
                .toList();
    }
}

