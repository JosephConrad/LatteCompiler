package Latte.Visitors;

import Latte.Absyn.MulOp;

/**
 * Created by konrad on 05/02/15.
 */
public class MulOpVisitor<R,A> implements MulOp.Visitor<String,A>
{
    public String visit(Latte.Absyn.Times p, A arg)
    {
        return "\timul eax, ebx\n";
    }
    public String visit(Latte.Absyn.Div p, A arg)
    {
        return "\tidiv ebx\n";
    }
    public String visit(Latte.Absyn.Mod p, A arg)
    {
        String asm = "\tidiv ebx\n";
        return asm + "\tmov eax, ebx\n";
    }

}
