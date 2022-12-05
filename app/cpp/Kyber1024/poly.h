#ifndef POLY_H
#define POLY_H

#include <stdint.h>
#include "params.h"

/*
 * Elements of R_q = Z_q[X]/(X^n + 1). Represents polynomial
 * coeffs[0] + X*coeffs[1] + X^2*xoeffs[2] + ... + X^{n-1}*coeffs[n-1]
 */
typedef struct{
  int16_t coeffs[KYBER1024_N];
} poly;

#define poly_compress KYBER1024_NAMESPACE(_poly_compress)
void poly_compress(uint8_t r[KYBER1024_POLYCOMPRESSEDBYTES], poly *a);
#define poly_decompress KYBER1024_NAMESPACE(_poly_decompress)
void poly_decompress(poly *r, const uint8_t a[KYBER1024_POLYCOMPRESSEDBYTES]);

#define poly_tobytes KYBER1024_NAMESPACE(_poly_tobytes)
void poly_tobytes(uint8_t r[KYBER1024_POLYBYTES], poly *a);
#define poly_frombytes KYBER1024_NAMESPACE(_poly_frombytes)
void poly_frombytes(poly *r, const uint8_t a[KYBER1024_POLYBYTES]);

#define poly_frommsg KYBER1024_NAMESPACE(_poly_frommsg)
void poly_frommsg(poly *r, const uint8_t msg[KYBER1024_INDCPA_MSGBYTES]);
#define poly_tomsg KYBER1024_NAMESPACE(_poly_tomsg)
void poly_tomsg(uint8_t msg[KYBER1024_INDCPA_MSGBYTES], poly *r);

#define poly_getnoise_eta1 KYBER1024_NAMESPACE(_poly_getnoise_eta1)
void poly_getnoise_eta1(poly *r, const uint8_t seed[KYBER1024_SYMBYTES], uint8_t nonce);

#define poly_getnoise_eta2 KYBER1024_NAMESPACE(_poly_getnoise_eta2)
void poly_getnoise_eta2(poly *r, const uint8_t seed[KYBER1024_SYMBYTES], uint8_t nonce);

#define poly_ntt KYBER1024_NAMESPACE(_poly_ntt)
void poly_ntt(poly *r);
#define poly_invntt_tomont KYBER1024_NAMESPACE(_poly_invntt_tomont)
void poly_invntt_tomont(poly *r);
#define poly_basemul_montgomery KYBER1024_NAMESPACE(_poly_basemul_montgomery)
void poly_basemul_montgomery(poly *r, const poly *a, const poly *b);
#define poly_tomont KYBER1024_NAMESPACE(_poly_tomont)
void poly_tomont(poly *r);

#define poly_reduce KYBER1024_NAMESPACE(_poly_reduce)
void poly_reduce(poly *r);
#define poly_csubq KYBER1024_NAMESPACE(_poly_csubq)
void poly_csubq(poly *r);

#define poly_add KYBER1024_NAMESPACE(_poly_add)
void poly_add(poly *r, const poly *a, const poly *b);
#define poly_sub KYBER1024_NAMESPACE(_poly_sub)
void poly_sub(poly *r, const poly *a, const poly *b);

#endif
