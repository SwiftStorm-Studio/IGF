package net.ririfa.igf

/**
 * Represents a single-page state for use with [DynamicGUI].
 *
 * This enum is intended to be used when a [DynamicGUI] does not require multiple states,
 * but still benefits from its dynamic button mapping and management capabilities.
 *
 * ## Example Usage:
 * ```
 * val gui = DynamicGUI<SinglePage>(player)
 *     .setButtonMappings(
 *         mapOf(SinglePage.PAGE to listOf(button1, button2, button3))
 *     )
 *     // SinglePage is the default state, so you don’t need to set it manually
 *     //.setState(SinglePage.PAGE)
 * ```
 *
 * This allows you to use [DynamicGUI] without having to define multiple states,
 * making it useful for simple, one-page GUI setups.
 */
enum class SinglePage {
	PAGE
}
