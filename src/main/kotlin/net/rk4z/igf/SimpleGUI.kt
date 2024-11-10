package net.rk4z.igf

import org.bukkit.entity.Player

/**
 * A simple GUI class that provides basic inventory GUI functionality.
 * It allows setting up buttons, listeners, and background materials for GUI customization.
 * This class can be extended to create more complex GUIs.
 * @param player The player who will view the GUI.
 * @see InventoryGUI
 * @since 1.0.0
 * @author Lars
 */
class SimpleGUI(
    player: Player
) : InventoryGUI(player) {
    override fun build(): InventoryGUI {
        create()
        applyBackground()
        displayItems()
        return this
    }
}