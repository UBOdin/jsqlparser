package net.sf.jsqlparser.expression.operators.arithmetic;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class Concat extends BinaryExpression {

	public Concat(){ super(); }
	public Concat(Expression lhs, Expression rhs){ super(lhs, rhs); }

	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}
	
	public String getStringExpression() {
		return "||";
	}

}
