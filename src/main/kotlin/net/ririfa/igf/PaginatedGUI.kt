package net.ririfa.igf

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.persistence.PersistentDataType

/**
 * A class that provides paginated inventory GUI functionality with improved item management.
 * It allows dynamic page navigation, slot management, and customization using PersistentDataContainer.
 *
 * @param player The player who will view the GUI.
 * @see InventoryGUI
 * @since 1.0.0
 * @author Lars
 */
@Suppress("unused")
class PaginatedGUI(
    player: Player
) : InventoryGUI(player) {
    private var currentPage = 0
    private var itemsPerPage = 9
    private var totalPages = 1
    private var slotPositions: List<Int> = emptyList()
    private var emptyMessageButton: Button? = null
    private var pageItems: List<Button> = emptyList()

    private var prevPageButton: Button? = null
    private var nextPageButton: Button? = null

    /**
     * Builds the paginated GUI.
     * This method should be overridden by subclasses to set up the layout and items.
     */
    override fun build(): PaginatedGUI {
        create()
        displayItemsForPage()
        return this
    }

    /**
     * Sets the slot positions for items.
     * @param slots The list of slot positions for the items.
     * @return This [PaginatedGUI] instance.
     */
    fun setSlotPositions(slots: List<Int>): PaginatedGUI {
        this.slotPositions = slots
        return this
    }

    /**
     * Sets the items to be paginated.
     * @param items The list of items to display across multiple pages.
     * @return This [PaginatedGUI] instance.
     */
    fun setPageItems(items: List<Button>): PaginatedGUI {
        this.pageItems = items
        setTotalPages(items.size)
        return this
    }

    /**
     * Sets a default button to display when there are no items.
     * @param button The button to display when there are no items.
     */
    fun setEmptyMessageButton(button: Button): PaginatedGUI {
        this.emptyMessageButton = button
        return this
    }

    /**
     * Sets the total number of pages.
     * This method should be called after setting up items.
     */
    fun setTotalPages(totalItems: Int): PaginatedGUI {
        this.totalPages = (totalItems + itemsPerPage - 1) / itemsPerPage
        return this
    }

    /**
     * Displays the items for the current page in the inventory.
     * Clears previous items and adds only the items that belong to the current page.
     */
    fun displayItemsForPage() {
        inventory.clear()
        applyBackground()

        if (pageItems.isEmpty()) {
            emptyMessageButton?.let { inventory.setItem(it.slot, it.toItemStack()) }
            return
        }

        val startIndex = currentPage * itemsPerPage
        val endIndex = (startIndex + itemsPerPage).coerceAtMost(pageItems.size)

        pageItems.subList(startIndex, endIndex).forEachIndexed { index, button ->
            slotPositions.getOrNull(index)?.let { slot ->
                inventory.setItem(slot, button.toItemStack())
            }
        }

        listOfNotNull(
            prevPageButton?.takeIf { currentPage > 0 },
            nextPageButton?.takeIf { currentPage < totalPages - 1 }
        ).forEach { button ->
            inventory.setItem(button.slot, button.toItemStack())
        }
    }

    /**
     * Sets the buttons for page navigation.
     * @param prevButton The button to navigate to the previous page.
     * @param nextButton The button to navigate to the next page.
     * @return This [PaginatedGUI] instance.
     * @throws IllegalStateException If custom IDs aren't set for page navigation.
     */
    fun setPageButtons(prevButton: Button, nextButton: Button): PaginatedGUI {
        prevPageButton = prevButton.also { it.onClick = { prevPage() } }
        nextPageButton = nextButton.also { it.onClick = { nextPage() } }
        return this
    }

    /**
     * Sets the number of items per page.
     * @param itemsPerPage The number of items to display per page.
     *
     * @return This [PaginatedGUI] instance.
     */
    fun setItemsPerPage(itemsPerPage: Int): PaginatedGUI {
        this.itemsPerPage = itemsPerPage
        setTotalPages(pageItems.size)
        return this
    }

    /**
     * Sets the current page for the inventory.
     * @param page The page number to set.
     * @return This [PaginatedGUI] instance.
     */
    fun setPage(page: Int): PaginatedGUI {
        val newPage = page.coerceIn(0, totalPages - 1)
        if (newPage != currentPage) {
            currentPage = newPage
            displayItemsForPage()
        }
        return this
    }

    fun prevPage() = setPage(currentPage - 1)
    fun nextPage() = setPage(currentPage + 1)
}
