## JLight2D

Draw light in Java.

Translate from "Light rendering in 2D" by [miloyip](https://github.com/miloyip/light2d).

### GUI

I made a swing GUI to setting renderer params.

![](screenshots/00_SettingDialog.png)

### Basic

Use Monte Carol integration and ray marching of signed distance field (SDF) to render a emissive circle.

Source code: 

* [Renderer.java](https://github.com/jmecn/JLight2d/blob/master/src/main/java/net/jmecn/Renderer.java)
* [RayTracing0Base.java](https://github.com/jmecn/JLight2d/blob/master/src/main/java/net/jmecn/renderer/RayTracing0Base.java)

Uniform sampling (64 samples per pixel):

![](screenshots/01_Uniform_64.png)

Stratified sampling (64 samples per pixel):

![](screenshots/02_Stratified_64.png)

Jittered sampling (64 samples per pixel):

![](screenshots/03_Jittered_64.png)

