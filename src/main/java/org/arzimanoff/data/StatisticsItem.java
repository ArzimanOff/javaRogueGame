package org.arzimanoff.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatisticsItem{

	@JsonProperty("gold")
	private int gold;

	@JsonProperty("tiles_passed")
	private int tilesPassed;

	@JsonProperty("enemy_killed")
	private int enemyKilled;

	@JsonProperty("attacks_dealt")
	private int attacksDealt;

	@JsonProperty("level")
	private int level;

	@JsonProperty("id")
	private int gameId;

	@JsonProperty("food_consumed")
	private int foodConsumed;

	@JsonProperty("attacks_received")
	private int attacksReceived;

	@JsonProperty("scrolls_consumed")
	private int scrollsConsumed;

	@JsonProperty("potions_consumed")
	private int potionsConsumed;

	public void setGold(int gold){
		this.gold = gold;
	}

	public int getGold(){
		return gold;
	}

	public void setTilesPassed(int tilesPassed){
		this.tilesPassed = tilesPassed;
	}

	public int getTilesPassed(){
		return tilesPassed;
	}

	public void setEnemyKilled(int enemyKilled){
		this.enemyKilled = enemyKilled;
	}

	public int getEnemyKilled(){
		return enemyKilled;
	}

	public void setAttacksDealt(int attacksDealt){
		this.attacksDealt = attacksDealt;
	}

	public int getAttacksDealt(){
		return attacksDealt;
	}

	public void setLevel(int level){
		this.level = level;
	}

	public int getLevel(){
		return level;
	}

	public void setGameId(int id){
		this.gameId = id;
	}

	@Override
	public String toString() {
		return "StatisticsItem{" +
				"gold=" + gold +
				", tilesPassed=" + tilesPassed +
				", enemyKilled=" + enemyKilled +
				", attacksDealt=" + attacksDealt +
				", level=" + level +
				", gameId=" + gameId +
				", foodConsumed=" + foodConsumed +
				", attacksReceived=" + attacksReceived +
				", scrollsConsumed=" + scrollsConsumed +
				", potionsConsumed=" + potionsConsumed +
				'}';
	}

	public int getGameId(){
		return gameId;
	}

	public void setFoodConsumed(int foodConsumed){
		this.foodConsumed = foodConsumed;
	}

	public int getFoodConsumed(){
		return foodConsumed;
	}

	public void setAttacksReceived(int attacksReceived){
		this.attacksReceived = attacksReceived;
	}

	public int getAttacksReceived(){
		return attacksReceived;
	}

	public void setScrollsConsumed(int scrollsConsumed){
		this.scrollsConsumed = scrollsConsumed;
	}

	public int getScrollsConsumed(){
		return scrollsConsumed;
	}

	public void setPotionsConsumed(int potionsConsumed){
		this.potionsConsumed = potionsConsumed;
	}

	public int getPotionsConsumed(){
		return potionsConsumed;
	}
}