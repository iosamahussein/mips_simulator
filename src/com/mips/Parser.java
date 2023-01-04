package com.mips;

import java.util.ArrayList;
import java.util.List;
import static com.mips.TokenType.*;

class Parser {
    private final List<Token> tokens;
    private int current = 0;

    static class ParseError extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Inst> parse() {
        List<Inst> instructions = new ArrayList<>();
        while (!isAtEnd()) {
            instructions.add(instruction());
        }
        return instructions;
    }
    private Inst instruction() {
        try {
            if (match(ADD, SUB, AND, OR, MUL, DIV)) {
                Token name = previous();
                Token src1 = consume(REGISTER, "Expect first source register.");
                consume(COMMA, "Expect comma.");
                Token src2 = consume(REGISTER, "Expect second source register.");
                consume(COMMA, "Expect comma.");
                Token dest = consume(REGISTER, "Expect destination register.");
                consume(SEMICOLON, "Expect semicolon.");
                return new Inst.Rtype(name, src1, src2, dest);
            }
            else if (match(ADDI, SUBI, ANDI, ORI, SLL, SRL, LW, SW , MULI , DIVI , SWAP)) {
                Token name = previous();
                Token sign = null;
                Token src = consume(REGISTER, "Expect source register.");
                consume(COMMA, "Expect comma.");
                Token dest = consume(REGISTER, "Expect destination register.");
                consume(COMMA, "Expect comma.");
                if (match(MINUS)) {
                    sign = previous();
                }
                Token imm = consume(NUMBER, "Expect immediate value.");
                if (sign != null ) {
                    imm = new Token(imm.type , imm.lexeme, -1 * (Integer) imm.literal, imm.line);
                }
                consume(SEMICOLON, "Expect semicolon.");
                return new Inst.Itype(name, src, dest, imm);
            }

            else if (match(BEQ, BNQ, BGE, BGT, BLT, BLE)) {
                Token name = previous();
                Token src1 = consume(REGISTER, "Expect first source register.");
                consume(COMMA, "Expect comma.");
                Token src2 = consume(REGISTER, "Expect second source register.");
                consume(COMMA, "Expect comma.");

                    Token sign = null;
                    if (match(MINUS)) {
                        sign = previous();
                    }
                    Token address = consume(NUMBER, "Relative address.");
                    if (sign != null ) {
                        address = new Token(address.type , address.lexeme, -1 * (Integer) address.literal, address.line);
                    }
                    consume(SEMICOLON, "Expect semicolon.");
                    return new Inst.Itype(name, src1, src2, address);
                }

            else if (match(J)) {
                Token name = previous();
                Token sign = null;
                if (match(MINUS)) {
                    sign = previous();
                }
                Token address = consume(NUMBER, "Absolute address.");
                if (sign != null ) {
                    address = new Token(address.type , address.lexeme, -1 * (Integer) address.literal, address.line);
                }
                consume(SEMICOLON, "Expect semicolon.");
                return new Inst.Jtype(name, address);
            }
            else {
                match(PRINT);
                Token reg = consume(REGISTER, "Expect register.");
                consume(SEMICOLON, "Expect semicolon.");
                return new Inst.Print(reg);
            }
        } catch (ParseError error) {
            synchronize();
        }
        return null;
    }   
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();
        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        Main.error(token, message);
        return new ParseError();
    }
    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            advance();
        }
    }

}