package ClassProject;
import java.util.Timer;
import java.util.TimerTask;
public class PlayGame {
    Timer timer;
    TimerTask task, task2;
    MyBoardGame myGame;

    int counter = 0;
    public PlayGame(int seconds) {

        timer = new Timer(true);
        start();
        counter = seconds - 1;
        task2 = new TimerTask() {
            @Override
            public void run() {
                int x = countdown();
                System.out.println("Time remaining: " + x);
                myGame.setTimeRemain(x);
            }
        };
        timer.scheduleAtFixedRate(task2, 1000, 1000);
        task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Game Stop ... ");
                stop();
            }
        };
        timer.schedule(task, seconds * 1000);
    }

    /**
     * Class method that counts down and when in the counter reaches zero, it changes the display
     * @return
     */
    private int countdown(){
        if(counter == 0){
            task2.cancel();
        }
        return  counter--;
    }

    /**
     * Class method that displays "Game Start" in the console and "builds" the game
     */
    public void start(){
        System.out.println("Game Start ... ");
        myGame = new MyBoardGame();
    }

    /**
     * Class method that calls the stopGame method in the MyBoardGame class to stop the game
     */
    public void stop(){
        myGame.stopGame();
        task.cancel();
    }

    public static void main (String[] args){
        PlayGame game = new PlayGame(60);
    }

}
