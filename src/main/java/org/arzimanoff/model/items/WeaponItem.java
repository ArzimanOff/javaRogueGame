package org.arzimanoff.model.items;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Logger;
import org.arzimanoff.model.entities.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeaponItem implements Item , Equipable, Storagable {

    private final WeaponType type;
    private static final GameEntity entity = GameEntity.WEAPON;
    private static final Random rand = new Random();
    private int enchantDamage = 0;

    private WeaponItem(WeaponType type) {
        this.type = type;
    }

    private record WeaponType(String name, String damage) {}

    /**
     * Creator class for random generation of various weapons.
     * Uses record {@code WeaponType} for creation of {@code WeaponItem} object
     * */
    public static class Creator {
        private static final Map<Integer, WeaponType> weapons = new HashMap<>();

        private Creator() {}

        static {
            weapons.put(1, new WeaponType("mace", "2d4"));
            weapons.put(2, new WeaponType("long sword", "3d4"));
            weapons.put(3, new WeaponType("dagger", "1d6"));
            weapons.put(4, new WeaponType("two-handed sword", "4d4"));
            weapons.put(5, new WeaponType("spear", "2d3"));
        }

        public static WeaponItem createWeapon() {
            return new WeaponItem(randomWeapon());
        }

        private static WeaponType randomWeapon() {
            return weapons.get(1 + rand.nextInt(weapons.size()));
        }

        public static WeaponItem createForName(String name) {
            WeaponType type = null;
            for (WeaponType weaponType : weapons.values()) {
                if (weaponType.name().equals(name)) {
                    type = weaponType;
                }
            }
            return new WeaponItem(type);
        }
    }

    @Override
    public void equip(Player player, Logger log) {
        if (player.getWeaponEquipped() == this) {
            unequip(player, log);
        } else {
            player.setWeaponEquipped(this);
            log.add(String.format("You are now wielding the %s", type.name()));
        }
    }

    @Override
    public void unequip(Player player, Logger log) {
        if (player.getWeaponEquipped() == null) {
            log.add(String.format("You aren't wielding the %s", type.name()));
        } else {
            player.setWeaponEquipped(null);
            log.add(String.format("You put away the %s", type.name()));
        }
    }

    @Override
    public String getEquipName() {
        return String.format("%s [%s]", type.name(), type.damage());
    }

    @Override
    public String getInventoryDescription() {
        return String.format("a %s", type.name());
    }

    @Override
    public String getName() {
        return type.name();
    }

    @Override
    public GameEntity getEntity() {
        return entity;
    }

    @Override
    public String getOnAcquireDescription() {
        return String.format("You found a %s [%s]", type.name(), type.damage());
    }

    public int calculateWeaponDamage() {
        int totalDamage = 0;
        String[] damageValues = type.damage().split("d");
        for (int i = 0; i < Integer.parseInt(damageValues[0]); i++) {
            totalDamage += 1 + rand.nextInt(Integer.parseInt(damageValues[1])) + enchantDamage;
        }
        return totalDamage;
    }

    public void addEnchantDamage(int enchantDamage) {
        this.enchantDamage += enchantDamage;
    }

    public boolean isEnchanted() {
        return enchantDamage > 0;
    }

    public int getEnchantDamage() {
        return enchantDamage;
    }

    @Override
    public String toString() {
        return "WeaponItem{" +
               "type=" + type +
               ", enchantDamage=" + enchantDamage +
               '}';
    }
}
