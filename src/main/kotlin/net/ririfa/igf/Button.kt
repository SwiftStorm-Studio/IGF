package net.ririfa.igf

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * Wrapper class to pair a value with its corresponding persistent data type.
 * This is typically used to store data in a strongly typed manner.
 *
 * @param T The type of the value being stored.
 * @property dataType The PersistentDataType defining how the value is serialized and deserialized.
 * @property value The actual value to be stored or retrieved.
 * @since 1.1.1
 * @author RiriFa
 */
data class DataWrapper<T>(
    val dataType: PersistentDataType<T, T>,
    val value: T
)

/**
 * Represents a button in an inventory GUI. This class is used to define the properties
 * and behavior of a button, including its material, display name, custom data, and click action.
 *
 * @property slot The inventory slot where this button will be placed.
 * @property material The material that represents this button visually.
 * @property name The display name of the button, shown in the inventory.
 * @property data A map of persistent data stored in the button's item.
 *                The keys are namespaced keys, and the values are wrapped in a data wrapper.
 * @property onClick An optional lambda function executed when the button is clicked.
 *                   The function takes the [Player] who clicked the button as a parameter.
 * @property skipGUIListenerCall A flag indicating whether to skip further GUI listener calls
 *                                after this button is clicked.
 * @since 1.1.0
 * @author RiriFa
 */
data class Button(
    val slot: Int,
    val material: Material,
    val name: Component,
    val data: Map<NamespacedKey, DataWrapper<*>> = emptyMap(),
    var onClick: ((Player) -> Unit)? = null,
    val skipGUIListenerCall: Boolean = true
) {
    fun toItemStack(): ItemStack = material.toItemStack(name, data)
}

/**
 * Converts the material into an ItemStack, optionally setting the display name and persistent data.
 *
 * @param name An optional display name for the resulting ItemStack. If null, no name will be set.
 * @param data A map of NamespacedKey to DataWrapper, representing persistent data to attach to the ItemStack. Defaults to an empty map.
 * @return The created ItemStack with the specified properties applied.
 * @since 1.1.0
 * @author RiriFa
 */
fun Material.toItemStack(
    name: Component? = null,
    data: Map<NamespacedKey, DataWrapper<*>> = emptyMap()
): ItemStack {
    val itemStack = ItemStack(this)
    val meta = itemStack.itemMeta ?: return itemStack

    if (name != null) {
        meta.displayName(name)
    }

    val container = meta.persistentDataContainer
    data.forEach { (key, wrapper) ->
        @Suppress("UNCHECKED_CAST")
        val typedWrapper = wrapper as? DataWrapper<Any> ?: return@forEach
        container.set(key, typedWrapper.dataType, typedWrapper.value)
    }

    itemStack.itemMeta = meta
    return itemStack
}

/**
 * Retrieves a value of a specified type from the persistent data container of the item stack.
 *
 * @param key The key associated with the data to retrieve.
 * @param dataType The data type of the value to retrieve.
 * @return The value of the specified type, or null if it doesn't exist.
 * @since 1.1.0
 * @author RiriFa
 */
inline fun <reified T> ItemStack.getValue(
    key: NamespacedKey,
    dataType: PersistentDataType<T, T>
): T? where T : Any {
    return itemMeta?.persistentDataContainer?.get(key, dataType)
}