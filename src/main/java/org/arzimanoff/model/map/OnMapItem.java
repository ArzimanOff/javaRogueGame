package org.arzimanoff.model.map;

import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.items.Item;

/**
 * An object that represents an {@code Item} instance on the map reflected by {@code Position}
 */
public record OnMapItem(Item item, Position position) {
}
