/**
 * Evaluate an object of Expression type
 *
 * Based on JSqlParser's Expressions.
 *
 * The main interface to this class is through eval(Expression)
 * 
 * For example, consider the tree:
 * x = Addition(Column("A"), Multiplication(LongValue(2), Column("B"))
 * 
 * If you define an eval(Column) that returns 10 for "A", and 22 for "B"
 * then eval(x) returns 54.
 */
package net.sf.jsqlparser.eval;

import java.util.*;
import java.util.regex.*;
import java.io.*;

import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.schema.*;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.*;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.sql.SQLException;


public abstract class Eval {

  enum Type { 
    LONG, DOUBLE, STRING, BOOL, DATE, TIMESTAMP, TIME
  }
  
  public Type assertNumeric(Type found)
    throws SQLException
  {
    if(found != Type.LONG && found != Type.DOUBLE){
      throw new SQLException("Typecheck Error: Found "+found+
                             ", but expected a number");
    }
    return found;
  }
  
  public Type escalateNumeric(Type lhs, Type rhs)
    throws SQLException
  {
    if(lhs == Type.DATE) {
      if(rhs == Type.DATE) { return Type.DATE; }
    }
    if(  (assertNumeric(lhs) == Type.DOUBLE)
       ||(assertNumeric(rhs) == Type.DOUBLE)){
      return Type.DOUBLE;
    } else {
      return Type.LONG;
    }
  }
  
  public PrimitiveValue missing(String element)
    throws SQLException
  {
    throw new SQLException("Incomplete expression support ("+element+")");
  }
  
  /** 
   * @param e  The expression to evaluate.
   * @return   The value of the expression
   * @throws SQLException if the expression is invalid or can not be processed
   *
   * The primary interface to Evaluator.  You will need to define 
   * The eval(Column) method to support scoped evaluation.
   *
   * You may define more Expression types by overriding this method.
   */
  public PrimitiveValue eval(Expression e)
    throws SQLException
  {
    if(e instanceof Addition){ return eval((Addition)e); }
    else if(e instanceof Division){ return eval((Division)e); }
    else if(e instanceof Multiplication){ return eval((Multiplication)e); }
    else if(e instanceof Subtraction){ return eval((Subtraction)e); }
    else if(e instanceof AndExpression){ return eval((AndExpression)e); }
    else if(e instanceof OrExpression){ return eval((OrExpression)e); }
    else if(e instanceof EqualsTo){ return eval((EqualsTo)e); }
    else if(e instanceof NotEqualsTo){ return eval((NotEqualsTo)e); }
    else if(e instanceof GreaterThan){ return eval((GreaterThan)e); }
    else if(e instanceof GreaterThanEquals){ return eval((GreaterThanEquals)e); }
    else if(e instanceof MinorThan){ return eval((MinorThan)e); }
    else if(e instanceof MinorThanEquals){ return eval((MinorThanEquals)e); }
    else if(e instanceof DateValue){ return eval((DateValue)e); }
    else if(e instanceof DoubleValue){ return eval((DoubleValue)e); }
    else if(e instanceof LongValue){ return eval((LongValue)e); }
    else if(e instanceof StringValue){ return eval((StringValue)e); }
    else if(e instanceof TimestampValue){ return eval((TimestampValue)e); }
    else if(e instanceof TimeValue){ return eval((TimeValue)e); }
    else if(e instanceof CaseExpression){ return eval((CaseExpression)e); }
    else if(e instanceof Column){ return eval((Column)e); }
    else if(e instanceof WhenClause){ return eval((WhenClause)e); }
    else if(e instanceof AllComparisonExpression){ return eval((AllComparisonExpression)e); }
    else if(e instanceof AnyComparisonExpression){ return eval((AnyComparisonExpression)e); }
    else if(e instanceof Between){ return eval((Between)e); }
    else if(e instanceof ExistsExpression){ return eval((ExistsExpression)e); }
    else if(e instanceof InExpression){ return eval((InExpression)e); }
    else if(e instanceof LikeExpression){ return eval((LikeExpression)e); }
    else if(e instanceof Matches){ return eval((Matches)e); }
    else if(e instanceof BitwiseXor){ return eval((BitwiseXor)e); }
    else if(e instanceof BitwiseOr){ return eval((BitwiseOr)e); }
    else if(e instanceof BitwiseAnd){ return eval((BitwiseAnd)e); }
    else if(e instanceof Concat){ return eval((Concat)e); }
    else if(e instanceof Function){ return eval((Function)e); }
    else if(e instanceof InverseExpression){ return eval((InverseExpression)e); }
    else if(e instanceof IsNullExpression){ return eval((IsNullExpression)e); }
    else if(e instanceof JdbcParameter){ return eval((JdbcParameter)e); }
    else if(e instanceof NullValue){ return eval((NullValue)e); }
    throw new SQLException("Invalid operator: "+e);
  }
  
