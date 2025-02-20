package org.arzimanoff.model.entities;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.map.Room;

import java.util.List;


public class Ghost extends Character implements Enemy {

    private static final GameEntity entity = GameEntity.GHOST;
    private boolean isVisible = true;

    public Ghost(int maxHP, int agility, int strength, Position position, int aggression) {
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

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void move(List<Character> characters, Room room) {
        Position currentPosition = getPosition();
        int possibleX = (Math.abs(room.getBorderTopLeftPosition().getX() - room.getBorderTopRightPosition().getX()) - 2);
        int possibleY = (Math.abs(room.getBorderTopLeftPosition().getY() - room.getBorderBottomLeftPosition().getY()) - 2);
        int x = rn.nextInt(possibleX) * 2 - possibleX;
        int y = rn.nextInt(possibleY) * 2 - possibleY;
        int xMove, yMove;
        xMove = currentPosition.getX() + x;
        yMove = currentPosition.getY() + y;

        Position newPosition = new Position(
                xMove,
                yMove
        );
        this.position = (super.checkEnemyPosition(newPosition, room, characters)) ? newPosition : position;
        isVisible = rn.nextInt(2) == 1;
    }

    @Override
    public boolean moveToPlayer(Player player, Room room, List<Character> characters) {
        isVisible = true;
        var playerP = player.getPosition();
        var enemyP = this.getPosition();
        Position newPosition;

        List<Position> newPositions = List.of(
                new Position(playerP.getX(), playerP.getY()-1),
                new Position(playerP.getX(), playerP.getY()+1),
                new Position(playerP.getX()-1, playerP.getY()),
                new Position(playerP.getX()+1, playerP.getY())
        );

        if (Math.abs(enemyP.getX() - playerP.getX()) > 1 || Math.abs(enemyP.getY() - playerP.getY()) > 1) {
            do {
                int index = rn.nextInt(newPositions.size());
                newPosition = newPositions.get(index);
            } while (!checkPositionInRoom(newPosition.getX(), newPosition.getY(), room) && !checkEnemyPosition(newPosition, room, characters));
            this.position = newPosition;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Ghost";
    }
}
