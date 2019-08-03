global _main

section .text
_main:
    mov ebx, 0x0 ;exit番号
    mov eax, 0x1 ;システムコール番号 (sys_exit)
    sub esp, 4 ; esp レジスタから 4 を引く（Mac のシステムコール時のおまじない） 
    int 0x80
; nasm -f macho32 exit_intel.asm
; ld -e _main -macosx_version_min 10.8 -o exit_intel exit_intel.o -lSystem
; ./exit_intel