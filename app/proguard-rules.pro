
-ignorewarning
-dontwarn
-keep class com.sun.** { *; }
-keep class javax.mail.** { *; }
-keep class javax.activation.** { *; }
-keep class org.apache.** { *; }
-keep class com.leo618.email.MailManager{
       public *;
}