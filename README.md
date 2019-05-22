
# 使用说明
## Step 1. Add the JitPack repository to your build file

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
## 	Step 2. Add the dependency	Step 2. Add the dependency

dependencies {
	        implementation 'com.github.kenxiong0113:BaseLibrary:1.0.0'
	}

## 	Step 3.初始化
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        初始化base_utils_class
        BaseLibrary.initBaseLibrary(this);
//        设置通知栏应用logo
        BaseLibrary.setAppIcon(R.mipmap.ic_launcher);

    }
}

