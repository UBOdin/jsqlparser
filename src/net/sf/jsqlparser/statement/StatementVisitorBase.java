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

package net.sf.jsqlparser.statement;

import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

public abstract class StatementVisitorBase implements StatementVisitor {

	java.io.PrintStream verboseFailure;
	public StatementVisitorBase() { this(null); }
	public StatementVisitorBase(java.io.PrintStream verboseFailure) 
		{ this.verboseFailure = verboseFailure; }

	private void unhandledStatement(String f) 
	{ 
		if(verboseFailure != null){ 
			verboseFailure.println("Unhandled: Statement-" + f);
		}
	}

	public void visit(Select select) { unhandledStatement("SELECT"); }
	public void visit(Delete delete) { unhandledStatement("DELETE"); }
	public void visit(Update update) { unhandledStatement("UPDATE"); }
	public void visit(Insert insert) { unhandledStatement("INSERT"); }
	public void visit(Replace replace) { unhandledStatement("REPLACE"); }
	public void visit(Drop drop) { unhandledStatement("DROP"); }
	public void visit(Truncate truncate) { unhandledStatement("TRUNCATE"); }
	public void visit(CreateTable createTable) { unhandledStatement("CREATETABLE"); }

}
