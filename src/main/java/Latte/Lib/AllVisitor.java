package Latte.Lib;

/** BNFC-Generated All Visitor */
public interface AllVisitor<R,A> extends
  Latte.Absyn.Program.Visitor<R,A>,
  Latte.Absyn.TopDef.Visitor<R,A>,
  Latte.Absyn.Arg.Visitor<R,A>,
  Latte.Absyn.Block.Visitor<R,A>,
  Latte.Absyn.Stmt.Visitor<R,A>,
  Latte.Absyn.Item.Visitor<R,A>,
  Latte.Absyn.Type.Visitor<R,A>,
  Latte.Absyn.Expr.Visitor<R,A>,
  Latte.Absyn.AddOp.Visitor<R,A>,
  Latte.Absyn.MulOp.Visitor<R,A>,
  Latte.Absyn.RelOp.Visitor<R,A>
{}
