package Latte.Visitors;

import Latte.Absyn.MulOp;

/**
 * Created by konrad on 05/02/15.
 */
public class MulOpVisitor<R,A> implements MulOp.Visitor<String,A>
{
    public String visit(Latte.Absyn.Times p, A arg)
    {
      /* Code For Times Goes Here */


        return "\timul";
    }
    public String visit(Latte.Absyn.Div p, A arg)
    {
      /* Code For Div Goes Here */


        return null;
    }
    public String visit(Latte.Absyn.Mod p, A arg)
    {
      /* Code For Mod Goes Here */


        return null;
    }

}
