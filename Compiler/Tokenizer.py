import sys
import re
from typing import NamedTuple

class Token(NamedTuple):
    type: str
    value: str
    line: int
    column: int

def tokenize(code):
    keywords = {'program', 'var', 'real', 'integer', 'begin', 'end', 'if', 'then', 'else', 'while', 'do', 'repeat',
                'until', 'readln', 'write', 'writeln', 'or', 'div', 'mod', 'and', 'true', 'false', 'not', 'trunc'}
    token_specification = [
        ('SYM',      r'(<=|>=|<>|:=|[;:.()+*/=<>\-])'),
        ('REAL',     r'\d+?\.\d+'),
        ('INT',      r'\d+'),
        ('STRING',   r'\".*?\"'),
        ('ID',       r'\b[A-Za-z]+\b'),
        ('NEWLINE',  r'\n'),
        ('SPACE',    r'[ \t]+'),
        ('MISMATCH', r'.'),
    ]
    tok_regex = '|'.join('(?P<%s>%s)' % pair for pair in token_specification)
    line_num = 1
    line_start = 0
    for mo in re.finditer(tok_regex, code):
        kind = mo.lastgroup
        value = mo.group()
        column = mo.start() - line_start
        if kind == 'ID' and value in keywords:
            kind = "KEYWORD"
        elif kind == 'INT':
            value = int(value)
        elif kind == 'REAL':
            value = float(value)
        elif kind == 'SPACE':
            continue
        elif kind == 'NEWLINE':
            line_start = mo.end()
            line_num += 1
            continue
        elif kind == 'MISMATCH':
            raise RuntimeError(f'{value!r} unexpected on line {line_num}')
        yield Token(kind, value, line_num, column)

if __name__ == "__main__":
    f = open("".join(sys.argv[1:]), "r")
    input = f.read()
    for token in tokenize(input):
        print(token)
    f.close()
