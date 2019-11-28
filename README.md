**UBOdin's fork of JSQLParser is no longer under active development, and has been replaced by [Sparsity](https://github.com/UBOdin/sparsity)**

It also appears that JSQLParser is in active development [On GitHub](https://github.com/JSQLParser/JSqlParser)

------


# JSqlParser

A fork of [JSqlParser](http://jsqlparser.sourceforge.net).  

### This fork exists because JSqlParser ...

* ... was last updated in 2013 (and is hosted on [SourceForge](https://twitter.com/newsycombinator/status/611534051548209153)).
* ... lacks support for Java Generics (e.g., List<...>) 
* ... is designed to capture the structure, rather than the semantics of SQL.  For example, explicitly encoding Parenthesis 
      in the SQL AST makes it easier to reproduce the original SQL statement, but much much much harder to work with.
* ... has numerous confusing inconsistencies.  For example, Boolean operators have inline constructors 
      (`new AndExpression(lhs,rhs)`), but Arithmetic operators do not (There's no `new Addition(lhs,rhs)`, just 
      `new Addition()`).

Not all of these issues are fixed yet.

### Documentation

JavaDoc for JSQLParser can be found [here](http://odin.cse.buffalo.edu/software/jsqlparser/)
