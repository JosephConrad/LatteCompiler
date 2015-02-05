package Latte.Visitors;

import Latte.Absyn.RelOp;

/**
 * Created by konrad on 05/02/15.
 */

public class RelOpVisitor<A> implements RelOp.Visitor<String,A>
{
    public String visit(Latte.Absyn.LTH p, A arg)
    {
        return null;
    }
    public String visit(Latte.Absyn.LE p, A arg)
    {
        return null;
    }
    public String visit(Latte.Absyn.GTH p, A arg)
    {
        return null;
    }
    public String visit(Latte.Absyn.GE p, A arg)
    {
        String asm = "\tjge";
        return asm;
    }
    public String visit(Latte.Absyn.EQU p, A arg)
    {
        String asm = "\tjne";
        return asm;
    }
    public String visit(Latte.Absyn.NE p, A arg)
    {
        return null;
    }

}