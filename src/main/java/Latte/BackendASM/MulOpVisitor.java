package Latte.BackendASM;

import Latte.Absyn.MulOp;

/**
 * Created by konrad on 05/02/15.
 */
public class MulOpVisitor<R,A> implements MulOp.Visitor<String,A>
{
    private String resetRdx() {
        return "\tmov edx, 0\n";
    }

    public String visit(Latte.Absyn.Times p, A arg) {
        return resetRdx() + "\timul eax, ecx\n";
    }

    public String visit(Latte.Absyn.Div p, A arg) {
        return resetRdx() + "\tidiv ecx\n";
    }

    public String visit(Latte.Absyn.Mod p, A arg) {
        String asm = resetRdx();
        asm += "\tidiv ecx\n";
        return asm + "\tmov eax, edx\n";
    }

}
