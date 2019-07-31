global _start

section .data
    msg db 'Hello World!',0x0a,0x0 ; db = 1byte , array
    msglen equ $ - msg ; "$" means current address.

section .text
_start:
    push msglen ; 文字列の長さ
    push msg ; 文字列のアドレス
    push 0x1 ; FD 1なら標準出力
    mov eax, 0x4 ;システムコール番号 (sys_write)
    sub esp, 4 ; esp レジスタから 4 を引く（Mac のシステムコール時のおまじない） 
    int 0x80
    ; pushした分popするかespをプラスする

    mov ebx, 0x0 ;exit番号
    mov eax, 0x1 ;システムコール番号 (sys_exit)
    sub esp, 4 ; esp レジスタから 4 を引く（Mac のシステムコール時のおまじない） 
    int 0x80 

; https://qiita.com/MoriokaReimen/items/b320e6cc82c8873a602f   
; https://codeday.me/jp/qa/20190301/294385.html

; nasm -f macho32 hello_world_intel.asm && ld -e _start -macosx_version_min 10.8 -o hello_world_intel hello_world_intel.o -lSystem && ./hello_world_intel