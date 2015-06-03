/* ================================================================
 * JSQLParser : java based sql parser 
 * ================================================================
 *
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
 
package net.sf.jsqlparser.expression;

import java.util.*;

import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

public abstract class ExpressionVisitorBase implements ExpressionVisitor {

	public java.io.PrintWriter verboseUnhandled;
	public boolean recur;

	public ExpressionVisitorBase() { this(null, true); }
	public ExpressionVisitorBase(boolean recur) { this(null, recur); }
	public ExpressionVisitorBase(java.io.PrintWriter verboseUnhandled, boolean recur)	
	{
		this.verboseUnhandled = verboseUnhandled;
		this.recur = recur;
	}

	private void unhandled(Expression e)
	{
		if(verboseUnhandled != null){ 
			verboseUnhandled.println("Unhandled: Expression-"+e.getClass().toString()); 
		}
		if(recur){ recur(e); }
	}

	public void recur(Expression e)
	{
		for(Expression child : getChildren(e)){
			child.accept(this);
		}
	}

	public void visit(NullValue e) { unhandled(e); }
	public void visit(Function e) { unhandled(e); }
	public void visit(InverseExpression e) { unhandled(e); }
	public void visit(JdbcParameter e) { unhandled(e); }
	public void visit(DoubleValue e) { unhandled(e); }
	public void visit(LongValue e) { unhandled(e); }
	public void visit(DateValue e) { unhandled(e); }
	public void visit(TimeValue e) { unhandled(e); }
	public void visit(TimestampValue e) { unhandled(e); }
	public void visit(BooleanValue e) { unhandled(e); }
	public void visit(Parenthesis e) { unhandled(e); }
	public void visit(StringValue e) { unhandled(e); }
	public void visit(Addition e) { unhandled(e); }
	public void visit(Division e) { unhandled(e); }
	public void visit(Multiplication e) { unhandled(e); }
	public void visit(Subtraction e) { unhandled(e); }
	public void visit(AndExpression e) { unhandled(e); }
	public void visit(OrExpression e) { unhandled(e); }
	public void visit(Between e) { unhandled(e); }
	public void visit(EqualsTo e) { unhandled(e); }
	public void visit(GreaterThan e) { unhandled(e); }
	public void visit(GreaterThanEquals e) { unhandled(e); }
	public void visit(InExpression e) { unhandled(e); }
	public void visit(IsNullExpression e) { unhandled(e); }
	public void visit(LikeExpression e) { unhandled(e); }
	public void visit(MinorThan e) { unhandled(e); }
	public void visit(MinorThanEquals e) { unhandled(e); }
	public void visit(NotEqualsTo e) { unhandled(e); }
	public void visit(Column e) { unhandled(e); }
	public void visit(SubSelect e) { unhandled(e); }
	public void visit(CaseExpression e) { unhandled(e); }
	public void visit(WhenClause e) { unhandled(e); }
	public void visit(ExistsExpression e) { unhandled(e); }
	public void visit(AllComparisonExpression e) { unhandled(e); }
	public void visit(AnyComparisonExpression e) { unhandled(e); }
	public void visit(Concat e) { unhandled(e); }
	public void visit(Matches e) { unhandled(e); }
	public void visit(BitwiseAnd e) { unhandled(e); }
	public void visit(BitwiseOr e) { unhandled(e); }
	public void visit(BitwiseXor e) { unhandled(e); }

	public static List<Expression> getChildren(Expression expr) {
		final List<Expression> children = new ArrayList<Expression>();
		expr.accept(new ExpressionVisitor() {
			public void binary(BinaryExpression be) {
				children.add(be.getLeftExpression());
				children.add(be.getRightExpression());
			}
			public void itemList(ItemsList il)
			{
				if(il == null){ return; }
				il.accept(new ItemsListVisitor(){
					public void visit(ExpressionList el){ 
						if(el.getExpressions() != null){
							children.addAll(el.getExpressions());
						}
					}
					public void visit(SubSelect ss){
						children.add(ss);
					}
				});
			}
			public void visit(NullValue nullValue) {}
			public void visit(Function function) { itemList(function.getParameters()); }
			public void visit(InverseExpression inverseExpression)
			{
				children.add(inverseExpression.getExpression());
			}
			public void visit(JdbcParameter jdbcParameter) {}
			public void visit(DoubleValue doubleValue) {}
			public void visit(LongValue longValue) {}
			public void visit(DateValue dateValue) {}
			public void visit(TimeValue timeValue) {}
			public void visit(TimestampValue timestampValue) {}
			public void visit(BooleanValue booleanValue) {}
			public void visit(StringValue stringValue) {}
			public void visit(Addition addition) { binary(addition); }
			public void visit(Division division) { binary(division); }
			public void visit(Multiplication multiplication) { binary(multiplication); }
			public void visit(Subtraction subtraction) { binary(subtraction); }
			public void visit(AndExpression andExpression) { binary(andExpression); }
			public void visit(OrExpression orExpression) { binary(orExpression); }
			public void visit(Between between) 
			{
				children.add(between.getLeftExpression());
				children.add(between.getBetweenExpressionStart());
				children.add(between.getBetweenExpressionEnd());
			}
			public void visit(EqualsTo equalsTo) { binary(equalsTo); }
			public void visit(GreaterThan greaterThan) { binary(greaterThan); }
			public void visit(GreaterThanEquals greaterThanEquals) { binary(greaterThanEquals); }
			public void visit(InExpression inExpression) 
			{
				children.add(inExpression.getLeftExpression());
				itemList(inExpression.getItemsList());
			}
			public void visit(IsNullExpression isNullExpression)
			{
				children.add(isNullExpression.getLeftExpression());
			}
			public void visit(LikeExpression likeExpression) { binary(likeExpression); }
			public void visit(MinorThan minorThan) { binary(minorThan); }
			public void visit(MinorThanEquals minorThanEquals) { binary(minorThanEquals); }
			public void visit(NotEqualsTo notEqualsTo) { binary(notEqualsTo); }
			public void visit(Column tableColumn) { }
			public void visit(SubSelect subSelect) { /* ignore */ }
			public void visit(CaseExpression caseExpression)
			{
				if(caseExpression.getSwitchExpression() != null){
					children.add(caseExpression.getSwitchExpression());
				}
				for(WhenClause when : caseExpression.getWhenClauses()){ visit(when); }
				children.add(caseExpression.getElseExpression());
			}
			public void visit(WhenClause whenClause)
			{
				children.add(whenClause.getWhenExpression());
				children.add(whenClause.getThenExpression());
			}
			public void visit(ExistsExpression existsExpression)
			{
				children.add(existsExpression.getRightExpression());
			}
			public void visit(AllComparisonExpression allComparisonExpression) 
			{ 
				children.add(allComparisonExpression.getSubSelect());
			}
			public void visit(AnyComparisonExpression anyComparisonExpression)
			{ 
				children.add(anyComparisonExpression.getSubSelect());
			}
			public void visit(Concat concat) { binary(concat); }
			public void visit(Matches matches) { binary(matches); }
			public void visit(BitwiseAnd bitwiseAnd) { binary(bitwiseAnd); }
			public void visit(BitwiseOr bitwiseOr) { binary(bitwiseOr); }
			public void visit(BitwiseXor bitwiseXor) { binary(bitwiseXor); }

		});
		return children;
	}



}
