package com.mips;

// some imports that we need.
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// Scanner is a class that scans the source code and returns a list of tokens.
public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    // start and current are the indices of the first and last characters of the
    // substring
    private int start = 0;
    private int current = 0;
    // line is the line number of the current token.
    private int line = 1;
    // keywords is a map of keywords to their token types.
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("add", TokenType.ADD);
        keywords.put("sub", TokenType.SUB);
        keywords.put("and", TokenType.AND);
        keywords.put("sll", TokenType.SLL);
        keywords.put("srl", TokenType.SRL);
        keywords.put("or", TokenType.OR);
        keywords.put("addi", TokenType.ADDI);
        keywords.put("subi", TokenType.SUBI);
        keywords.put("andi", TokenType.ANDI);
        keywords.put("ori", TokenType.ORI);
        keywords.put("beq", TokenType.BEQ);
        keywords.put("bnq", TokenType.BNQ);
        keywords.put("bgt", TokenType.BGT);
        keywords.put("bge", TokenType.BGE);
        keywords.put("blt", TokenType.BLT);
        keywords.put("ble", TokenType.BLE);
        keywords.put("j", TokenType.J);
        keywords.put("print", TokenType.PRINT);
        keywords.put("mul", TokenType.MUL);
        keywords.put("div", TokenType.DIV);
        keywords.put("muli", TokenType.MULI);
        keywords.put("divi", TokenType.DIVI);
        keywords.put("swap", TokenType.SWAP);
    }

    // Scanner is the constructor for the Scanner class.
    Scanner(String source) {
        this.source = source;
    }

    // scanTokens is a method that returns a list of tokens.
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }
        // add the EOF token to the list of tokens.
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    // isAtEnd is a method that returns true if we have reached the end of the
    // source code.
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // scanToken is a method that scans a single token.
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    // addToken is a method that adds a token to the list of tokens.
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // addToken is a method that adds a token to the list of tokens.
    private void addToken(TokenType type, Object literal) {
        // get the substring from start to current.
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    // peek is a method that returns the current character.
    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    // peekNext is a method that returns the next character.
    private char peekNext() {
        // if the current character is the last character, return null.
        if (current + 1 >= source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    // isAlpha is a method that returns true if the character is a letter.
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    // isDigit is a method that returns true if the character is a digit.
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    // number is a method that scans a number.
    private void number() {
        while (isDigit(peek()))
            advance();
        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();
            while (isDigit(peek()))
                advance();
        }
        // we want to convert the string to a double.
        addToken(TokenType.NUMBER,
                Integer.parseInt(source.substring(start, current)));
    }

    // identifier is a method that scans an identifier.
    private void identifier() {
        while (isAlphaNumeric(peek()))
            advance();
        // See if the identifier is a reserved word.
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        // if the identifier is not a reserved word, set the type to IDENTIFIER.
        if (type == null)
            type = TokenType.IDENTIFIER;
        addToken(type);
    }

    // isAlphaNumeric is a method that returns true if the character is a letter or
    // digit.
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isRegister(char c) {
        return (c == 'v' || c == 't' || c == 's' || c == 'a');
    }

    // register is a method that scans a register.
    private void register() {
        start++; // skip the $
        while (isAlphaNumeric(peek()))
            advance();
        String text = source.substring(start, current); // get the substring from start to current.
        if (text.length() != 2 || !isRegister(text.charAt(0)) || !isDigit(text.charAt(1)))
            Main.error(line, "Invalid register.");
        addToken(TokenType.REGISTER);
    }

    // scanToken is a method that scans a single token.
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case ':':
                addToken(TokenType.COLON);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                break;
            case '$':
                register();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Main.error(line, "Unexpected character.");
                }
                break;
        }
    }
}