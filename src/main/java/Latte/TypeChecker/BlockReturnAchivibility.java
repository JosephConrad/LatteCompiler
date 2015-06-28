package Latte.TypeChecker;

import Latte.Absyn.Block;
import Latte.Absyn.Stmt;
import Latte.Env;
import Latte.Exceptions.TypeException;

/**
 * Created by konrad on 28/06/15.
 */
public class BlockReturnAchivibility implements Latte.Absyn.Block.Visitor<Boolean, Env> {
    @Override
    public Boolean visit(Block block, Env environment) throws TypeException {
        boolean returnStmt = false;
        for (Stmt statement: block.liststmt_) {
            if (statement.accept(new StatementReturnAchivibility(), environment)){
                returnStmt = true;
            }
        }
        return returnStmt;
    }
}
