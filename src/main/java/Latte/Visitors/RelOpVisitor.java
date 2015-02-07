package Latte.Visitors;

import Latte.Absyn.RelOp;

/**
 * Created by konrad on 05/02/15.
 */

public class RelOpVisitor<A> implements RelOp.Visitor<String,A>
{
    public String visit(Latte.Absyn.LTH p, A arg)
    {
        return "\tjl";
    }
    public String visit(Latte.Absyn.LE p, A arg)
    {
        return "\tjle";
    }
    public String visit(Latte.Absyn.GTH p, A arg)
    {
        return "\tjg";
    }
    public String visit(Latte.Absyn.GE p, A arg)
    {
        return "\tjge";
    }
    public String visit(Latte.Absyn.EQU p, A arg)
    {
        return "\tje";
    }
    public String visit(Latte.Absyn.NE p, A arg)
    {
        return "\tjne";
    }

}
