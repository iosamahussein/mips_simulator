package com.mips;

import java.util.List;
import com.mips.Inst.Itype;
import com.mips.Inst.Jtype;
import com.mips.Inst.Print;
import com.mips.Inst.Rtype;

class Interpreter implements Inst.Visitor<Object> {
    private Environment environment = new Environment();
    int pc = 0; // program counter
    int length; // length of instructions

    // interpret method is used to interpret instructions
    void interpret(List<Inst> instructions) {
        try {
            pc = 0;
            length = instructions.size();
            for (; pc < length; pc++) {
                instructions.get(pc).accept(this);
            }
        } catch (RuntimeError error) {
            Main.runtimeError(error);
        }
    }

    // stringify method is used to convert an object to a string
    private String stringify(Object object) {
        if (object == null)
            return "nil";
        return object.toString();
    }

    // get register value from environment if not exist, return 0
    private Integer getRegValue(Token reg) {
        if (environment.get(reg) == null) {
            return 0;
        } else {
            return (Integer) environment.get(reg);
        }
    }

    // assign register value to environment
    private void setRegValue(Token reg, Integer value) {
        environment.assign(reg, value);
    }

    // visit methods are used to visit instructions
    @Override
    public Object visitRtypeInst(Rtype Ints) {
        if (Ints.opcode.type == TokenType.ADD) {
            setRegValue(Ints.rd, getRegValue(Ints.rs) + getRegValue(Ints.rt));
        } else if (Ints.opcode.type == TokenType.SUB) {
            setRegValue(Ints.rd, getRegValue(Ints.rs) - getRegValue(Ints.rt));
        } else if (Ints.opcode.type == TokenType.MUL) {
            setRegValue(Ints.rd, getRegValue(Ints.rs) * getRegValue(Ints.rt));
        } else if (Ints.opcode.type == TokenType.DIV) {
            try {
                setRegValue(Ints.rd, getRegValue(Ints.rs) / getRegValue(Ints.rt));
            } catch (ArithmeticException e) {
                throw new RuntimeError(Ints.opcode, "Division by zero.");
            }
        } else if (Ints.opcode.type == TokenType.AND) {
            setRegValue(Ints.rd, getRegValue(Ints.rs) & getRegValue(Ints.rt));
        } else if (Ints.opcode.type == TokenType.OR) {
            setRegValue(Ints.rd, getRegValue(Ints.rs) | getRegValue(Ints.rt));
        } else if (Ints.opcode.type == TokenType.XOR) {
            setRegValue(Ints.rd, getRegValue(Ints.rs) ^ getRegValue(Ints.rt));
        }
        return getRegValue(Ints.rd);
    }

    @Override
    public Object visitItypeInst(Itype Inst) {
        if (Inst.opcode.type == TokenType.ADDI) {
            setRegValue(Inst.rt, getRegValue(Inst.rs) + (Integer) Inst.imm.literal);
        }

        else if (Inst.opcode.type == TokenType.SUBI) {
            setRegValue(Inst.rt, getRegValue(Inst.rs) - (Integer) Inst.imm.literal);
        } else if (Inst.opcode.type == TokenType.MULI) {
            setRegValue(Inst.rt, getRegValue(Inst.rs) * (Integer) Inst.imm.literal);
        } else if (Inst.opcode.type == TokenType.DIVI) {
            try {
                setRegValue(Inst.rt, getRegValue(Inst.rs) / (Integer) Inst.imm.literal);
            } catch (ArithmeticException e) {
                throw new RuntimeError(Inst.opcode, "Division by zero.");
            }
        } else if (Inst.opcode.type == TokenType.ANDI) {
            setRegValue(Inst.rt, getRegValue(Inst.rs) & (Integer) Inst.imm.literal);
        } else if (Inst.opcode.type == TokenType.ORI) {
            setRegValue(Inst.rt, getRegValue(Inst.rs) | (Integer) Inst.imm.literal);
        } else if (Inst.opcode.type == TokenType.XORI) {
            setRegValue(Inst.rt, getRegValue(Inst.rs) ^ (Integer) Inst.imm.literal);
        } else if (Inst.opcode.type == TokenType.SLL) {
            setRegValue(Inst.rt, (getRegValue(Inst.rs) * (Integer) Inst.imm.literal) * 2);
        } else if (Inst.opcode.type == TokenType.SRL) {
            setRegValue(Inst.rt, (getRegValue(Inst.rs) / ((Integer) Inst.imm.literal) * 2));
        } else if (Inst.opcode.type == TokenType.BEQ) {
            if (getRegValue(Inst.rs) == getRegValue(Inst.rt)) {
                pc += (Integer) Inst.imm.literal;
                if (pc < -1 || pc >= length)
                    throw new RuntimeError(Inst.opcode, "Invalid address.");
            }
        } else if (Inst.opcode.type == TokenType.BNQ) {
            if (getRegValue(Inst.rs) != getRegValue(Inst.rt)) {
                pc += (Integer) Inst.imm.literal;
                if (pc < -1 || pc >= length)
                    throw new RuntimeError(Inst.opcode, "Invalid address.");
            }
        }

        else if (Inst.opcode.type == TokenType.BGT) {
            if (getRegValue(Inst.rs) > getRegValue(Inst.rt)) {
                pc += (Integer) Inst.imm.literal;
                if (pc < -1 || pc >= length)
                    throw new RuntimeError(Inst.opcode, "Invalid address.");
            }
        }

        else if (Inst.opcode.type == TokenType.BGE) {
            if (getRegValue(Inst.rs) >= getRegValue(Inst.rt)) {
                pc += (Integer) Inst.imm.literal;
                if (pc < -1 || pc >= length)
                    throw new RuntimeError(Inst.opcode, "Invalid address.");
            }
        }

        else if (Inst.opcode.type == TokenType.BLT) {
            if (getRegValue(Inst.rs) < getRegValue(Inst.rt)) {
                pc += (Integer) Inst.imm.literal;
                if (pc < -1 || pc >= length)
                    throw new RuntimeError(Inst.opcode, "Invalid address.");
            }
        }

        else if (Inst.opcode.type == TokenType.BLE) {
            if (getRegValue(Inst.rs) <= getRegValue(Inst.rt)) {
                pc += (Integer) Inst.imm.literal;
                if (pc < -1 || pc >= length)
                    throw new RuntimeError(Inst.opcode, "Invalid address.");
            }
        } else if (Inst.opcode.type == TokenType.SWAP) {
            Integer temp = getRegValue(Inst.rt);
            setRegValue(Inst.rt, getRegValue(Inst.rs));
            setRegValue(Inst.rs, temp);
        }

        return null;
    }

    @Override
    public Object visitJtypeInst(Jtype Inst) {
        pc = (Integer) Inst.address.literal;
        pc -= 2; // because pc++ will be executed after this method and we assume instructions
                 // are 1-based
        if (pc < -1 || pc >= length)
            throw new RuntimeError(Inst.opcode, "Invalid jump address.");
        return null;
    }

    @Override
    public Object visitPrintInst(Print Inst) {
        System.out.println(stringify(getRegValue(Inst.rd)));
        return null;
    }
}