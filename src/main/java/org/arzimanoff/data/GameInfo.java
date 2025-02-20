package org.arzimanoff.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameInfo{

	@JsonProperty("gold")
	private int gold;

	@JsonProperty("strength")
	private int strength;

	@JsonProperty("agility")
	private int agility;

	@JsonProperty("max_hp")
	private int maxHp;

	@JsonProperty("inventory")
	private List<InventoryItem> inventory;

	public void setGold(int gold){
		this.gold = gold;
	}

	public int getGold(){
		return gold;
	}

	public void setStrength(int strength){
		this.strength = strength;
	}

	public int getStrength(){
		return strength;
	}

	public void setAgility(int agility){
		this.agility = agility;
	}

	public int getAgility(){
		return agility;
	}

	public void setMaxHp(int maxHp){
		this.maxHp = maxHp;
	}

	public int getMaxHp(){
		return maxHp;
	}

	public void setInventory(List<InventoryItem> inventory){
		this.inventory = inventory;
	}

	public List<InventoryItem> getInventory(){
		return inventory;
	}

	@Override
	public String toString() {
		return "GameInfo{" +
			   "gold=" + gold +
			   ", strength=" + strength +
			   ", agility=" + agility +
			   ", maxHp=" + maxHp +
			   ", inventory=" + inventory +
			   '}';
	}
}