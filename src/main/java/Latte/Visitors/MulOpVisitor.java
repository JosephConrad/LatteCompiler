package Latte.Visitors;

import Latte.Absyn.MulOp;

/**
 * Created by konrad on 05/02/15.
 */
public class MulOpVisitor<R,A> implements MulOp.Visitor<String,A>
{
    private String resetRdx() {
        return "\tmov rdx, 0\n";
    }
    public String visit(Latte.Absyn.Times p, A arg)
    {
        return resetRdx() + "\timul rax, rbx\n";
    }
    public String visit(Latte.Absyn.Div p, A arg)
            
    {
        return resetRdx() + "\tidiv rbx\n";
    }
    public String visit(Latte.Absyn.Mod p, A arg)
    {
        String asm = resetRdx();
        asm += "\tidiv rbx\n";
        return asm + "\tmov rax, rdx\n";
    }

}
