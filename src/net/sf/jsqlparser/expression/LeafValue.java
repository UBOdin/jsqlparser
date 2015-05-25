package net.sf.jsqlparser.expression;


/** 
 * A terminal expression that can not be evaluated further (e.g., a Number or String)
 */

public interface LeafValue {
  
  public class InvalidLeaf extends Exception {}
  
  public long toLong() throws InvalidLeaf;
  public double toDouble() throws InvalidLeaf;  
  
}