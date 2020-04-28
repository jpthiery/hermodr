#include <stdlib.h>
#include <unistd.h>
#include <jni.h>
#include <shout/shout.h>
#include <stdio.h>
#include "com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout.h"

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1init
  (JNIEnv *env, jclass class) {
  shout_init();
}

JNIEXPORT void JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1shutdown
  (JNIEnv *env, jclass class) {
  shout_shutdown();
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_new
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1new
  (JNIEnv *env, jclass class) {
  return (jlong)(intptr_t)shout_new();
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1free
  (JNIEnv *env, jclass class, jlong shoutInstance) {
  shout_free((void *)(intptr_t)shoutInstance);
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_get_error
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1get_1error
  (JNIEnv *env, jclass class, jlong shoutInstance) {
  return (*env)->NewStringUTF(env, shout_get_error((void *)(intptr_t)shoutInstance));
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_get_errno
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1get_1errno
  (JNIEnv *env, jclass class, jlong shoutInstance) {
  return (jint)shout_get_errno((void *)(intptr_t)shoutInstance);
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_get_connected
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1get_1connected
  (JNIEnv *env, jclass class, jlong shoutInstance) {
  return (jint)shout_get_connected((void *)(intptr_t)shoutInstance);
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_set_host
 * Signature: (JLjava/lang/String;)V
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1set_1host
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring host) {
   return (jint)shout_set_host((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, host, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_set_port
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1set_1port
  (JNIEnv *env, jclass class, jlong shoutInstance, jint port){
  return (jint)shout_set_port((void *)(intptr_t)shoutInstance, (unsigned int)port);
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_set_agent
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1set_1agent
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring agent) {
  return (jint)shout_set_agent((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, agent, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_set_user
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1set_1user
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring user) {
  return (jint)shout_set_user((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, user, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_set_password
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1set_1password
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring password) {
  return (jint)shout_set_password((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, password, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_set_mount
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1set_1mount
  (JNIEnv *env, jclass class, jlong shoutInstance, jstring mount) {
  return (jint)shout_set_mount((void *)(intptr_t)shoutInstance, (*env)->GetStringUTFChars(env, mount, 0));
}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    open
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_open
  (JNIEnv *env, jclass class, jlong shoutInstance) {

}

/*
 * Class:     com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout
 * Method:    shout_version
 * Signature: (III)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_github_jpthiery_hermodr_broadcaster_infra_icecast_BindingLibShout_shout_1version
  (JNIEnv *env, jclass class, jint x, jint y, jint z) {
  return (*env)->NewStringUTF(env, shout_version((void *)(intptr_t)x, (void *)(intptr_t)y, (void *)(intptr_t)z));
}


//  const char *shout_version(int *major, int *minor, int *patch);