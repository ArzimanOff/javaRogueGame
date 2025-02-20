package org.arzimanoff.presentation;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.arzimanoff.data.StatisticsItem;
import org.arzimanoff.model.RogueGame;
import org.arzimanoff.model.common.Logger;
import org.arzimanoff.model.common.Position;
import org.arzimanoff.model.entities.Character;
import org.arzimanoff.model.entities.Ghost;
import org.arzimanoff.model.entities.Player;
import org.arzimanoff.model.items.*;
import org.arzimanoff.model.map.Corridor;
import org.arzimanoff.model.map.LevelMap;
import org.arzimanoff.model.map.OnMapItem;
import org.arzimanoff.model.map.Room;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Drawer {
    private static final String ROOM_BORDER_CHAR = " ";
    private static final String CORRIDOR_CHAR = ".";
    private static final int MAP_WIDTH = 124;
    private static final int MAP_HEIGHT = 46;
    private static Drawer instance;
    private final Screen screen;

    private Drawer(Screen screen) {
        this.screen = screen;
    }

    public static synchronized Drawer getInstance(Screen screen) {
        if (instance == null) {
            instance = new Drawer(screen);
        }
        return instance;
    }


    public final void drawAllRooms(LevelMap levelMap) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Position position = new Position(i, j);
                Room room = levelMap.getRoom(position);
                drawRoomContent(room);
            }
        }
    }

    public final void drawAllCorridors(LevelMap levelMap, Room  room) {
        List<Corridor> corridors = levelMap.getCorridors();
        for (Corridor corridor : corridors) {
            drawCorridor(corridor, room);
        }
    }

    private void drawCorridor(Corridor corridor, Room room) {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        List<Position> coords = corridor.getCorridorCoordinatesList();
        if (corridor.isExplored()) {
            for (Position p : coords) {
                textGraphics.putString(p.getX(), p.getY(), CORRIDOR_CHAR);
            }
        } else if (room != null){
            for (Position p : coords) {
                for (Position roomP : room.getBorderCoordinatesList()) {
                    if (p.equals(roomP)) {
                        textGraphics.putString(p.getX(), p.getY(), CORRIDOR_CHAR);
                    }
                }
            }
        }
    }


    public final void drawRoomContent(Room room) {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);

        // рисуем границы комнаты

        if (room.isExplored()) {
            List<Position> coords = room.getBorderCoordinatesList();
            for (Position p : coords) {
                textGraphics.putString(p.getX(), p.getY(), ROOM_BORDER_CHAR);
            }
            if (room.getTransitionPoint() != null) {
                textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
                textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
                textGraphics.putString(room.getTransitionPoint().getX(), room.getTransitionPoint().getY(), "N");
            }
        }
    }


    public final void drawMapBorders() {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        for (int i = 0; i < MAP_WIDTH; i++) {
            textGraphics.putString(i, 0, "-"); // Верхняя граница
            textGraphics.putString(i, MAP_HEIGHT, "-"); // Нижняя граница
        }

        for (int i = 0; i < MAP_HEIGHT; i++) {
            textGraphics.putString(0, i, "|"); // Левая граница
            textGraphics.putString(MAP_WIDTH, i, "|"); // Правая граница
        }

        for (int i = 0; i < MAP_WIDTH; i++) {
            textGraphics.putString(i, MAP_HEIGHT + 2, "-"); // Нижняя граница логгера
        }

        for (int i = 0; i < 3; i++) {
            textGraphics.putString(0, MAP_HEIGHT + i, "|"); // Левая граница
            textGraphics.putString(MAP_WIDTH, MAP_HEIGHT + i, "|"); // Правая граница
        }

        for (int i = MAP_WIDTH + 1; i < MAP_WIDTH + 30; i++) {
            textGraphics.putString(i, 0, "-"); // Верхняя граница
            textGraphics.putString(i, MAP_HEIGHT, "-"); // Нижняя граница
        }
        for (int i = 0; i < MAP_HEIGHT + 1; i++) {
            textGraphics.putString(MAP_WIDTH + 30, i, "|"); // Правая граница
        }

        //screen.refresh();
    }

    public void drawPlayer(Player player) {
        Position position = player.getPosition();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.BLUE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(position.getX(), position.getY(), String.valueOf(player.getEntity().getSymbol()));
    }

    public void drawLoggerText(Logger log) {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        String text = log.get();
        text = text == null ? " " : text;
        textGraphics.putString(1, MAP_HEIGHT + 1, text); // лог
    }

    public void drawGameStats(RogueGame game){

        StatisticsItem statisticsItem = game.getStatisticsItem();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
        textGraphics.putString(MAP_WIDTH + 2, 1, "GAME STATS:".toUpperCase());

        // кол-во золота
        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 3, "Gold: ".toUpperCase() + statisticsItem.getGold());

        // достигнутый уровень
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 5, "Level: ".toUpperCase() + statisticsItem.getLevel() + " / 21");

        // кол-во побежденных противников
        textGraphics.setForegroundColor(TextColor.ANSI.RED);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 7, "Defeated enemies: ".toUpperCase() + statisticsItem.getEnemyKilled());

        // кол-во побежденных противников
        textGraphics.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 9, "Eaten food: ".toUpperCase() + statisticsItem.getFoodConsumed());

        // кол-во побежденных противников
        textGraphics.setForegroundColor(TextColor.ANSI.MAGENTA_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 11, "Consumed Elixirs: ".toUpperCase() + statisticsItem.getPotionsConsumed());

        // кол-во прочитанных свитков
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 13, "Read scrolls: ".toUpperCase() + statisticsItem.getScrollsConsumed());

        // кол-во нанесенных ударов
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 15, "Struck strokes: ".toUpperCase() + statisticsItem.getAttacksDealt());

        // кол-во пропущенных ударов
        textGraphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 17, "Missed strokes: ".toUpperCase() + statisticsItem.getAttacksReceived());

        // кол-во пройденных шагов
        textGraphics.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 19, "Passed cells: ".toUpperCase() + statisticsItem.getTilesPassed());

        //screen.refresh();
    }

    public void drawHelpWindow() {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 5, 45, "Press u to get help...");
    }

    public void drawEnemies(Map<Room, List<Character>> allEnemies, Room room) {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        allEnemies.get(room).forEach(
                enemy -> {
                    Position position = enemy.getPosition();
                    switch (enemy.getEntity().getColor()) {
                        case "yellow" -> textGraphics.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
                        case "red" -> textGraphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
                        case "green" -> textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
                        default -> textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
                    }
                    if (enemy instanceof Ghost && !((Ghost) enemy).isVisible()) {
                        textGraphics.putString(position.getX(), position.getY(), " ");
                    } else {
                        textGraphics.putString(position.getX(), position.getY(), enemy.getEntity().getSymbol());
                    }
                });
    }

    public void eraseEnemies(Map<Room, List<Character>> allEnemies) {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        allEnemies.forEach((k, v) -> v.forEach(enemy -> {
            Position position = enemy.getPosition();
            textGraphics.putString(position.getX(), position.getY(), " ");
        }));
    }

    public void drawItems(Map<Room, List<OnMapItem>> allItems, Room room) {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        allItems.get(room).forEach(
                onMapItem -> {
                    Position position = onMapItem.position();
                    Item item = onMapItem.item();
                    textGraphics.putString(position.getX(), position.getY(), item.getEntity().getSymbol());
                });
    }

    public void render(RogueGame game) throws IOException {
        screen.clear();
        drawMapBorders();
        var levelMap = game.getLevelMap();
        Room playerRoom = game.getPlayerRoom();
        if (playerRoom != null) {
            drawItems(levelMap.getItems(), playerRoom);
            drawEnemies(levelMap.getEnemies(), playerRoom);
        }
        drawAllRooms(levelMap);
        drawAllCorridors(levelMap, playerRoom);
        drawPlayer(game.getPlayer());
        drawPlayerStats(game.getPlayer());
        drawLoggerText(game.getLogger());
        drawGameStats(game);
        drawHelpWindow();
        screen.refresh();
    }

    private void drawPlayerStats(Player player) {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(MAP_WIDTH + 2, 30, "Hits: ".toUpperCase() + player.getCurrentHP() + "(" + player.getMaxHP() + ")");
        textGraphics.putString(MAP_WIDTH + 2, 32, "Str: ".toUpperCase() + player.getStrength());
        textGraphics.putString(MAP_WIDTH + 2, 34, "Agi: ".toUpperCase() + player.getAgility());
        if (player.getWeaponEquipped() != null) {
            textGraphics.putString(MAP_WIDTH + 2, 36, "Weapon: ".toUpperCase() + player.getWeaponEquipped().getEquipName());
        }
        if (player.getWeaponEquipped() != null && player.getWeaponEquipped().isEnchanted()) {
            textGraphics.putString(MAP_WIDTH + 2, 38, "Enchanted damage: ".toUpperCase() + player.getWeaponEquipped().getEnchantDamage());
        }
    }

    public void erasePlayer(Player player) {
        Position position = player.getPosition();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(position.getX(), position.getY(), " ");
    }


    public void eraseItems(Map<Room, List<OnMapItem>> allItems) {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        allItems.forEach((k, v) -> v.forEach(item -> {
            Position position = item.position();
            textGraphics.putString(position.getX(), position.getY(), " ");
        }));
    }

    public void drawWeaponSelection(List<WeaponItem> weaponItems) throws IOException {
        screen.clear();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(5, 5, "Choose weapon: ");

        int cnt = 1;
        int lineN = 1;
        for (WeaponItem weaponItem : weaponItems) {
            textGraphics.putString(5, 5 + (cnt * 2), lineN + ") " + weaponItem.getInventoryDescription());
            cnt++;
            lineN++;
        }
        screen.refresh();
    }

    public void drawFoodSelection(List<FoodItem> foodItems) throws IOException {
        screen.clear();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(5, 5, "Choose food: ");

        int cnt = 1;
        int lineN = 1;
        for (FoodItem foodItem : foodItems) {
            textGraphics.putString(5, 5 + (cnt * 2), lineN + ") " + foodItem.getInventoryDescription());
            cnt++;
            lineN++;
        }
        screen.refresh();
    }

    public void drawPotionSelection(List<PotionItem> potionItems) throws IOException {
        screen.clear();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(5, 5, "Choose potion: ");

        int cnt = 1;
        int lineN = 1;
        for (PotionItem potionItem : potionItems) {
            textGraphics.putString(5, 5 + (cnt * 2), lineN + ") " + potionItem.getInventoryDescription());
            cnt++;
            lineN++;
        }
        screen.refresh();
    }

    public void drawScrollSelection(List<ScrollItem> scrollItems) throws IOException {
        screen.clear();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(5, 5, "Choose scroll: ");

        int cnt = 1;
        int lineN = 1;
        for (ScrollItem scrollItem : scrollItems) {
            textGraphics.putString(5, 5 + (cnt * 2), lineN + ") " + scrollItem.getInventoryDescription());
            cnt++;
            lineN++;
        }
        screen.refresh();
    }

    public void drawAllInventory(List<Item> items) throws IOException {
        screen.clear();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(5, 5, "Inventory: ");

        int cnt = 1;
        int lineN = 1;
        for (Item item : items) {
            textGraphics.putString(5, 5 + (cnt * 2), lineN + ") " + ((Storagable) item).getInventoryDescription());
            cnt++;
            lineN++;
        }
        screen.refresh();
    }

    public void drawHelp() throws IOException {
        screen.clear();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(5, 5, "Key bindings: ");
        List<String> keyBindings = List.of(
                "h - use weapon",
                "j - use food",
                "k - use potion",
                "e - use scroll",
                "i - open inventory",
                "q - close window",
                "p - open highscore"
        );

        int cnt = 1;
        for (String item : keyBindings) {
            textGraphics.putString(5, 5 + (cnt * 2), item);
            cnt++;
        }
        screen.refresh();
    }

    public void drawGameOverScreen(boolean isWinner, int activeBtn, RogueGame game) throws IOException {

        screen.clear();

        TextGraphics textGraphics = this.screen.newTextGraphics();
        if (isWinner){
            drawWinPicture(textGraphics);
        } else {
            drawLoosePicture(textGraphics);
        }


        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        textGraphics.putString(37, 39, "use the <- and -> keys to switch the buttons and");
        textGraphics.putString(55, 40, "enter to select:");


        if (activeBtn == 0){
            textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        } else {
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
        }
        textGraphics.setForegroundColor(TextColor.ANSI.BLACK);

        textGraphics.putString(38, 42, "                     ");
        textGraphics.putString(38, 43, "        EXIT         ");
        textGraphics.putString(38, 44, "                     ");

        if (activeBtn == 1){
            textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        } else {
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
        }
        textGraphics.setForegroundColor(TextColor.ANSI.BLACK);

        textGraphics.putString(62, 42, "                     ");
        textGraphics.putString(62, 43, "      NEW  GAME      ");
        textGraphics.putString(62, 44, "                     ");

        drawMapBorders();
        drawGameStats(game);
        drawLoggerText(game.getLogger());

        screen.refresh();
    }

    private void drawWinPicture(TextGraphics textGraphics) {
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);


        List<String> pictureLines = List.of(
                " _____  _____ _____ ____  _____ ____  _____ _________ _______    ",
                "|_   _||_   _|_   _|_   \\|_   _|_   \\|_   _|_   ___  |_   __ \\",
                "  | | /\\ | |   | |   |   \\ | |   |   \\ | |   | |_  \\_| | |__) |",
                "  | |/  \\| |   | |   | |\\ \\| |   | |\\ \\| |   |  _|  _  |  __ /",
                "  |   /\\   |  _| |_ _| |_\\   |_ _| |_\\   |_ _| |___/ |_| |  \\ \\_",
                "  |__/  \\__| |_____|_____|\\____|_____|\\____|_________|____| |___|",
                "",
                "",
                "                                           ,--.\n",
                "                                          {    }\n",
                "                                          K,   }\n",
                "                                         /  ~Y`\n",
                "                                    ,   /   /\n",
                "                                   {_'-K.__/\n",
                "                                     `/-.__L._\n",
                "                                     /  ' /`\\_}\n",
                "                                    /  ' /\n",
                "                            ____   /  ' /\n",
                "                     ,-'~~~~    ~~/  ' /_\n",
                "                   ,'             ``~~~  ',\n",
                "                  (                        Y\n",
                "                 {                         I\n",
                "                {      -                    `,\n",
                "                |       ',                   )\n",
                "                |        |   ,..__      __. Y\n",
                "                |    .,_./  Y ' / ^Y   J   )|\n",
                "                \\           |' /   |   |   ||\n",
                "                 \\          L_/    . _ (_,.'(\n",
                "                  \\,   ,      ^^\"\"' / |      )\n",
                "                    \\_  \\          /,L]     /\n",
                "                      '-_~-,       ` `   ./`\n",
                "                         `'{_            )\n",
                "                             ^^\\..___,.--`"
        );


        int r = 2;

        for (String line : pictureLines) {
            textGraphics.putString(30, r, line);
            r++;
        }

        textGraphics.putString(41, 37, "Congratulations! You've reached the end!");

    }

    private void drawLoosePicture(TextGraphics textGraphics){
        textGraphics.setForegroundColor(TextColor.ANSI.RED);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        // G
        textGraphics.putString(32, 10, "  GGGGG ");
        textGraphics.putString(32, 11, "GGGGGGGGG");
        textGraphics.putString(32, 12, "GG");
        textGraphics.putString(32, 13, "GG");
        textGraphics.putString(32, 14, "GG");
        textGraphics.putString(32, 15, "GG");
        textGraphics.putString(32, 16, "GG   GGGG");
        textGraphics.putString(32, 17, "GG     GG");
        textGraphics.putString(32, 18, "GGGGGGGGG");
        textGraphics.putString(32, 19, "  GGGGGG ");

        // A
        textGraphics.putString(47, 10, "  AAAAA  ");
        textGraphics.putString(47, 11, "AAAAAAAAA");
        textGraphics.putString(47, 12, "AA     AA");
        textGraphics.putString(47, 13, "AA     AA");
        textGraphics.putString(47, 14, "AA     AA");
        textGraphics.putString(47, 15, "AAAAAAAAA");
        textGraphics.putString(47, 16, "AAAAAAAAA");
        textGraphics.putString(47, 17, "AA     AA");
        textGraphics.putString(47, 18, "AA     AA");
        textGraphics.putString(47, 19, "AA     AA");

        // M
        textGraphics.putString(62, 10, "MM        MM");
        textGraphics.putString(62, 11, "MMM      MMM");
        textGraphics.putString(62, 12, "MMMM    MMMM");
        textGraphics.putString(62, 13, "MM MMMMMM MM");
        textGraphics.putString(62, 14, "MM   MM   MM");
        textGraphics.putString(62, 15, "MM        MM");
        textGraphics.putString(62, 16, "MM        MM");
        textGraphics.putString(62, 17, "MM        MM");
        textGraphics.putString(62, 18, "MM        MM");
        textGraphics.putString(62, 19, "MM        MM");

        // E
        textGraphics.putString(80, 10, "EEEEEEEEE");
        textGraphics.putString(80, 11, "EEEEEEEEE");
        textGraphics.putString(80, 12, "EE");
        textGraphics.putString(80, 13, "EE");
        textGraphics.putString(80, 14, "EEEEEEEEE");
        textGraphics.putString(80, 15, "EEEEEEEEE");
        textGraphics.putString(80, 16, "EE");
        textGraphics.putString(80, 17, "EE");
        textGraphics.putString(80, 18, "EEEEEEEEE");
        textGraphics.putString(80, 19, "EEEEEEEEE");

        // O
        textGraphics.putString(32, 26, "  OOOOO ");
        textGraphics.putString(32, 27, "OOOOOOOOO");
        textGraphics.putString(32, 28, "OO     OO");
        textGraphics.putString(32, 29, "OO     OO");
        textGraphics.putString(32, 30, "OO     OO");
        textGraphics.putString(32, 31, "OO     OO");
        textGraphics.putString(32, 32, "OO     OO");
        textGraphics.putString(32, 33, "OO     OO");
        textGraphics.putString(32, 34, "OOOOOOOOO");
        textGraphics.putString(32, 35, "  OOOOO ");

        // V
        textGraphics.putString(48, 26, "VV     VV");
        textGraphics.putString(48, 27, "VV     VV");
        textGraphics.putString(48, 28, "VV     VV");
        textGraphics.putString(48, 29, "VV     VV");
        textGraphics.putString(48, 30, "VV     VV");
        textGraphics.putString(48, 31, "VV     VV");
        textGraphics.putString(48, 32, " VV   VV ");
        textGraphics.putString(48, 33, " VV   VV ");
        textGraphics.putString(48, 34, "  VV VV  ");
        textGraphics.putString(48, 35, "   VVV   ");


        // E
        textGraphics.putString(64, 26, "EEEEEEEEE");
        textGraphics.putString(64, 27, "EEEEEEEEE");
        textGraphics.putString(64, 28, "EE");
        textGraphics.putString(64, 29, "EE");
        textGraphics.putString(64, 30, "EEEEEEEEE");
        textGraphics.putString(64, 31, "EEEEEEEEE");
        textGraphics.putString(64, 32, "EE");
        textGraphics.putString(64, 33, "EE");
        textGraphics.putString(64, 34, "EEEEEEEEE");
        textGraphics.putString(64, 35, "EEEEEEEEE");


        // R
        textGraphics.putString(80, 26, " RRRRRR  ");
        textGraphics.putString(80, 27, "RRRRRRRRR");
        textGraphics.putString(80, 28, "RR     RR");
        textGraphics.putString(80, 29, "RR     RR");
        textGraphics.putString(80, 30, "RRRRRRR ");
        textGraphics.putString(80, 31, "RRRRRR  ");
        textGraphics.putString(80, 32, "RR   RRR");
        textGraphics.putString(80, 33, "RR     RR");
        textGraphics.putString(80, 34, "RR     RR");
        textGraphics.putString(80, 35, "RR     RR");


        textGraphics.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        List<String> pictureLines = List.of(
                "        @@@@@@@S21@@@@@@\n",
                "       @@@@@LYJAJERK@@@@@\n",
                "     @@@@@@@@@@@@@@@@@@@@@@\n",
                "     @@@@@@@JACQUERA@@@@@@@@\n",
                "   /@@@@@@@@@@@@@@@@@@@@@@@@@\\\n",
                "   @@@@@@@@@MARGARIB@@@@@@@@@@\n",
                "  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n",
                " @@@@@@@@@@@@@@@/      \\@@@/   @",
                "@@@@@@@@@@@@@@@@\\      @@  @___@",
                "@@@@@@@@@@@@@ @@@@@@@@@@  | \\@@@@@",
                "@@@@@@@@@@@@@ @@@@@@@@@\\__@_/@@@@@",
                " @@@@@@@@@@@@@@@/,/,/./'/_|.\\'\\,\\",
                "   @@@@@@@@@@@@@|  | | | | | | | |",
                "                 \\_|_|_|_|_|_|_|_|"
        );


        int r = MAP_HEIGHT - 1;

        for (String line : pictureLines.reversed()) {
            textGraphics.putString(0, r, line);
            r--;
        }

    }

    public void drawPrevStatistics(List<StatisticsItem> list) throws IOException {
        screen.clear();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        int currLine = 0;
        for(StatisticsItem item : list) {
            currLine = drawStatistic(item, currLine);
        }
        screen.refresh();
    }

    private int drawStatistic(StatisticsItem statisticsItem, int line) {
        TextGraphics textGraphics = this.screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        if (line == 0) {
            textGraphics.putString(5, line + 2, "High scores: ");
        }
        textGraphics.putString(5, line + 4, "Game id: " + statisticsItem.getGameId());

        textGraphics.putString(5, line + 5, "Gold: " + statisticsItem.getGold());

        textGraphics.putString(5, line + 6, "Level: " + statisticsItem.getLevel());

        textGraphics.putString(5, line + 7, "Defeated enemies: " + statisticsItem.getEnemyKilled());

        textGraphics.putString(5, line + 8, "Eaten food: " + statisticsItem.getFoodConsumed());

        textGraphics.putString(5, line + 9, "Consumed Elixirs: " + statisticsItem.getPotionsConsumed());

        textGraphics.putString(5, line + 10, "Read scrolls: " + statisticsItem.getScrollsConsumed());

        textGraphics.putString(5, line + 11, "Struck strokes: " + statisticsItem.getAttacksDealt());

        textGraphics.putString(5, line + 12, "Missed strokes: " + statisticsItem.getAttacksReceived());

        textGraphics.putString(5, line + 13, "Passed cells: " + statisticsItem.getTilesPassed());

        textGraphics.putString(5, line + 14, "--------------------------------------------------");

        return line + 14;
    }

    public void drawExitGameDialog(int activeBtn, RogueGame game) throws IOException {
        screen.clear();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        List<String> pictureLines = List.of(
                "                      :::!~!!!!!:.",
                "                  .xUHWH!! !!?M88WHX:.",
                "                .X*#M@$!!  !X!M$$$$$$WWx:.",
                "               :!!!!!!?H! :!$!$$$$$$$$$$8X:",
                "              !!~  ~:~!! :~!$!#$$$$$$$$$$8X:",
                "             :!~::!H!<   ~.U$X!?R$$$$$$$$MM!",
                "             ~!~!!!!~~ .:XW$$$U!!?$$$$$$RMM!",
                "               !:~~~ .:!M\"T#$$$$WX??#MRRMMM!",
                "               ~?WuxiW*`   `\"#$$$$8!!!!??!!!",
                "             :X- M$$$$       `\"T#$T~!8$WUXU~",
                "            :%`  ~#$$$m:        ~!~ ?$$$$$$",
                "          :!`.-   ~T$$$$8xx.  .xWW- ~\"\"##*\"",
                ".....   -~~:<` !    ~?T#$$@@W@*?$$      /`",
                "W$@@M!!! .!~~ !!     .:XUW$W!~ `\"~:    :",
                "#\"~~`.:x%`!!  !H:   !WM$$$$Ti.: .!WUn+!`",
                ":::~:!!`:X~ .: ?H.!u \"$$$B$$$!W:U!T$$M~",
                ".~~   :X@!.-~   ?@WTWo(\"*$$$W$TH$! `",
                "Wi.~!X$?!-~    : ?$$$B$Wu(\"**$RM!",
                "$R@i.~~ !     :   ~$$$$$B$$en:``",
                "?MXT@Wx.~    :     ~\"##*$$$$M~"
        );


        int r = MAP_HEIGHT - 1;

        for (String line : pictureLines.reversed()) {
            textGraphics.putString(0, r, line);
            r--;
        }



        textGraphics.putString(40, 24, "Are you sure you want to finish the game?: ");
        textGraphics.putString(45, 28, "use the <- and -> keys to switch the buttons and");
        textGraphics.putString(60, 30, "enter to select:");

        if (activeBtn == 0){
            textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        } else {
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
        }
        textGraphics.setForegroundColor(TextColor.ANSI.BLACK);

        textGraphics.putString(38, 42, "                     ");
        textGraphics.putString(38, 43, "         NO          ");
        textGraphics.putString(38, 44, "                     ");

        if (activeBtn == 1){
            textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        } else {
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
        }
        textGraphics.setForegroundColor(TextColor.ANSI.BLACK);

        textGraphics.putString(62, 42, "                     ");
        textGraphics.putString(62, 43, "         YES         ");
        textGraphics.putString(62, 44, "                     ");


        drawGameStats(game);
        drawMapBorders();
        drawLoggerText(game.getLogger());

        screen.refresh();
    }

    public void drawMenu(int activeBtn) throws IOException {
        screen.clear();
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        List<String> pictureLines = List.of(
                "                 uuuuuuu\n",
                "             uu$$$$$$$$$$$uu",
                "          uu$$$$$$$$$$$$$$$$$uu",
                "         u$$$$$$$$$$$$$$$$$$$$$u",
                "        u$$$$$$$$$$$$$$$$$$$$$$$u",
                "       u$$$$$$$$$$$$$$$$$$$$$$$$$u",
                "       u$$$$$$$$$$$$$$$$$$$$$$$$$u",
                "       u$$$$$$\"   \"$$$\"   \"$$$$$$u",
                "       \"$$$$\"      u$u       $$$$\"",
                "        $$$u       u$u       u$$$",
                "        $$$u      u$$$u      u$$$",
                "         \"$$$$uu$$$   $$$uu$$$$\"",
                "          \"$$$$$$$\"   \"$$$$$$$\"",
                "            u$$$$$$$u$$$$$$$u",
                "             u$\"$\"$\"$\"$\"$\"$u",
                "  uuu        $$u$ $ $ $ $u$$       uuu",
                " u$$$$        $$$$$u$u$u$$$       u$$$$",
                "  $$$$$uu      \"$$$$$$$$$\"     uu$$$$$$",
                "u$$$$$$$$$$$uu    \"\"\"\"\"    uuuu$$$$$$$$$$\n",
                "$$$$\"\"\"$$$$$$$$$$uuu   uu$$$$$$$$$\"\"\"$$$\"\n",
                " \"\"\"      \"\"$$$$$$$$$$$uu \"\"$\"\"\"\n",
                "           uuuu \"\"$$$$$$$$$$uuu\n",
                "  u$$$uuu$$$$$$$$$uu \"\"$$$$$$$$$$$uuu$$$\n",
                "  $$$$$$$$$$\"\"\"\"           \"\"$$$$$$$$$$$\"\n",
                "   \"$$$$$\"                      \"\"$$$$\"\"\n",
                "     $$$\"         ROGUE           $$$$\"\n"
        );

        textGraphics.putString(70, 39, "LET'S PLAY A GAME");

        int r = 8;

        for (String line : pictureLines) {
            textGraphics.putString(58, r, line);
            r++;
        }




        if (activeBtn == 0){
            textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        } else {
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
        }
        textGraphics.setForegroundColor(TextColor.ANSI.BLACK);

        textGraphics.putString(55, 42, "                     ");
        textGraphics.putString(55, 43, "     LOAD  GAME      ");
        textGraphics.putString(55, 44, "                     ");

        if (activeBtn == 1){
            textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        } else {
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
        }
        textGraphics.setForegroundColor(TextColor.ANSI.BLACK);

        textGraphics.putString(80, 42, "                     ");
        textGraphics.putString(80, 43, "      NEW  GAME      ");
        textGraphics.putString(80, 44, "                     ");

        textGraphics.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(70, 47, "press 'Esc' to exit");
        textGraphics.putString(62, 2, "by students of School 21 by Sber");
        textGraphics.putString(63, 3, "jacquera   lyjajerk   margarib");

        screen.refresh();
    }

    public void drawSkullEyes() throws IOException {
        TextGraphics textGraphics = this.screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.putString(73, 17, "oO");
        textGraphics.putString(82, 17, "Oo");
        screen.refresh();
    }


}
