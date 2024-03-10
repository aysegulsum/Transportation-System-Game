/*
 * Ayse Gulsum Eren 150120005
 * Sena VatanSever 150119755
 * Farouk Tijjani Mohammed Deribe 150119544
 */

//The purpose of this program is creating a game using javaFx properties.  

package application;

import java.io.File;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Main extends Application {

	private Path path = new Path();
	int cellID;
	Level level = new Level();
	Cell cell;
	City start, dest;
	GridPane pane;
	BorderPane borderPane = new BorderPane();
	Pane root = new Pane();
	Label cityInfo;
	Label passengerInfo;
	Text text2;
	Text text1;
	Button nextLevel;

	@Override
	public void start(Stage primaryStage) {
		try {
			
			/*Button startGame = new Button("Start Game");
			Text info= new Text("Same clues about game\n"
					+ "Please click city buttons to see informations about clicked city and passengers");
			
			borderPane.setCenter(startGame);
			
			borderPane.setTop(info);
			startGame.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {			*/
			// fills level variables
			level.seperateInput();

			
//upper pane
			text1 = new Text("Level #" + level.levelNum);
			text2 = new Text("Score: " + Level.score);

			nextLevel = new Button("Next Level >>");
			nextLevel.setDisable(true);
			nextLevel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {

					// update level variables and show levelNum
					level.seperateInput();
					text1.setText("Level #" + level.levelNum);

					// clear game pane for new level
					pane.getChildren().clear();
					root.getChildren().clear();
					// create new game pane
					for (int i = 0; i < 10; i++)
						for (int j = 0; j < 10; j++)
							pane.add(new Cell(), j, i);

					// add city buttons for new level
					for (int i = 0; i < Level.getCities().size(); i++) {
						int r = ((Level.getCities().get(i).getcellID() - 1) / 10);
						int c = ((Level.getCities().get(i).getcellID() - 1) % 10);

						cell = new Cell();

						cell.createButtontoCity(Level.getCities().get(i));
						cell.addStation();
						pane.add(cell, c, r);
					}

					// add fixed points for new level
					for (int i = 0; i < Level.getFixedPoints().size(); i++) {
						int r = ((Level.getFixedPoints().get(i).getCellID() - 1) / 10);
						int c = ((Level.getFixedPoints().get(i).getCellID() - 1) % 10);

						cell = new Cell();
						ImageView view = new ImageView(new Image("photos/fixedPoint.png"));
						view.setFitHeight(40);
						view.setFitWidth(40);
						cell.setCenter(view);
						pane.add(cell, c, r);

					}

					// add vehicle image to start city for new level
					for (int i = 0; i < Level.getCities().size(); i++) {
						if (Level.getCities().get(i).getcityID() == level.vehicle.getCityID())
							cellID = Level.getCities().get(i).getcellID();
					}
					int r = ((cellID - 1) / 10);
					int c = ((cellID - 1) % 10);

					cell = new Cell();
					ImageView view = new ImageView(level.vehicle.whichVehicle());
					view.setFitHeight(30);
					view.setFitWidth(30);
					cell.setTop(view);
					BorderPane.setAlignment(view, Pos.TOP_LEFT);
					pane.add(cell, c, r);

					// After nextLevel button clicked make it isn't able to click
					nextLevel.setDisable(true);

					// if game over terminate the message
					if (level.levelNum > 5) {
						pane.getChildren().clear();
						root.getChildren().clear();

						Text terminatingMessage = new Text("GAME IS FINISHED \n" + " Your Score is: " + Level.score);
						terminatingMessage.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 15));
						terminatingMessage.setFill(Color.PINK);
						text1.setText("Level #");
						cityInfo.setText("");
						
						borderPane.setCenter(terminatingMessage);
					}
					
					root.getChildren().add(pane);
				}
			});
			BorderPane upper = new BorderPane();

			upper.setPadding(new Insets(5, 10, 5, 5));
			upper.setLeft(text1);
			upper.setCenter(text2);
			upper.setRight(nextLevel);

