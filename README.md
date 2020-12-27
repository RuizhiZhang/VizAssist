# VizAssist
VizAssist is a OCR Android App developed by Java using Android Studio and Google API.

VizAssist是一个java开发的安卓应用，具有拍照后显示并识别照片上文字的功能（目前仅支持英文识别）。应用部分由android studio开发，gradle打包，roboletric做单元测试，espresso做集成测试，虚机使用google pixel 2测试，调用http api服务实现相应功能。
http api服务由java在eclipse jee使用maven project的servelet开发，本地由tomcat起服务，postman做测试，调用google ocr api实现文字识别，打包成.war文件封装成docker镜像部署到gce/gke。为方便gke部署，镜像已发布到dockerhub。

