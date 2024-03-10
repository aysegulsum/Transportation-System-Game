/*
 * Ayse Gulsum Eren 150120005
 * Sena VatanSever 150119755
 * Farouk Tijjani Mohammed Deribe 150119544
 */
//This class for defining level properties which is needed for game
package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Level {
	private static ArrayList<City> cities = new ArrayList<City>();
	private ArrayList<Passenger> passengers = new ArrayList<Passenger>();
	private static ArrayList<FixedPoint> fixedPoints = new ArrayList<FixedPoint>();
	static int score;
	Vehicle vehicle;
	int numOfPassengersTransferred;
	int levelNum = 1;

	/*
	 * this method reads level files according to level number. Then divides given
	 * informations to groups and assign them to necessary variable using first
	 * words
	 */
	public void seperateInput() {
		try {
			Scanner scanner = new Scanner(new FileReader("levels/level" + levelNum + ".txt"));
			while (scanner.hasNextLine()) {
				String info = scanner.nextLine();
				String[] infoArray = info.split(",");

				if ("city".equalsIgnoreCase(infoArray[0])) {
					String name = (infoArray[1]);
					int cellid = Integer.valueOf(infoArray[2]);
					int cityid = Integer.valueOf(infoArray[3]);
					City city = new City(name, cellid, cityid);
					this.addCity(city);
				} else if ("passenger".equalsIgnoreCase(infoArray[0])) {
					int num = Integer.valueOf(infoArray[1]);
					int startcityid = Integer.valueOf(infoArray[2]);
					int destcityid = Integer.valueOf(infoArray[3]);
					Passenger passenger = new Passenger(num, startcityid, destcityid);
					this.addPassenger(passenger);
				} else if ("vehicle".equalsIgnoreCase(infoArray[0])) {

					int startcityid = Integer.valueOf(infoArray[1]);
					int capacity = Integer.valueOf(infoArray[2]);
					Vehicle vehicle = new Vehicle(startcityid, capacity);
					this.vehicle = vehicle;

				} else if ("fixed".equalsIgnoreCase(infoArray[0])) {
					int cellid = Integer.valueOf(infoArray[1]);
					FixedPoint fixed = new FixedPoint(cellid);
					this.addFixedPoint(fixed);

				}
			}

			scanner.close();
		} catch (FileNotFoundException e) {

		}
	}

	/*
	 * checks passenger arrayList to be sure that level is completed if level is
	 * completed it discharges fixedPoints and cities arrayLists for another level
	 */
	public boolean isCompleted() {
		boolean result = false;
		if (passengers.isEmpty()) {
			result = true;
			Level.fixedPoints.clear();
			Level.cities.clear();
		}
		return result;

	}

	// computes and returns the distance between two cities
	public double distance(double r1, double c1, double r2, double c2) {
		return Math.ceil(Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(c2 - c1, 2)));
	}

	// chooses the best option which has most crowded passenger group
	public void choosePassenger(City start, City dest) {
		Passenger temp = new Passenger(0, 1, 2);
		int i;
		// find the most crowded passenger group
		for (i = 0; i < this.getPassangers().size(); i++) {
			if (this.getPassangers().get(i).getStart().equals(start)
					&& this.getPassangers().get(i).getDest().equals(dest)) {

				if (temp.getNumOfPeople() < this.getPassangers().get(i).getNumOfPeople())
					temp = this.getPassangers().get(i);
				this.getPassangers().remove(this.getPassangers().get(i));
				this.getPassangers().add(temp);
			}
		}
		// arrange the numOfPassengersTransferred according to vehicle capacity and
		// chosen passenger group
		if (this.vehicle.getCapacity() < temp.getNumOfPeople()) {
			temp.setNumOfPeople(temp.getNumOfPeople() - this.vehicle.getCapacity());
			this.numOfPassengersTransferred = this.vehicle.getCapacity();

		}

		// if there are empty seats fill them from another passenger group
		else if (this.vehicle.getCapacity() > temp.getNumOfPeople()) {
			this.getPassangers().remove(temp);

			Passenger addTemp = new Passenger(0, 1, 2);

			int emptySeat = this.vehicle.getCapacity() - temp.getNumOfPeople();

			for (i = 0; i < this.getPassangers().size(); i++) {
				if (this.getPassangers().get(i).getStart().equals(start)
						&& this.getPassangers().get(i).getDest().equals(dest)) {

					if (this.getPassangers().get(i).getNumOfPeople() > emptySeat) {
						addTemp = this.getPassangers().get(i);
						this.getPassangers().remove(this.getPassangers().get(i));
						this.getPassangers().add(addTemp);
						this.numOfPassengersTransferred = this.vehicle.getCapacity();
					} else if (this.getPassangers().get(i).getNumOfPeople() == emptySeat) {
						this.getPassangers().remove(this.getPassangers().get(i));
						this.numOfPassengersTransferred = this.vehicle.getCapacity();
					} else {
						this.numOfPassengersTransferred = temp.getNumOfPeople()
								+ this.getPassangers().get(i).getNumOfPeople();
					}
				}
			}
			addTemp.setNumOfPeople(addTemp.getNumOfPeople() - emptySeat);
			this.numOfPassengersTransferred = temp.getNumOfPeople();

		} else if (this.vehicle.getCapacity() == temp.getNumOfPeople()) {
			this.getPassangers().remove(temp);
			this.numOfPassengersTransferred = this.vehicle.getCapacity();
		} else
			this.numOfPassengersTransferred = 0;

	}

	// returns the passenger group informations for chosen city
	public String writePassengers(City city) {
		String message = "";
		// groups who wants to leave from city
		for (int i = 0; i < this.getPassangers().size(); i++) {
			if (this.getPassangers().get(i).getStart().equals(city)) {
				message += city.getName() + " > " + this.getPassangers().get(i).getDest().getName() + " ("
						+ this.getPassangers().get(i).getNumOfPeople() + " Passengers)\n";
			}
		}

		// groups who wants to arrive city
		for (int i = 0; i < this.getPassangers().size(); i++) {
			if (this.getPassangers().get(i).getDest().equals(city)) {
				message += this.getPassangers().get(i).getStart().getName() + " > " + city.getName() + " ("
						+ this.getPassangers().get(i).getNumOfPeople() + " Passengers)\n";
			}
		}
		return message;
	}

	// returns city information
	public String writeCity(City start, City dest) {
		return dest.getName()
				+ " (City ID: " + dest.getcityID() + ", " + "Distance: " + this.distance((start.getcellID() - 1) / 10,
						(start.getcellID() - 1) % 10, (dest.getcellID() - 1) / 10, (dest.getcellID() - 1) % 10)
				+ ", " + "Vehicle Capacity: " + this.vehicle.capacity + ")\n";

	}

	// calculates the score per transportation and updates the score variable in
	// level class
	public void calculateScore(City start, City dest) {
		double cost = this.distance((start.getcellID() - 1) / 10, (start.getcellID() - 1) % 10,
				(dest.getcellID() - 1) / 10, (dest.getcellID() - 1) % 10);
		// we use cost * 2 because if we use 0.2 moveScore will be under 0 for most of
		// the cases
		double income = this.numOfPassengersTransferred * (cost * 2);
		double moveScore = income - cost;
		Level.score += moveScore;

	}

	// getter and setter methods
	public void addCity(City city) {
		cities.add(city);
	}

	public void addPassenger(Passenger p) {
		passengers.add(p);
	}

	public void addFixedPoint(FixedPoint f) {
		fixedPoints.add(f);
	}

	public static ArrayList<City> getCities() {
		return cities;
	}

	public ArrayList<Passenger> getPassangers() {
		return passengers;
	}

	public static ArrayList<FixedPoint> getFixedPoints() {
		return fixedPoints;
	}

}