//bottom pane
			BorderPane bottom = new BorderPane();
			bottom.setPadding(new Insets(20, 15, 20, 15));

			cityInfo = new Label();
			passengerInfo = new Label();

			bottom.setTop(cityInfo);
			bottom.setLeft(passengerInfo);

			Button drive = new Button("DRIVE");
			drive.setMinSize(100, 50);
			bottom.setRight(drive);

			drive.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {

					// update passenger arrayList and score
					level.choosePassenger(start, dest);
					level.calculateScore(start, dest);

					// delete vehicle from started city
					int row1 = ((start.getcellID() - 1) / 10);
					int column1 = ((start.getcellID() - 1) % 10);
					cell = new Cell();

					cell.createButtontoCity(start);
					cell.addStation();
					pane.add(cell, Math.abs(column1), row1);

					// play animation
					ImageView view = new ImageView(level.vehicle.whichVehicle());
					
					File sesDosyasi = new File("carSound.mp3");
					Media ses = new Media(sesDosyasi.toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(ses);
					
					view.setFitHeight(40);
					view.setFitWidth(40);
					root.getChildren().add(view);
					PathTransition pt = new PathTransition();
					pt.setDuration(Duration.millis(3500));
					pt.setPath(path);
					pt.setNode(view);
					pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
					pt.setCycleCount(1);
					pt.setAutoReverse(true);
					pt.play();
					mediaPlayer.setStartTime(Duration.seconds(1));
					mediaPlayer.play();

					// when animation finished delete vehicle and path from scene
					pt.setOnFinished(event -> {
						root.getChildren().remove(view);
						path.getElements().clear();
						mediaPlayer.stop();
					});

					// show updated score
					text2.setText("Score: " + Level.score);

					// add vehicle to destination city
					level.vehicle.setCityID(dest.getcityID());
					cellID = dest.getcellID();

					int row2 = ((cellID - 1) / 10);
					int column2 = ((cellID - 1) % 10);

					cell = new Cell();
					ImageView v = new ImageView(level.vehicle.whichVehicle());
					v.setFitHeight(30);
					v.setFitWidth(30);
					cell.setTop(v);
					BorderPane.setAlignment(v, Pos.TOP_LEFT);
					pane.add(cell, column2, row2);

					// if level is completed make nextLevel button be able to click and increase the
					// level count
					if (level.isCompleted()) {
						nextLevel.setDisable(false);
						level.levelNum++;
					} else
						nextLevel.setDisable(true);

					// show updated city and passenger informations
					cityInfo.setText(level.writeCity(start, dest));
					passengerInfo.setText(level.writePassengers(dest));
				}

			});

//game pane
			pane = new GridPane();
			for (int i = 0; i < 10; i++)
				for (int j = 0; j < 10; j++)
					pane.add(new Cell(), j, i);

//add city buttons
			for (int i = 0; i < Level.getCities().size(); i++) {
				int r = ((Level.getCities().get(i).getcellID() - 1) / 10);
				int c = ((Level.getCities().get(i).getcellID() - 1) % 10);

				cell = new Cell();

				cell.createButtontoCity(Level.getCities().get(i));
				cell.addStation();
				pane.add(cell, c, r);
			}

//add fixed points
			for (int i = 0; i < Level.getFixedPoints().size(); i++) {
				int r = ((Level.getFixedPoints().get(i).getCellID() - 1) / 10);
				int c = ((Level.getFixedPoints().get(i).getCellID() - 1) % 10);

				cell = new Cell();
				ImageView view = new ImageView(new Image("photos/fixedPoint.png"));
				view.setFitHeight(40);
				view.setFitWidth(40);
				cell.setCenter(view);
				pane.add(cell, c, r);

			}

