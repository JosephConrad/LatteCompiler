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
       return "\tadd";
    }
    public String visit(Latte.Absyn.Minus p, Env env)
    {
        return "\tsub";
    }

}

