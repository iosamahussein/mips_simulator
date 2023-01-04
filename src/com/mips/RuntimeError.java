package com.mips;
// Runtime error class is a subclass of RuntimeException
class RuntimeError extends RuntimeException {
    final Token token;
    RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}