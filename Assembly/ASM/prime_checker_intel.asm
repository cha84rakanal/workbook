global _main

section .text
_main:
    mov rdi, 0 ; exit(0)
    mov rax, 0x2000001 ;sys_exit
    syscall;
; nasm -f macho64 prime_checker_intel.asm
; ld -e _main -macosx_version_min 10.8 -o prime_checker_intel prime_checker_intel.o -lSystem
; ./prime_checker_intel

; http://thexploit.com/secdev/mac-os-x-64-bit-assembly-system-calls/