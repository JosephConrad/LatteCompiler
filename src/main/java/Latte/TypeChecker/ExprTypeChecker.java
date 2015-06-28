package Latte.TypeChecker;

import Latte.Absyn.*;
import Latte.Env;
import Latte.Lib.PrettyPrinter;

/**
 * Created by konrad on 27/06/15.
 */
public class ExprTypeChecker implements Latte.Absyn.Expr.Visitor<Type, Env> {
    @Override
    public Type visit(EVar p, Env arg) {
        return null;
    }

    @Override
    public Type visit(ELitInt eLitInt, Env environment) {
        eLitInt.expressionType = new Int();
        return eLitInt.expressionType;
    }

    @Override
    public Type visit(ELitTrue eLitTrue, Env environment) {
        eLitTrue.expressionType = new Bool();
        return eLitTrue.expressionType;
    }

    @Override
    public Type visit(ELitFalse eLitFalse, Env environment) {
        eLitFalse.expressionType = new Bool();
        return eLitFalse.expressionType;
    }

    @Override
    public Type visit(EApp p, Env environment) {
        return null;
    }

    @Override
    public Type visit(EString eString, Env environment) {
        environment.addString(eString.string_);
        eString.expressionType = new Str();
        return eString.expressionType;
    }

    @Override
    public Type visit(Neg neg, Env environment) throws Exception {
        Type netType = neg.expr_.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(netType, new Int())) {
            neg.expressionType = new Int();
            return neg.expressionType;
        }
        throw new Exception("Expected int type. Received: "+
                netType + " in expression " + PrettyPrinter.print(neg));
    }

    @Override
    public Type visit(Not not, Env environment) throws Exception {
        Type notType = not.expr_.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(notType, new Int())) {
            not.expressionType = new Int();
            return not.expressionType;
        }
        throw new Exception("Expected bool type. Received: "+
                notType + " in expression " + PrettyPrinter.print(not));
    }

    @Override
    public Type visit(EMul eMul, Env environment) throws Exception {
        Type eMulLeft  = eMul.expr_1.accept(new ExprTypeChecker(), environment);
        Type eMulRight = eMul.expr_2.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(eMulLeft, eMulRight) && environment.typeEquality(eMulLeft, new Int())) {
            eMul.expressionType = new Bool();
            return eMul.expressionType;
        }
        throw new Exception("Expected eMulLeft same bool types. Received: "+
                PrettyPrinter.print(eMulLeft) + ", " + PrettyPrinter.print(eMulRight));
    }

    @Override
    public Type visit(EAdd eAdd, Env environment) throws Exception {
        Type eAddLeft  = eAdd.expr_1.accept(new ExprTypeChecker(), environment);
        Type eAddRight = eAdd.expr_2.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(eAddLeft, eAddRight)) {
            throw new Exception("Expected the same types. Received: "+
                    PrettyPrinter.print(eAddLeft) + ", " + PrettyPrinter.print(eAddRight));
        }
        eAdd.expressionType = new Int();
        return eAdd.expressionType;
    }

    @Override
    public Type visit(ERel eRel, Env environment) throws Exception {
        Type eRelLeft  = eRel.expr_1.accept(new ExprTypeChecker(), environment);
        Type eRelRight = eRel.expr_2.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(eRelLeft, eRelRight)) {
            throw new Exception("Expected the same bool types. Received: "+
                    PrettyPrinter.print(eRelLeft) + ", " + PrettyPrinter.print(eRelRight));
        }
        eRel.expressionType = new Bool();
        return eRel.expressionType;
    }

    @Override
    public Type visit(EAnd eAnd, Env environment) throws Exception {
        Type eAndLeft  = eAnd.expr_1.accept(new ExprTypeChecker(), environment);
        Type eAndRight = eAnd.expr_2.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(eAndLeft, eAndRight) && environment.typeEquality(eAndLeft, new Bool())) {
            eAnd.expressionType = new Bool();
            return eAnd.expressionType;
        }
        throw new Exception("Expected the same bool types. Received: "+
                PrettyPrinter.print(eAndLeft) + ", " + PrettyPrinter.print(eAndRight));
    }

    @Override
    public Type visit(EOr eOr, Env environment) throws Exception {
        Type eOrLeft  = eOr.expr_1.accept(new ExprTypeChecker(), environment);
        Type eOrRight = eOr.expr_2.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(eOrLeft, eOrRight) && environment.typeEquality(eOrLeft, new Bool())) {
            eOr.expressionType = new Bool();
            return eOr.expressionType;
        }
        throw new Exception("Expected the same bool types. Received: "+
                PrettyPrinter.print(eOrLeft) + ", " + PrettyPrinter.print(eOrRight));
    }
}
