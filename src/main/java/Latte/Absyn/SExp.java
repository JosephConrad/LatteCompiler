package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;
import Latte.Exceptions.TypeException;

public class SExp extends Stmt {
    public final Expr expr_;

    public SExp(Expr p1) { expr_ = p1; }

    public <R,A> R accept(Latte.Absyn.Stmt.Visitor<R,A> v, A arg) throws TypeException { return v.visit(this, arg); }

    @Override
    public boolean functionsReturnAchievibility() {
        return false;
    }

    @Override
    public void checkTypes(Env env, String currentFunction) throws TypeException {
        String type = expr_.returnExprType(env, currentFunction);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.SExp) {
            Latte.Absyn.SExp x = (Latte.Absyn.SExp)o;
            return this.expr_.equals(x.expr_);
        }
        return false;
    }

    public String toString() {
        return expr_.toString();

    }


    public int hashCode() {
        return this.expr_.hashCode();
    }


}
