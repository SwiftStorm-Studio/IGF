package net.ririfa.igf

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

@Deprecated("Use Button-based click handlers instead", ReplaceWith("'Use onClick in Button'"))
interface GUIListener {
    fun onInventoryClick(event: InventoryClickEvent, gui: InventoryGUI) {}
    fun onInventoryClose(event: InventoryCloseEvent, gui: InventoryGUI) {}
    fun onInventoryOpen(event: InventoryOpenEvent, gui: InventoryGUI) {}
}
