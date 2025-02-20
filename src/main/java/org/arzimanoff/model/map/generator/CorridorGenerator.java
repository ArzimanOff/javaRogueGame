package org.arzimanoff.model.map.generator;

import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.map.Corridor;
import org.arzimanoff.model.map.Room;

import java.util.*;

public class CorridorGenerator {

    public static List<Corridor> generateCorridors(Map<Position, Room> roomMap) {
        List<Corridor> corridors = new ArrayList<>();
        List<Corridor> possibleCorridors = new ArrayList<>();

        // Генерируем все возможные коридоры (между соседними комнатами)
        for (Position position : roomMap.keySet()) {
            Room currentRoom = roomMap.get(position);

            // Проверяем соседей (вправо и вниз)
            Position rightNeighbor = new Position(position.getX() + 1, position.getY());
            Position downNeighbor = new Position(position.getX(), position.getY() + 1);

            if (roomMap.containsKey(rightNeighbor)) {
                possibleCorridors.add(new Corridor(currentRoom, roomMap.get(rightNeighbor)));
            }

            if (roomMap.containsKey(downNeighbor)) {
                possibleCorridors.add(new Corridor(currentRoom, roomMap.get(downNeighbor)));
            }
        }

        // Перемешиваем коридоры для случайного выбора
        Collections.shuffle(possibleCorridors);

        // Nспользуем Union-Find (DSU) для построения минимального связного графа
        DisjointSet disjointSet = new DisjointSet(roomMap.keySet());

        for (Corridor corridor : possibleCorridors) {
            Position posA = corridor.getRoomA().getPositionOnMap();
            Position posB = corridor.getRoomB().getPositionOnMap();

            if (disjointSet.find(posA) != disjointSet.find(posB)) {
                disjointSet.union(posA, posB);
                corridors.add(corridor);
            }
        }

        // Добавляем дополнительные коридоры с вероятностью 25%
        Random random = new Random();
        for (Corridor corridor : possibleCorridors) {
            if (!corridors.contains(corridor) && random.nextDouble() < 0.25) {
                corridors.add(corridor);
            }
        }

        for (Corridor corridor : corridors) {
            corridor.generateCorridorCoordinates();
        }

        return corridors;
    }
}
