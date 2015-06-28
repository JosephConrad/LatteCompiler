package Latte.TypeChecker;

import Latte.Absyn.*;
import Latte.Env;
import Latte.Exceptions.TypeException;

/**
 * Created by konrad on 28/06/15.
 */
public class StateMentLocalVarCounter implements Latte.Absyn.Stmt.Visitor<Integer, Env> {
    @Override
    public Integer visit(Empty p, Env environment) {
        return 0;
    }

    @Override
    public Integer visit(BStmt blockStmt, Env environment) throws TypeException {
        return blockStmt.block_.accept(new BlockLocalVarCounter(), environment);
    }

    @Override
    public Integer visit(Decl p, Env environment) throws TypeException {
        return 1;
    }

    @Override
    public Integer visit(Ass p, Env environment) throws TypeException {
        return 0;
    }

    @Override
    public Integer visit(Incr p, Env environment) throws TypeException {
        return 0;
    }

    @Override
    public Integer visit(Decr p, Env environment) throws TypeException {
        return 0;
    }

    @Override
    public Integer visit(Ret p, Env environment) throws TypeException {
        return 0;
    }

    @Override
    public Integer visit(VRet p, Env environment) throws TypeException {
        return 0;
    }

    @Override
    public Integer visit(Cond cond, Env environment) throws TypeException {
        return cond.stmt_.accept(new StateMentLocalVarCounter(), environment);
    }

    @Override
    public Integer visit(CondElse condElse, Env environment) throws TypeException {
        return condElse.stmt_1.accept(new StateMentLocalVarCounter(), environment) +
                condElse.stmt_2.accept(new StateMentLocalVarCounter(), environment);
    }

    @Override
    public Integer visit(While whileStmt, Env environment) throws TypeException {
        return whileStmt.stmt_.accept(new StateMentLocalVarCounter(), environment);
    }

    @Override
    public Integer visit(SExp p, Env environment) throws TypeException {
        return 0;
    }
}
