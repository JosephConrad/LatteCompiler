package Latte.BackendASM;

import Latte.Absyn.Item;
import Latte.Env;

/**
 * Created by konrad on 05/02/15.
 */

public class ItemVisitor implements Item.Visitor<String, Env>
{
    /*
     *  Item without initialization
     */
    public String visit(Latte.Absyn.NoInit p, Env env) {
        String asm = "";
        Env.ileZmiennych++;
        asm += "\tmov qword [rbp-"+env.localVarShift+"], qword 0\n";

        env.variableShifts.put(p.ident_, env.localVarShift);
        env.localVarShift += 8;

        return asm;
    }

    /*
     *  Item with initialization
     */
    public String visit(Latte.Absyn.Init p, Env env) {
        String asm = "";

        Env.ileZmiennych++;
        asm += p.expr_.accept(new ExprVisitor(), env);

        asm += "\tpop rax\n";
        asm += "\tmov [rbp-"+env.localVarShift+"], rax\n";

        env.variableShifts.put(p.ident_, env.localVarShift);
        env.localVarShift += 8;

        return asm;
    }

}