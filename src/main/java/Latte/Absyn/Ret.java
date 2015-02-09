package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;

import java.util.LinkedList;

public class Ret extends Stmt {
    public final Expr expr_;

    public Ret(Expr p1) { expr_ = p1; }

    public <R,A> R accept(Latte.Absyn.Stmt.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public boolean functionsReturnAchievibility() {
        return true;
    }

    @Override
    public void checkTypes(LinkedList<Env> envs, String currentFunction) {
        if (Env.functionsReturnType.get(currentFunction) != expr_.returnExprType()) 
            throw new IllegalArgumentException("Return type " + expr_.returnExprType() + " not complies with " +
                    "function " +  currentFunction + "return type ("+ Env.functionsReturnType.get(currentFunction) + ")");
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Ret) {
            Latte.Absyn.Ret x = (Latte.Absyn.Ret)o;
            return this.expr_.equals(x.expr_);
        }
        return false;
    }
        
    public String toString() {
        return "return " + expr_;
    }
    
    public int hashCode() {
        return this.expr_.hashCode();
    }


}
