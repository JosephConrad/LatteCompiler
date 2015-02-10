package Latte.Absyn; // Java Package generated by the BNF Converter.


import Latte.Env;

import java.util.LinkedList;


public class EApp extends Expr {
    public final String ident_;
    public final ListExpr listexpr_;

    public EApp(String p1, ListExpr p2) { ident_ = p1; listexpr_ = p2; }

    @Override
    public boolean evalExpr() {
        return true;
    }

    @Override
    public String returnExprType(LinkedList<Env> envs) {

        if ((ident_ == "printInt") && (listexpr_.get(0).returnExprType(envs) != "int"))
            throw new IllegalArgumentException("Function printInt takes as parameter int, not "
                    + listexpr_.get(0).returnExprType(envs));

        if ((ident_ == "printString") && (listexpr_.get(0).returnExprType(envs) != "string"))
            throw new IllegalArgumentException("Function printString takes as parameter string, not "
                    + listexpr_.get(0).returnExprType(envs));

        if (Env.functionsArgumentsNumber.get(ident_) != listexpr_.size())
            throw new IllegalArgumentException("Wrong number of arguments. Function takas "
                    + Env.functionsArgumentsNumber.get(ident_) + " arguments, but "
                    + listexpr_.size() + " found.");

        return Env.functionsReturnType.get(ident_);
    }

    public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

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
