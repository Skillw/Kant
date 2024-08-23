package com.skillw.kant.core.type

import java.lang.reflect.Modifier

class Structure<T: Any> private constructor(private val type: Type.Any<T>, private val clazz: Class<*>): Display {
    private val generics = mutableMapOf<String, Type>().apply {
        type.generics.forEachIndexed{index, type -> put(clazz.typeParameters[index].name, type) }
    }
    private val fields = HashMap<String,Field>().apply {
        clazz.declaredFields.filter { Modifier.isPublic(it.modifiers) }.forEach{
            put(it.name, Field.of(it, type, generics))
        }
    }
    private val methods = HashMap<String,MutableList<Method>>().apply {
        clazz.declaredMethods.filter { Modifier.isPublic(it.modifiers) }.forEach{
            getOrPut(it.name){ mutableListOf() }.add(Method.of(it, type, generics))
        }
    }
    private val constructors = clazz.declaredConstructors.filter { Modifier.isPublic(it.modifiers) }.map{ Constructor.of(it, type, generics) }
    override val name: String
        get() = clazz.name + " Structure"


    override fun fullDisplay(): String {
        return """
            |class ${type.fullDisplay()}{
            |   ${fields.values.joinToString("\n   "){it.display()}}
            |   ${methods.values.flatten().joinToString("\n   "){it.display()}}
            |   ${constructors.joinToString("\n   "){it.display()}}
            |}
        """.trimMargin()
    }
    companion object{
        internal fun<T : Any> of(clazz: Class<T>, type: Type.Any<T> = Type.Any.of(clazz)) = Structure(type, clazz)
    }
}