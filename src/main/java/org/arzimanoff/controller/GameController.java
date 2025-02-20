package org.arzimanoff.controller;

import com.googlecode.lanterna.input.KeyType;
import org.arzimanoff.data.InfoProcessor;
import org.arzimanoff.model.RogueGame;
import org.arzimanoff.model.entities.Player;
import org.arzimanoff.model.items.FoodItem;
import org.arzimanoff.model.items.PotionItem;
import org.arzimanoff.model.items.ScrollItem;
import org.arzimanoff.model.items.WeaponItem;
import org.arzimanoff.presentation.UI;

import java.io.IOException;
import java.util.stream.Collectors;

public class GameController {
    private static final int WIN_LEVEL = 21;
    private final UI ui;
    private RogueGame game;

    public GameController() throws IOException {
        this.game = new RogueGame();
        this.ui = new UI();
    }

    public void run() throws IOException {
        if (proccessInitiateGame()) {
            render();
            ui.getDrawer().drawMapBorders();
            gameLoop();
        }
    }

    private boolean proccessInitiateGame() throws IOException {
        int loadGame = 0;
        int startNewGame = 1;
        int activeBtn = startNewGame;
        boolean initiateGame = true;

        ui.getDrawer().drawMenu(activeBtn);

        while (true) {
            var keyStroke = ui.getScreen().pollInput();

            if (keyStroke == null) {
                continue; // Пропускаем итерацию, если нет ввода
            }

            if (keyStroke.getKeyType() == KeyType.Escape) {
                try {
                    ui.getDrawer().drawSkullEyes();
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ui.getScreen().stopScreen();
                initiateGame = false;
                break;
            }

            // Обрабатываем специальные клавиши
            switch (keyStroke.getKeyType()) {
                case ArrowLeft, ArrowRight -> {
                    activeBtn = (activeBtn == startNewGame) ? loadGame : startNewGame;
                    ui.getDrawer().drawMenu(activeBtn);
                }

                case Enter -> {
                    if (activeBtn == loadGame) {
                        if (!InfoProcessor.loadPrevGame(game)) {
                            System.out.println("No previous saved games. Starting new game.");
                        }
                    }
                    return initiateGame;
                }
                case Escape -> {
                    try {
                        ui.getDrawer().drawSkullEyes();
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ui.getScreen().stopScreen();
                    initiateGame = false;
                    return initiateGame;
                }
            }
        }
        return initiateGame;
    }

    public void gameLoop() throws IOException {
        boolean gameOver = false;
        while (!gameOver) {
            var keyStroke = ui.getScreen().pollInput();

            if (keyStroke == null) {
                continue; // Пропускаем итерацию, если нет ввода
            }

            Character input = keyStroke.getCharacter();
            if (input == null) {
                continue; // Пропускаем итерацию, если нажатая клавиша не символ
            }

            int xMove = 0;
            int yMove = 0;
            switch (input) {
                case 'w' -> yMove = -1;

                case 's' -> yMove = 1;

                case 'a' -> xMove = -1;

                case 'd' -> xMove = 1;

                case 'h' -> {
                    openSelectionWindow(input, game.getPlayer());
                    int choice = itemPlayerChoice();
                    if (choice != 0) {
                        game.useWeapon(choice);
                    }
                    render();
                }
                case 'j' -> {
                    openSelectionWindow(input, game.getPlayer());
                    int choice = itemPlayerChoice();
                    if (choice != 0) {
                        game.useFood(choice);
                    }
                    render();
                }
                case 'k' -> {
                    openSelectionWindow(input, game.getPlayer());
                    int choice = itemPlayerChoice();
                    if (choice != 0) {
                        game.usePotion(choice);
                    }
                    render();
                }
                case 'e' -> {
                    openSelectionWindow(input, game.getPlayer());
                    int choice = itemPlayerChoice();
                    if (choice != 0) {
                        game.useScroll(choice);
                    }
                    render();
                }

                case 'i', 'u', 'p' -> {
                    openSelectionWindow(input, game.getPlayer());
                    itemPlayerChoice();
                    render();
                }

                case 'q' -> {
                    gameOver = exitGame();
                    if (!gameOver) {
                        render();
                    }
                }

                default -> {
                    continue;
                }
            }

            if (xMove != 0 || yMove != 0) {
                erase();
                game.movePlayer(xMove, yMove);
                if (game.getPlayer().isAttack()) {
                    render();
                    waitForSpace();
                    game.getPlayer().setAttack(false);
                }
                game.checkItemOnMap();
                game.moveEnemy();
                if (!game.getPlayer().isAlive()) {
                    gameOver = gameOver(false);
                }
                if (game.getCurrentLevel() > WIN_LEVEL){
                    gameOver = gameOver(true);
                }
                render();
            }
        }
    }

    private boolean exitGame() throws IOException {
        int yes = 1;
        int no = 0;
        int chosenBtn = no;

        showExitGameDialog(chosenBtn);
        while (true) {
            var keyStroke = ui.getScreen().pollInput();

            if (keyStroke == null) {
                continue; // Пропускаем итерацию, если нет ввода
            }


            // Обрабатываем специальные клавиши
            switch (keyStroke.getKeyType()) {
                case ArrowLeft, ArrowRight -> {
                    chosenBtn = (chosenBtn == yes) ? no : yes;
                    showExitGameDialog(chosenBtn);
                }

                case Enter -> {
                    if (chosenBtn == no) {
                        return false;
                    } else {
                        gameOver(false);
                        return true;
                    }
                }
            }
        }
    }

    private boolean gameOver(boolean isWinner) throws IOException {
        int restart = 1;
        int exit = 0;
        int chosenBtn = restart;

        InfoProcessor.writeStatistics(game.getStatisticsItem());

        showGameOverScreen(isWinner, chosenBtn);

        while (true) {
            var keyStroke = ui.getScreen().pollInput();

            if (keyStroke == null) {
                continue; // Пропускаем итерацию, если нет ввода
            }

            if (keyStroke.getKeyType() == KeyType.Escape) {
                ui.getScreen().stopScreen();
                break;
            }

            // Обрабатываем специальные клавиши
            switch (keyStroke.getKeyType()) {
                case ArrowLeft, ArrowRight -> {
                    chosenBtn = (chosenBtn == restart) ? exit : restart;
                    showGameOverScreen(isWinner, chosenBtn);
                }

                case Enter -> {
                    if (chosenBtn == exit) {
                        ui.getScreen().stopScreen();
                    } else {
                        this.game = new RogueGame();
                        this.run();
                    }
                }
                case Escape -> {
                    ui.getScreen().stopScreen();
                }
            }
        }

        return true;
    }

    private void showGameOverScreen(boolean isWinner, int chosenBtn) throws IOException {
        ui.getDrawer().drawGameOverScreen(isWinner, chosenBtn, game);
    }

    private void showExitGameDialog(int chosenBtn) throws IOException {
        ui.getDrawer().drawExitGameDialog(chosenBtn, game);
    }


    private int itemPlayerChoice() throws IOException {
        int choice = 0;
        while (true) {
            var keyStroke = ui.getScreen().pollInput();
            if (keyStroke == null) {
                continue; // Пропускаем итерацию, если нет ввода
            }

            Character input = keyStroke.getCharacter();
            if (input == null) {
                continue; // Пропускаем итерацию, если нажатая клавиша не символ
            }

            if ('q' == input) {
                break;
            } else {
                try {
                    choice = Integer.parseInt(String.valueOf(input));
                    break;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return choice;
    }


    private void waitForSpace() throws IOException {
        boolean spacePushed = false;
        while (!spacePushed) {
            var keyStroke = ui.getScreen().pollInput();

            if (keyStroke == null) {
                continue; // Пропускаем итерацию, если нет ввода
            }
            var input = keyStroke.getCharacter();
            if (input != null && input == ' ') {
                spacePushed = true;
            }

        }

    }

    private void openSelectionWindow(char ch, Player player) throws IOException {
        switch (ch) {
            case 'h' -> {
                var weaponItems = player.getSatchel()
                        .getItems()
                        .stream()
                        .filter(item -> item instanceof WeaponItem)
                        .map(item -> (WeaponItem) item)
                        .collect(Collectors.toList());
                ui.getDrawer().drawWeaponSelection(weaponItems);
            }
            case 'j' -> {
                var foodItems = player.getSatchel()
                        .getItems()
                        .stream()
                        .filter(item -> item instanceof FoodItem)
                        .map(item -> (FoodItem) item)
                        .collect(Collectors.toList());
                ui.getDrawer().drawFoodSelection(foodItems);
            }
            case 'k' -> {
                var potionItems = player.getSatchel()
                        .getItems()
                        .stream()
                        .filter(item -> item instanceof PotionItem)
                        .map(item -> (PotionItem) item)
                        .collect(Collectors.toList());
                ui.getDrawer().drawPotionSelection(potionItems);
            }
            case 'e' -> {
                var scrollItems = player.getSatchel()
                        .getItems()
                        .stream()
                        .filter(item -> item instanceof ScrollItem)
                        .map(item -> (ScrollItem) item)
                        .collect(Collectors.toList());
                ui.getDrawer().drawScrollSelection(scrollItems);
            }
            case 'i' -> ui.getDrawer().drawAllInventory(player.getSatchel().getItems());
            case 'u' -> ui.getDrawer().drawHelp();
            case 'p' -> {
                if (InfoProcessor.getStatistics() != null) {
                    ui.getDrawer().drawPrevStatistics(
                            InfoProcessor.getStatistics()
                                    .stream()
                                    .limit(3)
                                    .toList()
                    );
                }
            }
        }
    }


    private void erase() {
        ui.getDrawer().erasePlayer(game.getPlayer());
        ui.getDrawer().eraseItems(game.getLevelMap().getItems());
        ui.getDrawer().eraseEnemies(game.getLevelMap().getEnemies());
        ui.getDrawer().eraseEnemies(game.getLevelMap().getEnemies());
    }

    private void render() throws IOException {
        ui.getDrawer().render(game);
    }
}
