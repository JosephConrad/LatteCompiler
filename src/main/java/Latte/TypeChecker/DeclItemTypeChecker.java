package Latte.TypeChecker;

import Latte.Absyn.Init;
import Latte.Absyn.Item;
import Latte.Absyn.NoInit;
import Latte.Absyn.Type;
import Latte.Env;

/**
 * Created by konrad on 28/06/15.
 */
public class DeclItemTypeChecker implements Item.Visitor<Void, Env> {

    private final Type type;

    public DeclItemTypeChecker(Type type_) {
        this.type = type_;
    }

    @Override
    public Void visit(NoInit p, Env arg) {
        return null;
    }

    @Override
    public Void visit(Init p, Env arg) {
        return null;
    }
}
