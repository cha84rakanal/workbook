/* cc -o nop_inline nop_inline.c */

int main(void){
    asm volatile(
        ".intel_syntax noprefix\n"
        "mov eax,0"
    );
    return 0;
}