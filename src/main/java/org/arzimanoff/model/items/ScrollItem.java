package org.arzimanoff.model.items;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Logger;
import org.arzimanoff.model.entities.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScrollItem implements Item, Consumable, Storagable {
    private final ScrollType scrollType;
    private static final Random rand = new Random();
    private static final GameEntity entity = GameEntity.SCROLL;

    private ScrollItem(ScrollType type) {
        this.scrollType = type;
    }

    private enum Effects {
        HEALING("healing", "You begin to feel much better."),
        GAIN_STRENGTH("strength", "You feel stronger, now. What bulging muscles!"),
        HASTE("haste", "You feel yourself moving much faster"),
        ENCHANT_WEAPON("enchant weapon", "Your weapon glows blue for a moment"),
        BLANK("blank", "This scroll seems to be blank");

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

    private record ScrollType(Effects effect) {
    }

    /**
     * Creator class for random generation of various scrolls.
     * Uses record {@code ScrollType} and enum {@code Effects} for creation of {@code ScrollItem} object
     */
    public static class Creator {
        private static final Map<Integer, ScrollType> scrolls = new HashMap<>();

        private Creator() {
        }

        static {
            scrolls.put(1, new ScrollType(Effects.HEALING));
            scrolls.put(2, new ScrollType(Effects.GAIN_STRENGTH));
            scrolls.put(3, new ScrollType(Effects.HASTE));
            scrolls.put(4, new ScrollType(Effects.ENCHANT_WEAPON));
            scrolls.put(5, new ScrollType(Effects.BLANK));
        }

        public static ScrollItem createScroll() {
            return new ScrollItem(randomScroll());
        }

        // Нужно добавить более подходящую вероятность выпадения в зависимости от ценности предмета
        private static ScrollType randomScroll() {
            return scrolls.get(1 + rand.nextInt(scrolls.size()));
        }

        public static ScrollItem createForName(String name) {
            ScrollType type = null;
            for (ScrollType scrollType : scrolls.values()) {
                if (scrollType.effect().getName().equals(name)) {
                    type = scrollType;
                }
            }
            return new ScrollItem(type);
        }
    }

    @Override
    public GameEntity getEntity() {
        return entity;
    }

    @Override
    public String getOnAcquireDescription() {
        return String.format("You now have a scroll titled %s", scrollType.effect().getName());
    }

    @Override
    public boolean consume(Player player, int level, Logger log) {
        useScroll(player, level);
        return true;
    }

    @Override
    public String getInventoryDescription() {
        return String.format("A scroll of %s", scrollType.effect().getName());
    }

    @Override
    public String getName() {
        return scrollType.effect().getName();
    }


    @Override
    public String getOnUseDescription() {
        return scrollType.effect().getMessage();
    }

    private void useScroll(Player player, int level) {
        switch (scrollType.effect()) {
            case HEALING:
                player.setMaxHP(player.getMaxHP() + level * 2);
                break;
            case GAIN_STRENGTH:
                player.setStrength(player.getStrength() + 2);
                break;
            case HASTE:
                player.setAgility(player.getAgility() + level + 1);
                break;
            case ENCHANT_WEAPON:
                if (player.getWeaponEquipped() != null){
                    player.getWeaponEquipped().addEnchantDamage(1 + rand.nextInt(3));
                }
                break;
            case BLANK:
                break;
        }
    }

}
