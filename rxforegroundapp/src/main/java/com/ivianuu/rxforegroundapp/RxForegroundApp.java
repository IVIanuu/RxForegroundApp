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
import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Static factory methods
 */
public final class RxForegroundApp {

    private RxForegroundApp() {
        // no instances
    }

    /**
     * Emits on foreground app changes
     * This will check the foreground app once per second
     */
    @RequiresPermission(allOf = {Manifest.permission.GET_TASKS, Manifest.permission.PACKAGE_USAGE_STATS})
    @CheckResult
    @NonNull
    public static Observable<String> observeForegroundApp(@NonNull Context context) {
        return observeForegroundApp(context, 1, TimeUnit.SECONDS);
    }

    /**
     * Emits on foreground app changes
     */
    @RequiresPermission(allOf = {Manifest.permission.GET_TASKS, Manifest.permission.PACKAGE_USAGE_STATS})
    @CheckResult
    @NonNull
    public static Observable<String> observeForegroundApp(@NonNull Context context,
                                                          long period,
                                                          @NonNull TimeUnit timeUnit) {
        return ForegroundAppObserver.create(context, period, timeUnit);
    }
}
