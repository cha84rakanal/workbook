#include <stdio.h>

int main(void){
    unsigned eax_val = 0;
    asm volatile(
        ".intel_syntax noprefix\n"
        "lea eax, [rbp - 8]\n"
		"cpuid\n"
		: "=a"(eax_val)
		);
    printf("eax: %u\n", eax_val);
    return 0;
}