package org.arzimanoff.model.items;

import org.arzimanoff.model.common.Logger;
import org.arzimanoff.model.entities.Player;

public interface Consumable {
    boolean consume(Player player, int level, Logger log);
    String getOnUseDescription();
}
