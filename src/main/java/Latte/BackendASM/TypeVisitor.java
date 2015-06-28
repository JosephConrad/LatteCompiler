package Latte.BackendASM;

import Latte.Absyn.Fun;
import Latte.Absyn.Type;
import Latte.Env;

/**
 * Created by konrad on 05/02/15.
 */

public class TypeVisitor implements Type.Visitor<String, Env>
{
    public String visit(Latte.Absyn.Int p, Env env) {
        return "";
    }

    public String visit(Latte.Absyn.Str p, Env env) {
        return "";
    }

    public String visit(Latte.Absyn.Bool p, Env env) {
        return "";
    }

    public String visit(Latte.Absyn.Void p, Env env) {
        return "";
    }

    @Override
    public String visit(Fun p, Env env) {
        return "";
    }
}
