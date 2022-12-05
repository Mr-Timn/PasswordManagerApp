#ifndef KEM_H1024
#define KEM_H1024

#include "params.h"

int pqcrystals_kyber1024_ref_keypair(unsigned char *pk, unsigned char *sk);
int pqcrystals_kyber1024_ref_enc(unsigned char *ct, unsigned char *ss, const unsigned char *pk);
int pqcrystals_kyber1024_ref_dec(unsigned char *ss, const unsigned char *ct, const unsigned char *sk);

#endif
