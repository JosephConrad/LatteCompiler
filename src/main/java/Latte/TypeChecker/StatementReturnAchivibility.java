package Latte.TypeChecker;

import Latte.Absyn.*;
import Latte.Env;
import Latte.Exceptions.TypeException;

/**
 * Created by konrad on 28/06/15.
 */
public class StatementReturnAchivibility implements Latte.Absyn.Stmt.Visitor<Boolean, Env> {
    @Override
    public Boolean visit(Empty p, Env arg) {
        return false;
    }

    @Override
    public Boolean visit(BStmt stmt, Env environment) throws TypeException {
        return stmt.block_.accept(new BlockReturnAchivibility(), environment);
    }

    @Override
    public Boolean visit(Decl p, Env environment) throws TypeException {
        return false;
    }

    @Override
    public Boolean visit(Ass p, Env environment) throws TypeException {
        return false;
    }

    @Override
    public Boolean visit(Incr p, Env environment) throws TypeException {
        return false;
    }

    @Override
    public Boolean visit(Decr p, Env environment) throws TypeException {
        return false;
    }

    @Override
    public Boolean visit(Ret p, Env environment) throws TypeException {
        return true;
    }

    @Override
    public Boolean visit(VRet p, Env environment) throws TypeException {
        return true;
    }

    @Override
    public Boolean visit(Cond cond, Env environment) throws TypeException {
        if (evalToTrue(cond.expr_)){
            return cond.stmt_.accept(new StatementReturnAchivibility(), environment);
        }
        return false;
    }

    @Override
    public Boolean visit(CondElse condElse, Env environment) throws TypeException {
        if (evalToTrue(condElse.expr_)){
            return condElse.stmt_1.accept(new StatementReturnAchivibility(), environment);
        } else if (evalToFalse(condElse.expr_)){
            return condElse.stmt_2.accept(new StatementReturnAchivibility(), environment);
        }
        // when we do not know if we can achieve this point
        return condElse.stmt_1.accept(new StatementReturnAchivibility(), environment) &&
                condElse.stmt_2.accept(new StatementReturnAchivibility(), environment);
    }

    private boolean evalToFalse(Expr expression) {
        return expression.equals(new ELitFalse());
    }

    private boolean evalToTrue(Expr expression) {
        return expression.equals(new ELitTrue());
    }


    @Override
    public Boolean visit(While whileStmt, Env environment) throws TypeException {
        if (whileStmt.expr_.equals(new ELitTrue())) {
            return whileStmt.stmt_.accept(new StatementReturnAchivibility(), environment);
        }
        return false;
    }

    @Override
    public Boolean visit(SExp exprStmt, Env environment) throws TypeException {
        return false;
    }
}