  public Type getType(PrimitiveValue e)
    throws SQLException
  {
    if(e instanceof LongValue) { return Type.LONG; }
    if(e instanceof DoubleValue) { return Type.DOUBLE; }
    if(e instanceof StringValue) { return Type.STRING; }
    if(e instanceof DateValue) { return Type.DATE; }
    if(e instanceof TimeValue) { return Type.TIME; }
    if(e instanceof TimestampValue) { return Type.TIMESTAMP; }
    if(e instanceof BooleanValue) { return Type.BOOL; }
    return null;
  }
  

  public PrimitiveValue arith(BinaryExpression e, ArithOp op)
    throws SQLException
  {
    try {
      PrimitiveValue lhs = eval(e.getLeftExpression());
      PrimitiveValue rhs = eval(e.getRightExpression());
      if(lhs == null || rhs == null) return null;
      
      switch(escalateNumeric(getType(lhs), getType(rhs))){
        case DOUBLE:
          return new DoubleValue(op.op(lhs.toDouble(), rhs.toDouble()));
        case LONG:
          return new LongValue(op.op(lhs.toLong(), rhs.toLong()));
        case DATE:
          throw new SQLException("Arithmetic on dates");
      }
    } catch(PrimitiveValue.InvalidPrimitive ex) { 
      throw new SQLException("Invalid leaf value", ex);
    }
    throw new SQLException("Invalid type escalation");
  }
  public PrimitiveValue cmp(BinaryExpression e, CmpOp op)
    throws SQLException
  {
    try {
      PrimitiveValue lhs = eval(e.getLeftExpression());
      PrimitiveValue rhs = eval(e.getRightExpression());
      if(lhs == null || rhs == null) return null;
      boolean ret;
    
      switch(escalateNumeric(getType(lhs), getType(rhs))){
        case DOUBLE:
          ret = op.op(lhs.toDouble(), rhs.toDouble());
          break;
        case LONG:
          ret = op.op(lhs.toLong(), rhs.toLong());
          break;
        case DATE: {
          DateValue dlhs = (DateValue)lhs,
                    drhs = (DateValue)rhs;
          ret = op.op(
            dlhs.getYear()*10000+
              dlhs.getMonth()*100+
              dlhs.getDate(),
            drhs.getYear()*10000+
              drhs.getMonth()*100+
              drhs.getDate()
          );
          }
          break;
        default: 
          throw new SQLException("Invalid type escalation");
      }
      return ret ? BooleanValue.TRUE : BooleanValue.FALSE;
    } catch(PrimitiveValue.InvalidPrimitive ex) { 
      throw new SQLException("Invalid leaf value", ex);
    }
  }
  public PrimitiveValue bool(BinaryExpression e, BoolOp op)
    throws SQLException
  {
    BooleanValue lhs = (BooleanValue)eval(e.getLeftExpression());
    BooleanValue rhs = (BooleanValue)eval(e.getRightExpression());
    if(lhs == null || rhs == null) return null;
    
    return op.op(lhs.getValue(), rhs.getValue()) ? 
      BooleanValue.TRUE : BooleanValue.FALSE;
  }
    
  public PrimitiveValue eval(Addition a)
    throws SQLException
  { 
    return arith(a, new ArithOp() { 
      public long op(long a, long b) { return a + b; }
      public double op(double a, double b) { return a + b; }
    });
  }
  public PrimitiveValue eval(Division a)
    throws SQLException
  { 
    return arith(a, new ArithOp() { 
      public long op(long a, long b) { return a / b; }
      public double op(double a, double b) { return a / b; }
    });
  }
  public PrimitiveValue eval(Multiplication a) 
    throws SQLException
  { 
    return arith(a, new ArithOp() { 
      public long op(long a, long b) { return a * b; }
      public double op(double a, double b) { return a * b; }
    });
  }
  public PrimitiveValue eval(Subtraction a) 
    throws SQLException
  { 
    return arith(a, new ArithOp() { 
      public long op(long a, long b) { return a - b; }
      public double op(double a, double b) { return a - b; }
    });
  }

  public PrimitiveValue eval(AndExpression a) 
    throws SQLException
  { 
    return bool(a, new BoolOp(){ public boolean op(boolean a, boolean b) { return a && b; } }); 
  }
  public PrimitiveValue eval(OrExpression a) 
    throws SQLException
  { 
    return bool(a, new BoolOp(){ public boolean op(boolean a, boolean b) { return a || b; } }); 
  }
  
