package dk.cphbusiness.vssl

import dk.cphbusiness.states.Range
import dk.cphbusiness.states.State


interface Expression

interface IntegerExpression : Expression {
    fun getRange(precondition: State) : Range
    }
   
interface BooleanExpression : Expression

class Constant(val value: Int) : IntegerExpression {
    override fun getRange(precondition: State) : Range {
        //Adding one to ensure Range has one value (Assuming "[floor...roof[", inclusive to exclusive)
        return Range(value, value, false)
        }

    override fun toString() = "$value"
    }

class Variable(val name: String) : IntegerExpression {
    override fun getRange(precondition: State) : Range {
        val rangeVal = precondition.get(name)
        if (rangeVal == null) return Range(Int.MIN_VALUE, Int.MAX_VALUE, true) else return rangeVal
        }

    override fun toString() = "$name"
    }

class PlusExpression(
        val left: IntegerExpression,
        val right: IntegerExpression
        ) : IntegerExpression {
    override fun getRange(precondition: State) : Range {
        return left.getRange(precondition) + right.getRange(precondition)
        }

    override fun toString() = "$left + $right"
    }

class LessThanExpression(
        val left: IntegerExpression,
        val right: IntegerExpression
        ) : BooleanExpression {
    override fun toString() = "$left < $right"
    }
    
class GreaterThanExpression(
        val left: IntegerExpression,
        val right: IntegerExpression
        ) : BooleanExpression {
    override fun toString() = "$left > $right"
    }

class LessThanOrEqualsExpression(
        val left: IntegerExpression,
        val right: IntegerExpression
        ) : BooleanExpression {
    override fun toString() = "$left <= $right"
    }
    
class GreaterThanOrEqualsExpression(
        val left: IntegerExpression,
        val right: IntegerExpression
        ) : BooleanExpression {
    override fun toString() = "$left >= $right"
    }

interface Statement {
    fun analyse(precondition: State): State
    }

class DefinitionStatement(val name: String) : Statement {
    override fun analyse(precondition: State): State {
        precondition[name] = Range(Int.MIN_VALUE, Int.MAX_VALUE, true)
        return precondition
        }

    override fun toString() = "DEF $name : INTEGER\n"
    }

class AssignmentStatement(
        val name: String,
        val value: IntegerExpression
        ) : Statement {
    
    override fun analyse(precondition: State): State {
        var condition = precondition
        val oldValue = precondition.get(name)
        
        condition.set(name, if (oldValue == null) value.getRange(precondition) else value.getRange(precondition) + oldValue)
        return condition
        }

    override fun toString() = "LET $name = $value\n"
    }

open class Block(vararg val statements: Statement) : Statement {
    
    override fun analyse(precondition: State): State {
        var condition = precondition
        for (statement in statements) {
            condition = statement.analyse(condition)
            }
        return condition
        }

    override fun toString() =
        statements.joinToString(separator = "\n", prefix = "{\n", postfix = "}\n")
        }

class IfStatement(
        val predicate: BooleanExpression,
        val thenBlock: Block
        ) : Statement {
        
    override fun analyse(precondition: State): State {
        var condition = precondition

        //#Should path depend on current state or take all possibilities into account?
        for (statement in thenBlock.statements) {
            condition = statement.analyse(condition)
            }
        return condition
        }

    override fun toString() = """
        |IF $predicate THEN
        |$thenBlock
        """.trimMargin()
    }

class IfElseStatement(
        val predicate: BooleanExpression,
        val thenBlock: Block,
        val elseBlock: Block
        ) : Statement {
    
    override fun analyse(precondition: State): State {
        var condition = precondition
        
        //#Should path depend on current state or take all possibilities into account?
        for (statement in thenBlock.statements) {
            condition = statement.analyse(condition)
            }

        for (statement in elseBlock.statements) {
            condition = statement.analyse(condition)
            }
        return condition
        }
    }

/**
 * Extends Block, by overriding toString with out the { }'s
 */
class Program (vararg statements: Statement) : Block(* statements) {

    override fun toString() =
        statements.joinToString(separator = "\n")

    }
