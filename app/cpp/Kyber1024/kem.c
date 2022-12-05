#include <stddef.h>
#include <stdint.h>
#include "kem.h"
#include "params.h"
#include "../../rng.h"
#include "symmetric.h"
#include "verify.h"
#include "indcpa.h"

/*************************************************
* Name:        pqcrystals_kyber1024_ref_keypair
*
* Description: Generates public and private key
*              for CCA-secure Kyber key encapsulation mechanism
*
* Arguments:   - unsigned char *pk: pointer to output public key
*                (an already allocated array of CRYPTO_PUBLICKEYBYTES bytes)
*              - unsigned char *sk: pointer to output private key
*                (an already allocated array of CRYPTO_SECRETKEYBYTES bytes)
*
* Returns 0 (success)
**************************************************/
int pqcrystals_kyber1024_ref_keypair(unsigned char *pk, unsigned char *sk)
{
  size_t i;
  indcpa_keypair(pk, sk);
  for(i=0;i<KYBER1024_INDCPA_PUBLICKEYBYTES;i++)
    sk[i+KYBER1024_INDCPA_SECRETKEYBYTES] = pk[i];
  hash_h(sk+KYBER1024_SECRETKEYBYTES-2*KYBER1024_SYMBYTES, pk, KYBER1024_PUBLICKEYBYTES);
  /* Value z for pseudo-random output on reject */
  kyber_randombytes(sk+KYBER1024_SECRETKEYBYTES-KYBER1024_SYMBYTES, KYBER1024_SYMBYTES);
  return 0;
}

/*************************************************
* Name:        pqcrystals_kyber1024_ref_enc
*
* Description: Generates cipher text and shared
*              secret for given public key
*
* Arguments:   - unsigned char *ct: pointer to output cipher text
*                (an already allocated array of CRYPTO_CIPHERTEXTBYTES bytes)
*              - unsigned char *ss: pointer to output shared secret
*                (an already allocated array of CRYPTO_BYTES bytes)
*              - const unsigned char *pk: pointer to input public key
*                (an already allocated array of CRYPTO_PUBLICKEYBYTES bytes)
*
* Returns 0 (success)
**************************************************/
int pqcrystals_kyber1024_ref_enc(unsigned char *ct,
                   unsigned char *ss,
                   const unsigned char *pk)
{
  uint8_t buf[2*KYBER1024_SYMBYTES];
  /* Will contain key, coins */
  uint8_t kr[2*KYBER1024_SYMBYTES];

  kyber_randombytes(buf, KYBER1024_SYMBYTES);
  /* Don't release system RNG output */
  hash_h(buf, buf, KYBER1024_SYMBYTES);

  /* Multitarget countermeasure for coins + contributory KEM */
  hash_h(buf+KYBER1024_SYMBYTES, pk, KYBER1024_PUBLICKEYBYTES);
  hash_g(kr, buf, 2*KYBER1024_SYMBYTES);

  /* coins are in kr+KYBER1024_SYMBYTES */
  indcpa_enc(ct, buf, pk, kr+KYBER1024_SYMBYTES);

  /* overwrite coins in kr with H(c) */
  hash_h(kr+KYBER1024_SYMBYTES, ct, KYBER1024_CIPHERTEXTBYTES);
  /* hash concatenation of pre-k and H(c) to k */
  kdf(ss, kr, 2*KYBER1024_SYMBYTES);
  return 0;
}

/*************************************************
* Name:        pqcrystals_kyber1024_ref_dec
*
* Description: Generates shared secret for given
*              cipher text and private key
*
* Arguments:   - unsigned char *ss: pointer to output shared secret
*                (an already allocated array of CRYPTO_BYTES bytes)
*              - const unsigned char *ct: pointer to input cipher text
*                (an already allocated array of CRYPTO_CIPHERTEXTBYTES bytes)
*              - const unsigned char *sk: pointer to input private key
*                (an already allocated array of CRYPTO_SECRETKEYBYTES bytes)
*
* Returns 0.
*
* On failure, ss will contain a pseudo-random value.
**************************************************/
int pqcrystals_kyber1024_ref_dec(unsigned char *ss,
                   const unsigned char *ct,
                   const unsigned char *sk)
{
  size_t i;
  int fail;
  uint8_t buf[2*KYBER1024_SYMBYTES];
  /* Will contain key, coins */
  uint8_t kr[2*KYBER1024_SYMBYTES];
  uint8_t cmp[KYBER1024_CIPHERTEXTBYTES];
  const uint8_t *pk = sk+KYBER1024_INDCPA_SECRETKEYBYTES;

  indcpa_dec(buf, ct, sk);

  /* Multitarget countermeasure for coins + contributory KEM */
  for(i=0;i<KYBER1024_SYMBYTES;i++)
    buf[KYBER1024_SYMBYTES+i] = sk[KYBER1024_SECRETKEYBYTES-2*KYBER1024_SYMBYTES+i];
  hash_g(kr, buf, 2*KYBER1024_SYMBYTES);

  /* coins are in kr+KYBER1024_SYMBYTES */
  indcpa_enc(cmp, buf, pk, kr+KYBER1024_SYMBYTES);

  fail = verify(ct, cmp, KYBER1024_CIPHERTEXTBYTES);

  /* overwrite coins in kr with H(c) */
  hash_h(kr+KYBER1024_SYMBYTES, ct, KYBER1024_CIPHERTEXTBYTES);

  /* Overwrite pre-k with z on re-encryption failure */
  cmov(kr, sk+KYBER1024_SECRETKEYBYTES-KYBER1024_SYMBYTES, KYBER1024_SYMBYTES, fail);

  /* hash concatenation of pre-k and H(c) to k */
  kdf(ss, kr, 2*KYBER1024_SYMBYTES);
  return 0;
}
