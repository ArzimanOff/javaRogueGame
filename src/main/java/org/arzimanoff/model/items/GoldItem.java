package org.arzimanoff.model.items;

import org.arzimanoff.model.common.GameEntity;

import java.util.Random;

public class GoldItem implements Item {
    private static final GameEntity entity = GameEntity.GOLD;
    private static final Random rand = new Random();
    private int amount;

    @Override
    public GameEntity getEntity() {
        return entity;
    }

    @Override
    public String getOnAcquireDescription() {
        return String.format("You found %d gold pieces", amount);
    }

    public int getAmount() {
        return amount;
    }

    /* Генерирует случайное количество золота на карте в зависимости от уровня */
    public static GoldItem randomizeMapGold() {
        GoldItem goldItem = new GoldItem();
        goldItem.amount = rand.nextInt(60) + 1;
        return goldItem;
    }

    /* Подсчет полученного золота с врага в зависимости от его сложности */
    public static int getFromEnemy(int enemyForce) {
        return rand.nextInt(20 + 10 * enemyForce) + 2;
    }


    @Override
    public String toString() {
        return "GoldItem{" +
               "amount=" + amount +
               '}';
    }
}
