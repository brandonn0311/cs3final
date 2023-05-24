package ClassProject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Timer;

public class MyBoardGame extends JFrame implements ActionListener {

    int row = 9;
    int col = 9;
    int start = 3;
    int x1, x2, x3;
    int y1, y2, y3;
    int r1, r2, r3;
    int p, newX, newY;

    int click, score;
    double accuracy;
    int hitScore;
    int currX1, currY1, currX2, currY2, currX3, currY3;
    //int countDown;
    static int POINT = 10;
    JPanel gridPanel;
    JPanel scorePanel;
    JPanel endGamePanel;
    JSplitPane splitPane;

    //------------------- Score Panel
    JLabel myScore;
    JLabel displayScore;
    JLabel myAccuracy;
    JLabel displayAcc;
    JLabel hits;
    JLabel displayHits;
    JLabel timeRemain;
    JButton startNewButton;
    JButton closeButton;
    int second;
    int frameWidth = 700;
    int frameHeight = 800;
    HashMap<Integer, String> arrayPos;

    //--------------- Color
    Color black = Color.BLACK;
    Color yellow = Color.YELLOW;

    CircleButton[][] disks = new CircleButton[row][col];
    public MyBoardGame () {

        frameInit();
        initialize();
        this.setTitle("*** My Board Game ***");
        this.setSize(frameWidth, frameHeight);
        this.add(splitPane);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

    /**
     * Class method that "initializes" all variables
     */
    public void initialize() {
        splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(100);
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(row, col));
        scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(4, 2));

        //---------------------Score Panel
        myScore = new JLabel(" Score: ");
        displayScore = new JLabel();
        myAccuracy = new JLabel(" Accuracy: ");
        displayAcc = new JLabel();
        hits = new JLabel(" Hits: ");
        displayHits = new JLabel();
        //timerLabel = new JLabel("");

        /**
         * scorePanel components that are added to the scorePanel
         */
        timeRemain = new JLabel();
        scorePanel.add(timeRemain);
        scorePanel.add(new JLabel());
        scorePanel.add(myScore);
        scorePanel.add(displayScore);
        scorePanel.add(myAccuracy);
        scorePanel.add(displayAcc);
        scorePanel.add(hits);
        scorePanel.add(displayHits);

        splitPane.setTopComponent(scorePanel);
        splitPane.setBottomComponent(gridPanel);

        click = 0;
        score = 0;
        accuracy = 0.0;

        generatePos();
        assignPos();
        setCords();

        currX1 = x1; currY1 = y1; currX2 = x2; currY2 = y2; currX3 = x3; currY3 = y3;
        System.out.println(x1 +"," + y1 + "-" + x2 +"," + y2 + "-" + x3 +"," + y3);

