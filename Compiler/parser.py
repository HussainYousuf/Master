import sys
import re

asm_plus = '''
    POP BX
    POP AX
    ADD AX,BX
    PUSH AX
'''

asm_minus = '''
    POP BX
    POP AX
    SUB AX,BX
    PUSH AX
'''

asm_mul = '''
    POP BX
    POP AX
    MUL BX
    PUSH AX
'''

asm_div = '''
    POP BX
    POP AX
    MOV DX,0
    DIV BX
    PUSH AX
'''

asm_mod = '''
    POP BX
    POP AX
    MOV DX,0
    DIV BX
    MOV AX,DX
    PUSH DX
'''


def asm_num(num):
    return f'''
    MOV AX,{num}
    PUSH AX
'''

assembly = ""

def tokenize(input):
    tokens = []
    regex = r"(?P<Num>\d+)|(?P<Add>\+)|(?P<Sub>-)|(?P<Mul>\*)|(?P<Div>\/)|(?P<Mod>%)|(?P<Cparen>\))|(?P<Oparen>\()|(?P<Space>\s+)"

    def func(match):
        for (i, j) in match.groupdict().items():
            if j and i != "Space":
                tokens.append((i, j))
                return ""

    input = re.sub(regex, func, input)
    if input:
        print(input)
        print("unidentified symbol in input")
        quit()
    else:
        for i in range(len(tokens)):
            print(f"token[{i}] = {tokens[i]}")
        parse(tokens)


def parse(tokens):

    def expr():
        term()
        expr_()

    def expr_():
        global assembly
        if len(tokens) > 0:
            if tokens[0][1] == "+":
                tokens.pop(0)
                term()
                expr_()
                assembly += asm_plus

            elif tokens[0][1] == "-":
                tokens.pop(0)
                term()
                expr_()
                assembly += (asm_minus)

    def term():
        factor()
        term_()

    def term_():
        global assembly
        if len(tokens) > 0:
            if tokens[0][1] == "*":
                tokens.pop(0)
                factor()
                term_()
                assembly += (asm_mul)

            elif tokens[0][1] == "/":
                tokens.pop(0)
                factor()
                term_()
                assembly += (asm_div)

            elif tokens[0][1] == "%":
                tokens.pop(0)
                factor()
                term_()
                assembly += (asm_mod)

    def factor():
        global assembly
        if tokens[0][1] == "(":
            tokens.pop(0)
            expr()
            if tokens[0][1] == ")":
                tokens.pop(0)
            else:
                raise Exception("")
                
        elif tokens[0][0] == "Num":
            assembly += (asm_num(tokens[0][1]))
            tokens.pop(0)
        else:
            raise Exception("")
        

    try:
        expr()
        if len(tokens) > 0: raise Exception("") 
        f = open("k163805.asm","w")
        output = '''
.386
.model flat, stdcall
option casemap :none
include \masm32\include\masm32.inc
include \masm32\include\kernel32.inc
include \masm32\macros\macros.asm
includelib \masm32\lib\masm32.lib
includelib \masm32\lib\kernel32.lib
.code
start:
    mov eax,0
'''
        output += assembly
        output += '''
print str$(eax),13,10;
exit
end start
'''
        print(output)
        f.write(output)
        f.close()
    except :
        print("syntax error")
        quit()

if __name__ == "__main__":
    tokenize("".join(sys.argv[1:]))
