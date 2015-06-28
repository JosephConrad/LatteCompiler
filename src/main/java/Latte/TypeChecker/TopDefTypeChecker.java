package Latte.TypeChecker;

import Latte.Absyn.Arg;
import Latte.Absyn.FnDef;
import Latte.Absyn.TopDef;
import Latte.Env;

/**
 * Created by konrad on 27/06/15.
 */
public class TopDefTypeChecker implements TopDef.Visitor<Void, Env> {
    @Override
    public Void visit(FnDef function, Env environment) throws Exception {
        environment.beginFunction(function.ident_);
        processArguments(function, environment);
        processBlock(function, environment);
        environment.endFunction();
        return null;
    }

    private void processArguments(FnDef function, Env environment) {
        for (Arg argument: function.listarg_) {
            environment.addVariable(argument.ident_, argument.type_);
        }
    }

    private void processBlock(FnDef function, Env environment) {
        environment.beginBlock();
        function.block_.accept(new BlockTypeChecker(), environment);
        environment.endBlock();
    }
}