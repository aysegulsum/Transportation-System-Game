/*
 * Ayse Gulsum Eren 150120005
 * Sena VatanSever 150119755
 * Farouk Tijjani Mohammed Deribe 150119544
 */
//This class for defining and creating city object for game
package application;

class City {
	
	private String name = "";
	private int cellID;
	private int cityID;
	
	public City() {	
	}
	
	public City(String name,int cellID,int cityID) {
		this.name = name;
		this.cellID = cellID;
		this.cityID = cityID;
	}
	
	public String getName() {
		return name;
	}
	
	public int getcellID() {
		return cellID;
	}
	
	public int getcityID() {
		return cityID;
	}
	
    //There isn't any city who has existing city name so if city names are same this 2 cities are also same city
	public boolean equals(City city) {
		if(this.getName() == city.getName())
			return true;
		else
			return false;		
	}

}
