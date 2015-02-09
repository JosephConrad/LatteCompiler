package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;

import java.util.LinkedList;

public class EString extends Expr {
  public final String string_;

  public EString(String p1) { string_ = p1; }

  public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public String returnType(LinkedList<Env> envs) {
        return "string";
    }
    @Override
    public String returnExprType() {
        return "string";
    }



    public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.EString) {
      Latte.Absyn.EString x = (Latte.Absyn.EString)o;
      return this.string_.equals(x.string_);
    }
    return false;
  }

  public int hashCode() {
    return this.string_.hashCode();
  }


}
