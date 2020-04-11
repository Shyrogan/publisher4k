package fr.shyrogan.publisher4k

/**
 * An object type that can break the iteration when published
 */
abstract class Cancellable(var isCancelled: Boolean = false)