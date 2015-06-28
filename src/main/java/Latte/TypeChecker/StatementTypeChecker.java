package Latte.TypeChecker;

import Latte.Absyn.*;
import Latte.Env;
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
    public Void visit(BStmt blockStmt, Env environment) {
        environment.beginBlock();
        blockStmt.block_.accept(new BlockTypeChecker(), environment);
        environment.endBlock();
        return null;
    }

    @Override
    public Void visit(Decl declarationStmt, Env environment) {
        for (Item item: declarationStmt.listitem_) {
            item.accept(new DeclItemTypeChecker(declarationStmt.type_), environment);
        }
        return null;
    }

    @Override
    public Void visit(Ass assStmt, Env environment) throws Exception {
        Type rightHandType = assStmt.expr_.accept(new ExprTypeChecker(), environment);
        Type leftHandType = assStmt.ident_;
        if (!environment.typeEquality(leftHandType, rightHandType)) {
            throw new Exception("Assignemnt types conflict: "+ PrettyPrinter.print(assStmt));
        }
        return null;
    }

    @Override
    public Void visit(Incr incrementStmt, Env environment) throws Exception {
        Type variableType = incrementStmt.ident_;
        if (!environment.typeEquality(variableType, new Int())) {
            throw new Exception("Increment type conflict: "+ PrettyPrinter.print(incrementStmt));
        }
        return null;
    }

    @Override
    public Void visit(Decr decrementStmt, Env environment) throws Exception {
        Type variableType = decrementStmt.ident_;
        if (!environment.typeEquality(variableType, new Int())) {
            throw new Exception("Decrement type conflict: "+ PrettyPrinter.print(decrementStmt));
        }
        return null;
    }

    @Override
    public Void visit(Ret returnType, Env environment) {
        return null;
    }

    @Override
    public Void visit(VRet voidReturnType, Env environment) throws Exception {
        Type funType = environment.getCurrentFunctionType();
        if (!environment.typeEquality(funType, new Latte.Absyn.Void())) {
            throw new Exception("Void returned. Expected: "+ PrettyPrinter.print(funType));
        }
        return null;
    }

    @Override
    public Void visit(Cond conditionalStmt, Env environment) throws Exception {
        Type condType = conditionalStmt.expr_.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(condType, new Bool())) {
            throw new Exception("Expected bool type, obtained: "+ PrettyPrinter.print(condType));
        }
        conditionalStmt.accept(new StatementTypeChecker(), environment);
        return null;
    }

    @Override
    public Void visit(CondElse conditionalElseStmt, Env environment) throws Exception {
        Type condType = conditionalElseStmt.expr_.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(condType, new Bool())) {
            throw new Exception("Expected bool type, obtained: "+ PrettyPrinter.print(conditionalElseStmt));
        }
        conditionalElseStmt.stmt_1.accept(new StatementTypeChecker(), environment);
        conditionalElseStmt.stmt_2.accept(new StatementTypeChecker(), environment);
        return null;
    }

    @Override
    public Void visit(While whileStmt, Env environment) throws Exception {
        Type condType = whileStmt.expr_.accept(new ExprTypeChecker(), environment);
        if (!environment.typeEquality(condType, new Bool())) {
            throw new Exception("Expected bool type, obtained: "+ PrettyPrinter.print(whileStmt));
        }
        whileStmt.stmt_.accept(new StatementTypeChecker(), environment);
        return null;
    }

    @Override
    public Void visit(SExp exprStmt, Env environment) {
        exprStmt.expr_.accept(new ExprTypeChecker(), environment);
        return null;
    }
}
