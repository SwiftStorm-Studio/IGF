package net.rk4z.igf

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent

interface GUIListener {
    fun onInventoryClick(event: InventoryClickEvent, gui: InventoryGUI)
    fun onInventoryClose(event: InventoryCloseEvent, gui: InventoryGUI)
    fun onInventoryOpen(event: InventoryOpenEvent, gui: InventoryGUI)
}