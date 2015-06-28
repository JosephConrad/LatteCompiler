package Latte.TypeChecker;

import Latte.Absyn.FnDef;
import Latte.Absyn.Program;
import Latte.Absyn.TopDef;
import Latte.Env;

/**
 * Created by konrad on 27/06/15.
 */
public class ProgTypeChecker implements Program.Visitor<Env, Env>  {
    @Override
    public Env visit(Program p, Env env) {
        for (TopDef topDef: p.listtopdef_) {
            env.addFunction((FnDef) topDef);
        }
        for (TopDef def : p.listtopdef_) {
            def.accept(new TopDefTypeChecker(), env);
        }
        return env;
    }
}
