/*
 * Copyright 2017 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.rxforegroundapp;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

/**
 * Lollipop implementation of an foreground app provider
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
final class LollipopForegroundAppProvider implements ForegroundAppProvider {

    private final UsageStatsManager usageStatsManager;

    private LollipopForegroundAppProvider(UsageStatsManager usageStatsManager) {
        this.usageStatsManager = usageStatsManager;
    }

    /**
     * Creates a new lollipop foreground app provider
     */
    @NonNull
    static ForegroundAppProvider create(@NonNull Context context) {
        return new LollipopForegroundAppProvider(
                (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE));
    }

    @Nullable
    @Override
    public String getForegroundApp() {
        String foregroundApp = null;

        long now = System.currentTimeMillis();

        UsageEvents usageEvents = usageStatsManager.queryEvents(now - 1000 * 3600, now);
        UsageEvents.Event event = new UsageEvents.Event();
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                foregroundApp = event.getPackageName();
            }
        }

        return foregroundApp;
    }
}
