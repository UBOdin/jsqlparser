package net.sf.jsqlparser.expression;


/** 
 * A terminal expression that can not be evaluated further (e.g., a Number or String)
 */

public interface PrimitiveValue {
  
  public class InvalidPrimitive extends Exception {}
  
  public long toLong() throws InvalidPrimitive;
  public double toDouble() throws InvalidPrimitive;  
  
}