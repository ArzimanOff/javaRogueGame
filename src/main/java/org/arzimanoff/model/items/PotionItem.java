package org.arzimanoff.model.items;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Logger;
import org.arzimanoff.model.entities.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PotionItem implements Item, Consumable, Storagable {

    private final PotionType type;
    private static final Random rand = new Random();
    private static final GameEntity entity = GameEntity.POTION;

    private PotionItem(PotionType type) {
        this.type = type;
    }

    private enum Effects {
        EXTRA_HEALING("extra healing", "You begin to feel much better for some time."),
        GAIN_STRENGTH("strength", "You feel stronger, but for how long"),
        HEALING("healing", "You begin to feel better for some time"),
        THIRST_QUENCHING("thirst quenching", "This potion tastes extremely dull"),
        HASTE("haste", "You feel yourself moving much faster for some time");

        private final String name;
        private final String message;

        Effects(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }

    private record PotionType(Effects effect) {
    }

    /**
     * Creator class for random generation of various potions.
     * Uses record {@code PotionType} and enum {@code Effects} for creation of {@code PotionItem} object
     */
    public static class Creator {
        private static final Map<Integer, PotionType> potions = new HashMap<>();

        private Creator() {
        }

        static {
            potions.put(1, new PotionType(Effects.EXTRA_HEALING));
            potions.put(2, new PotionType(Effects.GAIN_STRENGTH));
            potions.put(3, new PotionType(Effects.HEALING));
            potions.put(4, new PotionType(Effects.THIRST_QUENCHING));
            potions.put(5, new PotionType(Effects.HASTE));
        }

        public static PotionItem createPotionItem() {
            return new PotionItem(randomPotion());
        }

        private static PotionType randomPotion() {
            return potions.get(1 + rand.nextInt(potions.size()));
        }

        public static PotionItem createForName(String name) {
            PotionType type = null;
            for (PotionType potionType : potions.values()) {
                if (potionType.effect().getName().equals(name)) {
                    type = potionType;
                }
            }
            return new PotionItem(type);
        }
    }

    @Override
    public GameEntity getEntity() {
        return entity;
    }

    @Override
    public String getOnAcquireDescription() {
        return String.format("You now have a potion titled %s", type.effect().getName());
    }

    @Override
    public boolean consume(Player player, int level, Logger log) {
        boolean result = false;
        if (!player.isTimerSet(type.effect().getName())) {
            usePotion(player, level);
            result = true;
        }
        return result;
    }

    @Override
    public String getInventoryDescription() {
        return String.format("A potion of %s", type.effect().getName());
    }

    @Override
    public String getName() {
        return type.effect().getName();
    }

    @Override
    public String getOnUseDescription() {
        return type.effect().getMessage();
    }

    private void usePotion(Player player, int level) {
        switch (type.effect()) {
            case EXTRA_HEALING:
                player.addTemporaryHP(level * 5, 20 + rand.nextInt(8));
                break;
            case GAIN_STRENGTH:
                player.addTemporaryStrength(player.getStrength() + 2, 20 + rand.nextInt(8));
                break;
            case HEALING:
                player.addTemporaryHP(level * 3, 20 + rand.nextInt(8));
                break;
            case HASTE:
                player.addTemporaryAgility(level + 1, 20 + rand.nextInt(8));
                break;
            case THIRST_QUENCHING:
                break;
        }

    }

}
