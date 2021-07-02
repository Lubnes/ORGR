j   0x0400020
jr   $31
add   $8, $9, $10
lw   $8, 0($9)
beq   $8, $9, 0xfffffffb
bne   $9, $18, 0xfffffffa
ori   $9, $9, 2
and   $8, $9, $10
srl   $9, $10, 1
slti   $8, $9, 10
