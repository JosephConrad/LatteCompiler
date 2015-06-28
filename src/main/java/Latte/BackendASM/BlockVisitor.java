package Latte.BackendASM;

import Latte.Absyn.Block;
import Latte.Absyn.Stmt;
import Latte.Env;

/**
 * Created by konrad on 05/02/15.
 */

public class BlockVisitor implements Block.Visitor<String, Env>
{
    /*
     *  Block
     */
    public String visit(Latte.Absyn.Block p, Env env) {

        String asm = "";
        for (Stmt x : p.liststmt_) {
            asm += x.accept(new StmtVisitor(), env);
            asm += "\n";
        }

        return asm;
    }

}
