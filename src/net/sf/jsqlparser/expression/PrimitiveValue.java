package net.sf.jsqlparser.expression;

import java.io.Serializable;
import java.sql.SQLException;

import net.sf.jsqlparser.schema.PrimitiveType;
/** 
 * A terminal expression that can not be evaluated further (e.g., a Number or String)
 */

public interface PrimitiveValue extends Serializable, Expression {
  
  public class InvalidPrimitive extends SQLException {}
  
  public long toLong() throws InvalidPrimitive;
  public double toDouble() throws InvalidPrimitive;
  public boolean toBool() throws InvalidPrimitive;

  public PrimitiveType getType();
  
}