#ifndef API_H
#define API_H

#include "params.h"

#define CRYPTO_SECRETKEYBYTES  KYBER1024_SECRETKEYBYTES
#define CRYPTO_PUBLICKEYBYTES  KYBER1024_PUBLICKEYBYTES
#define CRYPTO_CIPHERTEXTBYTES KYBER1024_CIPHERTEXTBYTES
#define CRYPTO_BYTES           KYBER1024_SSBYTES

#if   (KYBER1024_K == 2)
#ifdef KYBER1024_90S
#define CRYPTO_ALGNAME "Kyber512-90s"
#else
#define CRYPTO_ALGNAME "Kyber512"
#endif
#elif (KYBER1024_K == 3)
#ifdef KYBER1024_90S
#define CRYPTO_ALGNAME "Kyber768-90s"
#else
#define CRYPTO_ALGNAME "Kyber768"
#endif
#elif (KYBER1024_K == 4)
#ifdef KYBER1024_90S
#define CRYPTO_ALGNAME "Kyber1024-90s"
#else
#define CRYPTO_ALGNAME "Kyber1024"
#endif
#endif

#define pqcrystals_kyber1024_ref_keypair KYBER1024_NAMESPACE(_keypair)
int pqcrystals_kyber1024_ref_keypair(unsigned char *pk, unsigned char *sk);

#define pqcrystals_kyber1024_ref_enc KYBER1024_NAMESPACE(_enc)
int pqcrystals_kyber1024_ref_enc(unsigned char *ct,
                   unsigned char *ss,
                   const unsigned char *pk);

#define pqcrystals_kyber1024_ref_dec KYBER1024_NAMESPACE(_dec)
int pqcrystals_kyber1024_ref_dec(unsigned char *ss,
                   const unsigned char *ct,
                   const unsigned char *sk);

#endif
