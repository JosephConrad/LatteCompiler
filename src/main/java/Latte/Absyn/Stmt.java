package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;
import Latte.Exceptions.TypeException;

import java.util.LinkedList;

public abstract class Stmt implements java.io.Serializable {
    public abstract <R,A> R accept(Stmt.Visitor<R,A> v, A arg);

    public abstract boolean functionsReturnAchievibility();

    public abstract void checkTypes(LinkedList<Env> envs, String currentFunction) throws TypeException;

    public interface Visitor <R,A> {
        public R visit(Latte.Absyn.Empty p, A arg);
        public R visit(Latte.Absyn.BStmt p, A arg);
        public R visit(Latte.Absyn.Decl p, A arg);
        public R visit(Latte.Absyn.Ass p, A arg);
        public R visit(Latte.Absyn.Incr p, A arg);
        public R visit(Latte.Absyn.Decr p, A arg);
        public R visit(Latte.Absyn.Ret p, A arg);
        public R visit(Latte.Absyn.VRet p, A arg);
        public R visit(Latte.Absyn.Cond p, A arg);
        public R visit(Latte.Absyn.CondElse p, A arg);
        public R visit(Latte.Absyn.While p, A arg);
        public R visit(Latte.Absyn.SExp p, A arg);

    }

}
