package org.arzimanoff.model.items;

import org.arzimanoff.model.common.Logger;
import org.arzimanoff.model.entities.Player;

public interface Equipable {
    void equip(Player player, Logger log);
    void unequip(Player player, Logger log);
    String getEquipName();
}
