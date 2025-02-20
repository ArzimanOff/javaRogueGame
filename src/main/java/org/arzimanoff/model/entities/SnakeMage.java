package org.arzimanoff.model.entities;

import org.arzimanoff.model.common.GameEntity;
import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.map.Room;

import java.util.List;


public class SnakeMage extends Character implements Enemy  {

    private static final GameEntity entity = GameEntity.SNAKE_MAGE;

    public SnakeMage(int maxHP, int agility, int strength, Position position, int aggression) {
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
    public int attack(Character target) {
        int damage = super.attack(target);
        int dice = (int) (Math.random() * 12 + 1);
        if (damage > 0 && dice > 7) {
            target.setParalyzed(true);
        }
        return damage;
    }

    @Override
    public void move(List<Character> characters, Room room) {
        Position currentPosition = getPosition();
        int x = (rn.nextInt(2) * 4) - 2;
        int y = (rn.nextInt(2) * 4) - 2;
        int xMove, yMove;
        xMove = currentPosition.getX() + x;
        yMove = currentPosition.getY() + y;

        Position newPosition = new Position(
                xMove,
                yMove
        );

        this.position = (super.checkEnemyPosition(newPosition, room, characters)) ? newPosition : position;
    }

    private int calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2); // Манхэттенское расстояние
    }

    @Override
    public boolean moveToPlayer(Player player, Room room, List<Character> characters) {
        var playerP = player.getPosition();
        var enemyP = this.getPosition();
        int distance = calculateDistance(enemyP.getX(), enemyP.getY(), playerP.getX(), playerP.getY());
        if (distance > 2 || (distance == 2 && (enemyP.getX() == playerP.getX() || enemyP.getY() == playerP.getY()))) {
            moveDiagonally(playerP, room, characters);
        } else {
            return false;
        }
        return true;
    }

    private void moveDiagonally(Position playerP, Room room, List<Character> characters) {
        Position enemyP = this.getPosition();
        int dx = (playerP.getX() > enemyP.getX()) ? 1 : -1; //Смешение по x
        int dy = (playerP.getY() > enemyP.getY()) ? 1 : -1; //Смещение по y

        //Случайное смещение по x и y
        if (rn.nextDouble() < 0.1) {
            dx = (rn.nextInt(2) == 0) ? 1 : -1;
        }

        if (rn.nextDouble() < 0.1) {
            dy = (rn.nextInt(2) == 0) ? 1 : -1;
        }

        var newPosition = new Position(enemyP.getX() + dx, enemyP.getY() + dy);

        //Проверяем, не застряли ли мы
        if (checkPositionInRoom(enemyP.getX() + dx, enemyP.getY() + dy, room) && checkEnemyPosition(newPosition, room, characters)) {
            enemyP.setX(enemyP.getX() + dx);
            enemyP.setY(enemyP.getY() + dy);
        } else {
            //если застряли, выбираем другое направление
            dx = -dx;
            dy = -dy;

            if (checkPositionInRoom(enemyP.getX() + dx, enemyP.getY() + dy, room) && checkEnemyPosition(newPosition, room, characters)) {
                enemyP.setX(enemyP.getX() + dx);
                enemyP.setY(enemyP.getY() + dy);
            }
        }
    }

    @Override
    public String toString() {
        return "SnakeMage";
    }
}
