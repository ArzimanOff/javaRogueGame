package org.arzimanoff.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.arzimanoff.model.RogueGame;
import org.arzimanoff.model.entities.Player;
import org.arzimanoff.model.items.*;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InfoProcessor {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String SAVE_TO_FILE_NAME = "resources/info/rogue.json";

    private InfoProcessor() {}

    /**
     * Writes game statistics specified by {@code StatisticsItem} instance to json
     * @param statisticsItem Current game statistic storage
     */
    public static void writeStatistics(StatisticsItem statisticsItem) {
        GameStatistics gameStatistics = readStatisticsFromJson();
        if (gameStatistics != null) {
            var statistics = gameStatistics.getStatistics();
            if (gameStatistics.getStatistics() != null) {
                int gameId = statistics.getLast().getGameId() + 1;
                statisticsItem.setGameId(gameId);
                statistics.add(statisticsItem);
            }
        }

        if (gameStatistics == null) {
            gameStatistics = new GameStatistics();
        }

        if (gameStatistics.getStatistics() == null) {
            gameStatistics.setStatistics(new ArrayList<>());
            statisticsItem.setGameId(1);
            gameStatistics.getStatistics().add(statisticsItem);
        }

        writeStatisticsToJson(gameStatistics);
    }

    /**
     * Loads last saved game
     * @param game Current game
     * @return If the last saved game has loaded or not
     */
    public static boolean loadPrevGame(RogueGame game) {
        boolean result = false;
        GameStatistics gameStatistics = readStatisticsFromJson();

        if (gameStatistics != null && gameStatistics.getSavedGame() != null) {
            game.setCurrentLevel(gameStatistics.getSavedGame().getLevel());
            loadPlayerStats(game, gameStatistics.getSavedGame().getGameInfo());
            game.setStatisticsItem(gameStatistics.getStatistics().getLast());
            result = true;
        }
        return result;
    }

    /**
     * Saves current game to json
     * @param game Current game
     */
    public static void writeSaveGame(RogueGame game) {
        GameStatistics gameStatistics = readStatisticsFromJson();

        if (gameStatistics == null) {
            gameStatistics = new GameStatistics();
        }

        SavedGame savedGame = new SavedGame();
        GameInfo gameInfo = new GameInfo();
        savedGame.setLevel(game.getCurrentLevel());

        savePlayerStats(game, gameInfo);

        savedGame.setGameInfo(gameInfo);
        gameStatistics.setSavedGame(savedGame);

        writeStatisticsToJson(gameStatistics);
    }

    /**
     * Provides sorted game statistics
     * @return A list which contains statistic records
     */
    public static List<StatisticsItem> getStatistics() {
        GameStatistics gameStatistics = readStatisticsFromJson();
        List<StatisticsItem> statistics = null;

        if (gameStatistics != null && gameStatistics.getStatistics() != null) {
            statistics = sortStatistics(gameStatistics.getStatistics());
        }
        return statistics;
    }

    /**
     * Set empty values to saved game and statistics in json
     */
    public static void cleanInfoFile() {
        GameStatistics gameStatistics = readStatisticsFromJson();
        if (gameStatistics != null) {
            gameStatistics.setSavedGame(null);
            gameStatistics.setStatistics(null);
        }

        writeStatisticsToJson(gameStatistics);
    }

    /**
     * Read game statistics and saved games from json
     * @return GameStatistics record from json
     */
    private static GameStatistics readStatisticsFromJson() {
        GameStatistics gameStatistics = null;
        String pathToJson = getInfoFileResource();
        if (pathToJson != null) {
            try {
                gameStatistics = mapper.readValue(new File(pathToJson), GameStatistics.class);
            } catch (Exception ignored) {
            }
        }

        return gameStatistics;
    }

    /**
     * Writes game statistics and saved games from json
     * @param gameStatistics Current game statistics
     */
    private static void writeStatisticsToJson(GameStatistics gameStatistics) {
        String pathToJson = getInfoFileResource();
        if (pathToJson != null) {
            try {
                String json = mapper.writeValueAsString(gameStatistics);
                Files.writeString(Paths.get(pathToJson), json);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Produces an absolute path for json from application context
     * @return Absolute path of json
     */
    private static String getInfoFileResource() {
        String fileName = null;
        URL url = InfoProcessor.class.getClassLoader().getResource(".");
        if (url != null) {
            try {
                fileName = url.getPath();
                fileName = fileName.substring(1, fileName.lastIndexOf("build")) + SAVE_TO_FILE_NAME;
            } catch (Exception ignored) {
            }
        }
        return fileName;
    }

    /**
     * Sorts statistic using special {@code Comparator}
     * @param items List with statistic records to sort
     * @return Sorted statistics list
     */
    private static List<StatisticsItem> sortStatistics(List<StatisticsItem> items) {
        return items.stream()
                .sorted(Comparator.comparingInt(StatisticsItem::getGold).reversed())
                .toList();
    }

    /**
     * Loads player stats to game model
     * @param game Current game
     * @param gameInfo Game information parsed from json
     */
    private static void loadPlayerStats(RogueGame game, GameInfo gameInfo) {
        Player player = game.getPlayer();

        game.setGoldAmount(gameInfo.getGold());

        player.setMaxHP(gameInfo.getMaxHp());
        player.setStrength(gameInfo.getStrength());
        player.setAgility(gameInfo.getAgility());
        player.setCurrentHP(player.getMaxHP());

        fillInventory(player.getSatchel(), gameInfo);

    }

    /**
     * Saves player stats from game model to json
     * @param game Current game
     * @param gameInfo Game information to save
     */
    private static void savePlayerStats(RogueGame game, GameInfo gameInfo) {
        Player player = game.getPlayer();

        gameInfo.setGold(game.getGoldAmount());
        gameInfo.setMaxHp(player.getMaxHP());
        gameInfo.setStrength(player.getStrength());
        gameInfo.setAgility(player.getAgility());

        saveInventory(player.getSatchel(), gameInfo);
    }

    /**
     * Fills the {@code GameInfo} instance with current's game inventory items
     * @param satchel Player's satchel
     * @param gameInfo Game information to save
     */
    private static void saveInventory(Satchel satchel, GameInfo gameInfo) {
        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (Item item : satchel.getItems()) {
            InventoryItem inventoryItem = new InventoryItem();
            if (item instanceof ScrollItem) {
                inventoryItem.setItem("scroll");
            } else if (item instanceof PotionItem) {
                inventoryItem.setItem("potion");
            } else if (item instanceof FoodItem) {
                inventoryItem.setItem("food");
            } else if (item instanceof WeaponItem) {
                inventoryItem.setItem("weapon");
            }

            if (item instanceof Storagable storagableItem) {
                inventoryItem.setName(storagableItem.getName());
            }

            inventoryItems.add(inventoryItem);
        }

        gameInfo.setInventory(inventoryItems);
    }

    /**
     * Fills current's game model inventory with items from json represented by {@code GameInfo} instance
     * @param satchel Player's satchel
     * @param gameInfo Game information parsed from json
     */
    private static void fillInventory(Satchel satchel, GameInfo gameInfo) {
        for (InventoryItem item : gameInfo.getInventory()) {
            switch (item.getItem()) {
                case "scroll":
                    satchel.addItem(ScrollItem.Creator.createForName(item.getName()));
                    break;
                case "potion":
                    satchel.addItem(PotionItem.Creator.createForName(item.getName()));
                    break;
                case "food":
                    satchel.addItem(FoodItem.Creator.createForName(item.getName()));
                    break;
                case "weapon":
                    satchel.addItem(WeaponItem.Creator.createForName(item.getName()));
                    break;
                default:
            }
        }
    }
}
