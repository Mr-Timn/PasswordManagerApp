//
// Created by jorda on 11/4/2022.
//

#include <jni.h>
#include <android/log.h>
#include <string>
#include "argon2/argon2.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_passwordmanager_Signup_createSecureHash(JNIEnv *env, jobject thiz, jstring password) {
	srand(time(NULL));

	argon2_type type = argon2_type::Argon2_d;
	uint32_t version = ARGON2_VERSION_13;
	uint32_t parallelism = 1;
	uint32_t t_cost = 4;
	uint32_t m_cost = (1 << 16);
	uint32_t hash_len = 64;
	uint32_t salt_len = 16;
	uint8_t hash[hash_len];
	uint8_t salt[salt_len];
	for (uint32_t i = 0; i < salt_len; i++) salt[i] = (uint8_t)rand();

	const char* cc_pass = env->GetStringUTFChars(password, 0);
	int cc_pass_len = strlen(cc_pass);

	if (argon2_hash(t_cost, m_cost, parallelism, cc_pass, cc_pass_len, salt, salt_len, hash, hash_len, NULL, 0, type, version) != 0) {
		__android_log_print(ANDROID_LOG_ERROR, "APP_DEBUG", "Failed to hash argon2");
		return env->NewStringUTF("");
	}

	const char HEX[] = "0123456789ABCDEF";
	char hex_hash[(hash_len * 2) + (salt_len * 2) + 1 + 2];
	uint32_t i, ii = 0;
	for (i = 0; i < hash_len; i++) {
		hex_hash[ii * 2 + 0] = HEX[(hash[i] & 0xF0) >> 4];
		hex_hash[ii * 2 + 1] = HEX[(hash[i] & 0x0F) >> 0];
		ii++;
	}
	hex_hash[ii * 2 + 0] = '_';
	hex_hash[ii * 2 + 1] = '_';
	ii++;
	for (i = 0; i < salt_len; i++) {
		hex_hash[ii * 2 + 0] = HEX[(salt[i] & 0xF0) >> 4];
		hex_hash[ii * 2 + 1] = HEX[(salt[i] & 0x0F) >> 0];
		ii++;
	}
	hex_hash[ii * 2] = '\0';
	return env->NewStringUTF(hex_hash);
}