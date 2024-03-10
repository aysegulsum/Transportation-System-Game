/*
 * Ayse Gulsum Eren 150120005
 * Sena VatanSever 150119755
 * Farouk Tijjani Mohammed Deribe 150119544
 */
//This class for defining and creating vehicle object for game
package application;

import javafx.scene.image.Image;

class Vehicle {
	int cityID;
	int capacity;
	Image image;
	
	public Vehicle(int cityID, int capacity) {
		this.capacity = capacity;
		this.cityID = cityID;
	}
	//this method returns needed vehicle image according to capacity 
	public Image whichVehicle() {	
		if(capacity <= 6) {
		image = new Image("photos/car.jpg");
		}
		else if(capacity < 14 ) {
	    image = new Image("photos/minibus.jpg");
		}
		else {
		image = new Image("photos/bus.jpg");
		}
		return image;
	}

	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cellID) {
		this.cityID = cellID;
	}

	public int getCapacity() {
		return capacity;
	}
	
}