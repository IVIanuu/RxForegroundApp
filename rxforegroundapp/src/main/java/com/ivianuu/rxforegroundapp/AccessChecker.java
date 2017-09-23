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

import android.Manifest;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Static access methods
 */
final class AccessChecker {

    /**
     * Returns whether the app has the usage stats permission or not
     */
    static boolean hasUsageStatsPermission(@NonNull Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1){
            return true;
        } else {
            AppOpsManager appOps
                    = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                    android.os.Process.myUid(), context.getPackageName());
            if (mode != AppOpsManager.MODE_ALLOWED) {
                return false;
            }

            // Verify that access is possible. Some devices "lie" and return MODE_ALLOWED even when it's not.
            final long now = System.currentTimeMillis();
            final UsageStatsManager usageStatsManager
                    = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            final List<UsageStats> stats
                    = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 10, now);
            return (stats != null && !stats.isEmpty());
        }
    }

    /**
     * Returns whether the app has the get tasks permission or not
     */
    static boolean hasGetTasksPermission(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return false;
        } else {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            return packageManager.checkPermission(
                    Manifest.permission.GET_TASKS, packageName) == PackageManager.PERMISSION_GRANTED;
        }
    }
}