  public PrimitiveValue eval(EqualsTo a) 
    throws SQLException
  { 
    PrimitiveValue lhs = eval(a.getLeftExpression());
    PrimitiveValue rhs = eval(a.getRightExpression());
if(lhs == null || rhs == null) return null;
    return lhs.equals(rhs) ? BooleanValue.TRUE : BooleanValue.FALSE;
  }
  public PrimitiveValue eval(NotEqualsTo a) 
    throws SQLException
  { 
    PrimitiveValue lhs = eval(a.getLeftExpression());
    PrimitiveValue rhs = eval(a.getRightExpression());
if(lhs == null || rhs == null) return null;
    return lhs.equals(rhs) ? BooleanValue.FALSE : BooleanValue.TRUE;
  }
  public PrimitiveValue eval(GreaterThan a) 
    throws SQLException
  { 
    return cmp(a, new CmpOp() { 
      public boolean op(long a, long b){ return a > b; }
      public boolean op(double a, double b){ return a > b; }
      public String toString() { return ">"; }
    });
  }
  public PrimitiveValue eval(GreaterThanEquals a) 
    throws SQLException
  { 
    return cmp(a, new CmpOp() { 
      public boolean op(long a, long b){ return a >= b; }
      public boolean op(double a, double b){ return a >= b; }
      public String toString() { return ">="; }
    });
  }
  public PrimitiveValue eval(MinorThan a) 
    throws SQLException
  { 
    return cmp(a, new CmpOp() { 
      public boolean op(long a, long b){ return a < b; }
      public boolean op(double a, double b){ return a < b; }
      public String toString() { return "<"; }
    });
  }
  public PrimitiveValue eval(MinorThanEquals a) 
    throws SQLException
  { 
    return cmp(a, new CmpOp() { 
      public boolean op(long a, long b){ return a <= b; }
      public boolean op(double a, double b){ return a <= b; }
      public String toString() { return "<="; }
    });
  }

  public PrimitiveValue eval(DateValue v) {
    return v;
  }
  public PrimitiveValue eval(DoubleValue v) { 
    return v; 
  }
  public PrimitiveValue eval(LongValue v) { 
    return v;
  }
  public PrimitiveValue eval(StringValue v) { 
    return v;
  }
  public PrimitiveValue eval(TimestampValue v) { 
    return v;
  }
  public PrimitiveValue eval(TimeValue v) { 
    return v;
  }  
  
  public PrimitiveValue eval(CaseExpression c) 
    throws SQLException
  { 
    if(c.getSwitchExpression() == null){
      for(Object ow : c.getWhenClauses()) { 
        WhenClause w = ((WhenClause)ow);
        BooleanValue cmp = (BooleanValue)eval(w.getWhenExpression());
        if(cmp.getValue()){
          return eval(w.getThenExpression());
        }
      }
    } else {
      PrimitiveValue sw = eval(c.getSwitchExpression());
      for(Object ow : c.getWhenClauses()) { 
        WhenClause w = ((WhenClause)ow);
        BooleanValue cmp = (BooleanValue)eval(w.getWhenExpression());
        if(sw.equals(cmp)){
          return eval(w.getThenExpression());
        }
      }
    }
    if(c.getElseExpression() != null){
      return eval(c.getElseExpression());
    }
    throw new SQLException("Unhandled CASE statement");
  }

  /** 
   * @param col  The Column to provide a value for in the current scope.
   * @return     The value of col in the current scope.
   * @throws SQLException when something goes wrong (e.g., col isn't in scope)
   * 
   * Override this method to define values for columns.  These should be
   * bound to values from the row that the current expression is being
   * evaluated in the context of.  For example, given the tuple
   * [ A: 100, B: 212.0 ] and the expression R.A + (2 * B)
   * this function will be be called twice: Once with Column(Table(null,R), A)
   * and once with Column(Table(null, null), B).  The first call (for A) should
   * return LongValue(100), and the second call (for B) should return 
   * DoubleValue(212.0).  The overall eval method will then return 
   * DoubleValue(514.0).
   */
  public abstract PrimitiveValue eval(Column col) throws SQLException;
  
  public PrimitiveValue eval(WhenClause whenClause) throws SQLException
    { throw new SQLException("Stand-alone WhenClause"); }
  
