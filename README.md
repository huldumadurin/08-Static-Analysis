# VSSL AST program in Kotlin

A kotlin program that creates an Abstract Syntax Tree, for a piece of code that utilizes a subset of VSSL.

Tries to predict possible program states based on the AST.

## Output

```
DEF X : INTEGER

IF Y < 10 THEN
{
LET X = 100
}

LET Y = Y + 10

IF Y >= 20 THEN
{
LET X = 4711
}

[Y: {...}; X: {?, -2147478837...}]
```