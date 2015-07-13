/* ================================================================
 * JSQLParser : java based sql parser 
 * ================================================================
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

package net.sf.jsqlparser.statement.select;

public abstract class SelectVisitorBase implements SelectVisitor {
	java.io.PrintStream verboseFailure;
	boolean recurDownUnions;
	public SelectVisitorBase() { this(null, false); }
	public SelectVisitorBase(java.io.PrintStream verboseFailure, boolean recurDownUnions) 
	{ 
		this.verboseFailure = verboseFailure; 
		this.recurDownUnions = recurDownUnions;
	}

	private void unhandledSelect(String f) 
	{ 
		if(verboseFailure != null){ 
			verboseFailure.println("Unhandled: Select-" + f);
		}
	}

	public void visit(PlainSelect plainSelect) { unhandledSelect("PlainSelect"); }
	public void visit(Union union) { 
		unhandledSelect("UNION"); 
		if(recurDownUnions){
			for(PlainSelect ps : union.getPlainSelects()){ visit(ps); }
		}
	}
}
