package Latte.Absyn; // Java Package generated by the BNF Converter.


/*
 * Klasa trzyma instrukcje, ktore sa wyrazenami, czyli wszystkie wyraznie, w szczegolnosci wywolanie funkcji
 */

public class SExp extends Stmt {
    public final Expr expr_;

    public SExp(Expr p1) { expr_ = p1; }

    public <R,A> R accept(Latte.Absyn.Stmt.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public boolean functionsReturnAchievibility() {
        return false;
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
