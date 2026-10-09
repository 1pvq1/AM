package com.example.androidmaiden.domain.model

/**
 * Separator symbol styles for navigation segments.
 */
enum class BreadcrumbSeparator(val label: String, val symbol: String) {
    CHEVRON("Chevron (>)", ">"),
    SLASH("Slash (/)", "/"),
    PIPE("Pipe (|)", "|"),
    DOT("Dot (•)", "•"),
    ARROW("Arrow (→)", "→")
}

/**
 * Maximum visible path segments display length configuration for breadcrumbs.
 */
enum class BreadcrumbMaxSegments(val label: String, val maxVisibleCount: Int) {
    UNLIMITED("Unlimited (Scrollable)", 0),
    COMPACT_3("Compact (Max 3)", 3),
    COMPACT_5("Compact (Max 5)", 5)
}

/**
 * Root segment display label options for breadcrumbs.
 */
enum class BreadcrumbRootLabel(val label: String, val text: String) {
    ROOT("Root", "Root"),
    SLASH("Slash (/)", "/"),
    HOME("Home", "Home"),
    INTERNAL_STORAGE("Internal Storage", "Internal Storage")
}

/**
 * Content description display modes for file list entries.
 */
enum class FileItemDescriptionMode(val label: String, val description: String) {
    CONCISE("Concise Summary", "Truncated summary with expand option"),
    FULL("Full Description", "Direct complete display of description text"),
    HIDDEN("Hidden", "Hide description text completely")
}

/**
 * Icon styling options for file entries.
 */
enum class FileItemIconStyle(val label: String) {
    DEFAULT("Default Filled"),
    OUTLINED("Outlined Vector"),
    MINIMAL("Monochrome Minimal")
}

/**
 * Filter modes for handling system hidden files and folders (items starting with '.').
 */
enum class HiddenFilterMode(val label: String, val description: String) {
    SHOW_ALL("Show All", "Show normal and system hidden files together"),
    EXCLUDE_HIDDEN("Exclude Hidden", "Hide system hidden files and folders"),
    ONLY_HIDDEN("Hidden Only", "Display system hidden files and folders separately")
}

/**
 * Composite configuration state holding UI control appearance choices.
 *
 * @param breadcrumbSeparator Separator symbol used in breadcrumb segments.
 * @param breadcrumbMaxSegments Maximum visible segment limit for path display.
 * @param breadcrumbRootLabel Display text for the root breadcrumb node.
 * @param fileItemDescriptionMode Display mode for item descriptions in file lists.
 * @param fileItemIconStyle Icon styling theme for file entries.
 * @param fileItemShowDetails Whether file timestamp and count metadata details are shown.
 * @param hiddenFilterMode Filter mode for system hidden files and folders.
 */
data class ControlAppearanceConfig(
    val breadcrumbSeparator: BreadcrumbSeparator = BreadcrumbSeparator.CHEVRON,
    val breadcrumbMaxSegments: BreadcrumbMaxSegments = BreadcrumbMaxSegments.UNLIMITED,
    val breadcrumbRootLabel: BreadcrumbRootLabel = BreadcrumbRootLabel.ROOT,
    val fileItemDescriptionMode: FileItemDescriptionMode = FileItemDescriptionMode.CONCISE,
    val fileItemIconStyle: FileItemIconStyle = FileItemIconStyle.DEFAULT,
    val fileItemShowDetails: Boolean = true,
    val hiddenFilterMode: HiddenFilterMode = HiddenFilterMode.SHOW_ALL
)
