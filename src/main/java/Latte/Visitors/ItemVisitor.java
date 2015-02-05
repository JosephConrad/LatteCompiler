package Latte.Visitors;

import Latte.Absyn.Item;
import Latte.Env;

/**
 * Created by konrad on 05/02/15.
 */

public class ItemVisitor implements Item.Visitor<String, Env>
{
    public String visit(Latte.Absyn.NoInit p, Env env)
    {
      /* Code For NoInit Goes Here */

        System.out.print("\t"+p.ident_+"\t"+ "resd\t1\n");
        
        
        //p.ident_;section .bss
        env.addVariable(p.ident_, env.getCurrentType(), env.rbp);
        String asm = "\tmov ["+p.ident_+"], dword 0\n";

        env.rbp += 4;
        //System.out.println("; dupa "+ env);

        return asm;
    }
    public String visit(Latte.Absyn.Init p, Env env)
    {
        System.out.print("\t"+p.ident_+"\t"+ "resd\t1\n");
        
        String asm = p.expr_.accept(new ExprVisitor(), env);
        env.addVariable(p.ident_, env.rbp);
        asm += "\tmov ["+p.ident_+"], eax\n";
        env.rbp += 4;
        //System.out.println(";init " + env);

        return asm;
    }
}