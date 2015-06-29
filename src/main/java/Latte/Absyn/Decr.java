package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Exceptions.TypeException;

public class Decr extends Stmt {
    public final String ident_;

    public Decr(String p1) { ident_ = p1; }

    public <R,A> R accept(Latte.Absyn.Stmt.Visitor<R,A> v, A arg) throws TypeException { return v.visit(this, arg); }

    @Override
    public boolean functionsReturnAchievibility() {
        return false;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Decr) {
            Latte.Absyn.Decr x = (Latte.Absyn.Decr)o;
            return this.ident_.equals(x.ident_);
        }
        return false;
    }

    public int hashCode() {
        return this.ident_.hashCode();
    }

}
