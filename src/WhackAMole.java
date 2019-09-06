
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

/**
 *
 * @author Kirst
 */
public class WhackAMole extends Application {
    
    Scene entry;
    BorderPane root;
    VBox levelMenu, nextLevel;
    Game gamePlay;
    Timeline timeline, moleTimeline;
    Alert wait;
    double timeToHit, maxMoleTimer;
    char diff;
    ArrayList<Mole> moleList;
    int maxMolesDisplayed, currentMolesDisplayed, numHoles, lastMole;
    ParentScenes parents;
    Button easyBtn, mediumBtn, hardBtn, pause;
    
    
    @Override
    public void start(Stage primaryStage) {
        
        //Prep GameBoard with settings and scenes leading to game
        gamePlay = new Game();
        parents= new ParentScenes();
        root = new BorderPane();
        primaryStage.setResizable(false);
        pause = new Button("Pause");
        //Create pop up alert to let the user know the timer will start after they click ok. 
        wait = new Alert(AlertType.INFORMATION,
                "Once you click the OK button, the timer will begin. Hit moles to gain points in order to go to the next level. "
                        + "The faster you click the mole after it appears, the more points you earn. If you win 3 levels, you win the game! Have fun!", ButtonType.OK );
        setEntryScene(); 
        parents.playBtn().setOnAction(e -> showDifficulty());

        //Game Stage
        primaryStage.setTitle("Whack-a-Mole!");
        primaryStage.setScene(entry);
        primaryStage.show();
    }
    
//Create entry, directions, and difficulty selection scenes
    public void setEntryScene(){
        entry = new Scene(root, 705, 550);
        root.setCenter(parents.getMain()); 
    }
    
    public void showDifficulty(){
        easyBtn = new Button("Easy");
        mediumBtn = new Button("Medium");
        hardBtn = new Button("Hard");
        levelMenu = new VBox(easyBtn, mediumBtn, hardBtn);
        root.setCenter(parents.createDiffScreen(levelMenu));
        
        //If user clicks easy, it will start level 1 of easy game.
        easyBtn.setOnAction(e -> {
            setDifficultyButton('e');
            maxMolesDisplayed = 1;
            maxMoleTimer = 2.75;
            numHoles = gamePlay.getNumHoles();
            //Show pop up to prompt user to start the timer of the game for moles to start displaying 
            wait.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> { startLevelTimer('e');});
        });
        
