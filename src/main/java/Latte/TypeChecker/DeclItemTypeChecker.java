package Latte.TypeChecker;

import Latte.Absyn.*;
import Latte.Env;
import Latte.Exceptions.TypeException;
import Latte.Lib.PrettyPrinter;

import java.lang.Void;

/**
 * Created by konrad on 28/06/15.
 */
public class DeclItemTypeChecker implements Item.Visitor<Void, Env> {

    private final Type type;

    public DeclItemTypeChecker(Type type_) {
        this.type = type_;
    }

    @Override
    public Void visit(NoInit noInitDecl, Env environment) throws TypeException {
        environment.addVariable(noInitDecl.ident_, this.type);
        if (environment.typeEquality(this.type, new Str())) {
            environment.addString("");
        }
        return null;
    }

    @Override
    public Void visit(Init initDecl, Env environment) throws TypeException {
        Type initDeclType = initDecl.expr_.accept(new ExprTypeChecker(), environment);

        if (!environment.typeEquality(this.type, initDeclType)) {
            throw new TypeException("Declaration is of type: "+this.type+", but expression is: "+initDeclType+
                    ". Instruction "+ PrettyPrinter.print(initDecl));
        }
        environment.addVariable(initDecl.ident_, this.type);
        return null;
    }
}
