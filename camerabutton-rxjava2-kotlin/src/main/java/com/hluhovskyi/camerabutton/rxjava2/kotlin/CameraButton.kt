/*
 * Copyright (C) 2018 Artem Hluhovskyi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hluhovskyi.camerabutton.rxjava2.kotlin

import com.hluhovskyi.camerabutton.CameraButton
import com.hluhovskyi.camerabutton.rxjava2.*
import io.reactivex.Observable

fun CameraButton.stateChanges(): Observable<CameraButton.State>
        = RxCameraButton.stateChanges(this)

fun CameraButton.stateChangeEvents(): Observable<StateChangeEvent>
        = RxCameraButton.stateChangeEvents(this)

fun CameraButton.progress(): Observable<Float>
        = RxCameraButton.progress(this)

fun CameraButton.progressEvents(): Observable<ProgressEvent>
        = RxCameraButton.progressEvents(this)

fun CameraButton.photoEvents(): Observable<PhotoEvent>
        = RxCameraButton.photoEvents(this)

fun CameraButton.videoEvents(): Observable<VideoEvent>
        = RxCameraButton.videoEvents(this)