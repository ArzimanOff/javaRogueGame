package org.arzimanoff.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameStatistics{

	@JsonProperty("saved_game")
	private SavedGame savedGame;

	@JsonProperty("statistics")
	private List<StatisticsItem> statistics;

	public void setSavedGame(SavedGame savedGame){
		this.savedGame = savedGame;
	}

	public SavedGame getSavedGame(){
		return savedGame;
	}

	public void setStatistics(List<StatisticsItem> statistics){
		this.statistics = statistics;
	}

	public List<StatisticsItem> getStatistics(){
		return statistics;
	}
}