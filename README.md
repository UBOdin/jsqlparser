# JSqlParser

A fork of [JSqlParser](http://jsqlparser.sourceforge.net).  

### This fork exists because JSqlParser ...

* ... was last updated in 2013 (and is hosted on [SourceForge](https://twitter.com/newsycombinator/status/611534051548209153)).
* ... lacks support for Java Generics (e.g., List<...>) 
* ... is designed to capture the structure, rather than the semantics of SQL.  For example, explicitly encoding Parenthesis 
      in the SQL AST makes it easier to reproduce the original SQL statement, but much much much harder to work with.
* ... has numerous confusing inconsistencies.  For example, Boolean operators have inline constructors 
      (`new AndExpression(lhs,rhs)`), but Arithmetic operators do not (There's no `new Addition(lhs,rhs)``, just 
      `new Addition()`).
