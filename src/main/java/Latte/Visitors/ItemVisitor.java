package Latte.Visitors;

import Latte.Absyn.Item;
import Latte.Env;

import java.util.LinkedList;

/**
 * Created by konrad on 05/02/15.
 */

public class ItemVisitor implements Item.Visitor<String, LinkedList<Env>>
{
    public String visit(Latte.Absyn.NoInit p, LinkedList<Env> envs)
    {
        Env env = envs.getLast();
        String asm = "";

        asm += "\tmov qword [rbp-"+env.localVarShift+"], qword 0\n";

        env.variableShifts.put(p.ident_, env.localVarShift);
        env.localVarShift += 8;
        return asm;
    }
    public String visit(Latte.Absyn.Init p, LinkedList<Env> envs)
    {
        Env env = envs.getLast();
        String asm = "";
        
        asm += p.expr_.accept(new ExprVisitor(), envs);
        asm += "\tpop rax\n";
        asm += "\tmov [rbp-"+env.localVarShift+"], rax\n";
        
        env.variableShifts.put(p.ident_, env.localVarShift);
        env.localVarShift += 8; 

        return asm;
    }
}