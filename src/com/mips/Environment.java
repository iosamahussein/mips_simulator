package com.mips;

import java.util.HashMap;
import java.util.Map;

// Environment class is a subclass of Stmt to represent variable declarations like var a = 1;
class Environment {
    // Map is a class that maps keys to values
    private final Map<String, Object> values = new HashMap<>();

    // get method is a method to get the value of a variable
    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        return 0 ;
    }
    // assign method is a method to assign a value to a variable
    void assign(Token name, Object value) {
        // if the variable is found in the current environment, assign the value to the
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }
        define(name.lexeme, value);
    }
    // define method is a method to define a variable
    void define(String name, Object value) {
        values.put(name, value);
    }
}