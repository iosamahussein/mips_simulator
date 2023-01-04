package com.mips;

abstract class Inst {
    
    interface Visitor<R> {
        R visitRtypeInst(Rtype Ints);

        R visitItypeInst(Itype Inst);

        R visitJtypeInst(Jtype Inst);

        R visitPrintInst(Print Inst);

    }

    static class Rtype extends Inst {
        Rtype(Token opcode, Token rd, Token rs, Token rt) {
            this.opcode = opcode;
            this.rs = rs;
            this.rt = rt;
            this.rd = rd;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitRtypeInst(this);
        }

        final Token opcode;
        final Token rs;
        final Token rt;
        final Token rd;
    }

    static class Itype extends Inst {
        Itype(Token opcode, Token rt, Token rs, Token imm) {
            this.opcode = opcode;
            this.rs = rs;
            this.rt = rt;
            this.imm = imm;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitItypeInst(this);
        }

        final Token opcode;
        final Token rs;
        final Token rt;
        final Token imm;
    }

    static class Jtype extends Inst {
        Jtype(Token opcode, Token address) {
            this.opcode = opcode;
            this.address = address;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitJtypeInst(this);
        }

        final Token opcode;
        final Token address;
    }

    static class Print extends Inst {
        Print(Token rd) {
            this.rd = rd;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintInst(this);
        }

        final Token rd;
    }

    abstract <R> R accept(Visitor<R> visitor);
}
