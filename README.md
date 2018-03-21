
本项目为测试集成Webview框架集成效果的dmeo

#一、记录Crosswalk集成步骤如下，以供参考：

###1、设置grade外部库为libs，拷贝aar文件到libs

    repositories {
        flatDir {
            dirs 'libs'
        }
    }

###2、关联crosswalk库：

    compile(name: 'crosswalk-23.53.589.4', ext: 'aar')

###3、检查最低系统版本是否为16

###4、清单文件中为application标签设置

    android:hardwareAccelerated="true"

###5、权限要求：

    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> 
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> 
    <uses-permission android:name="android.permission.INTERNET" /> 
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> 
	
###6、项目中引用org.xwalk.core.XWalkView标签   

说明：   
1)、显示XWalkView的Activity需要集成XWalkActivity，并重写onXWalkReady()方法。   
2)、加载网页和配置都需要在Crosswalk准备好，即在onXWalkReady()方法中进行。   
3)、XWalkView配置和webview基本相同，可以参考webview对XWalkView进行配置。   
4)、WebView中的setwebViewClient(webViewClient)对应XWalkView中的
setResourceClient(new XWalkResourceClient(mWebView)方法。   
5)、WebView中的setWebChromeClient(webChromeClient)对应XWalkView中的
setUIClient(new XWalkUIClient(mWebView)方法。   
6)、当然为了避免关闭xWalkView引起内存泄露的问题，建议动态加载Webview。

#二、关于webkit下的Webview集成

###1、如何避免WebView内存泄漏    

1）  不在xml中定义 Webview ，而是在需要的时候在Activity中创建，
并且Context使用 getApplicationgContext()

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mWebView = new WebView(getApplicationContext());
            mWebView.setLayoutParams(params);
            mLayout.addView(mWebView);    
  
<br>
2）在 Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，
然后移除 WebView，再销毁 WebView，最后置空。    

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
 
 
 <br> 
 <br> 
 <br> 
 <br>
 
 
 
 
 