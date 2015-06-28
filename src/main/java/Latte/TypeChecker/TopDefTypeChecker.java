package Latte.TypeChecker;

import Latte.Absyn.Arg;
import Latte.Absyn.FnDef;
import Latte.Absyn.TopDef;
import Latte.Env;
import Latte.Absyn.Void;
import Latte.Exceptions.TypeException;

/**
 * Created by konrad on 27/06/15.
 */
public class TopDefTypeChecker implements TopDef.Visitor<Void, Env> {
    @Override
    public Void visit(FnDef function, Env environment) throws TypeException {
        environment.beginFunction(function.ident_);
        processArguments(function, environment);
        processBlock(function, environment);
        checkReturnAccessibility(function, environment);
        function.localVars = calcLocalVar(function, environment);
        environment.endFunction();
        return null;
    }

    private int calcLocalVar(FnDef function, Env environment) throws TypeException {
        return function.block_.accept(new BlockLocalVarCounter(), environment);
    }

    private void checkReturnAccessibility(FnDef function, Env environment) throws TypeException {
        boolean voidFun = environment.typeEquality(function.type_, new Void());
        if (!voidFun) {
            if (!function.block_.accept(new BlockReturnAchivibility(), environment)) {
                throw new TypeException(environment.getCurrentFunctionIdent(), "Return statement in function is not achievable.");
            }
        }
    }

    private void processArguments(FnDef function, Env environment) throws TypeException {
        for (Arg argument: function.listarg_) {
            environment.addVariable(argument.ident_, argument.type_);
        }
    }

    private void processBlock(FnDef function, Env environment) throws TypeException {
        environment.beginBlock();
        function.block_.accept(new BlockTypeChecker(), environment);
        environment.endBlock();
    }
}
