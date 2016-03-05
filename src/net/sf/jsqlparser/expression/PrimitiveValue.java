package net.sf.jsqlparser.expression;

import java.io.Serializable;
import java.sql.SQLException;

import net.sf.jsqlparser.schema.PrimitiveType;
/** 
 * A terminal expression that can not be evaluated further (e.g., a Number or String)
 */

public interface PrimitiveValue extends Serializable, Expression {
  
  public class InvalidPrimitive extends SQLException {}
  
  /**
   * The long value of this primitive
   * @return    The long value of this primitive if it is a LongPrimitive
   * @throws     InvalidPrimitive otherwise
   */
  public long toLong() throws InvalidPrimitive;
  /**
   * The double value of this primitive
   * @return    The double value of this primitive if it is a LongPrimitive or DoublePrimitive
   * @throws     InvalidPrimitive otherwise
   */
  public double toDouble() throws InvalidPrimitive;
  /**
   * The boolean value of this primitive
   * @return    The boolean value of this primitive if it is a BoolPrimitive
   * @throws     InvalidPrimitive otherwise
   */
  public boolean toBool() throws InvalidPrimitive;
  /**
   * An unescaped string encoding of this primitive value. (toString() returns an escaped string)
   * @return    The unescaped string encoding of this primitive value
   */
  public String toRawString();

  public PrimitiveType getType();
  
}