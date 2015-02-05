package Latte.Visitors;

import Latte.Absyn.AddOp;

/**
 * Created by konrad on 05/02/15.
 */


public class AddOpVisitor<R,A> implements AddOp.Visitor<R,A>
{
    public R visit(Latte.Absyn.Plus p, A arg)
    {
       return null;
    }
    public R visit(Latte.Absyn.Minus p, A arg)
    {
        return null;
    }

}