  /** 
   * @param all The operator being evaluated
   * @return true if LHS (op) RHS[i] for all 'i's
   * @throws SQLException when something goes wrong
   *
   * X &gt; ALL (SELECT ...)
   * 
   * As a nested subquery, this function is too tightly coupled to the inner
   * workings of your database system.  You will need to eventually implement
   * a version of this method, or rewrite queries to remove it from 
   * consideration.
   */
  public PrimitiveValue eval(AllComparisonExpression all) throws SQLException
    { return missing("AllComparisonExpression"); }
  /** 
   * @param any The operator being evaluated
   * @return true if LHS (op) RHS[i] for at least one 'i'
   * @throws SQLException when something goes wrong
   *
   * X &gt; ANY (SELECT ...)
   * 
   * As a nested subquery, this function is too tightly coupled to the inner
   * workings of your database system.  You will need to eventually implement
   * a version of this method, or rewrite queries to remove it from 
   * consideration.
   */
  public PrimitiveValue eval(AnyComparisonExpression any) throws SQLException
    { return missing("AnyComparisonExpression"); }
  /** 
   * @param exists The operator being evaluated
   * @return true if the list provided as an argument has contains at least one record
   * @throws SQLException when something goes wrong
   * 
   * [NOT] EXISTS (SELECT ...)
   * 
   * As a nested subquery, this function is too tightly coupled to the inner
   * workings of your database system.  You will need to eventually implement
   * a version of this method, or rewrite queries to remove it from 
   * consideration.
   */
  public PrimitiveValue eval(ExistsExpression exists) throws SQLException
    { return missing("ExistsExpression"); }
  /** 
   * @param in The operator being evaluated
   * @return true if the list in the RHS argument has contains at least one record equal to the LHS argument
   * @throws SQLException when something goes wrong
   * 
   * X [NOT] IN (SELECT ...)
   * 
   * As a nested subquery, this function is too tightly coupled to the inner
   * workings of your database system.  You will need to eventually implement
   * a version of this method, or rewrite queries to remove it from 
   * consideration.
   */
  public PrimitiveValue eval(InExpression in) throws SQLException
    { return missing("InExpression"); }
  public PrimitiveValue eval(Between between) throws SQLException
    { return eval(new AndExpression(
        new GreaterThanEquals(
          between.getLeftExpression(),
          between.getBetweenExpressionStart()
        ),
        new MinorThan(
          between.getLeftExpression(),
          between.getBetweenExpressionEnd()
        )
      ));
    }
  public PrimitiveValue eval(LikeExpression like) throws SQLException
  { 
    String pattern = eval(like.getRightExpression()).toString().replaceAll("%", ".*");
    String cmp = eval(like.getLeftExpression()).toString();
    // System.out.println("/"+pattern+"/ =~ "+cmp);
    return Pattern.matches(pattern, cmp) ? BooleanValue.TRUE : BooleanValue.FALSE;
  }
  public PrimitiveValue eval(Matches matches) throws SQLException
    { return missing("Matches"); }

  public PrimitiveValue eval(BitwiseXor a) throws SQLException
    { return missing("BitwiseXor"); }
  public PrimitiveValue eval(BitwiseOr a) throws SQLException
    { return missing("BitwiseOr"); }
  public PrimitiveValue eval(BitwiseAnd a) throws SQLException
    { return missing("BitwiseAnd"); }

  public PrimitiveValue eval(Concat a) throws SQLException
    { return missing("Concat"); }
  public PrimitiveValue eval(Function function) 
    throws SQLException
  { 
    String fn = function.getName().toUpperCase();
    if("DATE".equals(fn)){
      List args = function.getParameters().getExpressions();
      if(args.size() != 1){
        throw new SQLException("DATE() takes exactly one argument");
      }
      return new DateValue(eval((Expression)args.get(0)).toString());
    }
    
    return missing("Function:"+fn);
  }
  public PrimitiveValue eval(InverseExpression inverse) throws SQLException
    { return missing("InverseExpression"); }
  public PrimitiveValue eval(IsNullExpression isNull) throws SQLException
    { return missing("IsNull"); }
  public PrimitiveValue eval(JdbcParameter jdbcParameter) throws SQLException
    { return missing("JdbcParameter"); }
  public PrimitiveValue eval(NullValue nullValue) throws SQLException
    { return missing("NullValue"); }
  
  public static abstract class ArithOp {
    public abstract long op(long a, long b);
    public abstract double op(double a, double b);
  }
  public static abstract class CmpOp {
    public abstract boolean op(long a, long b);
    public abstract boolean op(double a, double b);
  }
  public static abstract class BoolOp {
    public abstract boolean op(boolean a, boolean b);
  }

  public static class ScopeException extends SQLException {
    public ScopeException(String msg) { super(msg); }
  }
  
  public static void main(String[] args)
    throws Exception
  {
    if(args.length < 1){ args = new String[] { "-" }; }
    for(String file : args){
      Reader input;
      if(file.equals("-")){
        input = new InputStreamReader(System.in);
      } else {
        input = new FileReader(file);
      }
      CCJSqlParser parser = new CCJSqlParser(input);
      Eval eval = 
        new Eval() {
          public PrimitiveValue eval(Column c){ return new LongValue(0); }
        };
      Expression e = parser.SimpleExpression();
      PrimitiveValue ret = eval.eval(e);
      System.out.println(ret.toString());
      
    }
  }
  
}