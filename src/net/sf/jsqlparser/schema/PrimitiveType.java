package net.sf.jsqlparser.schema;

import java.sql.SQLException;
import net.sf.jsqlparser.expression.*;

public enum PrimitiveType { 
	LONG, DOUBLE, STRING, BOOL, DATE, TIMESTAMP, TIME;

	public static PrimitiveType fromString(String in){
		switch(in.toUpperCase())
		{
			case "VARCHAR":
			case "CHAR":
			case "STRING":
				return STRING;
			case "LONG":
			case "INTEGER":
				return LONG;
			case "DOUBLE":
			case "FLOAT":
			case "DECIMAL":
				return DOUBLE;
			case "BOOL":
				return BOOL;
			case "DATE":
				return DATE;
			case "TIME":
			case "DATETIME":
				return TIME;
			case "TIMESTAMP":
				return TIMESTAMP;
		}
		return null;
	}

}