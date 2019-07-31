# アセンブリ言語の練習

## コンパイラ
- gcc  
GNUのコンパイラ
- clang
Appleの作ったコンパイラ、バックはLLVM  

Macだとgccもclangにバインドしている

## アセンブラ
- GAS(GNU assembler)  
基本的に、AT&T構文  
`.intel_syntax` ディレクティブをつけるとIntel構文が使える 
- NASM(Netwide Assembler)  
Intel構文

https://www.ibm.com/developerworks/jp/linux/library/l-gas-nasm.html

## 記法
- Intel記法  
左が「Dist」、右が「Src」 
- AT&T  
右が「Dist」、左が「Src」 

# アーキテクチャ
- x86
- arm