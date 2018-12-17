package dk.cphbusiness

import dk.cphbusiness.states.MappedState
import dk.cphbusiness.states.Range
import dk.cphbusiness.states.State
import dk.cphbusiness.vssl.*

fun firstTest() {
    val r1 = Range(1, 100, false)
    val r2 = Range(-5, Int.MAX_VALUE, true)
    val r3 = Range(0, 0, true)

    println(r1)
    println(r2)
    println(r3)

    val r4 = r1 + r2
    println(r4)

    val s1 = MappedState()
    s1["X"] = r1
    s1["Y"] = r2
    println(s1)
}
fun main(args: Array<String>) {
    val ast = Program(
        DefinitionStatement("X"),
        IfStatement(
            LessThanExpression(Variable("Y"), Constant(10)),
            Block(AssignmentStatement("X", Constant(100)))
        ),
        AssignmentStatement("Y", PlusExpression(Variable("Y"), Constant(10))),
        IfStatement(
            GreaterThanOrEqualsExpression(Variable("Y"), Constant(20)),
            Block(AssignmentStatement("X", Constant(4711)))
        )
    )
    println(ast)
    
    val pre = MappedState() // some more
    pre.set("Y", Range(Int.MIN_VALUE, Int.MAX_VALUE, false))
    val post = ast.analyse(pre)
    println(post)
}
