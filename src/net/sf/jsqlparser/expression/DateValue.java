/* ================================================================
 * JSQLParser : java based sql parser 
 * ================================================================
 *
 * Project Info:  http://jsqlparser.sourceforge.net
 * Project Lead:  Leonardo Francalanci (leoonardoo@yahoo.it);
 *
 * (C) Copyright 2004, by Leonardo Francalanci
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

import java.sql.Date;
import java.text.SimpleDateFormat;


/**
 * A Date in the form {d 'yyyy-mm-dd'}
 */
public class DateValue implements Expression, PrimitiveValue {
	private Date value;
	
	public DateValue(String value) {
		this.value = Date.valueOf(value.substring(1, value.length()-1));
	}
	
	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}
  
  public int getYear() {
    return value.getYear();
  }
  public int getMonth() {
    return value.getMonth();
  }
  public int getDate() {
    return value.getDate();
  }

	public Date getValue() {
		return value;
	}

	public void setValue(Date d) {
		value = d;
	}
  
  public String toString()
  {
    return value.toString();
  }
  
  public long toLong() throws PrimitiveValue.InvalidPrimitive 
    { throw new PrimitiveValue.InvalidPrimitive(); }
  public double toDouble() throws PrimitiveValue.InvalidPrimitive 
    { throw new PrimitiveValue.InvalidPrimitive(); }
  
  public boolean equals(Object o){
    try { 
      return value.equals(((DateValue)o).value);
    } catch(ClassCastException e){
      return false;
    }
  }

}