//add vehicle image for start city

			for (int i = 0; i < Level.getCities().size(); i++) {
				if (Level.getCities().get(i).getcityID() == level.vehicle.getCityID())
					cellID = Level.getCities().get(i).getcellID();
			}
			int r = ((cellID - 1) / 10);
			int c = ((cellID - 1) % 10);

			cell = new Cell();
			ImageView view = new ImageView(level.vehicle.whichVehicle());
			view.setFitHeight(30);
			view.setFitWidth(30);
			cell.setTop(view);
			BorderPane.setAlignment(view, Pos.TOP_LEFT);
			pane.add(cell, c, r);

//add everything to the scene
			root.getChildren().add(pane);
			root.setPrefSize(400, 400);
			borderPane = new BorderPane();

			borderPane.setCenter(root);
			borderPane.setTop(upper);
			borderPane.setBottom(bottom);
			
			Scene scene = new Scene(borderPane, 600, 700);
			primaryStage.setTitle("Transportation System");
			primaryStage.setScene(scene);
			primaryStage.show();

			// draw path between start city and mouse clicked cell and add the path to root
			scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {

					int cell = 1;
					double startX, startY;

					// find the start city ID
					for (int i = 0; i < Level.getCities().size(); i++) {
						if (Level.getCities().get(i).getcityID() == level.vehicle.getCityID())
							cell = Level.getCities().get(i).getcellID();
					}

					// calculate X and Y coordinates of start city
					int col = (cell - 1) % 10;
					int roww = (cell) / 10;
					Bounds cellBounds = pane.getCellBounds(col, roww);

					double x = cellBounds.getMinX();
					double y = cellBounds.getMinY();

					startX = x + 60;
					startY = y + 45;

					path = new Path();
					path.getElements().add(new MoveTo(startX, startY));
					path.setStroke(Color.PURPLE);
					path.setStrokeWidth(3);

					double endX = event.getX();
					double endY = event.getY();

					path.getElements().add(new LineTo(startX, endY));
					path.getElements().add(new LineTo(endX, endY));
					root.getChildren().add(path);

				}
			});
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//inner cell class for creating 10x10 gridPane and adding necessary things in it
	class Cell extends BorderPane {

		public Cell() {
			setStyle("-fx-border-color: transparent");
			this.setPrefSize(60, 55);
			this.setMinSize(10, 10);

		}

		// creates city button for given city and adds to cell
		public void createButtontoCity(City city) {

			City destination = city;

			Button button = new Button(city.getName());
			ImageView view = new ImageView(new Image("photos/" + city.getName() + ".png"));

			view.setFitHeight(40);
			view.setFitWidth(40);
			view.setPreserveRatio(true);
			view.setSmooth(true);

			button.setGraphic(view);
			button.setContentDisplay(ContentDisplay.TOP);
			button.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					City temp = new City();
					for (int i = 0; i < Level.getCities().size(); i++) {
						if (Level.getCities().get(i).getcityID() == level.vehicle.getCityID()) {
							temp = Level.getCities().get(i);
						}
					}
					// update start and destination cities for using in other code segments
					start = temp;
					dest = destination;

					// show needed informations for clicked city
					cityInfo.setText(level.writeCity(temp, destination));
					passengerInfo.setText(level.writePassengers(destination));
				}

			});

			this.setCenter(button);

		}

		// adds station image to cell
		public void addStation() {
			ImageView view = new ImageView(new Image("photos/GO.PNG"));
			view.setFitHeight(30);
			view.setFitWidth(30);
			view.setPreserveRatio(true);
			view.setSmooth(true);

			this.setTop(view);
			BorderPane.setAlignment(view, Pos.TOP_LEFT);

		}

		// adds fixedPoint image to cell
		public void addImage() {
			ImageView view = new ImageView(new Image("photos/fixedPoint.png"));
			view.setFitHeight(70);
			view.setFitWidth(60);
			view.setPreserveRatio(true);
			view.setSmooth(true);

			this.getChildren().addAll(view);
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
