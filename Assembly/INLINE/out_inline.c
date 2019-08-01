#include <stdio.h>

int main(void){
    int a,b,c;
    asm volatile(
        ".intel_syntax noprefix\n"
        "mov dword ptr [rbp - 8], 3;"
        "mov dword ptr [rbp - 12], 2;"
        "mov dword ptr [rbp - 16], 1;"
	);
    printf("a: %d,b: %d,c %d\n", a,b,c);

    int d;
    asm volatile(
        ".intel_syntax noprefix\n"
        "mov dword ptr [rbp - 20], 100;"
    );
    printf("d: %d\n", d);
    return 0;
}