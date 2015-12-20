package Latte.BackendASM;

import Latte.Absyn.AddOp;
import Latte.Env;

/**
 * Created by konrad on 05/02/15.
 */

public class AddOpVisitor implements AddOp.Visitor<String, Env>
{
    /*
    *  Add two expressions
    */
    public String visit(Latte.Absyn.Plus p, Env env) {

        if (env.isPlusConcat()) {
            String asm = "\tpush ecx\n";
            asm += "\tpush eax\n";
            asm += "\tcall concatenateString\n";
            asm += "\tadd esp, 8\n";
            return asm;
        }
        return "\tadd eax, ecx\n" ;
    }

    /*
    *  Sub two expressions
    */
    public String visit(Latte.Absyn.Minus p, Env env) {
        return "\tsub eax, ecx\n";
    }

}

