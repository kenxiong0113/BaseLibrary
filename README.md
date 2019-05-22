
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

1. 初始化sdk
//        初始化base_utils_class
        BaseLibrary.initBaseLibrary(this);
//        设置应用图标，通知栏用到
        BaseLibrary.setAppIcon(R.mipmap.ic_launcher);

