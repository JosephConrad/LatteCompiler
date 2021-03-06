package Latte.BackendASM;

import Latte.Absyn.AddOp;
import Latte.Env;

import java.util.LinkedList;

/**
 * Created by konrad on 05/02/15.
 */

public class AddOpVisitor implements AddOp.Visitor<String, LinkedList<Env>>
{
    /*
    *  Add two expressions
    */
    public String visit(Latte.Absyn.Plus p, LinkedList<Env> envs) {
        
        if (envs.getLast().addIsString == true) {
            String asm = "\tmov rdi, rax\n";
            asm += "\tmov rsi, rcx\n";
            asm += "\tcall concatenateString\n";
            return asm;
        }
        return "\tadd rax, rcx\n" ;
    }

    /*
    *  Sub two expressions
    */
    public String visit(Latte.Absyn.Minus p, LinkedList<Env> envs) {
        return "\tsub rax, rcx\n";
    }

}

