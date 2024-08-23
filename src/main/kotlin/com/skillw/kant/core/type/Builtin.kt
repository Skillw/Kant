package com.skillw.kant.core.type

import com.skillw.kant.core.type.Type

object Builtin {
    val Any:Type.Any<Any> = Type.Any.of(kotlin.Any::class.java)

    val Number : Type.Any<Number>
        = Type.Any.of(kotlin.Number::class.java)

    val Double : Type.Any<Double>
        = Type.Any.of(kotlin.Double::class.java)

    val Float : Type.Any<Float>
        = Type.Any.of(kotlin.Float::class.java)

    val Long : Type.Any<Long>
        = Type.Any.of(kotlin.Long::class.java)

    val Int : Type.Any<Int>
        = Type.Any.of(kotlin.Int::class.java)

    val Short : Type.Any<Short>
        = Type.Any.of(kotlin.Short::class.java)

    val Byte : Type.Any<Byte>
        = Type.Any.of(kotlin.Byte::class.java)

    val Boolean : Type.Any<Boolean>
        = Type.Any.of(kotlin.Boolean::class.java)

    val Char : Type.Any<Char>
        = Type.Any.of(kotlin.Char::class.java)

    val String : Type.Any<String>
        = Type.Any.of(kotlin.String::class.java)

    val Unit : Type.Any<Unit>
        = Type.Any.of(kotlin.Unit::class.java)

    val Nothing : Type.Any<Nothing>
        = Type.Any.of(kotlin.Nothing::class.java)



    val Undefined =  object: Type {
        override val name: String
            get() = "Undefined"
        override fun display() = name
        override fun fullDisplay() = name
    }
}