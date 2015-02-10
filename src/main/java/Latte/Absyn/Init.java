package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;
import Latte.Exceptions.TypeException;

import java.util.LinkedList;

public class Init extends Item {
    public final String ident_;
    public final Expr expr_;

    public Init(String p1, Expr p2) { ident_ = p1; expr_ = p2; }

    public <R,A> R accept(Latte.Absyn.Item.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public String getIdent() {
        return ident_;
    }

    public void checkTypes(LinkedList<Env> envs, String currentFunction, Type type_) throws TypeException {
        Env env = envs.getLast();
        env.variableType.put(this.getIdent(), type_.toString());
        if (type_.toString() != expr_.returnExprType(envs, currentFunction))
            throw new TypeException(currentFunction,
                    " function: to variable " + ident_ +
                    " of type " + type_ +
                    " cannot be assigned " + expr_.returnExprType(envs, currentFunction) + " expression.");
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.Init) {
            Latte.Absyn.Init x = (Latte.Absyn.Init)o;
            return this.ident_.equals(x.ident_) && this.expr_.equals(x.expr_);
        }
        return false;
    }

    public int hashCode() {
        return 37*(this.ident_.hashCode())+this.expr_.hashCode();
    }


}
