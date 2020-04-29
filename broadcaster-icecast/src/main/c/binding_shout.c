#include <stdlib.h>
#include <unistd.h>
#include <jni.h>
#include <shout/shout.h>
#include <stdio.h>
#include "com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout.h"

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1init
  (JNIEnv *env, jclass class) {
  shout_init();
}

JNIEXPORT void JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1shutdown
  (JNIEnv *env, jclass class) {
  shout_shutdown();
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_new
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1new
  (JNIEnv *env, jclass class) {
  return (jlong)(intptr_t)shout_new();
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1free
  (JNIEnv *env, jclass class, jlong shoutInstance) {
  shout_free((void *)(intptr_t)shoutInstance);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_get_error
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1get_1error
  (JNIEnv *env, jclass class, jlong shoutInstance) {
  return (*env)->NewStringUTF(env, shout_get_error((void *)(intptr_t)shoutInstance));
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_get_errno
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1get_1errno
  (JNIEnv *env, jclass class, jlong shoutInstance) {
  return (jint)shout_get_errno((void *)(intptr_t)shoutInstance);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_get_connected
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1get_1connected
  (JNIEnv *env, jclass class, jlong shoutInstance) {
  return (jint)shout_get_connected((void *)(intptr_t)shoutInstance);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_host
 * Signature: (JLjava/lang/String;)V
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1host
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring host) {
   return (jint)shout_set_host((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, host, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_port
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1port
  (JNIEnv *env, jclass class, jlong shoutInstance, jint port){
  return (jint)shout_set_port((void *)(intptr_t)shoutInstance, (unsigned int)port);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_agent
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1agent
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring agent) {
  return (jint)shout_set_agent((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, agent, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_user
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1user
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring user) {
  return (jint)shout_set_user((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, user, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_password
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1password
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring password) {
  return (jint)shout_set_password((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, password, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_mount
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1mount
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring mount) {
  return (jint)shout_set_mount((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, mount, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_public
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1public
  (JNIEnv *env, jclass class, jlong shoutInstance, jint makePublic) {
  return (jint)shout_set_public((void *)(intptr_t)shoutInstance, (int)makePublic);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_format
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1format
  (JNIEnv *env, jclass class, jlong shoutInstance, jint format) {
  return (jint)shout_set_format((void *)(intptr_t)shoutInstance, (unsigned int)format);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_protocol
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1protocol
  (JNIEnv *env, jclass class, jlong shoutInstance, jint protocol) {
    return (jint)shout_set_protocol((void *)(intptr_t)shoutInstance, (unsigned int)protocol);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_open
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1open
  (JNIEnv *env , jclass class, jlong shoutInstance) {
  return (jint)shout_open((void *)(intptr_t)shoutInstance);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_close
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1close
  (JNIEnv *env, jclass class, jlong shoutInstance){
  return (jint)shout_close((void *)(intptr_t)shoutInstance);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_send
 * Signature: (J[BI)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1send
  (JNIEnv *env, jclass class, jlong shoutInstance, jbyteArray dataArray, jint dataLentght) {
  jbyte *buffer;
  int ret;
  buffer = (*env)->GetByteArrayElements(env, dataArray, 0);
  ret = shout_send((void *)(intptr_t)shoutInstance, (unsigned char*)buffer, (int)dataLentght);
  (*env)->ReleaseByteArrayElements(env, dataArray, buffer, 0);
  return (jint)ret;
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_sync
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1sync
  (JNIEnv *env, jclass class, jlong shoutInstance){
  shout_sync((void *)(intptr_t)shoutInstance);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_version
 * Signature: (III)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1version
  (JNIEnv *env, jclass class, jint x, jint y, jint z) {
  return (*env)->NewStringUTF(env, shout_version((void *)(intptr_t)x, (void *)(intptr_t)y, (void *)(intptr_t)z));
}


/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_name
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1name
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring name) {
    return (jint)shout_set_name((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, name, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_description
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1description
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring description) {
    return (jint)shout_set_description((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, description, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_genre
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1genre
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring genre){
    return (jint)shout_set_genre((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, genre, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_url
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1url
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring url) {
    return (jint)shout_set_url((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, url, 0));
}


/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_metadata_new
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1metadata_1new
  (JNIEnv *env, jclass class) {
    return (jlong)(intptr_t)shout_metadata_new();
}


/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_set_metadata
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1set_1metadata
  (JNIEnv *env, jclass class, jlong shoutInstance, jlong metadataPtr) {
    return (jint)shout_set_metadata((void *)(intptr_t)shoutInstance, (void *)(intptr_t)metadataPtr);
}

/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_metadata_add
 * Signature: (JLjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1metadata_1add
  (JNIEnv *env, jclass class, jlong metadataPtr, jstring key, jstring value) {
    return (jint)shout_metadata_add((void *)(intptr_t)metadataPtr, (*env)->GetStringUTFChars(env, key, 0), (*env)->GetStringUTFChars(env, value, 0));
}


/*
 * Class:     com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout
 * Method:    shout_metadata_free
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_github_jpthiery_hermodr_infra_broadcaster_icecast_binding_BindingLibShout_shout_1metadata_1free
  (JNIEnv *env, jclass class, jlong metadataPtr) {
    shout_metadata_free((void *)(intptr_t)metadataPtr);
}