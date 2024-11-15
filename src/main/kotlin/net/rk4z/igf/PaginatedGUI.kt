package net.rk4z.igf

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
    private var prevValue: String? = null
    private var nextValue: String? = null
    private val pageChangeKey: NamespacedKey = IGF.createKey("paginated", "pageChange", "type")

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
            emptyMessageButton?.let { addItem(it) }
            displayItems()
            return
        }

        displayItems()

        val startIndex = currentPage * itemsPerPage
        val endIndex = (startIndex + itemsPerPage).coerceAtMost(pageItems.size)

        pageItems.subList(startIndex, endIndex).forEachIndexed { index, button ->
            val slot = slotPositions.getOrNull(index) ?: return@forEachIndexed
            inventory.setItem(slot, button.toItemStack())
        }

        addPageNavigationButtons()
    }

    /**
     * Sets the buttons for page navigation.
     * @param prevButton The button to navigate to the previous page.
     * @param nextButton The button to navigate to the next page.
     * @return This [PaginatedGUI] instance.
     * @throws IllegalStateException If custom IDs are not set for page navigation.
     */
    fun setPageButtons(prevButton: Button, nextButton: Button): PaginatedGUI {
        this.prevValue = prevButton.keyValue
        this.nextValue = nextButton.keyValue
        prevPageButton = prevButton
        nextPageButton = nextButton
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
        this.currentPage = if (totalPages <= 0) 0 else page.coerceIn(0, totalPages - 1)
        return this
    }

    /**
     * Navigates to the next page if available.
     */
    fun nextPage() {
        if (currentPage < totalPages - 1) {
            setPage(currentPage + 1)
            displayItemsForPage()
        }
    }

    /**
     * Navigates to the previous page if available.
     */
    fun prevPage() {
        if (currentPage > 0) {
            setPage(currentPage - 1)
            displayItemsForPage()
        }
    }

    /**
     * Adds the navigation buttons for paging.
     * It uses the buttons set by [setPageButtons].
     */
    private fun addPageNavigationButtons() {
        prevPageButton?.let { button ->
            if (currentPage > 0) {
                inventory.setItem(button.slot, button.toItemStack())
            }
        }

        nextPageButton?.let { button ->
            if (currentPage < totalPages - 1) {
                inventory.setItem(button.slot, button.toItemStack())
            }
        }
    }

    /**
     * Handles the click events for page navigation using PersistentDataContainer.
     */
    fun handlePageNavigation(event: InventoryClickEvent) {
        val clickedItem = event.currentItem ?: return
        val meta = clickedItem.itemMeta ?: return
        val container = meta.persistentDataContainer
        val key = container.get(pageChangeKey, PersistentDataType.STRING) ?: return

        when (key) {
            prevValue -> prevPage()
            nextValue -> nextPage()
        }
    }
}
