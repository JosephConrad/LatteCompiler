package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;
import Latte.Exceptions.TypeException;

import java.util.LinkedList;

public class Not extends Expr {
    public final Expr expr_;

    public Not(Expr p1) { expr_ = p1; }

    public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }


    @Override
    public String returnExprType(LinkedList<Env> envs, String currentFunction) throws TypeException {
        String type = expr_.returnExprType(envs, currentFunction);

        if (type == "boolean") return "boolean";
        else
            throw new IllegalArgumentException("Not compatible operands in line");
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Not) {
            Latte.Absyn.Not x = (Latte.Absyn.Not)o;
            return this.expr_.equals(x.expr_);
        }
        return false;
    }

    public int hashCode() {
        return this.expr_.hashCode();
    }


}
