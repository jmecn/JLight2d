## JLight2D

Light rendering in 2D (Java).

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

### Constructive Solid Geometry

Source code : [RayTracing1Csg.java](https://github.com/jmecn/JLight2d/blob/master/src/main/java/net/jmecn/renderer/RayTracing1Csg.java)

Use union operation for creating multiple shapes:

![](screenshots/04_Csg_Capsule.png)

![](screenshots/05_Csg_Triangle.png)

![](screenshots/06_Csg_Moon.png)

### Reflection

Source code : [RayTracing2Reflection.java](https://github.com/jmecn/JLight2d/blob/master/src/main/java/net/jmecn/renderer/RayTracing2Reflection.java)

![](screenshots/07_Reflection.png)

![](screenshots/07_Reflection2.png)

### Refraction

Source code : [RayTracing3Refraction.java](https://github.com/jmecn/JLight2d/blob/master/src/main/java/net/jmecn/renderer/RayTracing3Refraction.java)

Applying Snell's law to compute refraction direction. Total internal reflection is also handled.

![](screenshots/08_Refraction.png)

![](screenshots/09_Refraction_ConvexLens.png)


### Fresnel Reflectance

Source code : [RayTracing4Fresnel.java](https://github.com/jmecn/JLight2d/blob/master/src/main/java/net/jmecn/renderer/RayTracing4Fresnel.java)

Applying Fresnel equation to compute reflectance of dielectric medium.

without fresnel:

![](screenshots/10_Refraction_ConvexLens.png)

with fresnel term:

![](screenshots/11_Fresnel_ConvexLens.png)

### Beer-Lambert

Source code : [RayTracing5Absorption.java](https://github.com/jmecn/JLight2d/blob/master/src/main/java/net/jmecn/renderer/RayTracing5Absorption.java)

Applying Beer-Lambert law to simulate absorption of light in medimum.

![](screenshots/12_Absorption_ConvexLens.png)

![](screenshots/13_Absorption_Ngon.png)