# Generate Normal Forms for Expressions in Propositional Logic

Exemplary Java implementation of a _recursive decent parser_, an _abstract syntax tree (AST) data structure_ and _structural recursive AST transformations_ over this data structure.

## Building

The maven project can be build with

```
mvn compile
```

which compiles the Java code.

Without maven you can compile the Java code manually as follows:

```
mkdir -p target/classes
javac -d target/classes src/main/java/*.java src/main/java/**/*.java
```

## Running

You can run the application and pass expressions of propositional logic as command line arguments. For example

```
java -cp target/classes Main (a||b) -> (c && !(d&&e))
```

will produce

```
Input:
a || b -> c && !(d && e)

Desugared:
!(a || b) || c && !(d && e)

Negation Normal Form:
!a && !b || c && (!d || !e)

Disjunctive Normal Form:
!a && !b || c && !d || c && !e
```