        mediumBtn.setOnAction(e -> {
            setDifficultyButton('m');
            maxMolesDisplayed = 4;
            maxMoleTimer = 2.25;
            numHoles = gamePlay.getNumHoles();
            //Show pop up to prompt user to start the timer of the game   
            wait.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> { startLevelTimer('m');
                    showMole();
                    });
        });
        
        hardBtn.setOnAction(e -> {
            setDifficultyButton('h');
            maxMolesDisplayed = 5;
            maxMoleTimer = 1.75;
            numHoles = gamePlay.getNumHoles();
            //Show pop up to prompt user to start the timer of the game   
            wait.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> { startLevelTimer('h');
                    showMole();
                    });
        });
    }
    
    //Shared steps to prep game no matter the difficulty
    public void setDifficultyButton(char d){
        pickGameDifficulty(d);;
        parents.setMoles(gamePlay.getMoles());
        root.setCenter(parents.startNextLevel(d, gamePlay.getUser().getLevel())); // get the stackpane where moles will be displayed
        root.getCenter().setCursor(gamePlay.getMalletCursor());
        root.setTop(gamePlay.getTop());
        root.setBottom(gamePlay.getBottom());
    }
     
    //Allows user to pick the difficulty of the game (easy, medium, hard)
    public void pickGameDifficulty(char d){
        gamePlay.getUser().setDifficulty(d);
        gamePlay.getUser().getPoints().calcMinPointsPerLevel(d);
        parents.setBackground(d); //set the image of the background based on difficulty
        gamePlay.gameSetup(d);
    }
    
    public void startTransition(char diff){
        Button main = new Button ("Main Menu");
        Button next = new Button("Start Next Level");
        if (nextLevel == null){
            nextLevel = gamePlay.createTransitionMenu();
            nextLevel.getChildren().addAll(main, next);   
        }
        main.setOnAction(e -> {
            root.setCenter(parents.getMain()); //Show main menu
            gamePlay.getUser().resetUser(); //Reset all user & point info
        });
        next.setOnAction(e-> {
            root.setCenter(parents.startNextLevel(diff, gamePlay.getUser().getLevel())); //Start Next Level
            root.getCenter().setCursor(gamePlay.getMalletCursor());
            root.setTop(gamePlay.getTop());
            startLevelTimer(diff); //start game timer & show moles
        });
    }
    
    public void startEnd(){
        gamePlay.setEnd();
        gamePlay.showLevelResults();
        Button home = new Button ("Main Menu");
        Button restart = new Button("Play Again");
        gamePlay.getGameOverBtns().getChildren().clear();
        gamePlay.getGameOverBtns().getChildren().addAll(restart, home);
        home.setOnAction(e ->{
            root.setCenter(parents.getMain()); //Show main menu
            gamePlay.getUser().resetUser(); //Reset all user & point info
        });
        restart.setOnAction(e -> {
           root.setCenter(parents.createDiffScreen(levelMenu)); //go back to difficulty screen
           gamePlay.getUser().resetUser(); //Reset all user & point info 
        });
    }
    
    public void startLevelTimer(char d){
        currentMolesDisplayed = 0;  
        gamePlay.startTimer();
        gamePlay.getGameTimeline().setOnFinished(ev -> { //When game timer runs out, check if user can go to the next level & show either level up or game over message
            startTransition(d);
            startEnd();
            parents.endMoleLevel(gamePlay.getUser().canLevelUp());
            if (gamePlay.getUser().isWinner()){
                Label temp = gamePlay.getTitle();
                temp.setAlignment(Pos.CENTER);
                root.setCenter(parents.getEnd(gamePlay.getEnd())); 
                gamePlay.getUser().resetUser();
            }
            if (gamePlay.getUser().canLevelUp()){
                root.setCenter(parents.startNextLevel(d, gamePlay.getUser().getLevel())); // get the stackpane where moles will be displayed
                root.getCenter().setCursor(gamePlay.getMalletCursor());
                root.setTop(gamePlay.getTop());
                gamePlay.setLabels();
                root.setCenter(parents.getTransition(nextLevel));
                maxMoleTimer -= .25;
                maxMolesDisplayed +=1;
            }
            else{
                Label temp = gamePlay.getTitle();
                temp.setAlignment(Pos.CENTER);
                root.setCenter(parents.getEnd(gamePlay.getEnd())); 
                gamePlay.getUser().resetUser();
            }
        });
        showMole();    
    }
    
    public void showMole(){
        //generate random moles to display if game timeline is running
        if (gamePlay.getGameTimeline().getStatus()== Animation.Status.RUNNING){
            int x = (int)(Math.random() * numHoles);
            parents.getMoles().get(x).getTimeLeftMole().addListener(new ChangeListener(){
                @Override
                public void changed(ObservableValue o, Object oldVal, Object newVal){
                    Double t = Double.parseDouble(newVal.toString());
                    Double y = (Math.random() * maxMoleTimer);
                    if (t < y)
                        showMole(); 
                }
            });
            if (currentMolesDisplayed == 0){
               parents.getMoles().get(x).setVisible(true);
                    parents.getMoles().get(x).startTimer();
                    currentMolesDisplayed++;
                    lastMole = x;
                    moleMissed(x);
                    moleHit(x);  
            }
            else if (currentMolesDisplayed < maxMolesDisplayed){
                if (lastMole != x || !parents.getMoles().get(x).isVisible()){
                    parents.getMoles().get(x).setVisible(true);
                    parents.getMoles().get(x).startTimer();
                    currentMolesDisplayed++;
                    lastMole = x;
                    moleMissed(x);
                    moleHit(x); 
                }
                else {
                    x = (int)(Math.random() * numHoles);
                    if (lastMole != x || !parents.getMoles().get(x).isVisible()){
                        parents.getMoles().get(x).setVisible(true);
                        parents.getMoles().get(x).startTimer();
                        currentMolesDisplayed++;
                        lastMole = x;
                        moleMissed(x);
                        moleHit(x); 
                    }
                }
            }               
        }
    }
        
    
    
    //Adds event handler to each mole in the arraylist
    public void moleHit(int x){
        parents.getMoles().get(x).setOnMouseClicked(e -> {
            if (parents.getMoles().get(x).getTimeline() != null) {
                parents.getMoles().get(x).getTimeline().stop();
            }
            if (gamePlay.getGameTimeline().getStatus()== Animation.Status.RUNNING){
                timeToHit = parents.getMoles().get(x).getTimeToHit();
                parents.getMoles().get(x).setVisible(false); 
                gamePlay.getUser().hitMole(timeToHit); 
                currentMolesDisplayed--; //decrease # of moles displayed
                gamePlay.updatePointsLabels();
                parents.getMoles().get(x).getHiddenTimeline().setOnFinished(ev -> { //After mole is has hidden for max time, show another mole
                    showMole();
            });
            parents.getMoles().get(x).getHiddenTimeline().playFromStart();  //when mole is hit, start max timer until next mole can display  
            }
        });
    }
    
    public void moleMissed(int x){
        parents.getMoles().get(x).getAndSetTransition().setOnFinished(e -> { //After mole is shown for max time, hide
            parents.getMoles().get(x).setVisible(false);
            parents.getMoles().get(x).getResetTransition().play(); //set back to original location before transition moved down
            currentMolesDisplayed--; //decrease # of moles displayed
            parents.getMoles().get(x).getHiddenTimeline().setOnFinished(ev -> { //After mole is has hidden for max time, show another mole
                showMole();
            });
            parents.getMoles().get(x).getHiddenTimeline().playFromStart(); //Play the animation
        });
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
