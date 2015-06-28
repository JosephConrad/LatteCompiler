package Latte.TypeChecker;

import Latte.Absyn.Block;
import Latte.Absyn.Stmt;
import Latte.Env;
import Latte.Exceptions.TypeException;

/**
 * Created by konrad on 28/06/15.
 */
public class BlockLocalVarCounter implements Latte.Absyn.Block.Visitor<Integer, Env> {
    @Override
    public Integer visit(Block block, Env environment) throws TypeException {
        int counter = 0;
        for (Stmt statement: block.liststmt_){
            counter += statement.accept(new StateMentLocalVarCounter(), environment );
        }
        return counter;
    }
}
