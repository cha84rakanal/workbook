#include<stdio.h>

int main(void){
    char c[] = "Hello World\n";
    // 8バイトの境界?
    int a = 2018; // rbp - 32
    double b = 0.0; // rbp - 40
    
    asm volatile(
        ".intel_syntax noprefix\n"
        "mov rdx, 13;"
        "mov rsi, rbp;"
        "sub rsi, 21;" // (rbp - 8 - 文字数)
        "mov rdi, 0x1;" // 1st argument
        "mov rax, 0x2000004;"
        "syscall;"
	);

    asm volatile(
        ".intel_syntax noprefix\n"
        "inc dword ptr [rbp - 32];"
    );

    // http://dustin.schultz.io/mac-os-x-64-bit-assembly-system-calls.html

    printf("a:%d\n",a);

    return 0;
}