package com.skillw.kant.core.type
data class Field(val owner: Type.Any<*>, override val name:String, val type: Type):Display{
    companion object{
        fun of(
            field:java.lang.reflect.Field,
            type: Type.Any<*> = Type.Any.of(field.declaringClass),
            generics:Map<String, Type> = emptyMap()
        ) = Field(type, field.name, Type.of(field.genericType,generics))
    }

    override fun fullDisplay() = "field $name : ${type.display()}"
}
data class Method(val owner: Type.Any<*>, override val name:String, val type: Type.Function):Display{
    companion object{
        fun of(
            method:java.lang.reflect.Method,
            type: Type.Any<*> = Type.Any.of(method.declaringClass),
            generics:Map<String, Type> = emptyMap()
        ) = Method(type, method.name, Type.Function.of(method.typeParameters,method.genericParameterTypes,method.genericReturnType,generics))
    }

    override fun fullDisplay() = "method $name : ${type.display()}"
}
data class Constructor(val owner: Type, val type: Type.Function):Display{
    companion object{
        fun of(
            constructor:java.lang.reflect.Constructor<*>,
            type: Type = Type.Any.of(constructor.declaringClass),
            generics:Map<String, Type> = emptyMap()
        ) = Constructor(type, Type.Function.of(emptyArray(),constructor.genericParameterTypes,type,generics))
    }

    override val name: String
        get() = "${owner.name} Constructor"
    override fun fullDisplay() = "constructor : ${type.display()}"
}
