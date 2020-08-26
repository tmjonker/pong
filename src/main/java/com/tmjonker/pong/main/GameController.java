package com.tmjonker.pong.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {

    private Circle ball;
    private double x_speed = 3;
    private double y_speed = 3;
    final private int WIDTH = 600;
    final private int HEIGHT = 500;
    final private int BALL_SIZE = 20;
    final private int RECTANGLE_HEIGHT = 80;
    final private int RECTANGLE_WIDTH = 20;
    private int playerScore;
    private int computerScore;
    private Timeline t;
    private Stage primaryStage;

    public GameController() {

        Group root = new Group();

        ball = new Circle(BALL_SIZE);
        ball.setCenterX(BALL_SIZE);
        ball.setCenterY(BALL_SIZE);

        Rectangle leftRectangle = new Rectangle(0, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        Rectangle rightRectangle = new Rectangle(WIDTH - RECTANGLE_WIDTH, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);

        root.getChildren().addAll(ball, leftRectangle, rightRectangle);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setOnMouseMoved(e -> {
            leftRectangle.setY(e.getY() - (RECTANGLE_HEIGHT * 0.5));

            if (leftRectangle.getY() >= HEIGHT - RECTANGLE_HEIGHT)
                leftRectangle.setY(HEIGHT - RECTANGLE_HEIGHT);

            if (leftRectangle.getY() <= 0)
                leftRectangle.setY(0);
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                startGame();
        });
        primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();

        resetBallPosition();

        KeyFrame k = new KeyFrame(Duration.millis(10), e -> {

            ball.setCenterX(ball.getCenterX() + x_speed);
            ball.setCenterY((ball.getCenterY() + y_speed));

            rightRectangle.setY(ball.getCenterY() - (RECTANGLE_HEIGHT * 0.5));

            if (rightRectangle.getY() >= HEIGHT - RECTANGLE_HEIGHT)
                rightRectangle.setY(HEIGHT - RECTANGLE_HEIGHT);

            if (rightRectangle.getY() <= 0)
                rightRectangle.setY(0);

            if ((ball.getCenterX() < BALL_SIZE)) {
                e.consume();
                computerScore++;
                resetGame();
                switchBallDirection();
            }

            if (ball.getCenterX() <= (RECTANGLE_WIDTH + (BALL_SIZE * 0.5))
                    && (ball.getCenterY() <= leftRectangle.getY() + RECTANGLE_HEIGHT)) {
                switchBallDirection();
            }

            if (ball.getCenterX() >= (WIDTH - RECTANGLE_WIDTH - (0.5 * BALL_SIZE))
                    && ball.getCenterY() <= rightRectangle.getY() + RECTANGLE_HEIGHT) {
                switchBallDirection();
            }


            if ((ball.getCenterX() > WIDTH - BALL_SIZE)) {
                e.consume();
                playerScore++;
                resetGame();
                switchBallDirection();
            }


            if ((ball.getCenterY() <= BALL_SIZE) || (ball.getCenterY() >= HEIGHT - BALL_SIZE))
                y_speed = -y_speed;
        });

        t = new Timeline(k);
        t.setCycleCount(Timeline.INDEFINITE);
    }

    private void resetGame() {

        t.stop();
        resetBallPosition();
    }

    private void resetBallPosition() {

        ball.setCenterX(primaryStage.getWidth() / 2);
        ball.setCenterY(primaryStage.getHeight() / 2);
    }

    private void switchBallDirection() {

        if (x_speed > 0) {
            x_speed = -x_speed;
        } else {
                x_speed = Math.abs(x_speed);
            }
    }

    private void startGame() {

        t.play();
    }
}
