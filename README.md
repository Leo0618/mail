# mail
简易邮箱发送管理器，目前暂时仅支持126邮箱，需要扩展可修改源码。



## Step1 添加权限 ##


    <uses-permission android:name="android.permission.INTERNET" />

## Step2 添加依赖 ##

	dependencies {
		compile 'com.leo618:mail:latest.integration'
	}

## Step3 代码使用 ##

	MailManager.config(senderMailName, senderMailPassword, revieverMailName)
               .sendMail(subjectStr, contentStr)

详情可参考demo中 [MainActivity.java](https://github.com/Leo0618/mail/blob/master/app/src/main/java/com/leo618/email/MainActivity.java "MainActivity.java")

## Step4 添加混淆(如需) ##

	-ignorewarning
	-dontwarn
	-keep class com.sun.** { *; }
	-keep class javax.mail.** { *; }
	-keep class javax.activation.** { *; }
	-keep class org.apache.** { *; }
	-keep class com.leo618.email.MailManager{
	       public *;
	}


----

参考：  https://github.com/Dev-Wiki/MailSender
