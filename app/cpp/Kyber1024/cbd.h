#ifndef CBD_H
#define CBD_H

#include <stdint.h>
#include "params.h"
#include "poly.h"

#define cbd_eta1 KYBER1024_NAMESPACE(_cbd_eta1)
void cbd_eta1(poly *r, const uint8_t buf[KYBER1024_ETA1*KYBER1024_N/4]);

#define cbd_eta2 KYBER1024_NAMESPACE(_cbd_eta2)
void cbd_eta2(poly *r, const uint8_t buf[KYBER1024_ETA2*KYBER1024_N/4]);

#endif
