# VizAssist
VizAssist is an Android App developed by Java. It can realize the functions of taking photos and recognizing the text on images. The App was developed by Android Studio using Gradle, roboletric for unit test, espresso for integration test, emulator using Google Pixel 2, calling for a self-developed HTTP API. The HTTP API was developed by Java in Eclipse JEE using Maven Project Servelet, served locally by Tomcat, tested by Postman, calling for Google OCR API. It was encapsulated as .war and made as docker image to deploy on GCE/GKE. For GKE use, the image was also uploaded on DockerHub.

VizAssist是一个java开发的安卓应用，具有拍照后显示并识别照片上文字的功能（目前仅支持英文识别）。应用部分由android studio开发，gradle打包，roboletric做单元测试，espresso做集成测试，虚机使用google pixel 2测试，调用http api服务实现相应功能。
http api服务由java在eclipse jee使用maven project的servelet开发，本地由tomcat起服务，postman做测试，调用google ocr api实现文字识别，打包成.war文件封装成docker镜像部署到gce/gke。为方便gke部署，镜像已发布到dockerhub。

