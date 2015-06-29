package Latte.BackendASM;

import Latte.Absyn.Item;
import Latte.Env;
import Latte.Exceptions.TypeException;

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
        int varNumber = env.getLocalVarNumber();
        env.addVariableStack(p.ident_, varNumber);
        asm += "\tmov dword [ebp"+varNumber+"], dword 0\n";
        return asm;
    }

    /*
     *  Item with initialization
     */
    public String visit(Latte.Absyn.Init p, Env env) throws TypeException {
        String asm = "";


        int varNumber = env.getLocalVarNumber();
        env.addVariableStack(p.ident_, varNumber);
        asm += p.expr_.accept(new ExprVisitor(), env);

        asm += "\tpop eax\n";
        asm += "\tmov [ebp"+varNumber+"], eax\n";

        return asm;
    }

}