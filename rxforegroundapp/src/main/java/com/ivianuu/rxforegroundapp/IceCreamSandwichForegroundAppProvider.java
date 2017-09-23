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

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Ice cream sandwich implementation of an foreground app provider
 */
final class IceCreamSandwichForegroundAppProvider implements ForegroundAppProvider {

    private final ActivityManager activityManager;
    private final PackageManager packageManager;

    private IceCreamSandwichForegroundAppProvider(ActivityManager activityManager,
                                                  PackageManager packageManager) {
        this.activityManager = activityManager;
        this.packageManager = packageManager;
    }

    /**
     * Returns a new ice cream sandwich foreground app provider
     */
    @NonNull
    static ForegroundAppProvider create(@NonNull Context context) {
        return new IceCreamSandwichForegroundAppProvider(
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE), context.getPackageManager());
    }

    @Nullable
    @Override
    public String getForegroundApp() {
        ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
        String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
        PackageInfo foregroundAppPackageInfo;
        try {
            foregroundAppPackageInfo = packageManager.getPackageInfo(foregroundTaskPackageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String foregroundApp = null;
        if (foregroundAppPackageInfo != null) {
            foregroundApp = foregroundAppPackageInfo.applicationInfo.packageName;
        }

        return foregroundApp;
    }
}
