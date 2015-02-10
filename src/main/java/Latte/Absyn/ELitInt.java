package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;

import java.util.LinkedList;

public class ELitInt extends Expr {
    public final Integer integer_;

    public ELitInt(Integer p1) { integer_ = p1; }

    public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public String returnExprType(LinkedList<Env> env) {
        return "int";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.ELitInt) {
            Latte.Absyn.ELitInt x = (Latte.Absyn.ELitInt)o;
            return this.integer_.equals(x.integer_);
        }
        return false;
    }

    public String toString() {
        return integer_.toString();
    }

    public int hashCode() {
        return this.integer_.hashCode();
    }

}
