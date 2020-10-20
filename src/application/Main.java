/*Junior Green
 * Mr. Bulhao
 * ICS 4U1
 * 12 December 2019
 */
package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Main extends Application {

	private TextArea[] txtTeams; //Declare text areas where teams are displayed
	private int[] teamRatings = new int[16]; //Declare int array which stores each team's ratings
	private TitledPane[] teamNumbers;
	private static ArrayList<Player> players = new ArrayList<Player>(); //Initialize and ArrayList of players. Primary reference to players
	@SuppressWarnings("unchecked")
	private ArrayList<Player>[] teams = new ArrayList[16], tiers = new ArrayList[5]; //Initializes arrays of ArrayLists 
	private Button btnClear, btnSave;
	private int index = 0, teamRating = 0; //Declare and initialize int variables
	private double size;
	private File text = null; //Primary file which is used to read and write players

	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) {
		try {	
			
			for(int i = 0; i < tiers.length; i++)
				tiers[i] = new ArrayList<Player>(); //Initialize all Player array lists
			
			ImageView ivBanner = new ImageView(new Image("file:images\\battle_royale.jpg"));

			TilePane teamPanel = new TilePane();
			teamPanel.setPrefRows(4);
			teamPanel.setPrefColumns(4);
			teamPanel.setHgap(10);
			teamPanel.setVgap(10);
			teamPanel.setPadding(new Insets(10, 10, 10, 10));
			teamPanel.setStyle("-fx-background-color: rgb(118,201,241)");

			FlowPane root = new FlowPane();
			root.setStyle("-fx-background-color: rgb(118,201,241)");
			root.setAlignment(Pos.TOP_CENTER);
			root.setPadding(new Insets(15, 10, 10, 10));
			root.setVgap(10);

			txtTeams = new TextArea[16];
			teamNumbers = new TitledPane[16];

			for (int i = 0; i < txtTeams.length; i++)
			{
				teamNumbers[i] = new TitledPane();
				teamNumbers[i].setText("TEAM " + (i + 1));
				teamNumbers[i].setCollapsible(false);
				teamNumbers[i].setFont(Font.font("Rockwell", FontWeight.BOLD, 14));

				txtTeams[i] = new TextArea();
				txtTeams[i].setPrefSize(175, 100);
				txtTeams[i].setMaxSize(175, 100);
				txtTeams[i].setEditable(false);
				txtTeams[i].setFont(Font.font("Consolas", 11));

				teamNumbers[i].setContent(txtTeams[i]);
				teamPanel.getChildren().add(teamNumbers[i]);
			}

			Button btnView = new Button("VIEW PLAYERS");
			btnView.setPrefSize(150, 50);
			btnView.setFocusTraversable(false);
			btnView.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					primaryStage.hide();
					new PlayerList(primaryStage);				
				}
			});

			Button btnGenerate = new Button();
			btnGenerate.setText("GENERATE");
			btnGenerate.setPrefSize(150, 50);
			btnGenerate.setFocusTraversable(false);
			btnGenerate.setDisable(true);
			btnGenerate.setOnAction(e -> {
				if(btnGenerate.getText() == "OPTIMIZE") //Gets best player from highest team and worst player from lowest team and swaps
				{
					//Declare two PLayer objects used to store players which will be swapped
					Player playerHigh;
					Player playerLow;
					
						int highest = 0, lowest = Integer.MAX_VALUE, indexHigh = 0, indexLow = 0; //Variables being used in linear search
						
						for(int i = 0; i < size; i++) //Linear search algorithm to find Players suitable to be swapped
						{
							if(teamRatings[i] > highest)
							{
								highest = teamRatings[i];
								indexHigh = i;
							}
							if(teamRatings[i] < lowest)
							{
								lowest = teamRatings[i];
								indexLow = i;
							}
						}
						
						//Swaps players
						if(teams[indexHigh].size() <= 2)
						{
							playerHigh = teams[indexHigh].get(teams[indexHigh].size() - 1);
							teams[indexHigh].remove(teams[indexHigh].size() - 1);
						}
						else
						{
							playerHigh = teams[indexHigh].get(teams[indexHigh].size() - 2);
							teams[indexHigh].remove(teams[indexHigh].size() - 2);
						}
						playerLow = teams[indexLow].get(0);
						teams[indexLow].remove(0);
						
						teams[indexHigh].add(playerLow);
						teams[indexLow].add(playerHigh);
						
						Player.sort = Player.SORT_BY_RATING; //Sorts by rating when teams are output
							
						for(int i = 0; i < size; i++) //Clears current contents of text area and updates teams
						{
							txtTeams[i].clear();
							Collections.sort(teams[i]);
							
							for(int k = 0; k < teams[i].size(); k++)
							{
								txtTeams[i].appendText(String.format("%-23s%d\n", teams[i].get(k).getName(), teams[i].get(k).getRating()));
								teamRating += teams[i].get(k).getRating();
							}
							teamRatings[i] = teamRating;
							txtTeams[i].appendText(String.format("\n%-23s%d", "TEAM RATING:", teamRating));
							teamRating = 0;
						}
					btnClear.setDisable(false); //Enable clear button
					btnSave.setDisable(false); //Enable save button
				}
				else if(btnGenerate.getText() == "GENERATE")
				{
					Random rnd = new Random();			
					ArrayList<Player> temp = (ArrayList<Player>) players.clone();
				
					size = Math.ceil(temp.size() / 4d); //Determines how much teams there will be
					
					if(temp.size() <= 4)
					size = 2;
									
					for(int i = 0; i < teams.length; i++) //Initializes and array list for each team
						teams[i] = new ArrayList<Player>();
					
					while(temp.size() != 0) //Organizes all players to their respective ArrayList according to their tier
					{
						switch(temp.get(0).getTier())
						{
							case "Scout": 
							{
								tiers[0].add(temp.get(0));
								temp.remove(0);						
								break;
							}
							case "Ranger":
							{
								tiers[1].add(temp.get(0));
								temp.remove(0);							
								break;
							}
							case "Agent":
							{
								tiers[2].add(temp.get(0));
								temp.remove(0);						
								break;
							}
							case "Epic":
							{
								tiers[3].add(temp.get(0));
								temp.remove(0);							
								break;
							}
							case "Legend":
							{
								tiers[4].add(temp.get(0));
								temp.remove(0);							
								break;
							}
							default: System.out.println("Error");
									break;			
						}
					}
					
					for(int i = tiers.length - 1; i >= 0; i--) //Fills teams sequentially until all players are assigned
					{
						while (tiers[i].size() != 0)
						{
							if(index >= size)
							{
								index = 0;
								continue;
							}
							int num = rnd.nextInt(tiers[i].size());
							teams[index].add(tiers[i].get(num));
							tiers[i].remove(num);
							index++;
						}
					} 
						
					//Sorts by rating and outputs the teams
						Player.sort = Player.SORT_BY_RATING;
						
						for(int i = 0; i < size; i++)
						{		
							Collections.sort(teams[i]);
							for(int j = 0; j < teams[i].size(); j++)
							{
								txtTeams[i].appendText(String.format("%-23s%d\n", teams[i].get(j).getName(), teams[i].get(j).getRating()));
								teamRating += teams[i].get(j).getRating();
							}
							teamRatings[i] = teamRating;
							txtTeams[i].appendText(String.format("\n%-23s%d", "TEAM RATING:", teamRating));
							teamRating = 0;
						}
						btnGenerate.setText("OPTIMIZE"); //Changes button functionality
						btnClear.setDisable(false);
						btnSave.setDisable(false);
					}						
			});

			Button btnPlayers = new Button();
			btnPlayers.setText("GET PLAYERS");
			btnPlayers.setPrefSize(150, 50);
			btnPlayers.setFocusTraversable(false);
			btnPlayers.setOnAction(e -> {			
				try
				{
					
					FileChooser choose = new FileChooser();
					choose.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt")); //Sets a text file filter
							
					text = choose.showOpenDialog(primaryStage); //Gets file from file chooser
					
					//Gets info from delimited file fragments info and assigns respective characteristic to each Player 
					if(text != null)
					{
						players.clear();
						BufferedReader reader = new BufferedReader(new FileReader(text));
						
						String line = "";
						String[] lineSplit;
						
						line = reader.readLine();
						while (line != null)
						{		
							lineSplit = line.split(",");		
							players.add(new Player(lineSplit[1], lineSplit[0], Integer.parseInt(lineSplit[2]), lineSplit[3]));
							line = reader.readLine();
						}
					reader.close(); //Closes reader
					btnGenerate.setDisable(false); //Enables Generate button
					}
					
				
				}
				catch (FileNotFoundException ex)
				{
					System.out.println("File not found!");
					System.err.println("FileNotFoundException: " +
					ex.getMessage());
				}
				catch (IOException ex)
				{
					System.out.println("Problem reading file!");
					System.err.println("IOException: " + ex.getMessage());
				}
							
			});

			btnSave = new Button();
			btnSave.setText("SAVE TEAMS");
			btnSave.setPrefSize(150, 50);
			btnSave.setFocusTraversable(false);
			btnSave.setDisable(true);
			btnSave.setOnAction(e -> {	
				int teamRating = 0; //Initialize variable that stores the overall team rating of each team
				FileChooser save = new FileChooser(); //Initialize file chooser
				
				save.getExtensionFilters().add(new ExtensionFilter("Word Document", "*.doc"));
				try //Gets file to  save to and writes all the current players on to the selected file
				{
					File saveTo = save.showSaveDialog(primaryStage);
					if(saveTo != null)
					{
						BufferedWriter out = new BufferedWriter(new FileWriter(saveTo));
						for(int i = 0; i < size; i++)
						{
							out.write("Team #" + String.valueOf(i + 1));
							out.newLine();
							out.newLine();
							for(int j = 0; j < teams[i].size(); j++)
							{
								out.write(String.format("%-23s%d", teams[i].get(j).getName(), teams[i].get(j).getRating()));
								out.newLine();
								teamRating += teams[i].get(j).getRating();
							}
							 out.newLine();
							 out.newLine();
							 out.write(String.format("%-23s%d", "TEAM RATING:", teamRating));
							 teamRating = 0;
							 out.newLine();
							 out.newLine();
						}
						out.close();
					}
				} 
				catch(FileNotFoundException e0) //Catches exception
				{
					Alert openRN = new Alert(AlertType.ERROR);
					openRN.setTitle("Error");
					openRN.setHeaderText(null);
					openRN.setContentText("The file you are trying to reference is being used by another source. Please close before exporting data to file");
					openRN.show();
				}
				catch (IOException e1) //Catches exception
				{
					e1.printStackTrace();
				}					
			});
			
			btnClear = new Button();
			btnClear.setText("CLEAR");
			btnClear.setPrefSize(150, 50);
			btnClear.setFocusTraversable(false);
			btnClear.setDisable(true);
			btnClear.setOnAction(e -> { //Clears all the displayed teams and allows user to generate again
				for (int i = 0; i < txtTeams.length; i++)
					txtTeams[i].clear();
				btnClear.setDisable(true);
				btnGenerate.setDisable(false);
				btnGenerate.setText("GENERATE");
				btnSave.setDisable(true);
			});

			Button btnExit = new Button(); 
			btnExit.setText("EXIT");
			btnExit.setPrefSize(150, 50);
			btnExit.setFocusTraversable(false);
			btnExit.setOnAction(e -> {exitGame();}); //Upon clicking execute exitGame method which saves all players to original text file and terminates program

			ImageView ivLogo = new ImageView(new Image("file:images/fortnite_logo.png", 175, 130, true, true));

			VBox sidePanel = new VBox();
			sidePanel.setStyle("-fx-background-color: rgb(118,201,241)");
			sidePanel.setPadding(new Insets(10, 10, 10, 10));
			sidePanel.setSpacing(15);
			sidePanel.setAlignment(Pos.TOP_CENTER);
			sidePanel.getChildren().addAll(btnPlayers, btnView, btnGenerate, btnSave, btnClear, btnExit, ivLogo);
			root.getChildren().addAll(ivBanner, teamPanel, sidePanel);

			Scene scene = new Scene(root, 1000, 750);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Fortnite Team Tournament");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(e ->
			{
				
				Alert exit = new Alert(AlertType.CONFIRMATION);
				exit.getButtonTypes().clear();
				exit.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
				exit.setTitle("Exit");
				exit.setHeaderText(null);
				exit.setContentText("Are you sure you want to leave?");
				
				Optional<ButtonType> result = exit.showAndWait();
				
				if(result.get() == ButtonType.YES) 
				{
					try 
					{
						if(text != null)
						{
							BufferedWriter writer = new BufferedWriter(new FileWriter(text));
							
							
							for(int i = 0; i < players.size(); i++)
							{
								writer.append(players.get(i).toFile());
								writer.newLine();
							}
							writer.close();
						}
						
					} catch (IOException e1) 
					{
						
						e1.printStackTrace();
					}
					
					Platform.exit();
					System.exit(0);
				} 
				else {
					e.consume();
				}
				});
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Returns an array list that contains all the players saved to the program which 
	 * is globally used across all classes
	 * 
	 * @return a static array list that contains all the players
	 */
	public static ArrayList<Player> getPlayers()
	{
		return players;
	}
	/*Shows an alert that asks user whether they want to quit the program or not. 
	 * If the user selects yes overwrite all the current players on to the original text
	 * file. If the user selects no return to the main menu
	 * 
	 * @return void
	 * @throws IOException
	 */
	public void exitGame()
	{
		Alert exit = new Alert(AlertType.CONFIRMATION);
		exit.getButtonTypes().clear();
		exit.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		exit.setTitle("Exit");
		exit.setHeaderText(null);
		exit.setContentText("Are you sure you want to leave?");
		
		Optional<ButtonType> result = exit.showAndWait();
		
		if(result.get() == ButtonType.YES) 
		{
			try 
			{
				if(text != null)
				{
					BufferedWriter writer = new BufferedWriter(new FileWriter(text));
					
					
					for(int i = 0; i < players.size(); i++)
					{
						writer.append(players.get(i).toFile());
						writer.newLine();
					}
					writer.close();
				}
				
			} catch (IOException e1) 
			{
				
				e1.printStackTrace();
			}
			
			Platform.exit();
			System.exit(0);
		} 
			
	}
	public static void main(String[] args) {
		launch(args);
	}
}
