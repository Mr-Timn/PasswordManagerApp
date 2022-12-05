#ifndef INDCPA_H
#define INDCPA_H

#include <stdint.h>
#include "params.h"
#include "polyvec.h"

#define gen_matrix KYBER1024_NAMESPACE(_gen_matrix)
void gen_matrix(polyvec *a, const uint8_t seed[KYBER1024_SYMBYTES], int transposed);
#define indcpa_keypair KYBER1024_NAMESPACE(_indcpa_keypair)
void indcpa_keypair(uint8_t pk[KYBER1024_INDCPA_PUBLICKEYBYTES],
                    uint8_t sk[KYBER1024_INDCPA_SECRETKEYBYTES]);

#define indcpa_enc KYBER1024_NAMESPACE(_indcpa_enc)
void indcpa_enc(uint8_t c[KYBER1024_INDCPA_BYTES],
                const uint8_t m[KYBER1024_INDCPA_MSGBYTES],
                const uint8_t pk[KYBER1024_INDCPA_PUBLICKEYBYTES],
                const uint8_t coins[KYBER1024_SYMBYTES]);

#define indcpa_dec KYBER1024_NAMESPACE(_indcpa_dec)
void indcpa_dec(uint8_t m[KYBER1024_INDCPA_MSGBYTES],
                const uint8_t c[KYBER1024_INDCPA_BYTES],
                const uint8_t sk[KYBER1024_INDCPA_SECRETKEYBYTES]);

#endif
