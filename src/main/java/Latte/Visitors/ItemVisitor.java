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
      /* Code For NoInit Goes Here */

        System.out.print("\t"+p.ident_+"\t"+ "resq\t1\n");
        
        Env env = envs.getLast();
        
        env.variableShifts.put(p.ident_, env.localVarShift);
        env.localVarShift += 8;
        //p.ident_;section .bss
        //env.addVariable(p.ident_, env.getCurrentType(), env.rbp);
        String asm = "\tmov ["+p.ident_+"], dword 0\n";

        //env.rbp += 4;
        //System.out.println("; dupa "+ env);

        return asm;
    }
    public String visit(Latte.Absyn.Init p, LinkedList<Env> envs)
    {
        //System.out.print("\t"+p.ident_+"\t"+ "resq\t1\n");

        Env env = envs.getLast();

        
        String asm = p.expr_.accept(new ExprVisitor(), envs);
        //env.addVariable(p.ident_, env.rbp);
        asm += "\tpop rax\n";
        asm += "\tmov [rbp-"+env.localVarShift+"], rax\n";
        
        env.variableShifts.put(p.ident_, env.localVarShift);
        env.localVarShift += 8;

        //env.rbp += 4;
        //System.out.println(";init " + env);

        return asm;
    }
}