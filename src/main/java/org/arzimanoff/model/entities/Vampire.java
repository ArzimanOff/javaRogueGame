package org.arzimanoff.model.entities;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.map.Room;

import java.util.List;

public class Vampire extends Character implements Enemy {

    private boolean firstDamage = true;
    private static final GameEntity entity = GameEntity.VAMPIRE;

    public Vampire(int maxHP, int agility, int strength, Position position, int aggression) {
        super(maxHP, agility, strength, position, aggression);
    }

    @Override
    public int weaponModifier() {
        return 0;
    }

    @Override
    public GameEntity getEntity() {
        return entity;
    }


    @Override
    public void takeDamage(int damage) {
        if (!firstDamage) {
            super.takeDamage(damage);
        } else {
            super.takeDamage(0);
            firstDamage = false;
        }
    }

    @Override
    public int attack(Character target) {
        int damage = super.attack(target);
        if (damage > 0) {
            int dice = rn.nextInt(2) + 1;
            int stealHealth = target.getMaxHP() - dice;
            target.setMaxHP(stealHealth);
        }
        return damage;
    }

    @Override
    public void move(List<Character> characters, Room room) {
        super.move(characters, room);
    }

    @Override
    public boolean moveToPlayer(Player player, Room room, List<Character> characters) {
        var playerP = player.getPosition();
        var enemyP = this.getPosition();

        int dx = 0, dy = 0;


        if (enemyP.getX() - playerP.getX() < -1 && checkPositionInRoom(enemyP.getX() + 1, enemyP.getY(), room)) {
            dx = 1;
        } else if (enemyP.getX() - playerP.getX() > 1 && checkPositionInRoom(enemyP.getX() - 1, enemyP.getY(), room)) {
            dx = -1;
        } else if (enemyP.getY() - playerP.getY() < -1 && checkPositionInRoom(enemyP.getX(), enemyP.getY() + 1, room)) {
            dy = 1;
        } else if (enemyP.getY() - playerP.getY() > 1 && checkPositionInRoom(enemyP.getX(), enemyP.getY() - 1, room)) {
            dy = -1;
        } else {
            return false;
        }

        var newPosition = new Position(enemyP.getX() + 2 * dx, enemyP.getY() + 2 * dy);

        if (checkPositionInRoom(newPosition.getX(), newPosition.getY(), room) && checkEnemyAndPlayerCollision(newPosition, playerP) && checkEnemyPosition(newPosition, room, characters)) {
            this.position = newPosition;
        } else if (checkPositionInRoom(enemyP.getX() + dx, enemyP.getY() + dy, room)) {
            enemyP.setX(enemyP.getX() + dx);
            enemyP.setY(enemyP.getY() + dy);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Vampire";
    }
}
