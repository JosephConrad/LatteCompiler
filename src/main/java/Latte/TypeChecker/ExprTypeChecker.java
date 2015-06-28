package Latte.TypeChecker;

import Latte.Absyn.*;
import Latte.Env;
import Latte.Exceptions.TypeException;
import Latte.Lib.PrettyPrinter;

/**
 * Created by konrad on 27/06/15.
 */
public class ExprTypeChecker implements Latte.Absyn.Expr.Visitor<Type, Env> {
    @Override
    public Type visit(EVar eVar, Env environment) throws TypeException {
        eVar.expressionType = environment.getVariableType(eVar.ident_);
        return eVar.expressionType;
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
    public Type visit(EApp eApp, Env environment) throws TypeException {
        checkArguments(eApp.listexpr_, environment.getFunctionById(eApp.ident_).listtype_, environment, eApp.ident_);
        return environment.getFunctionById(eApp.ident_).type_;
    }

    private void checkArguments(ListExpr listexpr_, ListType listtype_, Env environment, String ident) throws TypeException {
        int providedArgs = listexpr_.size();
        int declaredArgs = listtype_.size();

        if (providedArgs != declaredArgs){
            throw new TypeException(environment.getCurrentFunctionIdent(),
                    "At function application: Wrong number of arguments. Function takes (" +
                            declaredArgs + ") argument(s), but (" + providedArgs + ") found.");
        }
        for (int i = 0; i < providedArgs; ++i) {
            Type providedArg = listexpr_.get(i).accept(new ExprTypeChecker(), environment);
            Type declaredArg = listtype_.get(i);
            if (!providedArg.equals(declaredArg)) {
                throw new TypeException(environment.getCurrentFunctionIdent(),
                        "function "+ident+" takes as "+(i+1)+" parameter ("+declaredArg+"), not ("+providedArg+ ")");

            }
        }
    }

    @Override
    public Type visit(EString eString, Env environment) {
        environment.addString(eString.string_);
        eString.expressionType = new Str();
        return eString.expressionType;
    }

    @Override
    public Type visit(Neg neg, Env environment) throws TypeException {
        Type netType = neg.expr_.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(netType, new Int())) {
            neg.expressionType = new Int();
            return neg.expressionType;
        }
        throw new TypeException("Expected int type. Received: "+
                netType + " in expression " + PrettyPrinter.print(neg));
    }

    @Override
    public Type visit(Not not, Env environment) throws TypeException {
        Type notType = not.expr_.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(notType, new Bool())) {
            not.expressionType = new Bool();
            return not.expressionType;
        }
        throw new TypeException("Expected bool type. Received: "+
                notType + " in expression " + PrettyPrinter.print(not));
    }

    @Override
    public Type visit(EMul eMul, Env environment) throws TypeException {
        Type eMulLeft  = eMul.expr_1.accept(new ExprTypeChecker(), environment);
        Type eMulRight = eMul.expr_2.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(eMulLeft, eMulRight) && environment.typeEquality(eMulLeft, new Int())) {
            eMul.expressionType = eMulRight;
            return eMul.expressionType;
        }
        throw new TypeException(environment.getCurrentFunctionIdent(), "Expected the same types. Received: "+
                PrettyPrinter.print(eMulLeft) + ", " + PrettyPrinter.print(eMulRight));
    }

    @Override
    public Type visit(EAdd eAdd, Env environment) throws TypeException {
        Type eAddLeft  = eAdd.expr_1.accept(new ExprTypeChecker(), environment);
        Type eAddRight = eAdd.expr_2.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(eAddLeft, eAddRight)) {
            throw new TypeException(environment.getCurrentFunctionIdent(),
                    "At add operator expression: invalid operands: ("+eAddLeft+"), and ("+eAddRight+").");
        }
        eAdd.expressionType = eAddLeft;
        return eAdd.expressionType;
    }

    @Override
    public Type visit(ERel eRel, Env environment) throws TypeException {
        Type eRelLeft  = eRel.expr_1.accept(new ExprTypeChecker(), environment);
        Type eRelRight = eRel.expr_2.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(eRelLeft, eRelRight)) {
            throw new TypeException("Expected the same bool types. Received: "+
                    PrettyPrinter.print(eRelLeft) + ", " + PrettyPrinter.print(eRelRight));
        }
        eRel.expressionType = new Bool();
        return eRel.expressionType;
    }

    @Override
    public Type visit(EAnd eAnd, Env environment) throws TypeException {
        Type eAndLeft  = eAnd.expr_1.accept(new ExprTypeChecker(), environment);
        Type eAndRight = eAnd.expr_2.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(eAndLeft, eAndRight) && environment.typeEquality(eAndLeft, new Bool())) {
            eAnd.expressionType = new Bool();
            return eAnd.expressionType;
        }
        throw new TypeException("Expected the same bool types. Received: "+
                PrettyPrinter.print(eAndLeft) + ", " + PrettyPrinter.print(eAndRight));
    }

    @Override
    public Type visit(EOr eOr, Env environment) throws TypeException {
        Type eOrLeft  = eOr.expr_1.accept(new ExprTypeChecker(), environment);
        Type eOrRight = eOr.expr_2.accept(new ExprTypeChecker(), environment);
        if (environment.typeEquality(eOrLeft, eOrRight) && environment.typeEquality(eOrLeft, new Bool())) {
            eOr.expressionType = new Bool();
            return eOr.expressionType;
        }
        throw new TypeException("Expected the same bool types. Received: "+
                PrettyPrinter.print(eOrLeft) + ", " + PrettyPrinter.print(eOrRight));
    }
}
