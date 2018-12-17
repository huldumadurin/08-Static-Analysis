package dk.cphbusiness.states

interface State {
    fun unite(other: State): State = UnionState(this, other)
    fun intersect(other: State): State = InsersectionState(this, other)
    operator fun set(name: String, range: Range)
    operator fun get(name: String): Range?
    }

class MappedState : State {
    val variables = mutableMapOf<String, Range>()

    override fun set(name: String, range: Range) { variables[name] = range }
    // operator fun get(name: String): Range { return variables[name] }
    override fun get(name: String): Range? = variables[name]

    override fun toString(): String {
        return variables
            .entries
            .map { "${it.key}: ${it.value}"}
            .joinToString(separator = "; ", prefix = "[", postfix = "]")
        }

    }

    
class UnionState(val first: State, val second: State) : State {
    override fun get(name: String): Range? {
        TODO("not implemented")
    }

    override fun set(name: String, range: Range) {
        TODO("not implemented")
    }
}

class InsersectionState(val first: State, val second: State) : State {
    override fun get(name: String): Range? {
        TODO("not implemented")
    }

    override fun set(name: String, range: Range) {
        TODO("not implemented")
    }
}