package org.arzimanoff.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SavedGame{

	@JsonProperty("id")
	private int id;

	@JsonProperty("level")
	private int level;

	@JsonProperty("game_info")
	private GameInfo gameInfo;

	public void setLevel(int level){
		this.level = level;
	}

	public int getLevel(){
		return level;
	}

	public void setGameInfo(GameInfo gameInfo){
		this.gameInfo = gameInfo;
	}

	public GameInfo getGameInfo(){
		return gameInfo;
	}

	@Override
	public String toString() {
		return "SavedGame{" +
			   "id=" + id +
			   ", level=" + level +
			   ", gameInfo=" + gameInfo +
			   '}';
	}
}