        /**
         * for-loop that create the field of black button and yellow buttons that makes up the game screen
         */
        for(int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                disks[r][c] = new CircleButton();
                disks[r][c].setBackground(black);
                disks[r][c].setForeground(black);
                if((r == x1 && c == y1) || (r == x2 && c == y2) || (r == x3 && c == y3) ) {
                    disks[r][c].setForeground(yellow);
                }
                disks[r][c].addActionListener(this);
                gridPanel.add(disks[r][c]);
            }
        }
        validate();
    }

    private void setUpEndGamePanel(){
        endGamePanel = new JPanel();
        //endGamePanel.setSize()
        endGamePanel.setLayout(new GridLayout(7, 1, 20, 15));
        endGamePanel.setBackground(black);
        endGamePanel.setForeground(yellow);

        JLabel overText = new JLabel();
        overText.setForeground(yellow);
        overText.setHorizontalAlignment(JLabel.CENTER);
        overText.setVerticalAlignment(JLabel.CENTER);
        overText.setFont(new Font(Font.DIALOG, Font.BOLD, 40));
        overText.setText("Game Over!");

        JLabel scoreHolder = new JLabel();
        scoreHolder.setForeground(yellow);
        scoreHolder.setHorizontalAlignment(JLabel.CENTER);
        scoreHolder.setVerticalAlignment(JLabel.CENTER);
        scoreHolder.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        scoreHolder.setText("Your Highest Score: " + highestScore());

/*        JLabel accuracyHolder = new JLabel();
        accuracyHolder.setForeground(yellow);
        accuracyHolder.setVerticalAlignment(JLabel.CENTER);
        accuracyHolder.setHorizontalAlignment(JLabel.CENTER);
        accuracyHolder.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        accuracyHolder.setText("Your accuracy: " + getPercentage() + "%");*/

        JLabel currentScoreHolder = new JLabel();
        currentScoreHolder.setForeground(yellow);
        currentScoreHolder.setVerticalAlignment(JLabel.CENTER);
        currentScoreHolder.setHorizontalAlignment(JLabel.CENTER);
        currentScoreHolder.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        currentScoreHolder.setText("Your current score: " + score);



        startNewButton = new JButton("Start New Game ");
        startNewButton.addActionListener(new StartGame());
        closeButton = new JButton("Close ");
        closeButton.addActionListener(new StopGame());
        closeButton.setSize(40, 20);
        endGamePanel.add(new JLabel());
        endGamePanel.add(overText);
        endGamePanel.add(scoreHolder);
        endGamePanel.add(currentScoreHolder);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(black);
        buttonPanel.add(startNewButton);
        buttonPanel.add(closeButton);
        endGamePanel.add(buttonPanel);
    }

    /**
     * Class method that closes the game screen, builds the endGamePanel
     * and replaces the game screen with the endGamePanel
     *
     */
    public void stopGame(){
        splitPane.remove(gridPanel);
        setUpEndGamePanel();
        splitPane.setBottomComponent(endGamePanel);
        this.validate();
    }

    /**
     * Class method that displays the time remaining and when the time reaches 0,
     * displays "Times Up!"
     * @param time = the amount of starting time users are given
     */
    public void setTimeRemain(int time){
        timeRemain.setText("Time remaining: " + time);
        if(time == 0){
            timeRemain.setText("Times Up!");
        }
    }

    /**
     * Class method that uses System.setProperty() and System.getProperty() that "stores" the total score from that session
     * and checks if the current score for that session is greater than the previous games score.
     * If there are no scores yet, displays the score of the game.
     * If there are scores available, checks and displays highest score in that total session
     * @return returns the currentHighest score from that total session
     */
    private int highestScore(){
        String getProp = System.getProperty("highestScore");
        int currentHighest = score;
        if(getProp == null)
            System.setProperty("highestScore", "" + score);
        else{
            currentHighest = Integer.parseInt(getProp);

            if(currentHighest < score) {
                currentHighest = score;
                System.setProperty("highestScore", "" + currentHighest);
            }
        }

        return currentHighest;
    }

    /**
     * Class method to generate randomly positions for 3 yellow disks
     */
    private void generatePos() {

        r1 = (int)(Math.random()*9) +1;
        r2 = (int)(Math.random()*9) +1;
        r3 = (int)(Math.random()*9) +1;

        while((r1 == r2) || (r1 == r3) || (r2 == r3)) {
            r2 = (int)(Math.random() * 9) +1;
            r3 = (int)(Math.random() * 9) +1;
        }
    }

    /**
     * Class method set up coordinate for 9 possible positions for yellow disks
     *  coordination for 9 boxes where yellow disks should be appeared as ex:
     *  [0][0][0][0][0][0][0][0][0]
     *  [0][0][0][0][0][0][0][0][0]
     *  [0][0][0][1][2][3][0][0][0]
     *  [0][0][0][4][5][6][0][0][0]
     *  [0][0][0][7][8][9][0][0][0]
     *  [0][0][0][0][0][0][0][0][0]
     *  [0][0][0][0][0][0][0][0][0]
     *  [0][0][0][0][0][0][0][0][0]
     */
    private void assignPos () {
        arrayPos = new HashMap<>();
        arrayPos.put(1,"0:0");
        arrayPos.put(2,"0:1");
        arrayPos.put(3,"0:2");
        arrayPos.put(4,"1:0");
        arrayPos.put(5,"1:1");
        arrayPos.put(6,"1:2");
        arrayPos.put(7,"2:0");
        arrayPos.put(8,"2:1");
        arrayPos.put(9,"2:2");
    }

    /**
     * Class method get coordinate for each disk location plus start point
     */
    private void setCords () {
        // : is a delimeter
        x1 = Integer.parseInt(arrayPos.get(r1).split(":")[0]) + start;
        y1 = Integer.parseInt(arrayPos.get(r1).split(":")[1]) + start;

        x2 = Integer.parseInt(arrayPos.get(r2).split(":")[0]) + start;
        y2 = Integer.parseInt(arrayPos.get(r2).split(":")[1]) + start;

        x3 = Integer.parseInt(arrayPos.get(r3).split(":")[0]) + start;
        y3 = Integer.parseInt(arrayPos.get(r3).split(":")[1]) + start;
    }

    /**
     * Method to generate the new position for the disk got click and check to make sure not the same
     *  old location and others yellow disks
     */
    private void genNewPos(){
        p = (int)(Math.random() * 9) + 1;
        newX = Integer.parseInt(arrayPos.get(p).split(":")[0]) + start;
        newY = Integer.parseInt(arrayPos.get(p).split(":")[1])+ start;
        while ((newX == currX1 && newY == currY1) || (newX == currX2 && newY == currY2) || (newX == currX3 && newY == currY3)) {
            p = (int)(Math.random() * 9) + 1;
            newX = Integer.parseInt(arrayPos.get(p).split(":")[0]) + start;
            newY = Integer.parseInt(arrayPos.get(p).split(":")[1]) + start;
        }
    }

    /**
     * method reset new position for the yellow disk just got click
     * @param x is x coordinate of the position
     * @param y is y coordinate of the position
     */
    public void reSetCurrent (int x, int y) {
        if (x == currX1 && y == currY1) {
            currX1 = newX;
            currY1 = newY;
        }
        if (x == currX2 && y == currY2) {
            currX2 = newX;
            currY2 = newY;
        }
        if (x == currX3 && y == currY3) {
            currX3 = newX;
            currY3 = newY;
        }
    }

    /**
     * getPercentage returns that accuracy of the session
     * @return returns that accuracy of that round/session
     */
    private double getPercentage() {
        accuracy = (double) hitScore / click * 100;
        return accuracy;
    }

    /**
     * Method that when the 'click' is recieved on the yellow disks,
     * the yellow disk turns black, the position is changed,
     * score increases by a constant POINT, and the number of hits increases by 1
     * Displays the score, click, and accuracy of that session
     * @param e the event to be processed which is the click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        click += 1;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++) {
                if(e.getSource() == disks[i][j] && disks[i][j].getForeground() == yellow){
                    disks[i][j].setForeground(black);
                    genNewPos();
                    disks[newX][newY].setForeground(yellow);
                    reSetCurrent(i,j);
                    score = score + POINT;
                    hitScore += 1;


                }
            }
        }
        displayScore.setText("" + score);
        displayHits.setText("" + click);
        displayAcc.setText(""+ Math.round(getPercentage()) + "%");
    }

    /**
     * Inner class that implements ActionListener to perform action when click on start new game button
     */
    class StartGame implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            dispose(); //to completely destroy the old gameFrame
            new PlayGame(6);
        }
    }

    /**
     * Inner class that implements ActionListener to perform action when click on close button
     */
    class StopGame implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}



