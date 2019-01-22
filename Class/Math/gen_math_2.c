#include <stdio.h>
#include <math.h>

int main(){

    double a1 = 0.1;
    double an = 0.1;
    int iter = 1;

    printf("a%d \t=\t %lf\n",iter,an);

    while(iter < 56){
        an = 1 - fabs(1 - 2*an);
        iter ++;
        printf("a%d \t=\t %lf\n",iter,an);
    }

    return 0;
}