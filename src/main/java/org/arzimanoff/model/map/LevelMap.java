package org.arzimanoff.model.map;

import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.entities.Character;
import org.arzimanoff.model.map.generator.CorridorGenerator;

import java.util.*;

public class LevelMap {

    private static final Random random = new Random();
    // Хранилище комнат, ключ — объект Position
    private final Map<Position, Room> roomMap = new HashMap<>();
    private Map<Room, List<Character>> enemies = new HashMap<>();
    private Map<Room, List<OnMapItem>> items = new HashMap<>();
    private List<Corridor> corridors = new ArrayList<>();

    public Map<Position, Room> getRoomMap() {
        return roomMap;
    }

    public void generateMap(){
        generateRoomsList();
        generateCorridors();
    }
    // Метод для генерации списка комнат
    private void generateRoomsList() {
        // Размер карты 3x3
        int size = 3;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                // Создание комнаты
                Room room = generateRoom(i, j);

                // Добавление комнаты в карту
                roomMap.put(new Position(i, j), room);
            }
        }
        generateTransitionPoint();
    }

    public void generateTransitionPoint(){
        int i = random.nextInt(3);
        int j = random.nextInt(3);
        Position position = new Position(i, j);
        Room room = getRoom(position);
        getEnemies().put(room, new ArrayList<>());
        int outDoorX = getRandomBetween(
                room.getBorderTopLeftPosition().getX(),
                room.getBorderTopRightPosition().getX()
        );
        int outDoorY = getRandomBetween(
                room.getBorderTopLeftPosition().getY(),
                room.getBorderBottomLeftPosition().getY()
        );
        room.setTransitionPoint(new Position(outDoorX, outDoorY));
    }

    private void generateCorridors() {
        this.corridors = CorridorGenerator.generateCorridors(roomMap);

        // смотрим список сгенерированных коридоров
        //this.corridors.forEach(System.out::println);
    }

    public List<Corridor> getCorridors() {
        return corridors;
    }

    private Room generateRoom(int i, int j) {
        int xA = i * 41 + 1;
        int xB = i * 41 + 20;
        int yA = j * 15 + 1;
        int yB = j * 15 + 7;
        int x = random.nextInt((xB - xA) + 1) + xA;
        int y = random.nextInt((yB - yA) + 1) + yA;

        // от 7 до (40 - x%40), где (40 - x%40) максимально возможная ширина
        int maxW = ((i+1) * 41 + 1) - x - 1;
        int maxH = ((j+1) * 15 + 1) - y - 1;
        int w = random.nextInt((maxW - 7) + 1) + 7;
        // от 7 до (15 - x%15), где (15 - x%15) максимально возможная ширина
        int h = random.nextInt((maxH - 6) + 1) + 6;

        Position roomPositionOnMap = new Position(i, j);
        Position borderTopLeftPosition = new Position(x, y);
        return new Room(roomPositionOnMap, borderTopLeftPosition, w, h);
    }

    public void setEnemies(Map<Room, List<Character>> enemies) {
        this.enemies = enemies;
    }

    public void setItems(Map<Room, List<OnMapItem>> items) {
        this.items = items;
    }

    public Map<Room, List<Character>> getEnemies() {
        return enemies;
    }

    public Map<Room, List<OnMapItem>> getItems() {
        return items;
    }

    // Получение комнаты по позиции
    public Room getRoom(Position position) {
        return roomMap.get(position);
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
}

