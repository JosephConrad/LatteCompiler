package Latte.TypeChecker;

import Latte.Absyn.Block;
import Latte.Absyn.Stmt;
import Latte.Env;

/**
 * Created by konrad on 27/06/15.
 */
public class BlockTypeChecker implements Latte.Absyn.Block.Visitor<Void, Env> {
    @Override
    public Void visit(Block block, Env environment) {
        for (Stmt statement: block.liststmt_) {
            statement.accept(new StatementTypeChecker(), environment);
        }
        return null;
    }
}
