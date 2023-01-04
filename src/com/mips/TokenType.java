package com.mips;

enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN,
    COMMA, SEMICOLON, MINUS, COLON,

    // Literals.
    IDENTIFIER, NUMBER, REGISTER,

    // Keywords.
    ADD, ADDI, SUB, SUBI, AND, ANDI, SLL, SRL, LW, SW, OR, ORI, BEQ, BNQ, BGT, BGE, BLT, BLE, J, PRINT,
    MUL , DIV, MOD, NOT, XOR, SWAP, MULI , DIVI, XORI, 
    EOF
}
