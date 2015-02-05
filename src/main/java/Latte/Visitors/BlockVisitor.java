package Latte.Visitors;

import Latte.Absyn.Block;
import Latte.Absyn.Stmt;
import Latte.Env;

/**
 * Created by konrad on 05/02/15.
 */
public class BlockVisitor implements Block.Visitor<String, Env>
{
    public String visit(Latte.Absyn.Block p, Env arg)
    {
      /* Code For Block Goes Here */
        String asm = "";
        for (Stmt x : p.liststmt_) {
            asm += x.accept(new StmtVisitor(), arg);
            asm += "\n";
        }
        return asm;
    }

}
