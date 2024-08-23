package com.skillw.kant.core.eval

import com.skillw.kant.core.type.Builtin
import com.skillw.kant.core.util.Span
import com.skillw.kant.core.type.Type
interface Node {
    val span: Span
    val type:Type
}
class Null(override val span: Span): Node {
    override val type: Type = Builtin.Any
}
sealed class Literal<T : Any>(
    override val type: Type.Any<T>,
    val value: T,
    override val span: Span
): Node{
    class Double(value: kotlin.Double, span: Span): Literal<kotlin.Double>(Builtin.Double, value, span)
    class Float(value: kotlin.Float, span: Span): Literal<kotlin.Float>(Builtin.Float, value, span)
    class Long(value: kotlin.Long, span: Span): Literal<kotlin.Long>(Builtin.Long, value, span)
    class Int(value: kotlin.Int, span: Span): Literal<kotlin.Int>(Builtin.Int, value, span)
    class Short(value: kotlin.Short, span: Span): Literal<kotlin.Short>(Builtin.Short, value, span)
    class Byte(value: kotlin.Byte, span: Span): Literal<kotlin.Byte>(Builtin.Byte, value, span)
    class Boolean(value: kotlin.Boolean, span: Span): Literal<kotlin.Boolean>(Builtin.Boolean, value, span)
    class String(value: kotlin.String, span: Span): Literal<kotlin.String>(Builtin.String, value, span)
    class Char(value: kotlin.Char, span: Span): Literal<kotlin.Char>(Builtin.Char, value, span)
    class Unit(span: Span): Literal<kotlin.Unit>(Builtin.Unit, Unit, span)
}

