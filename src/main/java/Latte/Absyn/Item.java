package Latte.Absyn; // Java Package generated by the BNF Converter.

import Latte.Env;
import Latte.Exceptions.TypeException;

public abstract class Item implements java.io.Serializable {
    public abstract <R,A> R accept(Item.Visitor<R,A> v, A arg) throws TypeException;

    public abstract String getIdent();

    public void checkTypes(Env env, String currentFunction, Type type_) throws TypeException {

        if (env.varDeclarationEnv.containsKey(this.getIdent()))
            // instad of 0 was env.size()
            if (env.varDeclarationEnv.get(this.getIdent())== 0)
                throw new TypeException(currentFunction, "\n\t\t At variable " + this.getIdent() + " declaration: "
                        + "variable " + this.getIdent() + " is redeclared.");

        env.variableType.put(this.getIdent(), type_.toString());

    }

    public interface Visitor <R,A> {
        public R visit(Latte.Absyn.NoInit p, A arg) throws TypeException;
        public R visit(Latte.Absyn.Init p, A arg) throws TypeException;

    }

}
