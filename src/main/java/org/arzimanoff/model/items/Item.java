package org.arzimanoff.model.items;

import org.arzimanoff.model.common.GameEntity;

public interface Item {
    GameEntity getEntity();
    String getOnAcquireDescription();
}
