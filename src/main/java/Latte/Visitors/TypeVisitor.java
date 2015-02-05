package Latte.Visitors;

import Latte.Absyn.Fun;
import Latte.Absyn.Type;

/**
 * Created by konrad on 05/02/15.
 */




public class TypeVisitor<R,A> implements Type.Visitor<R,A>
{
    public R visit(Latte.Absyn.Int p, A arg)
    {
        return null;
    }
    public R visit(Latte.Absyn.Str p, A arg)
    {
        return null;
    }
    public R visit(Latte.Absyn.Bool p, A arg)
    {
        return null;
    }
    public R visit(Latte.Absyn.Void p, A arg)
    {
        return null;
    }
    @Override
    public R visit(Fun p, A arg) {
        return null;
    }
}
