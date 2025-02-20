package org.arzimanoff.model.common;

public enum GameEntity {
    PLAYER("@", "player", "white"),
    GHOST("g", "ghost", "white"),
    OGRE("o", "ogre", "yellow"),
    SNAKE_MAGE("s", "snake mage", "white"),
    VAMPIRE("v", "vampire", "red"),
    ZOMBIE("z", "zombie", "green"),
    FOOD(":", "food", "white"),
    POTION("!", "potion", "white"),
    GOLD("*", "gold", "white"),
    SCROLL("?", "scroll", "white"),
    WEAPON(")", "weapon", "white");


    private final String symbol;
    private final String color;
    private final String name;

    GameEntity(String symbol,String name, String color) {
        this.symbol = symbol;
        this.color = color;
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
