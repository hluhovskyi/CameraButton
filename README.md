# CameraButton

Instagram-like button for taking photos or recording videos.

## Getting started

Add library as dependency to your `build.gradle`.

WIP // Without any dependencies

WIP // For RxJava2 users

WIP // For RxJava2 and Kotlin users

Please, feel free to open issues you are stuck with. PRs are also welcome :)

## How to use?

WIP

## Customization

- `cb_main_circle_radius` or `setMainCircleRadius()`

Default value - `28dp`

<img width="250" src="/.github/arts/cb_main_circle_radius.png">

- `cb_main_circle_color` or `setMainCircleColor()`

Default value - `#ffffff`

<img width="250" src="/.github/arts/cb_main_circle_color.png">

- `cb_stroke_width` or `setStrokeWidth()`

Default value - `12dp`

<img width="250" src="/.github/arts/cb_stroke_width.png">

- `cb_main_circle_radius_expanded` or `setMainCircleRadiusExpanded()`

Default value - `24dp`

<img width="250" src="/.github/arts/cb_main_circle_radius_expanded.png">

- Expanded stroke width can't be set explicitly. It is calculated by following formula:

`stroke_width_expanded = min(layout_width, layout_height) - main_circle_expanded`

<img width="250" src="/.github/arts/cb_stroke_width_expanded.png">

- `cb_progress_arc_width` or `setProgressArcWidth()`

Default value - `4dp`

<img width="250" src="/.github/arts/cb_progress_arc_width.png">

- `cb_progress_arc_colors` or `setProgressArcColors()`

Default values - `[#feda75, #fa7e1e, #d62976, #962fbf, #4f5bd5]`

<img width="250" src="/.github/arts/cb_progress_arc_colors.png">

To set values via xml you have to define all colors separately and **merge their references into one array**:

    <color name="my_color_1">#000000</color>
    <color name="my_color_2">#ffffff</color>

    <array name="my_progress_colors">
        <item>@color/my_color_1</item>
        <item>@color/my_color_2</item>
    </array>
    
    ...
    
    <com.dewarder.camerabutton.CameraButton
        ...
        app:cb_progress_arc_colors="@color/my_progress_colors"/>
        
To set values programmatically you have to call `setProgressArcColors` with **array of color values**:

     setProgressArcColors(new int[]{Color.BLACK, Color.WHITE});
        

## License

```
Copyright (C) 2017 Artem Glugovsky

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
