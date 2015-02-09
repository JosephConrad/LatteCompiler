package Latte.Absyn; // Java Package generated by the BNF Converter.

public class While extends Stmt {
  public final Expr expr_;
  public final Stmt stmt_;

  public While(Expr p1, Stmt p2) { expr_ = p1; stmt_ = p2; }

  public <R,A> R accept(Latte.Absyn.Stmt.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public boolean functionsReturnAchievibility() {
        if (expr_.evalExpr()) return stmt_.functionsReturnAchievibility();
        return false;
    }

    public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.While) {
      Latte.Absyn.While x = (Latte.Absyn.While)o;
      return this.expr_.equals(x.expr_) && this.stmt_.equals(x.stmt_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.expr_.hashCode())+this.stmt_.hashCode();
  }


}
