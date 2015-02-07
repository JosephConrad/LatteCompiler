package Latte.Visitors;

import Latte.Absyn.AddOp;
import Latte.Env;

/**
 * Created by konrad on 05/02/15.
 */


public class AddOpVisitor implements AddOp.Visitor<String, Env>
{
    public String visit(Latte.Absyn.Plus p, Env env)
            
            
    {
        if (env.addIsString == true) {
            String asm = "\tmov rdi, rax\n";
            asm += "\tmov rsi, rbx\n";
            asm += "\tcall concatenateString\n";
            return asm;
        }
       return "\tadd rax, rbx\n" ;
    }
    public String visit(Latte.Absyn.Minus p, Env env)
    {
        return "\tsub rax, rbx\n";
    }

}

