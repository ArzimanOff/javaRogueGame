package org.arzimanoff.model.map;

import org.arzimanoff.model.common.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс, хранящий параметры отдельной комнаты.
 */
public class Room {
    private final Position positionOnMap;
    // координаты углов внутри комнаты
    private final Position roomTopLeftPosition;
    private final Position roomTopRightPosition;
    private final Position roomBottomLeftPosition;
    private final Position roomBottomRightPosition;

    // координаты углов границ комнаты
    private final Position borderTopLeftPosition;
    private final Position borderTopRightPosition;
    private final Position borderBottomLeftPosition;
    private final Position borderBottomRightPosition;
    private final int width;
    private final int height;
    private final List<Position> borderCoordinatesList = new ArrayList<>();

    private Position transitionPoint;

    private boolean isExplored;

    public Room(Position positionOnMap, Position borderTopLeftPosition, int w, int h) {
        this.positionOnMap = positionOnMap;
        this.isExplored = false;
        this.width = w - 1;
        this.height = h - 1;

        this.borderTopLeftPosition = borderTopLeftPosition;
        this.borderTopRightPosition = new Position(
                borderTopLeftPosition.getX() + this.width,
                borderTopLeftPosition.getY()
        );
        this.borderBottomLeftPosition = new Position(
                borderTopLeftPosition.getX(),
                borderTopLeftPosition.getY() + this.height
        );
        this.borderBottomRightPosition = new Position(
                borderTopLeftPosition.getX() + this.width,
                borderTopLeftPosition.getY() + this.height
        );

        this.roomTopLeftPosition = new Position(
                borderTopLeftPosition.getX() + 1,
                borderTopLeftPosition.getY() + 1
        );
        this.roomTopRightPosition = new Position(
                borderTopRightPosition.getX() - 1,
                borderTopRightPosition.getY() + 1
        );
        this.roomBottomLeftPosition = new Position(
                borderBottomLeftPosition.getX() + 1,
                borderBottomLeftPosition.getY() - 1
        );
        this.roomBottomRightPosition = new Position(
                borderBottomRightPosition.getX() - 1,
                borderBottomRightPosition.getY() - 1
        );

        this.transitionPoint = null;
        updateBorderCoordinatesList();
    }

    private void updateBorderCoordinatesList() {
        Position a;

        a = this.borderTopLeftPosition;
        borderCoordinatesList.add(a);
        while (!a.equals(this.borderTopRightPosition)) {
            a = new Position(
                    a.getX() + 1,
                    a.getY()
            );
            borderCoordinatesList.add(a);
        }

        a = this.borderBottomLeftPosition;
        borderCoordinatesList.add(a);
        while (!a.equals(this.borderBottomRightPosition)) {
            a = new Position(
                    a.getX() + 1,
                    a.getY()
            );
            borderCoordinatesList.add(a);
        }

        a = this.borderTopLeftPosition;
        borderCoordinatesList.add(a);
        while (!a.equals(this.borderBottomLeftPosition)) {
            a = new Position(
                    a.getX(),
                    a.getY() + 1
            );
            borderCoordinatesList.add(a);
        }

        a = this.borderTopRightPosition;
        borderCoordinatesList.add(a);
        while (!a.equals(this.borderBottomRightPosition)) {
            a = new Position(
                    a.getX(),
                    a.getY() + 1
            );
            borderCoordinatesList.add(a);
        }

    }

    public Position getBorderTopLeftPosition() {
        return borderTopLeftPosition;
    }

    public Position getBorderTopRightPosition() {
        return borderTopRightPosition;
    }

    public Position getBorderBottomLeftPosition() {
        return borderBottomLeftPosition;
    }

    public Position getBorderBottomRightPosition() {
        return borderBottomRightPosition;
    }

    public void setExplored(boolean explored) {
        isExplored = explored;
    }

    public boolean isExplored() {
        return isExplored;
    }

    public List<Position> getBorderCoordinatesList() {
        return borderCoordinatesList;
    }

    public Position getTransitionPoint() {
        return transitionPoint;
    }

    public void setTransitionPoint(Position transitionPoint) {
        this.transitionPoint = transitionPoint;
    }

    public Position getPositionOnMap() {
        return positionOnMap;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(positionOnMap, room.positionOnMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionOnMap);
    }

    @Override
    public String toString() {
        return "Room{" +
               "positionOnMap=" + positionOnMap +
               ", roomTopLeftPosition=" + roomTopLeftPosition +
               ", roomTopRightPosition=" + roomTopRightPosition +
               ", roomBottomLeftPosition=" + roomBottomLeftPosition +
               ", roomBottomRightPosition=" + roomBottomRightPosition +
               ", borderTopLeftPosition=" + borderTopLeftPosition +
               ", borderTopRightPosition=" + borderTopRightPosition +
               ", borderBottomLeftPosition=" + borderBottomLeftPosition +
               ", borderBottomRightPosition=" + borderBottomRightPosition +
               ", width=" + width +
               ", height=" + height +
               ", borderCoordinatesList=" + borderCoordinatesList +
               ", isExplored=" + isExplored +
               '}';
    }
}
