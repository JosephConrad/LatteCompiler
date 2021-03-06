package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;
import Latte.Exceptions.TypeException;

import java.util.LinkedList;

public class EAdd extends Expr {
    public final Expr expr_1, expr_2;
    public final AddOp addop_;

    public EAdd(Expr p1, AddOp p2, Expr p3) { expr_1 = p1; addop_ = p2; expr_2 = p3; }

    public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

    @Override
    public String returnExprType(LinkedList<Env> envs, String  currentFunction) throws TypeException {
        
        String expr1Type = expr_1.returnExprType(envs, currentFunction);
        String expr2Type = expr_2.returnExprType(envs, currentFunction);
        Env env = envs.getLast();  
        
        if ((expr1Type == expr2Type) && (expr1Type == "int"))
            return "int";
        else if ((expr1Type == expr2Type) && (expr1Type == "string"))
            return "string";
        else
            throw new TypeException(currentFunction, "\n\t\t At add operator expression: invalid operands: "
                    + expr1Type + ", and " + expr2Type);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Latte.Absyn.EAdd) {
            Latte.Absyn.EAdd x = (Latte.Absyn.EAdd)o;
            return this.expr_1.equals(x.expr_1) && this.addop_.equals(x.addop_) && this.expr_2.equals(x.expr_2);
        }
        return false;
    }

    public int hashCode() {
        return 37*(37*(this.expr_1.hashCode())+this.addop_.hashCode())+this.expr_2.hashCode();
    }


}
