package com.tmjonker.pong.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {

    private Circle ball;
    private double x_speed_ball = 2;
    private double y_speed_ball = 0;
    private double max_angle_ball = 3;
    private double y_speed_paddle = (x_speed_ball + y_speed_ball) * 0.75;
    final private int WIDTH = 600;
    final private int HEIGHT = 500;
    final private int BALL_SIZE = 8;
    final private Rectangle leftPaddle;
    final private Rectangle rightPaddle;
    final private int RECTANGLE_HEIGHT = 80;
    final private int RECTANGLE_WIDTH = 10;
    private int playerScore;
    private int computerScore;
    private Timeline t;
    private Stage primaryStage;

    public GameController() {

        Group root = new Group();

        ball = new Circle(BALL_SIZE, Color.WHITE);
        ball.setCenterX(BALL_SIZE);
        ball.setCenterY(BALL_SIZE);

        leftPaddle = new Rectangle(0, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        leftPaddle.setFill(Color.WHITE);
        rightPaddle = new Rectangle(WIDTH - RECTANGLE_WIDTH, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        rightPaddle.setFill(Color.WHITE);

        root.getChildren().addAll(ball, leftPaddle, rightPaddle);
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        // Controls movement of the left paddle by the human player.
        scene.setOnMouseMoved(e -> {
            leftPaddle.setY(e.getY() - (RECTANGLE_HEIGHT * 0.5));
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                startGame(); // Start game by pressing Enter.
        });

        primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();

        KeyFrame k = new KeyFrame(Duration.millis(5), e -> gamePlay(e));

        t = new Timeline(k);
        t.setCycleCount(Timeline.INDEFINITE);

        resetGame();
    }

    private void resetGame() {

        playerScore = 0;
        computerScore = 0;
        resetNextRound();
    }

    private void resetNextRound() {

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
        y_speed_paddle = ((Math.abs(y_speed_ball) + Math.abs(x_speed_ball)) * 1.25);

        y_speed_ball = impactZone * max_angle_ball;
        x_speed_ball = Math.abs(x_speed_ball);

        if (impactZone > 0.65)
            y_speed_ball = Math.abs(y_speed_ball);

        if (impactZone > 0.48 && impactZone < 0.52)
            y_speed_ball = 0;

        if (impactZone < 0.45) {
            y_speed_ball += max_angle_ball / 2;
            y_speed_ball = -y_speed_ball;
        }

        if (computerHit) {
            x_speed_ball = -x_speed_ball;
        }
    }

    private void computerBallDirection() {

        x_speed_ball = -x_speed_ball;
        y_speed_ball = -y_speed_ball;
    }

    private void startGame() {

        t.play();
    }

    private void stopGame() {

        t.stop();
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
            if (ball.getCenterX() > WIDTH * 0.75) {
                if (rightPaddle.getY() < right_paddle_center) {
                    rightPaddle.setY(rightPaddle.getY() + y_speed_paddle);
                } else if (rightPaddle.getY() > right_paddle_center) {
                    rightPaddle.setY(rightPaddle.getY() - y_speed_paddle);
                }
            }
        }

        // indicates that the computer has scored.
        if ((ball.getCenterX() < BALL_SIZE)) {
            e.consume();
            computerScore++;
            resetGame();
            computerBallDirection();
        }

        // indicates that the human player has scored.
        if ((ball.getCenterX() > WIDTH - BALL_SIZE)) {
            e.consume();
            playerScore++;
            resetGame();
            computerBallDirection();
        }

        // human player paddle hit indicator.
        if (ball.getCenterX() <= (RECTANGLE_WIDTH + (BALL_SIZE * 0.5))
                && (ball.getCenterY() + (BALL_SIZE * 0.5) <= leftPaddle.getY() + (RECTANGLE_HEIGHT))
                && ball.getCenterY() >= leftPaddle.getY() + (BALL_SIZE * 0.5)) {
            switchBallDirection((((leftPaddle.getY() + (RECTANGLE_HEIGHT)) - ball.getCenterY()) / RECTANGLE_HEIGHT) - 1);
        }

        // computer player paddle hit indicator.
        if (ball.getCenterX() >= (WIDTH - RECTANGLE_WIDTH )
                && (ball.getCenterY() + (0.5 * BALL_SIZE) <= rightPaddle.getY() + (RECTANGLE_HEIGHT))
                && ball.getCenterY() >= rightPaddle.getY()+ (BALL_SIZE * 0.5)) {
            switchBallDirection((((rightPaddle.getY() + (RECTANGLE_HEIGHT)) - ball.getCenterY()) / RECTANGLE_HEIGHT) - 1);
        }

        // allows ball to bounce off top and bottom of gameplay window instead of exiting the scene.
        if ((ball.getCenterY() <= BALL_SIZE) || (ball.getCenterY() >= HEIGHT - BALL_SIZE))
            y_speed_ball = -y_speed_ball;
    }
}
