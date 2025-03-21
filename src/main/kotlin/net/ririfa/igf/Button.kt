package net.ririfa.igf

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

@FunctionalInterface
fun interface ClickEvent {
    fun onClick(player: Player)
}

/**
 * A wrapper class to encapsulate a value and its associated PersistentDataType.
 * This class provides utility methods for interacting with PersistentDataContainers.
 *
 * @param P The primitive type of the PersistentDataType.
 * @param C The complex type of the PersistentDataType.
 * @property dataType The PersistentDataType used to interact with the container.
 * @property value The value to be stored or retrieved from the container.
 * @since 1.1.1
 */
data class DataWrapper<P : Any, C : Any>(
    val dataType: PersistentDataType<P, C>,
    val value: C
) {
    /**
     * Sets a value into the provided PersistentDataContainer using a specified key.
     *
     * @param container The PersistentDataContainer where the value will be stored.
     * @param key The NamespacedKey used to store and retrieve the value.
     */
    fun setTo(container: PersistentDataContainer, key: NamespacedKey) {
        container.set(key, dataType, value)
    }

    /**
     * Companion object providing utility functions for managing persistent data storage
     * and retrieval within a data container.
     */
    companion object {
        /**
         * Retrieves a value from a PersistentDataContainer and wraps it in a DataWrapper.
         *
         * @param container The PersistentDataContainer to retrieve the value from.
         * @param key The NamespacedKey used to identify the data.
         * @param dataType The PersistentDataType that specifies the type of the data.
         * @return A DataWrapper containing the retrieved value if present, or null if no value is found.
         */
        fun <P : Any, C : Any> getFrom(
            container: PersistentDataContainer,
            key: NamespacedKey,
            dataType: PersistentDataType<P, C>
        ): DataWrapper<P, C>? {
            return container.get(key, dataType)?.let { DataWrapper(dataType, it) }
        }
    }
}

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
    val data: Map<NamespacedKey, DataWrapper<out Any, out Any>> = emptyMap(),
    var onClick: ClickEvent? = null,
    val skipGUIListenerCall: Boolean = true
) {
    /**
     * Converts the button's material, display name, and custom data into an ItemStack.
     *
     * @return An ItemStack representing the visual and data properties of this button.
     */
    fun toItemStack(): ItemStack = material.toItemStack(name, data)
}

fun Button.setClick(callback: (Player) -> Unit): Button {
    this.onClick = ClickEvent { player -> callback(player) }
    return this
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
    data: Map<NamespacedKey, DataWrapper<*, *>> = emptyMap()
): ItemStack {
    val itemStack = ItemStack(this)
    val meta = itemStack.itemMeta ?: return itemStack

    if (name != null) {
        meta.displayName(name)
    }

    val container = meta.persistentDataContainer
    data.forEach { (key, wrapper) ->
        wrapper.setTo(container, key)
    }

    itemStack.itemMeta = meta
    return itemStack
}

/**
 * Retrieves a value from the ItemStack's PersistentDataContainer.
 *
 * @param key The NamespacedKey used to locate the data within the PersistentDataContainer.
 * @param dataType The PersistentDataType that specifies the type of data being retrieved.
 * @return The retrieved value, or null if the data is not found.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> ItemStack.getValue(
    key: NamespacedKey,
    dataType: PersistentDataType<T, T>
): T? {
    val container = this.itemMeta?.persistentDataContainer ?: return null
    return DataWrapper.getFrom(container, key, dataType)?.value
}

/**
 * Retrieves a DataWrapper from the ItemStack's PersistentDataContainer.
 *
 * @param key The NamespacedKey used to locate the data within the PersistentDataContainer.
 * @param dataType The PersistentDataType that specifies the type of data being retrieved.
 * @return A DataWrapper containing the retrieved value and its associated PersistentDataType, or null if the data is not found.
 */
inline fun <reified P : Any, reified C : Any> ItemStack.getWrapper(
    key: NamespacedKey,
    dataType: PersistentDataType<P, C>
): DataWrapper<P, C>? {
    val container = this.itemMeta?.persistentDataContainer ?: return null
    return DataWrapper.getFrom(container, key, dataType)
}