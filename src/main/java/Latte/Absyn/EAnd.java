package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;

import java.util.LinkedList;

public class EAnd extends Expr {
    public final Expr expr_1, expr_2;

    public EAnd(Expr p1, Expr p2) { expr_1 = p1; expr_2 = p2; }

    public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }


    @Override
    public String returnExprType(LinkedList<Env> envs) {
        String expr1Type = expr_1.returnExprType(envs);
        String expr2Type = expr_2.returnExprType(envs);
        if ((expr1Type == expr2Type) && (expr1Type == "boolean"))
            return "boolean";
        else
            throw new IllegalArgumentException("Add: invalid operands: " + expr1Type + ", and " + expr1Type);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.EAnd) {
            Latte.Absyn.EAnd x = (Latte.Absyn.EAnd)o;
            return this.expr_1.equals(x.expr_1) && this.expr_2.equals(x.expr_2);
        }
        return false;
    }

    public int hashCode() {
        return 37*(this.expr_1.hashCode())+this.expr_2.hashCode();
    }

}
