/*
 * Ayse Gulsum Eren 150120005
 * Sena VatanSever 150119755
 * Farouk Tijjani Mohammed Deribe 150119544
 */
//This class for defining and creating passenger object for game
package application;

class Passenger{
	
	private City start;
	private City dest;
	private int numOfPeople;// passenger number who waits for transportation
		
	public Passenger(int num, int startID, int destID) {
		this.numOfPeople = num;
		//finds the start city from cities array in level class using city ID
		for(int i=0; i< Level.getCities().size();i++) {
			if(Level.getCities().get(i).getcityID() == startID)
				this.start = Level.getCities().get(i);
		}
		//finds the destination city from cities array in level class using city ID
		for(int i=0; i< Level.getCities().size();i++) {
			if(Level.getCities().get(i).getcityID() == destID)
				this.dest = Level.getCities().get(i);
		}

	}
	public City getStart() {
		return start;
	}
	public City getDest() {
		return dest;
	}	
	public int getNumOfPeople() {
		return numOfPeople;
	}
	
	public void setNumOfPeople(int num) {
		this.numOfPeople= num;
	}
}