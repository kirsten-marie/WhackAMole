import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Kirsten
 */
public class Game {  
    private Button play, pause;
    private HBox top, bottom, transitionButtons, gameOver;  
    private VBox startMenu, levelMenu, transitionMenu, end, pauseMenu;
    private Label highScore, currentLevel, nextLevel, pointsNeeded, currentScore, timerDisplay, 
                    intro, numberHit, numberHit2, currentDifficulty, tempTotal, target, finalScore;
    private User user;
    private Timeline timeline;
    private final Integer TIMERSTART = 30;
    private IntegerProperty timerSeconds;
    private ArrayList<Mole> moleList;
    private ImageCursor malletCursor; 
    private int numHoles;
    private Text endGame; 
    private final Background PLAINFIELD = new Background(new BackgroundImage(
                new Image ("images/emptyBackground.jpg"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT));
    
    public Game(){
        highScore = new Label();
        currentLevel = new Label();
        currentScore = new Label ();
        pointsNeeded = new Label ();
        user = new User();
        timerSeconds = new SimpleIntegerProperty(TIMERSTART);
        timerDisplay = new Label(timerSeconds.toString());
        timerDisplay.textProperty().bind(timerSeconds.asString());
        moleList = new ArrayList<>();
        Image mallet = new Image("images/woodMallet.png");
        malletCursor = new ImageCursor(mallet);
        end = new VBox();
        gameOver = new HBox();
        intro = new Label ("Whack-a-Mole!");
        intro.setTextFill(Color.SADDLEBROWN);
        intro.setStyle("-fx-font-size: 4em;");
    }
    
    public VBox createStartMenu(){
        play = new Button("Play");
        startMenu = new VBox(intro, play, new ImageView(new Image("images/smallmole.png")));
        startMenu.setBackground(PLAINFIELD);
        startMenu.setAlignment(Pos.CENTER);
        startMenu.setSpacing(25);
        startMenu.setPadding(new Insets (20));
        return startMenu;
    }
    
    public Label getTitle(){
        return intro;
    }
    
    //Timer for the level
    public void startTimer(){
        if (timeline != null) {
            timeline.stop();
        }
        timerSeconds.set(TIMERSTART);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(TIMERSTART+1),
                new KeyValue(timerSeconds, 0)));
        timeline.playFromStart();
    } 
    
    public Timeline getGameTimeline(){
        return timeline;
    }      
    //Set the game background & moles based on the difficulty level selected by user
    public void gameSetup(char difficulty){
        switch (difficulty){
            case 'e':
                numHoles =5;
                createEasyMoles(); break;
            case 'm':
                numHoles = 9;
                createMediumMoles(); break;
            case 'h':
                numHoles = 15;
                createHardMoles(); break;  
        }
        createMainGame();
    }  
    
    public User getUser(){
        return user;
    }
    
    public ImageCursor getMalletCursor(){
        return malletCursor;
    }      
    
    public int getNumHoles(){
        return numHoles;
    }
    
    public ArrayList<Mole> getMoles(){
        return moleList;
    }    
   
    public HBox getBottom(){
        return bottom;
    }
    
    public HBox getTop(){
        return top;
    }   
    
    //public BorderPane createMainGame(){
    public void createMainGame(){
        pause = new Button("Pause");    
        top = new HBox (highScore, pause, timerDisplay);
        top.setAlignment(Pos.CENTER);
        top.setSpacing(50);
        top.setPadding(new Insets (20,0,20,0));
        bottom = new HBox(currentLevel, currentScore, pointsNeeded);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(20);
        bottom.setPadding(new Insets (20,0,20,0));
        timerDisplay.setTextFill(Color.RED);
        timerDisplay.setStyle("-fx-font-size: 4em;");
        user.setLevelTarget(user.getLevel());
        setLabels();
        pause.setOnAction(e -> {
            if (timeline.getStatus() == Animation.Status.PAUSED){
                pause.setText("Pause");
                timeline.play();
                playMoleTimers();     
            }
            else {
                pause.setText("Play");
                timeline.pause();
                pauseMoleTimers();
            }
        });
    } 
    
    public VBox createTransitionMenu(){
        transitionMenu = new VBox();
        transitionMenu.setAlignment(Pos.CENTER);
        numberHit2 = new Label();
        tempTotal = new Label();
        nextLevel = new Label();
        target = new Label();
        transitionMenu.getChildren().clear();
        transitionMenu.getChildren().addAll(numberHit2, tempTotal, nextLevel, target);
        transitionMenu.setSpacing(15);
        return transitionMenu;
    } 
    
    public void updateLevelStats(){
        int nxtLvl = user.getLevel();
        int index = (nxtLvl-2);
        numberHit2.setText("Total Number of Moles Hit: " + user.getTotalHit());
        numberHit2.setStyle("-fx-font-size: 2em; "
                + "-fx-font-weight: bold");
        tempTotal.setText("Points Earned Level " + (nxtLvl - 1) + ": " + user.getPointsPerLevel()[index]);
        tempTotal.setStyle("-fx-font-size: 2em; "
                + "-fx-font-weight: bold");
        nextLevel.setText("Next Level: " + nxtLvl);
        nextLevel.setStyle("-fx-font-size: 2em; "
                + "-fx-font-weight: bold");
        target = new Label("Points Needed Next Level: " + user.getPoints().getTarget());
        target.setStyle("-fx-font-size: 2em; "
                + "-fx-font-weight: bold");
    }
    
    public void showLevelResults(){
        System.out.println(user.getLevel());
        user.endLevel();
        System.out.println(user.getLevel());
        if (user.getLevel() == 3 && user.isWinner()){
            endGame.setText("Congratulations, you won!"); 
            updateFinalStats();
        }
        else if (user.canLevelUp()){
            updateLevelStats();
        }
        else{
            endGame.setText("Sorry, you lost. Try again!");
            updateFinalStats();
        }
    }  
    
    public void updateFinalStats(){
        finalScore.setText("Final score: " + user.getFinalScore());
        finalScore.setStyle("-fx-font-size: 2em; "
                + "-fx-font-weight: bold");
        highScore.setText("High Score: " + user.getHighScore());
        highScore.setStyle("-fx-font-size: 2em; "
                + "-fx-font-weight: bold");
        numberHit.setText("Number Hit: " + user.getTotalHit());
        numberHit.setStyle("-fx-font-size: 2em; "
                + "-fx-font-weight: bold");
        
    }
    
    public void setEnd(){
        gameOver.setSpacing(20);
        gameOver.setAlignment(Pos.CENTER);
        endGame = new Text();
        finalScore = new Label();
        highScore = new Label();
        numberHit = new Label();
        end.getChildren().clear();
        end.getChildren().addAll(endGame, finalScore, highScore, numberHit, gameOver);
    }
    
    public VBox getEnd(){
        return end;
    }     
    
    public HBox getTransitionButtons(){
        return transitionButtons;
    }
    
    public HBox getGameOverBtns(){
        return gameOver;
    }  
    
    public void setLabels(){
        currentScore.setText("Current Points: " + user.getPoints().getLevelPoints());
        highScore.setText("High Score: " + user.getHighScore());
        currentLevel.setText("Current Level: " + user.getLevel());
        pointsNeeded.setText("Points to Next Level: " + user.getPoints().getTarget());
    }
    
    public void updatePointsLabels(){
        currentScore.setText("Current Points: " + user.getPoints().getLevelPoints());
        if (user.getPointsToNext() < 0)
          pointsNeeded.setText("Points to Next Level: 0");  
        else
            pointsNeeded.setText("Points to Next Level: " + user.getPointsToNext());
        if (user.getHighScore() < user.getPoints().getTotalPoints()){
            user.setFinalScore();
            highScore.setText("High Score: " + user.getHighScore());
        }     
    } 

    public Button getPauseBtn(){
        return pause;
    } 
    
    public void pauseMoleTimers(){
        for (Mole mole : moleList){
            if (mole.getTimeline() != null && mole.getTimeLeftMole().doubleValue() > 0)
                mole.getTimeline().pause();
        }
    }
    
    public void playMoleTimers(){
        for (Mole mole : moleList){
            if (mole.getTimeline() != null && mole.getTimeline().getStatus() == Animation.Status.PAUSED )
                mole.getTimeline().play();
        }
    }  
    
    //Create new moles for Easy Game by setting the duration to display & time between moles
    public void createEasyMoles(){
        moleList.clear();
        for (int i = 0; i < numHoles; i++){
            moleList.add(new Mole(5, 2.75));
        }
        //Set translate x & y properties to add moles to the correct location on the background
        moleList.get(0).setTranslateX(-90);
        moleList.get(0).setTranslateY(90);
        moleList.get(1).setTranslateX(40);
        moleList.get(1).setTranslateY(90);
        moleList.get(2).setTranslateX(170);
        moleList.get(2).setTranslateY(90);
        moleList.get(3).setTranslateX(-25);
        moleList.get(3).setTranslateY(152);
        moleList.get(4).setTranslateX(106);
        moleList.get(4).setTranslateY(152);  
    }
    
    //Create new moles for Medium Game by setting the duration to display & time between moles
    public void createMediumMoles(){       
        moleList.clear();
        for (int i = 0; i < numHoles; i++){
            moleList.add(new Mole(4, 2.25));
        }
        //Set translate x & y properties to add moles to the correct location on the background
        moleList.get(0).setTranslateX(-89);
        moleList.get(0).setTranslateY(66);
        moleList.get(1).setTranslateX(35);
        moleList.get(1).setTranslateY(66);
        moleList.get(2).setTranslateX(157);
        moleList.get(2).setTranslateY(66);
        moleList.get(3).setTranslateX(-89);
        moleList.get(3).setTranslateY(115);
        moleList.get(4).setTranslateX(35);
        moleList.get(4).setTranslateY(115); 
        moleList.get(5).setTranslateX(157);
        moleList.get(5).setTranslateY(115);
        moleList.get(6).setTranslateX(-89);
        moleList.get(6).setTranslateY(163);
        moleList.get(7).setTranslateX(35);
        moleList.get(7).setTranslateY(163);
        moleList.get(8).setTranslateX(157);
        moleList.get(8).setTranslateY(163);
    }
    
    //Create new moles for Medium Game by setting the duration to display & time between moles
    public void createHardMoles(){       
        moleList.clear();
        for (int i = 0; i < numHoles; i++){
            moleList.add(new Mole(3, 1.75));
        }
        //Set translate x & y properties to add moles to the correct location on the background
        moleList.get(0).setTranslateX(-121);
        moleList.get(0).setTranslateY(65);
        moleList.get(1).setTranslateX(-42);
        moleList.get(1).setTranslateY(65);
        moleList.get(2).setTranslateX(34);
        moleList.get(2).setTranslateY(65);
        moleList.get(3).setTranslateX(113);
        moleList.get(3).setTranslateY(65);
        moleList.get(4).setTranslateX(192);
        moleList.get(4).setTranslateY(65); 
        moleList.get(5).setTranslateX(-121);
        moleList.get(5).setTranslateY(114);
        moleList.get(6).setTranslateX(-42);
        moleList.get(6).setTranslateY(114);
        moleList.get(7).setTranslateX(34);
        moleList.get(7).setTranslateY(114);
        moleList.get(8).setTranslateX(113);
        moleList.get(8).setTranslateY(114);
        moleList.get(9).setTranslateX(192);
        moleList.get(9).setTranslateY(114);
        moleList.get(10).setTranslateX(-121);
        moleList.get(10).setTranslateY(163);
        moleList.get(11).setTranslateX(-42);
        moleList.get(11).setTranslateY(163);
        moleList.get(12).setTranslateX(34);
        moleList.get(12).setTranslateY(163);
        moleList.get(13).setTranslateX(113);
        moleList.get(13).setTranslateY(163);
        moleList.get(14).setTranslateX(192);
        moleList.get(14).setTranslateY(163);
    }       
    
}
