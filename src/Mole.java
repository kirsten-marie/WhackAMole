
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author Kirsten McCain
 */
public class Mole extends ImageView {
    private TranslateTransition hide, reset;
    private Image moleGraphic;
    private Timeline timeline, timeline2;
    private Integer moleTimer;
    private Double hiddenTimer;
    private IntegerProperty seconds;
    private DoubleProperty hiddenSeconds;
    
    public Mole(Integer duration, Double timeBetween){   
        Image moleGraphic = new Image("images/tinymole.png");
        setImage(moleGraphic);
        hide = new TranslateTransition(Duration.millis(500), this);
        reset = new TranslateTransition(Duration.millis(100), this);
        moleTimer = duration;
        hiddenTimer = timeBetween;
        seconds = new SimpleIntegerProperty(moleTimer);
        hiddenSeconds = new SimpleDoubleProperty(hiddenTimer);
        setVisible(false);
    }    
    
    public Image getMoleImage(){
        return moleGraphic;
    }
    
    public Integer getTimeLimit(){
        return moleTimer;
    }
    
    //set the timelimit a moles can display
    public void setTimeLimit(Integer time){
        moleTimer = time;
        seconds.set(moleTimer);
    }
    
    //decrease time that mole can display by 1 second
    public void decreaseTimeLimit(){
        moleTimer-=1;
        seconds.set(moleTimer);
    }
    
    public double getTimeToHit(){ 
        return seconds.doubleValue();
    }
    
    public TranslateTransition getAndSetTransition(){
        setTransition();
        return hide;
    }
    
    public TranslateTransition getResetTransition(){
        return reset;
    }
    
    public void setTransition(){
        hide.setByY(5);
        hide.setCycleCount(1);
        reset.setByY(-5);
        reset.setCycleCount(1);
    }
    
    //Start timer when mole appears, when timer ends mole will disappear
    public void startTimer(){
        if (timeline != null) {
            timeline.stop();
        }
        seconds.set(moleTimer);
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(moleTimer+0.5),
                new KeyValue(seconds, 0)));
        timeline.playFromStart();
        setHiddenTimer();
        timeline.setOnFinished(e -> {
            setTransition();
            hide.play();
        });
    } 
    
    //Create timeline for calculating the max time between before another mole can display
    public void setHiddenTimer(){
        if (timeline2 != null){
            timeline2.stop();
        }
        hiddenSeconds.set(hiddenTimer);
        timeline2 = new Timeline();
        timeline2.getKeyFrames().add(new KeyFrame(Duration.seconds(hiddenTimer-1),
                new KeyValue(hiddenSeconds, 0)));
        
    }
    
    //Update duration of time between moles
    public void updateHiddenTimer(double duration){
        hiddenTimer = duration;
    }
    
    public void decreaseHiddenTimer(double duration){
        hiddenTimer -= duration;
    }    
    
    public double getMoleHiddenLimit(){
        return hiddenTimer;
    }
    
    //Get timeline for mole being displayed
    public Timeline getTimeline(){
        return timeline;
    }
    
    //Get timeline for mole being hidden
    public Timeline getHiddenTimeline(){
        return timeline2;
    }
    
    //Get time left before mole will be hidden if user does not click
    public IntegerProperty getTimeLeftMole(){
        return seconds;
    }
}
