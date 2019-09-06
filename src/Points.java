
/**
 *
 * @author Kirsten
 */
public class Points {
    
    private int totalPoints;
    private int levelPoints;
    private int target;    
    private int[] minPointsPerLevel;
    private boolean proceed;
    
    public Points(){
        totalPoints = 0;
        levelPoints = 0;
        proceed = false;
    }
    
    public void calcMinPointsPerLevel(char d){
        switch (d){
            case 'e':
                minPointsPerLevel = new int[] {15, 20, 25}; break;
            case 'm':
                minPointsPerLevel = new int[] {25, 30, 35}; break;
            case 'h':
                minPointsPerLevel = new int[] {30, 35, 40}; break;                
        }
    } 
    
    public int[] getMinPointsPerLevel(){
        return minPointsPerLevel;
    }    
    
    //Check if user got enough points to go to the next level
    public boolean canProceed(int level){
        if (levelPoints >= minPointsPerLevel[level-1])
            proceed = true;
        else
            proceed = false;
        return proceed;
    }
    
    public void setProceed(boolean p){
       proceed = p;
    }   
    
    public void increasePoints(int x){
        totalPoints += x;
        levelPoints +=x;
    }
    
    public int getTotalPoints(){
        return totalPoints;
    }
    
    public int getLevelPoints(){
        return levelPoints;
    }
    
    public void setTotalPoints(int x){
        totalPoints = x;
    }
    
    public void setLevelPoints(int x){
        levelPoints = x;
    }
    
    public void setTarget(int x){
        target = minPointsPerLevel[x-1]; 
    }
    
    public int getTarget(){
        return target;
    } 
    
    public void resetPoints(){
        totalPoints = 0;
        levelPoints = 0;
        target = 0;
        proceed = false;
    }

}
