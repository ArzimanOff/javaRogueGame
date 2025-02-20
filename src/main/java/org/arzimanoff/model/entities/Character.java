package org.arzimanoff.model.entities;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Logger;
import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.map.Room;

import java.util.List;
import java.util.Random;

public abstract class Character {
    private static int level = 1;
    protected int maxHP;
    protected int currentHP;
    protected int agility;
    protected int strength;
    protected boolean isAlive;
    protected int aggression;
    private boolean isParalyzed = false;
    protected Position position;
    public Random rn = new Random();
    public static Logger logger;

    public Character(int maxHP, int agility, int strength, Position position, int aggression) {
        this.maxHP = maxHP + level / 3;
        this.agility = agility + level / 3;
        this.strength = strength + level / 3;
        this.isAlive = true;
        this.position = position;
        this.aggression = aggression;
        this.currentHP = this.maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getAgility() {
        return agility;
    }

    public int getStrength() {
        return strength;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public static void setLevel(int currentLevel) {
        Character.level = currentLevel;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getAggression() {
        return aggression;
    }

    public boolean isParalyzed() {
        return isParalyzed;
    }

    public void setParalyzed(boolean paralyzed) {
        isParalyzed = paralyzed;
    }

    public static void setLogger(Logger log) {
        logger = log;
    }

    public void takeDamage(int damage) {
        int remainHP = getCurrentHP() - damage;
        //System.out.println(this + " has " + remainHP + " / " + this.maxHP);
        setCurrentHP(remainHP);
        if (getCurrentHP() <= 0) {
            this.setAlive(false);
        }
    }

    public int attack(Character target) {
        if (this instanceof Player p) {
            p.setAttack(true);
        }
        int diceThis = (int) (Math.random() * 12 + 1);
        int diceTarget = (int) (Math.random() * 12 + 1);
        int hitChance = diceThis + getAgility();
        int dodgeChance = diceTarget + target.getAgility();
        if (hitChance > dodgeChance) {
            int damage = this.getStrength() + weaponModifier();
            logger.add(this + " has done damage to " + target + ": " + damage);
            if (target instanceof Player p) {
                p.setMissedStrokes(p.getMissedStrokes() + 1);
            }
            if (this instanceof Player p) {
                p.setStruckStrokes(p.getStruckStrokes() + 1);
            }
            target.takeDamage(damage);
            return damage;
        } else {
            logger.add(this + " missed on " + target);
            return 0;
        }
    }

    public abstract int weaponModifier();

    public abstract GameEntity getEntity();

    public void move(List<Character> characters, Room room) {
        int moveDirection = rn.nextInt(2);
        int coordinate = (rn.nextInt(2) * 4) - 2;
        int xMove = 0, yMove = 0;
        switch (moveDirection) {
            case 0:
                xMove = this.position.getX() + coordinate;
                yMove = this.position.getY();
                break;
            case 1:
                yMove = this.position.getY() + coordinate;
                xMove = this.position.getX();
                break;
        }

        Position newPosition = new Position(
                xMove,
                yMove
        );

        this.position = (checkEnemyPosition(newPosition, room, characters)) ? newPosition : this.position;

    }


    protected boolean checkEnemyPosition(Position newPosition, Room room, List<Character> characters) {
        return room.getBorderTopLeftPosition().getX() < newPosition.getX() &&
               room.getBorderBottomRightPosition().getX() > newPosition.getX() &&
               room.getBorderTopLeftPosition().getY() < newPosition.getY() &&
               room.getBorderBottomRightPosition().getY() > newPosition.getY() &&
               characters.stream().noneMatch(character -> character.getPosition().equals(newPosition));

    }

    protected boolean checkPositionInRoom(int x, int y, Room room) {
        Position newPosition = new Position(x, y);
        return room.getBorderTopLeftPosition().getX() < newPosition.getX() &&
               room.getBorderBottomRightPosition().getX() > newPosition.getX() &&
               room.getBorderTopLeftPosition().getY() < newPosition.getY() &&
               room.getBorderBottomRightPosition().getY() > newPosition.getY();
    }

    protected boolean checkEnemyAndPlayerCollision(Position enemyPosition, Position playerPosition) {
        return !enemyPosition.equals(playerPosition);
    }

}
