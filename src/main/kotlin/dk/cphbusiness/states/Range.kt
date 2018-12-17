package dk.cphbusiness.states

import kotlin.math.min
import kotlin.math.max

class Range(val floor: Int, val roof: Int, val hasUndefined: Boolean) {
    // private final int floor;
    // public int getFloor() { return floor; }

    val hasValues = floor <= roof

    operator fun plus(other: Range): Range {
        val f = max(
                this.floor.toLong() + other.floor.toLong(),
                Int.MIN_VALUE.toLong()
                ).toInt()
        val r = min(
                this.roof.toLong() + other.roof.toLong(),
                Int.MAX_VALUE.toLong()
                ).toInt()

        return Range(f, r, this.hasUndefined || other.hasUndefined)
        }

    private val floorText = if (floor == Int.MIN_VALUE) "" else "$floor"
    private val roofText = if (roof == Int.MAX_VALUE) "" else "${roof - 1}"

    override fun toString(): String {
        if (hasUndefined) return if (hasValues) "{?, $floorText...$roofText}" else "{?}"
        else return if (hasValues) "{$floorText...$roofText}" else "{}"
        }

    }
