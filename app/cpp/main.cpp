//
// Created by jorda on 11/4/2022.
//

#include <jni.h>
#include <android/log.h>
#include <string>
#include "argon2/argon2.h"
#include "aes.h"

uint8_t hextoval(char h) {
	switch (h) {
		case '0': return 0x0;
		case '1': return 0x1;
		case '2': return 0x2;
		case '3': return 0x3;
		case '4': return 0x4;
		case '5': return 0x5;
		case '6': return 0x6;
		case '7': return 0x7;
		case '8': return 0x8;
		case '9': return 0x9;
		case 'A': return 0xA;
		case 'B': return 0xB;
		case 'C': return 0xC;
		case 'D': return 0xD;
		case 'E': return 0xE;
		case 'F': return 0xF;
		default:  return 0;
	}
}

const uint32_t DEFAULT_T_COST = 4;
const uint32_t DEFAULT_M_COST = (1 << 16);
const uint32_t DEFAULT_HASH_LENGTH = 64;
const uint32_t DEFAULT_SALT_LENGTH = 16;
const uint32_t HEXHASH_LENGTH = (DEFAULT_HASH_LENGTH * 2) + (DEFAULT_SALT_LENGTH * 2) + 1 + 2;
const char HEXVAL[] = "0123456789ABCDEF";

void createHash(char* hex_hash, const char* cc_pass, const char* cc_salt, uint8_t* hash) {
	srand(time(NULL));

	int cc_pass_len = strlen(cc_pass);

	argon2_type type = argon2_type::Argon2_d;
	uint32_t version = ARGON2_VERSION_13;
	uint32_t parallelism = 1;
	uint32_t t_cost = DEFAULT_T_COST;
	uint32_t m_cost = DEFAULT_M_COST;
	uint32_t hash_len = DEFAULT_HASH_LENGTH;
	uint32_t salt_len = DEFAULT_SALT_LENGTH;

	uint8_t salt[salt_len];
	char ssalt[salt_len * 2];

	// Empty salt
	if (cc_salt == NULL) {
		for (uint32_t i = 0; i < salt_len * 2; i++) ssalt[i] = '0';
	} else {
		// Load salt
		if (strlen(cc_salt)) {
			for (uint32_t i = 0; i < salt_len * 2; i++) ssalt[i] = cc_salt[i];
		// Random salt
		} else {
			for (uint32_t i = 0; i < salt_len * 2; i++) ssalt[i] = HEXVAL[rand() % 16];
		}
	}

	for (uint32_t i = 0; i < salt_len; i++) salt[i] = (hextoval(ssalt[i * 2 + 0]) << 4) | (hextoval(ssalt[i * 2 + 1]) << 0);

	if (argon2_hash(t_cost, m_cost, parallelism, cc_pass, cc_pass_len, salt, salt_len, hash, hash_len, NULL, 0, type, version) != 0) {
		__android_log_print(ANDROID_LOG_ERROR, "APP_DEBUG", "Failed to hash argon2");
		hex_hash[0] = '\0';
		free(ssalt);
		return;
	}

	uint32_t i, ii = 0;
	for (i = 0; i < DEFAULT_HASH_LENGTH; i++) {
		hex_hash[ii++] = HEXVAL[(hash[i] & 0xF0) >> 4];
		hex_hash[ii++] = HEXVAL[(hash[i] & 0x0F) >> 0];
	}
	hex_hash[ii++] = '_';
	hex_hash[ii++] = '_';
	for (i = 0; i < salt_len * 2; i++) hex_hash[ii++] = ssalt[i];
	hex_hash[ii] = '\0';
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_passwordmanager_StoredData_encryptData(JNIEnv *env, jobject thiz, jstring data, jstring key, jstring encr) {
	const char* cc_key = env->GetStringUTFChars(key, 0);
	const char* cc_data = env->GetStringUTFChars(data, 0);
	int cc_data_len = strlen(cc_data);

	uint8_t hash[DEFAULT_HASH_LENGTH];
	char hex_hash[HEXHASH_LENGTH];
	createHash(hex_hash, cc_key, NULL, hash);

	AESKeySet aeskey;
	aeskey.key = (uint8_t*)malloc(DEFAULT_HASH_LENGTH);
	memcpy(aeskey.key, hash, DEFAULT_HASH_LENGTH);
	initAESKey(Encryption::AES256, &aeskey);

	std::string ddata = std::string(cc_data, cc_data_len);
	encryptAES(ddata, &aeskey);
	free(aeskey.key);

	size_t dlen = ddata.length();
	char hexdata[dlen * 2 + 1]; memset(hexdata, 0, dlen * 2 + 1);
	uint32_t i, ii = 0;
	for (i = 0; i < dlen; i++) {
		hexdata[ii * 2 + 0] = HEXVAL[(ddata[i] & 0xF0) >> 4];
		hexdata[ii * 2 + 1] = HEXVAL[(ddata[i] & 0x0F) >> 0];
		ii++;
	}

	return env->NewStringUTF(hexdata);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_passwordmanager_StoredData_decryptData(JNIEnv *env, jobject thiz, jstring data, jstring key, jstring encr) {
	const char* cc_key = env->GetStringUTFChars(key, 0);
	const char* cc_data = env->GetStringUTFChars(data, 0);
	int cc_data_len = strlen(cc_data);

	uint8_t hash[DEFAULT_HASH_LENGTH];
	char hex_hash[HEXHASH_LENGTH];
	createHash(hex_hash, cc_key, NULL, hash);

	AESKeySet aeskey;
	aeskey.key = (uint8_t*)malloc(DEFAULT_HASH_LENGTH);
	memcpy(aeskey.key, hash, DEFAULT_HASH_LENGTH);
	initAESKey(Encryption::AES256, &aeskey);

	int byte_len = cc_data_len / 2;
	char bytedata[byte_len + 1];
	for (size_t i = 0; i < byte_len; i++) {
		bytedata[i] = (hextoval(cc_data[i * 2 + 0]) << 4) | (hextoval(cc_data[i * 2 + 1]) << 0);
	}

	size_t v = decryptAES(bytedata, byte_len, &aeskey);
	free(aeskey.key);

	if (v) {
		bytedata[byte_len + 1] = '\0';
		return env->NewStringUTF(bytedata);
	}

	return env->NewStringUTF("");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_passwordmanager_StoredData_createSecureHash(JNIEnv *env, jobject thiz, jstring password, jstring salt) {
	const char* cc_pass = env->GetStringUTFChars(password, 0);
	const char* cc_salt = env->GetStringUTFChars(salt, 0);
	uint8_t hash[DEFAULT_HASH_LENGTH];
	char hex_hash[HEXHASH_LENGTH];
	createHash(hex_hash, cc_pass, cc_salt, hash);
	return env->NewStringUTF(hex_hash);
}