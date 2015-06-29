package Latte.Absyn; // Java Package generated by the BNF Converter.


import Latte.Env;
import Latte.Exceptions.TypeException;


public class EApp extends Expr {
    public final String ident_;
    public final ListExpr listexpr_;

    public EApp(String p1, ListExpr p2) { ident_ = p1; listexpr_ = p2; }

    @Override
    public boolean evalExpr() {
        return true;
    }


    public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) throws TypeException { return v.visit(this, arg); }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.EApp) {
            Latte.Absyn.EApp x = (Latte.Absyn.EApp)o;
            return this.ident_.equals(x.ident_) && this.listexpr_.equals(x.listexpr_);
        }
        return false;
    }

    public String toString() {
        String res = ident_ + " ";
        for(Expr e: listexpr_) {
            res += e + "; ";
        }
        return res;
    }

    public int hashCode() {
        return 37*(this.ident_.hashCode())+this.listexpr_.hashCode();
    }

}
