package Latte.Absyn; // Java Package generated by the BNF Converter.

public class ELitFalse extends Expr {

    public ELitFalse() { }

    public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public boolean evalExpr() {
        return false;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.ELitFalse) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37;
    }

}
