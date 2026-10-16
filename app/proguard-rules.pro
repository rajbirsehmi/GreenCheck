# Preserve line numbers and source file attributes for stack trace de-obfuscation
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Preserve Annotations, Signatures, and Inner Classes for reflection/serialization
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Data Transfer Objects (DTOs) & Kotlinx Serialization
-keep class com.creative.greencheck.data.remote.dto.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
-keepclassmembers class * implements kotlinx.serialization.KSerializer {
    public static final ** INSTANCE;
}
-keepclassmembers class **$Companion {
    *** serializer(...);
}

# Room Database Entities and Converters
-keep class com.creative.greencheck.data.local.entity.** { *; }
-keep class com.creative.greencheck.data.local.converters.** { *; }
-keep class * extends androidx.room.RoomDatabase

# Domain Models
-keep class com.creative.greencheck.domain.model.** { *; }

# Retrofit & OkHttp
-dontwarn retrofit2.**
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# ML Kit Barcode Scanning & CameraX
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# Coil Image Loading
-dontwarn coil.**

# Google Credentials & Auth
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
