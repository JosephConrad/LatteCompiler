package Latte.BackendASM;

import Latte.Absyn.Block;
import Latte.Absyn.Stmt;
import Latte.Env;

import java.util.LinkedList;

/**
 * Created by konrad on 05/02/15.
 */

public class BlockVisitor implements Block.Visitor<String, LinkedList<Env>>
{   
    /*
     *  Block
     */
    public String visit(Latte.Absyn.Block p, LinkedList<Env> envs) { 
        Env env = envs.getLast();
        
        envs.add(Env.copyEnv(env, env.funName));
        
        String asm = "";
        for (Stmt x : p.liststmt_) {
            asm += x.accept(new StmtVisitor(), envs);
            asm += "\n";
        }
        
        envs.removeLast();

        return asm;
    }

}
