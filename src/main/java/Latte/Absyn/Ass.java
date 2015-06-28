package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;
import Latte.Exceptions.TypeException;

public class Ass extends Stmt {
    public final String ident_;
    public final Expr expr_;

    public Ass(String p1, Expr p2) { ident_ = p1; expr_ = p2; }

    public <R,A> R accept(Latte.Absyn.Stmt.Visitor<R,A> v, A arg) throws Exception { return v.visit(this, arg); }

    @Override
    public boolean functionsReturnAchievibility() {
        return false;
    }

    @Override
    public void checkTypes(Env env, String currentFunction) throws TypeException {
        String identType = env.variableType.get(ident_);
        if (identType != expr_.returnExprType(env, currentFunction))
            throw new TypeException(currentFunction, "\n\t\tAt assignment to " +
                    ident_ + " variable: " + "incompatible type of variable (" + identType + ")" +
                    " with operand type (" + expr_.returnExprType(env, currentFunction) +")");
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Ass) {
            Latte.Absyn.Ass x = (Latte.Absyn.Ass)o;
            return this.ident_.equals(x.ident_) && this.expr_.equals(x.expr_);
        }
        return false;
    }

    public int hashCode() {
        return 37*(this.ident_.hashCode())+this.expr_.hashCode();
    }


}
