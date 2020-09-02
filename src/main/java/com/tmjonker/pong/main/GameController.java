package com.tmjonker.pong.main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {

    private Circle ball;
    private double x_speed_ball = 3;
    private double y_speed_ball = 0;
    private double max_angle_ball = 1.5;
    private double y_speed_paddle = (x_speed_ball + y_speed_ball) * 0.75;
    final private int WIDTH = 900;
    final private int HEIGHT = 800;
    final private int BALL_SIZE = 8;
    private Rectangle leftPaddle;
    private Rectangle rightPaddle;
    final private int RECTANGLE_HEIGHT = 80;
    final private int RECTANGLE_WIDTH = 10;
    private int playerScore = 0;
    private int computerScore = 0;
    private Text playerScoreText;
    private Text computerScoreText;
    private Text computerWinnerText;
    private Text playerWinnerText;
    private Timeline timeLine;
    private Stage primaryStage;

    public GameController() {

        drawGameBoard(); // generates the game layout.

        implementAnimation(); // generates KeyFrame and TimeLine.

        resetGame(); // sets game up.
    }

    private void implementAnimation() {

        KeyFrame keyFrame = new KeyFrame(Duration.millis(5), e -> gamePlay(e));

        timeLine = new Timeline(keyFrame);
        timeLine.setCycleCount(Timeline.INDEFINITE);
    }

    private void drawGameBoard() {
        ball = new Circle(BALL_SIZE, Color.WHITE);
        ball.setCenterX(BALL_SIZE);
        ball.setCenterY(BALL_SIZE);

        leftPaddle = new Rectangle(10, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        leftPaddle.setFill(Color.WHITE);
        rightPaddle = new Rectangle(WIDTH - RECTANGLE_WIDTH - 10, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        rightPaddle.setFill(Color.WHITE);

        Line middleLine = new Line(WIDTH/2, 0, WIDTH/2, HEIGHT);
        middleLine.setStroke(Color.WHITE);
        middleLine.getStrokeDashArray().addAll(12d, 20d);
        middleLine.setStrokeWidth(2);

        playerScoreText = new Text();
        playerScoreText.setFill(Color.WHITE);
        playerScoreText.setFont(Font.font("times new roman", FontWeight.NORMAL, 48));
        playerScoreText.setLayoutX(194.5);
        playerScoreText.setLayoutY((100));
        playerScoreText.setText(Integer.toString(playerScore));

        computerScoreText = new Text();
        computerScoreText.setFill(Color.WHITE);
        computerScoreText.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 48));
        computerScoreText.setLayoutX((WIDTH/2) + 194.5);
        computerScoreText.setLayoutY((100));
        computerScoreText.setText(Integer.toString(computerScore));

        playerWinnerText = new Text();
        playerWinnerText.setFill(Color.WHITE);
        playerWinnerText.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 48));
        playerWinnerText.setLayoutX(151.2);
        playerWinnerText.setLayoutY((HEIGHT /4));

        computerWinnerText = new Text();
        computerWinnerText.setFill(Color.WHITE);
        computerWinnerText.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 48));
        computerWinnerText.setLayoutX((WIDTH/2) + 151.2);
        computerWinnerText.setLayoutY((HEIGHT / 4));

        Group root = new Group();

        root.getChildren().addAll(ball, middleLine, leftPaddle, rightPaddle,
                playerScoreText, computerScoreText, playerWinnerText, computerWinnerText);
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        // Controls movement of the left paddle by the human player.
        scene.setOnMouseMoved(e -> {
            leftPaddle.setY(e.getY() - (RECTANGLE_HEIGHT * 0.5));
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                startGame(); // Start game by pressing Enter.
            else if (e.getCode().equals(KeyCode.SPACE))
                resetGame();
        });

        primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pong");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }

    private void resetGame() {

        playerScore = 0;
        updatePlayerScore(true);
        computerScore = 0;
        updateComputerScore(true);
        resetNextRound();
    }

    private void resetNextRound() {

        checkScore();
        stopGame();
        ball.setCenterX(primaryStage.getWidth() / 2);
        ball.setCenterY(primaryStage.getHeight() / 2);
        rightPaddle.setY(HEIGHT/2 - (RECTANGLE_HEIGHT*0.5));
    }

    /**
     * determines launch angle of ball based on where it impacts paddle.
     *
     * @param impactZone
     */
    private void switchBallDirection(double impactZone) {

        boolean computerHit;

        if (x_speed_ball > 0 ) {
            computerHit = true;
        } else
            computerHit = false;

        impactZone = Math.abs(impactZone);
        y_speed_paddle = ((Math.abs(y_speed_ball) + Math.abs(x_speed_ball))) * 1.10; //sets computer paddle response speed.

        y_speed_ball = impactZone * max_angle_ball;
        x_speed_ball = Math.abs(x_speed_ball);

        if (impactZone > 0.52)
            y_speed_ball = Math.abs(y_speed_ball);

        if (impactZone >= 0.48 && impactZone <= 0.52)
            y_speed_ball = 0;

        if (impactZone < 0.48) {
            y_speed_ball = (1.00 - impactZone) * max_angle_ball;
            y_speed_ball = -y_speed_ball;
        }

        if (computerHit) {
            x_speed_ball = -x_speed_ball;
        }
    }

    private void computerBallDirection() {

        if (x_speed_ball > 0)
            x_speed_ball = -x_speed_ball;
        else if (x_speed_ball < 0)
            x_speed_ball = Math.abs(x_speed_ball);
    }

    private void updateComputerScore(boolean isNull) {

        if (isNull)
            computerScoreText.setText(Integer.toString(computerScore));
        else
            computerScoreText.setText(Integer.toString(++computerScore));
    }

    private void updatePlayerScore(boolean isNull) {

        if (isNull)
            playerScoreText.setText(Integer.toString(computerScore));
        else
            playerScoreText.setText(Integer.toString(++computerScore));
    }

    private void startGame() {

        timeLine.play();
    }

    private void stopGame() {

        timeLine.stop();
    }

    private void checkScore() {

        if (computerScore == 10 && playerScore == 10) {
            playerWinnerText.setText("TIE");
            computerWinnerText.setText("TIE");
        } else if (computerScore == 10 && playerScore < 10) {
            playerWinnerText.setText("LOSER");
            computerWinnerText.setText("WINNER");
        } else if (computerScore < 10 && playerScore == 10) {
            playerWinnerText.setText("WINNER");
            computerWinnerText.setText("LOSER");
        }
    }

    /**
     * code that determines gameplay logic.
     *
     * @param e     the catalyst that sets the game in action.
     */
    private void gamePlay(ActionEvent e) {

        ball.setCenterX(ball.getCenterX() + x_speed_ball);
        ball.setCenterY((ball.getCenterY() + y_speed_ball));

        double right_paddle_center = ball.getCenterY() - (RECTANGLE_HEIGHT/2);

        // determines placement of computer paddle.
        if (x_speed_ball > 0 ) {
            if (ball.getCenterX() > WIDTH * 0.60) {
                if (rightPaddle.getY() < right_paddle_center) {
                    rightPaddle.setY(rightPaddle.getY() + y_speed_paddle);
                } else if (rightPaddle.getY() > right_paddle_center) {
                    rightPaddle.setY(rightPaddle.getY() - y_speed_paddle);
                }
            }
        } else if (rightPaddle.getY() > (HEIGHT/2 - (RECTANGLE_HEIGHT*0.5))) {
            rightPaddle.setY(rightPaddle.getY() - 1);
        } else if (rightPaddle.getY() < (HEIGHT/2 - (RECTANGLE_HEIGHT*0.5))) {
            rightPaddle.setY(rightPaddle.getY() + 1);
        }

        // indicates that the computer has scored.
        if ((ball.getCenterX() < BALL_SIZE)) {
            updateComputerScore(false);
            resetNextRound();
            computerBallDirection();
        }

        // indicates that the human player has scored.
        if ((ball.getCenterX() > WIDTH - BALL_SIZE)) {
            updatePlayerScore(false);
            resetNextRound();
            computerBallDirection();
        }

        // human player paddle hit indicator.
        if (ball.getCenterX() <= (10 + RECTANGLE_WIDTH + (BALL_SIZE * 0.5))
                && (ball.getCenterY() <= leftPaddle.getY() + (RECTANGLE_HEIGHT))
                && ball.getCenterY() + (BALL_SIZE / 2) >= leftPaddle.getY()) {
            switchBallDirection((((leftPaddle.getY() + (RECTANGLE_HEIGHT)) - ball.getCenterY()) / RECTANGLE_HEIGHT) - 1);
        }

        // computer player paddle hit indicator.
        if (ball.getCenterX() >= (WIDTH - RECTANGLE_WIDTH - 10)
                && (ball.getCenterY() <= rightPaddle.getY() + (RECTANGLE_HEIGHT))
                && ball.getCenterY() + (BALL_SIZE / 2) >= rightPaddle.getY()) {
            switchBallDirection((((rightPaddle.getY() + (RECTANGLE_HEIGHT)) - ball.getCenterY()) / RECTANGLE_HEIGHT) - 1);
        }

        // allows ball to bounce off top and bottom of gameplay window instead of exiting the scene.
        if ((ball.getCenterY() <= BALL_SIZE) || (ball.getCenterY() >= HEIGHT - BALL_SIZE))
            y_speed_ball = -y_speed_ball;
    }
}
