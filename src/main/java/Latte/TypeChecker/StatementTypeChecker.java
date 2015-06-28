package Latte.TypeChecker;

import Latte.Absyn.*;
import Latte.Env;
import Latte.Exceptions.TypeException;
import Latte.Lib.PrettyPrinter;

import java.lang.Void;


/**
 * Created by konrad on 27/06/15.
 */
public class StatementTypeChecker implements Latte.Absyn.Stmt.Visitor<Void, Env> {
    @Override
    public Void visit(Empty emptyStmt, Env environment) {
        return null;
    }

    @Override
    public Void visit(BStmt blockStmt, Env environment) throws TypeException {
        environment.beginBlock();
        blockStmt.block_.accept(new BlockTypeChecker(), environment);
        environment.endBlock();
        return null;
    }

    @Override
    public Void visit(Decl declarationStmt, Env environment) throws TypeException {
        for (Item item: declarationStmt.listitem_) {
            item.accept(new DeclItemTypeChecker(declarationStmt.type_), environment);
        }
        return null;
    }

    @Override
    public Void visit(Ass assStmt, Env environment) throws TypeException {
        Type rightHandType = assStmt.expr_.accept(new ExprTypeChecker(), environment);
        Type leftHandType = environment.getVariableType(assStmt.ident_);
        if (!environment.typeEquality(leftHandType, rightHandType)) {
            throw new TypeException(environment.getCurrentFunctionIdent(),
                    "At assignment to " + assStmt.ident_ + " variable: incompatible type of variable ("+leftHandType+") with operand type ("+rightHandType+")");
        }
        return null;
    }

    @Override
    public Void visit(Incr incrementStmt, Env environment) throws TypeException {
        Type variableType = environment.getVariableType(incrementStmt.ident_);
        if (!environment.typeEquality(variableType, new Int())) {
            throw new TypeException("Increment type conflict: "+ PrettyPrinter.print(incrementStmt));
        }
        return null;
    }

    @Override
    public Void visit(Decr decrementStmt, Env environment) throws TypeException {
        Type variableType = environment.getVariableType(decrementStmt.ident_);
        if (!environment.typeEquality(variableType, new Int())) {
            throw new TypeException("Decrement type conflict: "+ PrettyPrinter.print(decrementStmt));
        }
        return null;
    }

    @Override
    public Void visit(Ret ret, Env environment) throws TypeException {
        Type returnType = ret.expr_.accept(new ExprTypeChecker(), environment);
        Type funType = environment.getCurrentFunctionType();
        if (!environment.typeEquality(funType, returnType)) {
            throw new TypeException(environment.getCurrentFunctionIdent(),
                    "return statement of ("+returnType+") type not complies with function return type ("+funType+").");
        }
        return null;
    }

    @Override
    public Void visit(VRet voidRet, Env environment) throws TypeException {
        Type funType = environment.getCurrentFunctionType();
        if (!environment.typeEquality(funType, new Latte.Absyn.Void())) {
            throw new TypeException(environment.getCurrentFunctionIdent(),
                    "At return void statement: return type of the function is ("+funType +")");
        }
        return null;
    }

    @Override
    public Void visit(Cond conditionalStmt, Env environment) throws TypeException {
        Type condType = conditionalStmt.expr_.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(condType, new Bool())) {
            throw new TypeException("Expected "+ new Bool() +" type, obtained: " + condType);
        }
        conditionalStmt.stmt_.accept(new StatementTypeChecker(), environment);
        return null;
    }

    @Override
    public Void visit(CondElse conditionalElseStmt, Env environment) throws TypeException {
        Type condType = conditionalElseStmt.expr_.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(condType, new Bool())) {
            throw new TypeException("Expected bool type, obtained: "+ PrettyPrinter.print(conditionalElseStmt));
        }
        conditionalElseStmt.stmt_1.accept(new StatementTypeChecker(), environment);
        conditionalElseStmt.stmt_2.accept(new StatementTypeChecker(), environment);
        return null;
    }

    @Override
    public Void visit(While whileStmt, Env environment) throws TypeException {
        Type condType = whileStmt.expr_.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(condType, new Bool())) {
            throw new TypeException("Expected bool type, obtained: "+ PrettyPrinter.print(whileStmt));
        }
        whileStmt.stmt_.accept(new StatementTypeChecker(), environment);
        return null;
    }

    @Override
    public Void visit(SExp exprStmt, Env environment) throws TypeException {
        exprStmt.expr_.accept(new ExprTypeChecker(), environment);
        return null;
    }
}
