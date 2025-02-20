package org.arzimanoff.model.entities;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.items.Satchel;
import org.arzimanoff.model.items.WeaponItem;

import java.util.HashMap;
import java.util.Map;

public class Player extends Character {

    private int temporaryHP;
    private int temporaryAgility;
    private int temporaryStrength;
    private final Satchel satchel;
    private WeaponItem weaponEquipped;
    private final Map<String, Integer> timers = new HashMap<>();
    private static final GameEntity entity = GameEntity.PLAYER;
    private int struckStrokes;
    private int missedStrokes;
    private boolean isAttack = false;


    public Player(int maxHP, int agility, int strength, Satchel satchel, Position position) {
        super(maxHP, agility, strength, position, 0);
        this.satchel = satchel;
        this.position = position;
        this.weaponEquipped = null;
    }

    public WeaponItem getWeaponEquipped() {
        return weaponEquipped;
    }

    public void setWeaponEquipped(WeaponItem weaponEquipped) {
        this.weaponEquipped = weaponEquipped;
    }

    /**
     * Increases HP stat
     *
     * @param healHP HP amount to add
     */
    public void heal(int healHP) {
        if (currentHP + healHP <= maxHP) {
            currentHP += healHP;
        } else {
            currentHP = maxHP;
        }
    }

    /**
     * Increases HP stat for the certain amount of turns
     *
     * @param addHP HP amount to add
     * @param turns number of turns while effect is holding
     */
    public void addTemporaryHP(int addHP, int turns) {
        temporaryHP += addHP;
        this.setMaxHP(this.getMaxHP() + temporaryHP);
        timers.put("healing", turns);
        logger.add(String.format("You have been promoted with extra %d health for %d turns", addHP, turns));
    }

    /**
     * Increases strength stat for the certain amount of turns
     *
     * @param addStrength strength points to add
     * @param turns       number of turns while effect is holding
     */
    public void addTemporaryStrength(int addStrength, int turns) {
        temporaryStrength = addStrength;
        this.setStrength(this.getStrength() + temporaryStrength);
        timers.put("strength", turns);
        logger.add(String.format("You have been promoted with extra %d strength for %d turns", addStrength, turns));
    }

    /**
     * Increases agility stat for the certain amount of turns
     *
     * @param addAgility agility points to add
     * @param turns      number of turns while effect is holding
     */
    public void addTemporaryAgility(int addAgility, int turns) {
        temporaryAgility = addAgility;
        this.setAgility(this.getAgility() + temporaryAgility);
        timers.put("haste", turns);
        logger.add(String.format("You have been promoted with extra %d agility for %d turns", addAgility, turns));
    }

    /**
     * Updates timers every turn
     */
    public void updateStats() {
        for (String key : timers.keySet()) {
            timers.compute(key, (k, v) -> v - 1);
            if (timers.get(key) <= 0) {
                rewindTemporaryEffects(key);
                timers.remove(key);
            }
        }
    }

    /**
     * Reverts temporary stat changes
     *
     * @param effect effect to revert
     */
    private void rewindTemporaryEffects(String effect) {
        switch (effect) {
            case "healing":
                this.setMaxHP(this.getMaxHP() - temporaryHP);
                temporaryHP = 0;
                break;
            case "strength":
                this.setStrength(this.getStrength() - temporaryStrength);
                temporaryStrength = 0;
                break;
            case "haste":
                this.setAgility(this.getAgility() - temporaryAgility);
                agility -= temporaryAgility;
                temporaryAgility = 0;
                break;
            default:
                break;
        }
    }

    public boolean isTimerSet(String effect) {
        boolean result;
        if ("extra healing".equals(effect)) {
            result = timers.containsKey("healing");
        } else {
            result = timers.containsKey(effect);
        }
        return result;
    }

    public int getStruckStrokes() {
        return struckStrokes;
    }

    public void setStruckStrokes(int struckStrokes) {
        this.struckStrokes = struckStrokes;
    }

    public int getMissedStrokes() {
        return missedStrokes;
    }

    public void setMissedStrokes(int missedStrokes) {
        this.missedStrokes = missedStrokes;
    }

    @Override
    public int weaponModifier() {
        int gameModifier = 0;
        if (weaponEquipped != null) {
            gameModifier = weaponEquipped.calculateWeaponDamage();
        }
        return gameModifier;
    }

    @Override
    public GameEntity getEntity() {
        return entity;
    }

    public Satchel getSatchel() {
        return satchel;
    }

    public void makeMove(Position newPosition) {
        this.setPosition(newPosition);
    }

    @Override
    public String toString() {
        return "Player";
    }

    @Override
    public int attack(Character target) {
        return super.attack(target);
    }

    public boolean isAttack() {
        return isAttack;
    }

    public void setAttack(boolean attacking) {
        isAttack = attacking;
    }
}
