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

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Foreground app checker
 */
final class ForegroundAppObserver {

    private final long period;
    private final TimeUnit timeUnit;
    private final ForegroundAppProvider foregroundAppProvider;

    private String currentForegroundApp;

    private ForegroundAppObserver(ForegroundAppProvider foregroundAppProvider,
                                  long period,
                                  TimeUnit timeUnit) {
        this.foregroundAppProvider = foregroundAppProvider;
        this.period = period;
        this.timeUnit = timeUnit;
    }

    /**
     * Returns a new foreground app listener
     */
    @CheckResult @NonNull
    static Observable<String> create(@NonNull Context context,
                                        long period,
                                        @NonNull TimeUnit timeUnit) {
        return new ForegroundAppObserver(
                ForegroundAppProviderFactory.create(context), period, timeUnit).observe()
                .subscribeOn(Schedulers.io());
    }

    private Observable<String> observe() {
        return Observable.interval(period, timeUnit)
                .flatMapMaybe(__ -> {
                    String foregroundApp = foregroundAppProvider.getForegroundApp();
                    if (foregroundApp != null) {
                        return Maybe.just(foregroundApp);
                    } else {
                        return Maybe.empty();
                    }
                })
                .defaultIfEmpty("")
                .filter(app -> !app.isEmpty()
                        && (currentForegroundApp == null || !currentForegroundApp.equals(app)))
                .doOnNext(app -> currentForegroundApp = app);
    }
}
