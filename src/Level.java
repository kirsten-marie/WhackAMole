/**
 *
 * @author Kirsten
 */
public class Level {
    private int level;
    private char difficulty;
    private int pointsPerHit;
    double speed;
    
    public Level(){
        level = 1;
        speed =  6;
    }
    
    public void setLevel(int level){
        this.level = level;
    }
    
    public void levelUp(){
        level++;
    }
    
    public int getLevel(){
        return level;
    }

    public void setDifficulty(char d){
        difficulty = d;
    }
    
    public char getDifficulty(){
        return difficulty;
    }    
    
    public int calcPointsPerHit(double t){
        switch(difficulty){
            case 'e': 
                if (t > 4){pointsPerHit = 3; break;}
                else if (t > 2){pointsPerHit = 2; break;}
                else if (t > 0){pointsPerHit = 1; break;}
                else break;
            case 'm':
                if (t > 2){pointsPerHit = 4; break;}
                else if (t > 1){pointsPerHit = 3; break;}
                else if (t > 0){pointsPerHit = 2; break;}
                else break;
             case 'h':
                if (t > 1){pointsPerHit = 5; break;}
                else if (t > 0){pointsPerHit = 4; break;}
                else break;               
        }
        return pointsPerHit;
    }
    
    public void calcSpeed(){
        switch(level){
            case 1:
                speed = 6; break;
            case 2:
                speed = 4.5; break;
            case 3:
                speed = 3.25; break;
            case 4: 
                speed = 2.5; break;
            case 5:
                speed = 1.5; break;
        }
    }
    
    public double getSpeed(){
        return speed;
    }
    
}


