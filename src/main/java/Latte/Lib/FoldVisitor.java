package Latte.Lib;

import Latte.Absyn.*;

/** BNFC-Generated Fold Visitor */
public abstract class FoldVisitor<R,A> implements AllVisitor<R,A> {
    public abstract R leaf(A arg);
    public abstract R combine(R x, R y, A arg);

/* Program */
    public R visit(Latte.Absyn.Program p, A arg) {
      R r = leaf(arg);
      for (TopDef x : p.listtopdef_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      return r;
    }

/* TopDef */
    public R visit(Latte.Absyn.FnDef p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Arg x : p.listarg_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      r = combine(p.block_.accept(this, arg), r, arg);
      return r;
    }

/* Arg */
    public R visit(Latte.Absyn.Arg p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }

/* Block */
    public R visit(Latte.Absyn.Block p, A arg) {
      R r = leaf(arg);
      for (Stmt x : p.liststmt_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      return r;
    }

/* Stmt */
    public R visit(Latte.Absyn.Empty p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.BStmt p, A arg) {
      R r = leaf(arg);
      r = combine(p.block_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.Decl p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Item x : p.listitem_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      return r;
    }
    public R visit(Latte.Absyn.Ass p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.Incr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Decr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Ret p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.VRet p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Cond p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.CondElse p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_1.accept(this, arg), r, arg);
      r = combine(p.stmt_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.While p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.SExp p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Item */
    public R visit(Latte.Absyn.NoInit p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Init p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Type */
    public R visit(Latte.Absyn.Int p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Str p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Bool p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Void p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Fun p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Type x : p.listtype_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      return r;
    }

/* Expr */
    public R visit(Latte.Absyn.EVar p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.ELitInt p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.ELitTrue p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.ELitFalse p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.EApp p, A arg) {
      R r = leaf(arg);
      for (Expr x : p.listexpr_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      return r;
    }
    public R visit(Latte.Absyn.EString p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Neg p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.Not p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EMul p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.mulop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EAdd p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.addop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.ERel p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.relop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EAnd p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EOr p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }

/* AddOp */
    public R visit(Latte.Absyn.Plus p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Minus p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* MulOp */
    public R visit(Latte.Absyn.Times p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Div p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Mod p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* RelOp */
    public R visit(Latte.Absyn.LTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.LE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.GTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.GE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.EQU p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.NE p, A arg) {
      R r = leaf(arg);
      return r;
    }


}
