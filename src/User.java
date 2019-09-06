/**
 *
 * @author Kirst
 */
public class User {

    private int numHit;
    private int[] actualPointsPerLevel;
    private int pointsToNext;
    private int highScore; 
    private Points points;
    private int level;
    private char difficulty;
    private int pointsPerHit;   
    private int finalScore;
    private boolean levelUp;
    private boolean winner;
    
    public User(){
        numHit = 0;
        level = 1;
        points = new Points();
        levelUp = false;
        winner = false;
        actualPointsPerLevel = new int[]{0,0,0};
    }

    public void setDifficulty(char d){
        difficulty = d;
    }
    
    public char getDifficulty(){
        return difficulty;
    }
    
    public void setLevelTarget(int level){
        this.level = level;
        points.setTarget(level);
        pointsToNext = points.getTarget();
        points.setLevelPoints(0);
    }
    
    public int getLevel(){
        return level;
    }

    public void calcPointsPerHit(double t){
        switch(difficulty){
            case 'e': 
                if (t > 4){pointsPerHit = 3; break;}
                else if (t > 2){pointsPerHit = 2; break;}
                else if (t > 0){pointsPerHit = 1; break;}
                else break;
            case 'm':
                if (t > 2){pointsPerHit = 3; break;}
                else if (t > 1){pointsPerHit = 2; break;}
                else if (t > 0){pointsPerHit = 1; break;}
                else break;
             case 'h':
                if (t > 1){pointsPerHit = 3; break;}
                else if (t > 0){pointsPerHit = 2; break;}
                else break;               
        }
    }   
    
    public void incrementMolesHit(){
        numHit++;
    }
    
    public int getTotalHit(){
        return numHit;
    }
    
    public int getPointsToNext(){
        return pointsToNext;
    }
    
    public void setPointsToNext(int x){
        pointsToNext = x;   
    }
    
    public void lowerPointsToNext(double x){
        pointsToNext -= x;
    }
    
    public void updateGameStats(double t){
        incrementMolesHit();
        calcPointsPerHit(t);
        points.increasePoints(pointsPerHit);
        lowerPointsToNext(pointsPerHit);
    }
    
    public Points getPoints(){
        return points;
    }
    
    public void hitMole(double t){
        updateGameStats(t);     
    }    
    
    public void endLevel(){
        //When level ends, set the number of points earned during that level
        int x = (level-1);
        actualPointsPerLevel[x] = points.getLevelPoints();
        
        //Check if points earned will allow user to level up
        if (points.canProceed(level)){
            levelUp();
        }
        else{
            levelUp = false;
            winner = false;
            setFinalScore();
        }  
    } 
        
    public void levelUp(){
        if (level < 3){
        level++;
        setLevelTarget(level);
        levelUp = true;
        }
        else{
            winner = true;
            setFinalScore();
        }
    }
    
    public boolean canLevelUp(){
        return levelUp;
    }
    
    public boolean isWinner(){
        return winner;
    }    
    
    public int getHighScore(){
        return highScore;
    }
    
    public int getFinalScore(){
        return finalScore;
    }
    
    public void setFinalScore(){
        finalScore = points.getTotalPoints();
        if (finalScore > highScore){
            highScore =finalScore;
        }
    }    
    
    public int[] getPointsPerLevel(){
        return actualPointsPerLevel;
    }    
    
    public void resetUser(){
        numHit = 0;
        level = 1;
        levelUp = false;
        winner = false;
        for (int i = 0; i< 3; i++){
            actualPointsPerLevel[i] = 0;
        }
        points.resetPoints();
    }
  
}
