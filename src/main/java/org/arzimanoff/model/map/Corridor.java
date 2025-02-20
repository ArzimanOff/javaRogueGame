package org.arzimanoff.model.map;

import org.arzimanoff.model.common.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Corridor {
    private static final Random random = new Random();
    private boolean isExplored;
    private final Room roomA;
    private final Room roomB;
    private final List<Position> corridorCoordinatesList;


    public Corridor(Room roomA, Room roomB) {
        this.roomA = roomA;
        this.roomB = roomB;
        this.isExplored = false;
        corridorCoordinatesList = new ArrayList<>();
    }


    public void generateCorridorCoordinates() {
        Position from = this.roomA.getPositionOnMap();
        Position to = this.roomB.getPositionOnMap();
        // комната на позиции from всегда либо ЛЕВЕЕ, либо ВЫШЕ комнаты на позиции to

        if (from.getY() == to.getY()) {
            // если комната from ЛЕВЕЕ чем to, у них равны координаты Y (так как находятся в одной строке):
            // значит надо строить коридор от правой границы from к левой границе to

            // нужно определить стартовую координату на правой границе from
            Position start;
            // координата X совпадает с координатой X верхнего правого угла границ комнаты,
            // а координата Y лежит между координатами Y верхнего правого и нижнего правого угла границ комнаты
            int startX = this.roomA.getBorderTopRightPosition().getX();
            int startY = getRandomBetween(
                    this.roomA.getBorderTopRightPosition().getY(),
                    this.roomA.getBorderBottomRightPosition().getY()
            );
            start = new Position(startX, startY);

            // нужно определить конечную координату на левой границе to
            Position finish;
            // координата X совпадает с координатой X верхнего левого угла границ комнаты,
            // а координата Y лежит между координатами Y верхнего левого и нижнего левого угла границ комнаты
            int finishX = this.roomB.getBorderTopLeftPosition().getX();
            int finishY = getRandomBetween(
                    this.roomB.getBorderTopLeftPosition().getY(),
                    this.roomB.getBorderBottomLeftPosition().getY()
            );
            finish = new Position(finishX, finishY);

            // нужно найти координату по X где будет поворот в коридоре
            // пусть это будет случайная координата между startX и finishX

            int tempX;
            if (finishX - startX >= 4) {
                tempX = getRandomBetween(startX + 1, finishX - 1);
            } else {
                tempX = getRandomBetween(startX, finishX);
            }
            int tempFromY = startY;
            int tempToY = finishY;

            Position current;
            Position tempFrom = new Position(tempX, tempFromY);
            Position tempTo = new Position(tempX, tempToY);

            // добавляем в список координат коридора все позиции от точки начала
            // до точки первого поворота tempFrom
            current = new Position(start.getX(), start.getY());
            while (!current.equals(tempFrom)) {
                corridorCoordinatesList.add(current);
                current = new Position(current.getX() + 1, current.getY());
            }


            // k это своего рода направление в которое нужно строить коридор
            // от tempFrom к tempTo, если tempFrom выше то k = 1 иначе -1
            int k = (tempFrom.getY() < tempTo.getY()) ? 1 : -1;

            // добавляем в список координат коридора все позиции от точки первого поворота tempFrom
            // до точки второго поворота tempTo
            current = new Position(tempFrom.getX(), tempFrom.getY());
            while (!current.equals(tempTo)) {
                corridorCoordinatesList.add(current);
                current = new Position(current.getX(), current.getY() + k);
            }

            // добавляем в список координат коридора все позиции от точки второго поворота tempTo
            // до точки конца
            current = new Position(tempTo.getX(), tempTo.getY());
            while (!current.equals(finish)) {
                corridorCoordinatesList.add(current);
                current = new Position(current.getX() + 1, current.getY());
            }
            corridorCoordinatesList.add(finish);


        } else if (from.getX() == to.getX()) {
            // если комната from ВЫШЕ чем to, у них равны координаты X (так как находятся в одном столбце):
            // значит надо строить коридор от нижней границы from к верхней границе to


            // нужно определить стартовую координату на нижней границе from
            Position start;
            // координата X лежит между координатами X нижнего левого и нижнего правого угла границ комнаты,
            // а координата Y совпадает с координатой Y нижнего левого угла границ комнаты
            int startY = this.roomA.getBorderBottomLeftPosition().getY();
            int startX = getRandomBetween(
                    this.roomA.getBorderBottomLeftPosition().getX(),
                    this.roomA.getBorderBottomRightPosition().getX()
            );
            start = new Position(startX, startY);

            // нужно определить конечную координату на верхней границе to
            Position finish;
            // координата X лежит между координатами X верхнего левого и верхнего правого угла границ комнаты,
            // а координата Y совпадает с координатой Y верхнего левого угла границ комнаты
            int finishY = this.roomB.getBorderTopLeftPosition().getY();
            int finishX = getRandomBetween(
                    this.roomB.getBorderTopLeftPosition().getX(),
                    this.roomB.getBorderTopRightPosition().getX()
            );
            finish = new Position(finishX, finishY);

            // нужно найти координату по Y где будет поворот в коридоре
            // пусть это будет случайная координата между startX и finishX

            int tempY;
            if (finishY - startY >= 4) {
                tempY = getRandomBetween(startY + 1, finishY - 1);
            } else {
                tempY = getRandomBetween(startY, finishY);
            }

            int tempFromX = startX;
            int tempToX = finishX;

            Position current;
            Position tempFrom = new Position(tempFromX, tempY);
            Position tempTo = new Position(tempToX, tempY);

            // добавляем в список координат коридора все позиции от точки начала
            // до точки первого поворота tempFrom
            current = new Position(start.getX(), start.getY());
            while (!current.equals(tempFrom)) {
                corridorCoordinatesList.add(current);
                current = new Position(current.getX(), current.getY()+1);
            }

            // k это своего рода направление в которое нужно строить коридор
            // от tempFrom к tempTo, если tempFrom левее то k = 1 иначе -1
            int k = (tempFrom.getX() < tempTo.getX()) ? 1 : -1;

            // добавляем в список координат коридора все позиции от точки первого поворота tempFrom
            // до точки второго поворота tempTo
            current = new Position(tempFrom.getX(), tempFrom.getY());
            while (!current.equals(tempTo)) {
                corridorCoordinatesList.add(current);
                current = new Position(current.getX()+k, current.getY());
            }

            // добавляем в список координат коридора все позиции от точки второго поворота tempTo
            // до точки конца
            current = new Position(tempTo.getX(), tempTo.getY());
            while (!current.equals(finish)) {
                corridorCoordinatesList.add(current);
                current = new Position(current.getX(), current.getY()+1);
            }
            corridorCoordinatesList.add(finish);
        } else {
            throw new RuntimeException("Ошибка при генерации коридоров! Такой связи быть не может.");
        }

    }

    private static int getRandomBetween(int num1, int num2) {
        // Проверка, чтобы num1 было меньше num2
        if (num1 > num2) {
            // Если num1 больше num2, меняем их местами
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }

        // Генерация случайного числа между num1 и num2, не включая их
        return random.nextInt(num2 - num1 - 1) + num1 + 1;
    }

    public Room getRoomA() {
        return roomA;
    }

    public Room getRoomB() {
        return roomB;
    }

    public List<Position> getCorridorCoordinatesList() {
        return corridorCoordinatesList;
    }

    public boolean isExplored() {
        return isExplored;
    }

    public void setExplored(boolean explored) {
        isExplored = explored;
    }

    @Override
    public String toString() {
        return "Corridor{" +
               "roomA=" + roomA.getPositionOnMap() +
               ", roomB=" + roomB.getPositionOnMap() +
               '}';
    }
}
