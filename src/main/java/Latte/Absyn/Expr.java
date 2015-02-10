package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;

import java.util.LinkedList;

public abstract class Expr implements java.io.Serializable {
    public abstract <R,A> R accept(Expr.Visitor<R,A> v, A arg);

    public boolean evalExpr() {
        return false;
    }

    public abstract String returnExprType(LinkedList<Env> envs);

    public interface Visitor <R,A> {
        public R visit(Latte.Absyn.EVar p, A arg);
        public R visit(Latte.Absyn.ELitInt p, A arg);
        public R visit(Latte.Absyn.ELitTrue p, A arg);
        public R visit(Latte.Absyn.ELitFalse p, A arg);
        public R visit(Latte.Absyn.EApp p, A arg);
        public R visit(Latte.Absyn.EString p, A arg);
        public R visit(Latte.Absyn.Neg p, A arg);
        public R visit(Latte.Absyn.Not p, A arg);
        public R visit(Latte.Absyn.EMul p, A arg);
        public R visit(Latte.Absyn.EAdd p, A arg);
        public R visit(Latte.Absyn.ERel p, A arg);
        public R visit(Latte.Absyn.EAnd p, A arg);
        public R visit(Latte.Absyn.EOr p, A arg);

    }

}
