package net.rk4z.igf

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

data class Button<T>(
    val slot: Int,
    val material: Material,
    val name: Component,
    val key: NamespacedKey? = null,
    val keyValue: T? = null,
    val dataType: PersistentDataType<T, T>? = null
) {
    fun toItemStack(): ItemStack {
        return material.toItemStack(name, key, keyValue, dataType)
    }
}

fun <T> Material.toItemStack(
    name: Component? = null,
    key: NamespacedKey? = null,
    value: T? = null,
    dataType: PersistentDataType<T, T>? = null
): ItemStack {
    val itemStack = ItemStack(this)
    val meta: ItemMeta? = itemStack.itemMeta

    if (name != null) {
        meta?.displayName(name)
    }
    if (value != null && key != null && dataType != null) {
        meta?.persistentDataContainer?.set(key, dataType, value)
    }
    if (meta != null) {
        itemStack.itemMeta = meta
    }

    return itemStack
}

fun Material.toItemStack(
    name: Component? = null
): ItemStack {
    val itemStack = ItemStack(this)
    val meta: ItemMeta? = itemStack.itemMeta

    if (name != null) {
        meta?.displayName(name)
    }
    if (meta != null) {
        itemStack.itemMeta = meta
    }

    return itemStack
}

inline fun <reified T> ItemStack.getValue(key: NamespacedKey): T? {
    return when (T::class) {
        // Primitive types
        Byte::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.BYTE) as? T
        }
        Short::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.SHORT) as? T
        }
        Int::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.INTEGER) as? T
        }
        Long::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.LONG) as? T
        }
        Float::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.FLOAT) as? T
        }
        Double::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.DOUBLE) as? T
        }
        String::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.STRING) as? T
        }

        // Array types
        ByteArray::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.BYTE_ARRAY) as? T
        }
        IntArray::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.INTEGER_ARRAY) as? T
        }
        LongArray::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.LONG_ARRAY) as? T
        }

        // Complex types
        PersistentDataContainer::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.TAG_CONTAINER) as? T
        }

        // Special Types
        Boolean::class -> {
            itemMeta?.persistentDataContainer?.get(key, PersistentDataType.BOOLEAN) as? T
        }
        else -> null
    }
}
