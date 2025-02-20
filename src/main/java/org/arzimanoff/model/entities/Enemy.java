package org.arzimanoff.model.entities;


import org.arzimanoff.model.map.Room;

import java.util.List;

public interface Enemy {
    boolean moveToPlayer(Player player, Room room, List<Character> characters);

}
