package com.dewarder.camerabutton.rx.kotlin

import com.dewarder.camerabutton.CameraButton
import com.dewarder.camerabutton.rx.HoldEvent
import com.dewarder.camerabutton.rx.RxCameraButton
import com.dewarder.camerabutton.rx.TapEvent
import io.reactivex.Observable

fun CameraButton.stateChanges(): Observable<CameraButton.State>
        = RxCameraButton.stateChanges(this)

fun CameraButton.progress(): Observable<Float>
        = RxCameraButton.progress(this)

fun CameraButton.tapEvents(): Observable<TapEvent>
        = RxCameraButton.tapEvents(this)

fun CameraButton.holdEvents(): Observable<HoldEvent>
        = RxCameraButton.holdEvents(this)