#ifndef POLYVEC_H
#define POLYVEC_H

#include <stdint.h>
#include "params.h"
#include "poly.h"

typedef struct{
  poly vec[KYBER1024_K];
} polyvec;

#define polyvec_compress KYBER1024_NAMESPACE(_polyvec_compress)
void polyvec_compress(uint8_t r[KYBER1024_POLYVECCOMPRESSEDBYTES], polyvec *a);
#define polyvec_decompress KYBER1024_NAMESPACE(_polyvec_decompress)
void polyvec_decompress(polyvec *r,
                        const uint8_t a[KYBER1024_POLYVECCOMPRESSEDBYTES]);

#define polyvec_tobytes KYBER1024_NAMESPACE(_polyvec_tobytes)
void polyvec_tobytes(uint8_t r[KYBER1024_POLYVECBYTES], polyvec *a);
#define polyvec_frombytes KYBER1024_NAMESPACE(_polyvec_frombytes)
void polyvec_frombytes(polyvec *r, const uint8_t a[KYBER1024_POLYVECBYTES]);

#define polyvec_ntt KYBER1024_NAMESPACE(_polyvec_ntt)
void polyvec_ntt(polyvec *r);
#define polyvec_invntt_tomont KYBER1024_NAMESPACE(_polyvec_invntt_tomont)
void polyvec_invntt_tomont(polyvec *r);

#define polyvec_pointwise_acc_montgomery \
        KYBER1024_NAMESPACE(_polyvec_pointwise_acc_montgomery)
void polyvec_pointwise_acc_montgomery(poly *r,
                                      const polyvec *a,
                                      const polyvec *b);

#define polyvec_reduce KYBER1024_NAMESPACE(_polyvec_reduce)
void polyvec_reduce(polyvec *r);
#define polyvec_csubq KYBER1024_NAMESPACE(_polyvec_csubq)
void polyvec_csubq(polyvec *r);

#define polyvec_add KYBER1024_NAMESPACE(_polyvec_add)
void polyvec_add(polyvec *r, const polyvec *a, const polyvec *b);

#endif
