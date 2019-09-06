
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
/**
 *
 * @author Kirst
 */
public class ParentScenes {
    
    private Parent main, difficulty, easy, medium, hard, transition, round1, levelUp1, levelUp2, end;
    private Image image;
    private Button play;
    private Background game;
    private ArrayList<Mole> moleList;
    private ArrayList<StackPane> easyGames, mediumGames, hardGames;
    private final Background PLAINFIELD = new Background(new BackgroundImage(
                new Image ("images/emptyBackground.jpg"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT));
    
    
    public Parent getMain(){
        if (main == null){
           createMain();
        }
        return main;   
    }
    
    public void createMain(){
        final Label INTRO = new Label ("Whack-a-mole");
        INTRO.setTextFill(Color.SADDLEBROWN);
        INTRO.setStyle("-fx-font-size: 4em;"
            + "-fx-font-family: Georgia, serif;");
        play = new Button("Play");
        VBox startMenu = new VBox(INTRO, play, new ImageView(new Image("images/smallmole.png")));
        startMenu.setAlignment(Pos.CENTER);
        startMenu.setSpacing(25);
        startMenu.setPadding(new Insets (20));
        startMenu.setBackground(PLAINFIELD);
        main = startMenu;
    }
    
    public Button playBtn(){
        return play;
    }
    
    public Parent createDiffScreen(VBox v){
        VBox levelMenu = v;
        levelMenu.setAlignment(Pos.CENTER);
        levelMenu.setSpacing(25);
        levelMenu.setBackground(PLAINFIELD);
        return difficulty = levelMenu;
    }
    
    public void setBackground(char difficulty){
        if (difficulty == 'e'){image = new Image("images/background_easy2.jpg");}
        else if (difficulty == 'm'){image = new Image("images/background_normal.jpg");}
        else {image = new Image("images/background_hard.jpg");}
        
        game = new Background(new BackgroundImage (image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        ));
    }
   
    public Parent getEasy(int level){
        if (easyGames == null)
           easyGames = new ArrayList<>(); 
        int temp = (level-1);
        easyGames.add(temp, new StackPane());
        easyGames.get(temp).setBackground(game); //set the appropriate background based on dificulty
        addMoles(easyGames.get(temp), 5); //Add the moles in the moleList to the StackPane for the level
        return easy = easyGames.get(temp);
    }

    public Parent getMedium(int level){
        if (mediumGames == null)
           mediumGames = new ArrayList<>(); 
        int temp = (level-1);
        mediumGames.add(temp, new StackPane());
        mediumGames.get(temp).setBackground(game); //set the appropriate background based on dificulty
        addMoles(mediumGames.get(temp), 9); //Add the moles in the moleList to the StackPane for the level
        return medium = mediumGames.get(temp);
    } 
    
    public Parent getHard(int level){
        if (hardGames == null)
           hardGames = new ArrayList<>(); 
        int temp = (level-1);
        hardGames.add(temp, new StackPane());
        hardGames.get(temp).setBackground(game); //set the appropriate background based on dificulty
        addMoles(hardGames.get(temp), 15); //Add the moles in the moleList to the StackPane for the level
        return hard = hardGames.get(temp);
    } 
    
    public void addMoles(StackPane s, int x){
        for (int i = 0; i < x; i++){
            s.getChildren().add(moleList.get(i));
        }
    }
    
    public Parent getTransition(VBox transitionBox){
        if (transition == null){
            createTransition(transitionBox);
        }
        return transition;
    }
    
    public void createTransition(VBox transitionBox){
        transitionBox.setSpacing(20);
        transitionBox.setAlignment(Pos.CENTER);
        transitionBox.setPadding(new Insets(25));
        transitionBox.setBackground(PLAINFIELD);
        transition = transitionBox;
    }
    
    public Parent startNextLevel(char diff, int level){;
        if (diff == 'e'){
            if (level == 1) {return round1 = getEasy(level);}
            else if (level == 2) {return levelUp1 = getEasy(level);}
            else {return levelUp2 = getEasy(level);}
        }
        else if (diff == 'm'){
            if (level == 1) {return round1 = getMedium(level);}
            else if (level == 2) {return levelUp1 = getMedium(level);}
            else {return levelUp2 = getMedium(level);}
        }
        else if (level == 1) {return round1 = getHard(level);}
        else if (level == 2) {return levelUp1 = getHard(level);}
        else {return levelUp2 = getHard(level);}
    }
    
    public Parent getEnd(VBox endBox){
        if (end == null){
            createEnd(endBox);
        }
        return end;
    }
    
    public void createEnd(VBox endBox){
        VBox endScene = endBox;
        endBox.setSpacing(20);
        endBox.setAlignment(Pos.CENTER);
        endBox.setPadding(new Insets(25));
        endScene.setBackground(PLAINFIELD);
        end = endScene;
    }
    
    public void setMoles(ArrayList<Mole> moles){
        moleList = moles;
    }
    
    public ArrayList<Mole> getMoles(){
        return moleList;
    }
    
    public void endMoleLevel(boolean levelUp){
        for (Mole mole: moleList){
            if (mole.getTimeline() != null && mole.getTimeline().getStatus() == Animation.Status.RUNNING)
                mole.getTimeline().stop();
            if (mole.getHiddenTimeline()!= null && mole.getHiddenTimeline().getStatus() == Animation.Status.RUNNING)
                mole.getHiddenTimeline().stop();
            mole.setVisible(false);
            if (levelUp)
                mole.decreaseTimeLimit();
                mole.decreaseHiddenTimer(.25);
        }
    }    
}
