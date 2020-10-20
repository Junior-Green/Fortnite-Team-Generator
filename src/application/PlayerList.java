package application;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PlayerList extends Stage {

	private Button btnRemove, btnAdd, btnSort; //Declare all buttons
	private ObservableList<Player> list; //Declare Observable list
	private ListView<Player> listview; //Declare list view
	private Alert alert; //Declare alert
	private Stage primaryStage; 

	public PlayerList(Stage ps) {

		Alert error = new Alert(AlertType.ERROR);
		error.setHeaderText(null);
		error.setTitle("Error");
		
		Image imgBanner = new Image("file:images/fortnite.png", 200, 100, true, true);
		ImageView ivBanner = new ImageView(imgBanner);
		
		alert = new Alert(AlertType.NONE);
		alert.setHeaderText(null);

		primaryStage = ps;

		list = FXCollections.observableArrayList(Main.getPlayers()); //Gets the Array list of players and converts to Observable list
		listview = new ListView<Player>();
		listview.setPrefSize(350, 500);
		listview.setItems(list);
		
		btnRemove = new Button("REMOVE");
		btnRemove.setPrefSize(100, 40);
		btnRemove.setOnAction(e -> {
			
			//If no item is selected alert user 
			if(listview.getSelectionModel().getSelectedItem() == null) 
			{
				error.setContentText("Please select a player to remove!");
				error.showAndWait();
			}
			else if(Main.getPlayers().size() == 4)
			{
				error.setContentText("You need a minimum of 4 players to generate a competitive tournament");
				error.showAndWait();
			}
			else //Shows confirmation about removal of said player. If yes removes player from list view and array list.
			{
				Alert removeConfirm = new Alert(AlertType.CONFIRMATION);
				removeConfirm.getButtonTypes().clear();
				removeConfirm.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
				removeConfirm.setTitle("Remove Player");
				removeConfirm.setHeaderText(null);
				removeConfirm.setContentText("Are you sure you want to remove " + listview.getSelectionModel().getSelectedItem().getName());
				
				Optional<ButtonType> result = removeConfirm.showAndWait();
				
				if(result.get() == ButtonType.YES)
				{
					Main.getPlayers().remove(listview.getSelectionModel().getSelectedItem());
					list.remove(listview.getSelectionModel().getSelectedItem());					
				}
				else 
					e.consume();
			}
		});

		btnAdd = new Button("ADD");
		btnAdd.setPrefSize(100, 40);
		btnAdd.setOnAction(e -> //Shows a series of txtInputDialogs getting the fields and creates new Player object from those entries
		{
			Alert wrong = new Alert(AlertType.ERROR);
			wrong.setTitle("Error");
			wrong.setHeaderText(null);
			wrong.setContentText("Please enter a number greater or equal to zero");
			
			Boolean exit = false, exists = false;		
			String first = "", last = "", rating = "", tier = "";
			
			while(first == "")//Gets first name
			{
				try
				{
					TextInputDialog input = new TextInputDialog();
					input.setHeaderText(null);
					input.setContentText(null);
					input.setTitle("Add Player");		
					input.setContentText("Enter player's first name:");
					Optional<String> result = input.showAndWait();		
					
					if(!result.get().equals(""))
						first = result.get();
					else
						first = "";
				}
				catch(NoSuchElementException e2)
				{					
					exit = true;
					break;
				}
			}
			if(exit == false)
			while(last == "") //Gets last name
			{
				try
				{
					TextInputDialog input = new TextInputDialog();
					input.setHeaderText(null);
					input.setContentText(null);
					input.setTitle("Add Player");		
					input.setContentText("Enter player's last name:");
					Optional<String> result = input.showAndWait();		
					
					if(!result.get().equals(""))
						last = result.get();
					else
						last = "";
				}
				catch(NoSuchElementException e2)
				{					
					exit = true;
					break;
				}
			}
			if(exit == false)
			while(rating == "") //Gets rating
			{
				try
				{
					TextInputDialog input = new TextInputDialog();
					input.setHeaderText(null);
					input.setContentText(null);
					input.setTitle("Add Player");		
					input.setContentText("Enter player's rating (0-100):");
					Optional<String> result = input.showAndWait();		
					
					if(Integer.parseInt(result.get()) < 0)
					{
						rating = "";
						wrong.setContentText("The rating must a number greater or equal to zero");
						wrong.showAndWait();
					}
					else if(!result.get().equals(""))
						rating = result.get();
					
					else
						rating = "";
				}
				catch(NoSuchElementException e2)
				{					
					exit = true;
					break;
				}
				catch(NumberFormatException e3) //If user doesn't enter a valid number shows an error
				{
					rating = "";
					wrong.setContentText("Please enter a number greater or equal to zero");
					wrong.showAndWait();
				}
			}
			if((first != "" && last != "") && rating != "")
			{
				//Decides the tier based on the Player's rating field variable
				if(Integer.parseInt(rating) < 50)
					tier = "Scout";
				else if(Integer.parseInt(rating) >= 50 && Integer.parseInt(rating) <= 59)
					tier = "Ranger";
				else if(Integer.parseInt(rating) >= 60 && Integer.parseInt(rating) <= 69)
					tier = "Agent";
				else if (Integer.parseInt(rating) >= 70 && Integer.parseInt(rating) <= 79)
					tier = "Epic";
				else
					tier = "Legend";	
				
				Player temp = new Player(first, last, Integer.parseInt(rating), tier); //Creates new player based on info given
				for (Player p : Main.getPlayers())
				{
					if(p.getName().equalsIgnoreCase(temp.getName())) //Checks if a Player under the given name exists
					{
						wrong.setContentText(p.getName() + " already exists!"); //If it exists prompts user that the player exists already
						wrong.showAndWait();
						exists = true;					
						break;
					}
				}
				if(Main.getPlayers().size() == 64) //If there are already 64 players tells user that there is no space left and cancels action
				{
					Alert exceed = new Alert(AlertType.WARNING);
					exceed.setTitle("Error");
					exceed.setHeaderText(null);
					exceed.setContentText("No more players can be added.\n The tournament has reached its capacity of 64 competitors.");
					exceed.showAndWait();
				}
				else if(exists != true) //If all the restrictions are passed add player to the list view and the array list of players
				{
					Main.getPlayers().add(temp);
					list.add(temp);				
				}		
			}
		});

		btnSort = new Button("SORT");
		btnSort.setPrefSize(100, 40);
		btnSort.setOnAction(e -> {	
			ButtonType btnName = new ButtonType("Name");
			ButtonType btnRating = new ButtonType("Rating");
			Alert sort = new Alert(AlertType.CONFIRMATION);
			sort.setHeaderText(null);
			sort.setContentText("How would you like the list to be sorted?");
			sort.setTitle("Sort List");
			sort.getButtonTypes().clear();
			sort.getButtonTypes().addAll(btnName, btnRating);
			
			Optional<ButtonType> result = sort.showAndWait();
			
			//Sorts array list of players based on what user chooses
			if(result.get() == btnName)
			{
				Player.sort = Player.SORT_BY_NAME;
				Collections.sort(list);
			}
			else
			{
				Player.sort = Player.SORT_BY_RATING;
				Collections.sort(list, Collections.reverseOrder());
			}			
		});

		TitledPane listPane = new TitledPane(String.format("%-30s%-10s%-10s", "NAME", "RATING", "TIER"), listview);
		listPane.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
		listPane.setCollapsible(false);

		FlowPane root = new FlowPane();
		root.setStyle("-fx-background-color: rgb(118,201,241)");
		root.setPadding(new Insets(20, 10, 20, 10));
		root.setHgap(10);
		root.setVgap(15);
		root.setAlignment(Pos.TOP_CENTER);
		root.getChildren().addAll(ivBanner, listPane, btnRemove, btnAdd, btnSort);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		setTitle("Player List");
		setScene(scene);
		show();
		setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent e) {
				primaryStage.show();
			}
		});
	}
}
