package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;
import Latte.Exceptions.TypeException;

import java.util.LinkedList;

public class Neg extends Expr {
    public final Expr expr_;

    public Neg(Expr p1) { expr_ = p1; }

    public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public boolean evalExpr() {
        return false;
    }

    @Override
    public String returnExprType(LinkedList<Env> envs, String currentFunction) throws TypeException {
        String type = expr_.returnExprType(envs, currentFunction);
        if (type == "int") return "int";
        else
            throw new TypeException(currentFunction, "not appropriate argument for negation");
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Neg) {
            Latte.Absyn.Neg x = (Latte.Absyn.Neg)o;
            return this.expr_.equals(x.expr_);
        }
        return false;
    }

    public String toString() {
        return "-" + expr_;
    }

    public int hashCode() {
        return this.expr_.hashCode();
    }

}
