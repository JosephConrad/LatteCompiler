package Latte.Visitors;

import Latte.Absyn.Fun;
import Latte.Absyn.Type;
import Latte.Env;

import java.util.LinkedList;

/**
 * Created by konrad on 05/02/15.
 */




public class TypeVisitor implements Type.Visitor<String, LinkedList<Env>>
{
    public String visit(Latte.Absyn.Int p, LinkedList<Env> envs) {
;
        String asm = "";
        return asm;
    }
    public String visit(Latte.Absyn.Str p, LinkedList<Env> envs)
    {
        return null;
    }
    public String visit(Latte.Absyn.Bool p, LinkedList<Env> envs)
            
    {
        String asm = "";
        return asm;
    }
    public String visit(Latte.Absyn.Void p, LinkedList<Env> envs)
    {
        return null;
    }
    @Override
    public String visit(Fun p, LinkedList<Env> envs) {
        return null;
    }
}
