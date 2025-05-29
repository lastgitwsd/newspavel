plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinSerialization)
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}
dependencies {
    api(libs.retrofit)
    api(libs.kotlinx.serialization.json)
    api(libs.retrofitSerializationConverter)
    api(libs.retrofit.adapters)
    implementation(libs.kotlin.corutines.core)
    implementation(libs.androidx.annotation)



}
