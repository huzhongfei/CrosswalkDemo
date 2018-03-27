
本项目为测试集成Webview框架集成效果的dmeo

# 一、记录TBS X5集成步骤如下，以供参考：

### 1、到TBS官网下载SDK，并拷贝SDK文件到libs


### 2、下载demo将so包放进src/main/jniLibs

### 3、AndroidManifest.xml里加入权限声明：

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />

### 4、application的onCreate中调用：
		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
		
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
			
			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				Log.d("app", " onViewInitFinished is " + arg0);
			}
			
			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);
		
### 5、X5WebView weview 和 WebSettings

备注：   
1、详细步骤请参考TBS官网和demo教程：http://x5.tencent.com/tbs/guide/sdkInit.html    
2、测试的时候编译了CrossWalk的引擎，又编译了X5的引擎，将会导致X5只会采用native的webview。
如果非要在项目中测试这两种引擎，可以放在两个分支分别来做。

 
 <br> 
 <br> 
 <br> 
 <br>
 
 
 
 
 