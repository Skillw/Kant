package com.skillw.kant.core.type

import com.skillw.kant.core.util.unsafeLazy
import java.lang.reflect.Method
import java.lang.reflect.TypeVariable

interface Type: Display {

    data class Function(val generics:List<Generic>, val types:Array<Type>): Type {
        override val name: String
            get() = display()
        override fun fullDisplay() =
            (if (generics.isNotEmpty()) "forall " + generics.joinToString(separator = ", "){it.fullDisplay()} + "  .  " else "")+ types.joinToString(" -> ","(",")", transform = Display::display)

        override fun equals(other: kotlin.Any?): Boolean {
            if (this === other) return true
            if (other !is Function) return false

            if (!types.contentEquals(other.types)) return false

            return true
        }

        override fun hashCode() = types.contentHashCode()
        companion object{
            fun of(
                generics:Array<TypeVariable<Method>>,
                params:Array<java.lang.reflect.Type>,
                returnType: kotlin.Any,
                genericsMap:Map<String, Type> = emptyMap()
            ) = Function(
                generics.map{ of(it) as Generic },
                params.map{ of(it,genericsMap) }
                    .toMutableList()
                    .apply {
                        when (returnType) {
                            is Type -> add(returnType)
                            is java.lang.reflect.Type -> add(of(returnType, genericsMap))
                            else -> throw IllegalArgumentException("Unsupported return type: $returnType")
                        }
                    }
                    .toTypedArray()
            )
        }
    }
    class Any<T:kotlin.Any> private constructor(val clazz: Class<T>, generics:List<Type> = emptyList()): Type {
        val typeParams = clazz.typeParameters.map{ of(it) }
        val generics:List<Type> = if (generics.isEmpty()) typeParams else generics.apply {
            if(typeParams.size != generics.size)
                throw IllegalArgumentException("Generics size mismatch: Expect ${typeParams.size} but got ${generics.size}")
        }


        val structure by unsafeLazy { Structure.of(clazz, this) }

        companion object{
             val cache = mutableMapOf<Class<*>, MutableMap<String, Any<*>>>()
            fun<T : kotlin.Any> of(clazz: Class<T>, generics:List<Type> = emptyList()): Any<T> {
                val key = if(generics.isNotEmpty()) generics.joinToString(",","<",">", transform = Display::display) else ""
               return  cache.getOrPut(clazz){ mutableMapOf() }.getOrPut(key){
                   Any(clazz,generics)
                } as Any<T>
            }
        }

        override val name: String
            get() = clazz.name
        override fun fullDisplay() = clazz.name + if(generics.isNotEmpty()) generics.joinToString(",","<",">"){it.fullDisplay()} else ""
        override fun display() = clazz.name + if(generics.isNotEmpty()) generics.joinToString(",","<",">", transform = Display::display) else ""
    }


    class Generic private constructor(override val name:String, val uppers:List<Type> = emptyList()): Type {
        companion object{
            private val cache = mutableMapOf<String, Generic>()
            fun of(name:String,uppers:List<Type> = emptyList()) = cache.getOrPut(name){ Generic(name,uppers) }
            fun of(type: TypeVariable<*>) = of(type.name,type.bounds.map{ of(it) })
        }

        override fun display() = name
        override fun fullDisplay() = name + " : " + if(uppers.isNotEmpty()) uppers.joinToString(", ", transform = Display::display) else ""

    }
    class GenericArray private constructor(val type: Type): Type {
        companion object{
            private val cache = mutableMapOf<Type, GenericArray>()
            fun of(type: Type) = cache.getOrPut(type){ GenericArray(type) }
        }

        override val name: String
            get() = display()

        override fun fullDisplay() = type.name + "[]"
    }
    class Wildcard private constructor(val uppers:List<Type>, val lowers:List<Type>): Type {
        companion object{
            private val cache = mutableMapOf<Pair<List<Type>,List<Type>>, Wildcard>()
            fun of(uppers:List<Type> = emptyList(), lowers:List<Type> = emptyList()) = cache.getOrPut(uppers to lowers){ Wildcard(uppers,lowers) }
        }

        override val name: String
            get() = display()

        override fun fullDisplay() =
            if(uppers.isNotEmpty())
                uppers.joinToString(" & ","? extends ","", transform = Display::display)
            else
                lowers.joinToString(" & ","? super ","", transform = Display::display)
    }
    companion object{
        fun of(type:java.lang.reflect.Type,generics:Map<String, Type> = emptyMap()): Type = when(type){
            is Class<*> -> Any.of(type)
            is java.lang.reflect.ParameterizedType -> Any.of(
                type.rawType as Class<*>,
                type.actualTypeArguments.map { of(it,generics) }
            )
            is TypeVariable<*> ->
                generics[type.name] ?: Generic.of(type)
            is java.lang.reflect.GenericArrayType -> GenericArray.of(of(type.genericComponentType))
            is java.lang.reflect.WildcardType -> Wildcard.of(
                type.upperBounds.map { of(it,generics) },
                type.lowerBounds.map { of(it,generics) }
            )
            else -> throw IllegalArgumentException("Unsupported type:   $type : ${type.javaClass.name}")
        }
    }

}