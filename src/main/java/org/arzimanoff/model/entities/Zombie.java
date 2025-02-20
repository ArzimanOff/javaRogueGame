package org.arzimanoff.model.entities;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.map.Room;

import java.util.List;


public class Zombie extends Character implements Enemy {

    private static final GameEntity entity = GameEntity.ZOMBIE;

    public Zombie(int maxHP, int agility, int strength, Position position, int aggression) {
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

    public void move(List<Character> characters, Room room) {
        int moveDirection = rn.nextInt(2);
        int coordinate = rn.nextInt(2) * 2 - 1;
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

        this.position = (super.checkEnemyPosition(newPosition, room, characters)) ? newPosition : position;
    }

    @Override
    public boolean moveToPlayer(Player player, Room room, List<Character> characters) {
        var playerP = player.getPosition();
        var enemyP = this.getPosition();

        int dx = 0, dy = 0;

        var newPosition1 = new Position(enemyP.getX() + 1, enemyP.getY());
        var newPosition2 = new Position(enemyP.getX() - 1, enemyP.getY());
        var newPosition3 = new Position(enemyP.getX(), enemyP.getY() + 1);
        var newPosition4 = new Position(enemyP.getX(), enemyP.getY() - 1);

        if (enemyP.getX() - playerP.getX() < -1 && checkPositionInRoom(enemyP.getX() + 1, enemyP.getY(), room) && checkEnemyPosition(newPosition1, room, characters)) {
            dx = 1;
        } else if (enemyP.getX() - playerP.getX() > 1 && checkPositionInRoom(enemyP.getX() - 1, enemyP.getY(), room) && checkEnemyPosition(newPosition2, room, characters)) {
            dx = -1;
        } else if (enemyP.getY() - playerP.getY() < -1 && checkPositionInRoom(enemyP.getX(), enemyP.getY() + 1, room) && checkEnemyPosition(newPosition3, room, characters)) {
            dy = 1;
        } else if (enemyP.getY() - playerP.getY() > 1 && checkPositionInRoom(enemyP.getX(), enemyP.getY() - 1, room) && checkEnemyPosition(newPosition4, room, characters)) {
            dy = -1;
        } else {
            return false;
        }

        enemyP.setX(enemyP.getX() + dx);
        enemyP.setY(enemyP.getY() + dy);
        return true;
    }

    @Override
    public String toString() {
        return "Zombie";
    }
}

