# AutoUpdate
android auto update/安卓自动更新模块  
   
#中
1.服务器配置json参数
```json
{  
   "url":"http://....",  
   "md5":"qagi13njidn...",  
   "need_update":"ture",  
   "is_force":"false"  
}  
```
你可以自己添加参数，在 [UpdateNet](https://github.com/guohuanwen/AutoUpdate/blob/master/updatelib/src/main/java/com/bcgtgjyb/updatelib/UpdateNet.java)解析,并更改 [UpdateInfo](https://github.com/guohuanwen/AutoUpdate/blob/master/updatelib/src/main/java/com/bcgtgjyb/updatelib/UpdateInfo.java)
  
2.现在的dialog很简陋，你可以自己设计 [DialogActivity](https://github.com/guohuanwen/AutoUpdate/blob/master/updatelib/src/main/java/com/bcgtgjyb/updatelib/dialog/DialogActivity.java)
   
3.此时代码中的UpdateInfo是本地写的测试数据，在[UpdateUtil](https://github.com/guohuanwen/AutoUpdate/blob/master/updatelib/src/main/java/com/bcgtgjyb/updatelib/UpdateUtil.java)中的有如下方法  

```java
   private UpdateInfo test() {
      UpdateInfo updateInfo = new UpdateInfo();
      updateInfo.apkUrl = "http://ddmyapp.kw.tc.qq.com/16891/9DF04CE7F7706D080E05E321A80234BB.apk?mkey=576d005f82ff575e&f=ae10&c=0&fsname=com.devuni.flashlight_10.0.6_20160624.apk&p=.apk";
      updateInfo.is_force = true;
      updateInfo.md5 = "9df04ce7f7706d080e05e321a80234bb";
      updateInfo.need_update = true;
      return updateInfo;
   }
```
  
  
  
  
  
#English
1.your server should set up the json  
```json
{  
   "url":"http://....",  
   "md5":"qagi13njidn...",  
   "need_update":"ture",  
   "is_force":"false"  
}   
```
you can add other param,and analyze json data in [UpdateNet](https://github.com/guohuanwen/AutoUpdate/blob/master/updatelib/src/main/java/com/bcgtgjyb/updatelib/UpdateNet.java),change [UpdateInfo](https://github.com/guohuanwen/AutoUpdate/blob/master/updatelib/src/main/java/com/bcgtgjyb/updatelib/UpdateInfo.java)
  
2.the dialog is simple,you can design [DialogActivity](https://github.com/guohuanwen/AutoUpdate/blob/master/updatelib/src/main/java/com/bcgtgjyb/updatelib/dialog/DialogActivity.java)  
  
3.at now UpdateInfo is test data，like this test method in [UpdateUtil](https://github.com/guohuanwen/AutoUpdate/blob/master/updatelib/src/main/java/com/bcgtgjyb/updatelib/UpdateUtil.java)     
```java
   private UpdateInfo test() {
      UpdateInfo updateInfo = new UpdateInfo();
      updateInfo.apkUrl = "http://ddmyapp.kw.tc.qq.com/16891/9DF04CE7F7706D080E05E321A80234BB.apk?mkey=576d005f82ff575e&f=ae10&c=0&fsname=com.devuni.flashlight_10.0.6_20160624.apk&p=.apk";
      updateInfo.is_force = true;
      updateInfo.md5 = "9df04ce7f7706d080e05e321a80234bb";
      updateInfo.need_update = true;
      return updateInfo;
   }
```
  
  
  
  
  
  
#License
    Copyright 2016 guohuanwen

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
