package Latte.Visitors;

import Latte.Absyn.MulOp;

/**
 * Created by konrad on 05/02/15.
 */
public class MulOpVisitor<R,A> implements MulOp.Visitor<R,A>
{
    public R visit(Latte.Absyn.Times p, A arg)
    {
      /* Code For Times Goes Here */


        return null;
    }
    public R visit(Latte.Absyn.Div p, A arg)
    {
      /* Code For Div Goes Here */


        return null;
    }
    public R visit(Latte.Absyn.Mod p, A arg)
    {
      /* Code For Mod Goes Here */


        return null;
    }

}
