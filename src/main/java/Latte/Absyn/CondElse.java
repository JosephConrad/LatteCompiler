package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;

import java.util.LinkedList;

public class CondElse extends Stmt {
    public final Expr expr_;
    public final Stmt stmt_1, stmt_2;

    public CondElse(Expr p1, Stmt p2, Stmt p3) { expr_ = p1; stmt_1 = p2; stmt_2 = p3; }

    public <R,A> R accept(Latte.Absyn.Stmt.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public boolean functionsReturnAchievibility() {
        if (expr_.evalExpr()) return stmt_1.functionsReturnAchievibility();
        else
            return stmt_2.functionsReturnAchievibility();
    }

    @Override
    public void checkTypes(LinkedList<Env> envs, String currentFunction) {
        if (expr_.returnType(envs) != "boolean")
            throw new IllegalArgumentException("Expresion in conditional statment must return boolean ");
        stmt_1.checkTypes(envs, currentFunction);
        stmt_2.checkTypes(envs, currentFunction);
    }



    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.CondElse) {
            Latte.Absyn.CondElse x = (Latte.Absyn.CondElse)o;
            return this.expr_.equals(x.expr_) && this.stmt_1.equals(x.stmt_1) && this.stmt_2.equals(x.stmt_2);
        }
        return false;
    }

    public int hashCode() {
        return 37*(37*(this.expr_.hashCode())+this.stmt_1.hashCode())+this.stmt_2.hashCode();
    }


}
