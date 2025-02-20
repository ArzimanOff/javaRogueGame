package org.arzimanoff.model.map.generator;

import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.entities.*;
import org.arzimanoff.model.entities.Character;
import org.arzimanoff.model.items.*;
import org.arzimanoff.model.map.LevelMap;
import org.arzimanoff.model.map.OnMapItem;
import org.arzimanoff.model.map.Room;

import java.util.*;

public class EntitiesGenerator {
    private static final Random random = new Random();

    private EntitiesGenerator() {
    }

    public static Map<Room, List<Character>> generateEnemies(LevelMap levelMap) {

        Map<Position, Room> rooms = levelMap.getRoomMap();
        Map<Room, List<Character>> enemies = new HashMap<>();

        rooms.forEach((key, room) -> {
            List<Character> roomEnemies = new ArrayList<>();

            // не включительно
            int cnt = getRandomBetween(0, 3);
            for (int i = 0; i < cnt; i++) {
                Character enemy = generateEnemy(room);
                roomEnemies.add(enemy);
            }

            enemies.put(room, roomEnemies);
        });

        return enemies;
    }

    public static Map<Room, List<OnMapItem>> generateItems(LevelMap levelMap) {
        Map<Position, Room> rooms = levelMap.getRoomMap();
        Map<Room, List<OnMapItem>> items = new HashMap<>();

        rooms.forEach((pos, room) -> {
            List<OnMapItem> onMapItems = new ArrayList<>();
            int count = getRandomBetween(0, 2);
            for (int i = 0; i < count; i++) {
                OnMapItem onMapItem = new OnMapItem(generateItem(), generateEntityPosition(room));
                onMapItems.add(onMapItem);
            }

            items.put(room, onMapItems);
        });

        return items;
    }

    public static Position generatePlayerPosition(LevelMap levelMap) {
        int playerX;
        int playerY;
        Room room;
        do {
            int i = random.nextInt(3);
            int j = random.nextInt(3);
            Position position = new Position(i, j);
            room = levelMap.getRoom(position);
            levelMap.getEnemies().put(room, new ArrayList<>());
            playerX = getRandomBetween(
                    room.getBorderTopLeftPosition().getX(),
                    room.getBorderTopRightPosition().getX()
            );
            playerY = getRandomBetween(
                    room.getBorderTopLeftPosition().getY(),
                    room.getBorderBottomLeftPosition().getY()
            );
        }
        while (room.getTransitionPoint() != null);

        room.setExplored(true);
        return new Position(playerX, playerY);
    }

    private static Item generateItem() {
        return switch (getRandomBetween(0, 6)) {
            case 1 -> FoodItem.Creator.createFood();
            case 2 -> GoldItem.randomizeMapGold();
            case 3 -> PotionItem.Creator.createPotionItem();
            case 4 -> ScrollItem.Creator.createScroll();
            case 5 -> WeaponItem.Creator.createWeapon();
            default -> null;
        };
    }

    private static Character generateEnemy(Room room) {
        int lowAgr = 2;
        int mediumAgr = 3;
        int highAgr = 4;
        Position position = generateEntityPosition(room);
        return switch (getRandomBetween(0, 6)) {
            case 1 -> new Ghost(
                    3,
                    3,
                    1,
                    position,
                    lowAgr
            );
            case 2 -> new Ogre(
                    8,
                    1,
                    4,
                    position,
                    mediumAgr
            );
            case 3 -> new SnakeMage(
                    6,
                    4,
                    2,
                    position,
                    highAgr
            );
            case 4 -> new Vampire(
                    7,
                    3,
                    2,
                    position,
                    highAgr
            );
            case 5 -> new Zombie(
                    7,
                    1,
                    2,
                    position,
                    mediumAgr
            );
            default -> null;
        };
    }

    private static Position generateEntityPosition(Room room) {
        int x = getRandomBetween(
                room.getBorderTopLeftPosition().getX(),
                room.getBorderTopRightPosition().getX()
        );
        int y = getRandomBetween(
                room.getBorderTopLeftPosition().getY(),
                room.getBorderBottomLeftPosition().getY()
        );
        return new Position(x, y);
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
