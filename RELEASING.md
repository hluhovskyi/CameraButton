## Releasing

1.  Upload new version of CameraButton
   
   ```groovy
  gradlew :camerabutton:clean :camerabutton:build :camerabutton:bintrayUpload -PdryRun=false -PbintrayUser=%bintray.user% -PbintrayKey=%bintray.key%
   ```
2. Update dependency in CameraButton-RxJava2 to uploaded version of CameraButton
3. Upload new version of CameraButton-RxJava2

  ```groovy
  gradlew :camerabutton-rxjava2:clean :camerabutton-rxjava2:build :camerabutton-rxjava2:bintrayUpload -PdryRun=false -PbintrayUser=%bintray.user% -PbintrayKey=%bintray.key%
  ```
    
4. Update dependency in CameraButton-RxJava2-Kotlin to uploaded version of CameraButton-RxJava2
5. Upload new version of CameraButton-RxJava2-Kotlin

  ```groovy
  gradlew :camerabutton-rxjava2-kotlin:clean :camerabutton-rxjava2-kotlin:build :camerabutton-rxjava2-kotlin:bintrayUpload -PdryRun=false -PbintrayUser=%bintray.user% -PbintrayKey=%bintray.key%
  ```

6. Change README.