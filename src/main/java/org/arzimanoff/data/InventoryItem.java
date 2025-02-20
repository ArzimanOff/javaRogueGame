package org.arzimanoff.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryItem{

	@JsonProperty("item")
	private String item;

	@JsonProperty("name")
	private String name;

	public void setItem(String item){
		this.item = item;
	}

	public String getItem(){
		return item;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
	public String toString() {
		return "InventoryItem{" +
			   "item='" + item + '\'' +
			   ", name='" + name + '\'' +
			   '}';
	}
}