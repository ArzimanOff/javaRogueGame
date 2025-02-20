package org.arzimanoff.model.items;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Logger;
import org.arzimanoff.model.entities.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FoodItem implements Item, Consumable, Storagable {

    private final FoodType type;
    private static final Random rand = new Random();
    private static final GameEntity entity = GameEntity.FOOD;

    private FoodItem(FoodType type) {
        this.type = type;
    }

    private record FoodType(String name, int healingValue){}

    public static class Creator {
        private static final Map<Integer, FoodType> food = new HashMap<>();

        private Creator() {}

        static {
            food.put(1, new FoodType("meat", 9));
            food.put(2, new FoodType("fish", 4));
            food.put(3, new FoodType("potatoes", 2));
            food.put(4, new FoodType("crackers", 1));
        }

        public static FoodItem createFood() {
            return new FoodItem(randomFood());
        }

        private static FoodType randomFood() {
            return food.get(1 + rand.nextInt(food.size()));
        }

        public static FoodItem createForName(String name) {
            FoodType type = null;
            for (FoodType foodType : food.values()) {
                if (foodType.name().equals(name)) {
                    type = foodType;
                }
            }
            return new FoodItem(type);
        }
    }


    @Override
    public boolean consume(Player player, int level, Logger log) {
        player.heal(type.healingValue + level);
        return true;
    }

    @Override
    public String getInventoryDescription() {
        return String.format("Some %s", type.name);
    }

    @Override
    public String getName() {
        return type.name();
    }

    @Override
    public String getOnUseDescription() {
        return String.format("You ate some %s and feel better", type.name());
    }

    @Override
    public GameEntity getEntity() {
        return entity;
    }

    @Override
    public String getOnAcquireDescription() {
        return String.format("You found some %s", type.name);
    }


}
