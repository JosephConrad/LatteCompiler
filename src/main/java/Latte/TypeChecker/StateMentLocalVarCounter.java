package Latte.TypeChecker;

import Latte.Absyn.*;
import Latte.Env;
import Latte.Exceptions.TypeException;

/**
 * Created by konrad on 28/06/15.
 */
public class StatementLocalVarCounter implements Latte.Absyn.Stmt.Visitor<Integer, Env> {
    @Override
    public Integer visit(Empty p, Env environment) {
        return 0;
    }

    @Override
    public Integer visit(BStmt blockStmt, Env environment) throws TypeException {
        int z = blockStmt.block_.accept(new BlockLocalVarCounter(), environment);
        return z;
    }

    @Override
    public Integer visit(Decl decl, Env environment) throws TypeException {
        return decl.listitem_.size();
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
        return cond.stmt_.accept(new StatementLocalVarCounter(), environment);
    }

    @Override
    public Integer visit(CondElse condElse, Env environment) throws TypeException {
        return condElse.stmt_1.accept(new StatementLocalVarCounter(), environment) +
                condElse.stmt_2.accept(new StatementLocalVarCounter(), environment);
    }

    @Override
    public Integer visit(While whileStmt, Env environment) throws TypeException {
        return whileStmt.stmt_.accept(new StatementLocalVarCounter(), environment);
    }

    @Override
    public Integer visit(SExp p, Env environment) throws TypeException {
        return 0;
    }
}
