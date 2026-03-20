package org.czeal.urireference

object ObjectUtils {

    fun hash(vararg values: Any?): Int {
        return hashCode(values.toList().toTypedArray())
    }

    private fun hashCode(a: Array<Any?>?): Int {
        if (a == null) return 0

        var result = 1

        for (element in a) result = 31 * result + (element?.hashCode() ?: 0)

        return result
    }

}