package cf.arjun.dev.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Delegate {

    private SnakeView snakeView;
    public SnakeModel snake;
    private boolean gameOn = true;
    private Thread gameEngine;
    private int score, highScore;
    private TextView scoreView, highScoreView;
    private volatile SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = getSharedPreferences("SnakeGame", Context.MODE_PRIVATE);

        ImageButton btnUp = findViewById(R.id.arrow_up);
        ImageButton btnDown = findViewById(R.id.arrow_down);
        ImageButton btnLeft = findViewById(R.id.arrow_left);
        ImageButton btnRight = findViewById(R.id.arrow_right);
        ImageButton btnReset = findViewById(R.id.restartButton);
        scoreView = findViewById(R.id.score);
        highScoreView = findViewById(R.id.highScore);

        btnUp.setOnClickListener( v -> moveUp());
        btnDown.setOnClickListener( v -> moveDown());
        btnLeft.setOnClickListener( v -> moveLeft());
        btnRight.setOnClickListener( v -> moveRight());
        btnReset.setOnClickListener( v -> {
            if (gameEngine != null) {
                gameEngine.interrupt();
                gameEngine = null;
                snake = null;
                snakeView = null;
                startGame();
            }
        });

        startGame();

    }

    private void startGame () {

        gameEngine = null;

        score = 0;
        highScore = sharedPreferences.getInt("score", 0);
        if (highScore > 0) {
            String highScoreText = "HIGH SCORE: " + highScore;
            highScoreView.setText(highScoreText);
        }
        String txt = "SCORE: 0";
        scoreView.setText(txt);

        gameOn = true;

        snake = new SnakeModel();

        snakeView = findViewById(R.id.snakeView);
        snakeView.delegate = this;
        snakeView.setGameState(false);

        gameEngine = new Thread (() -> {

            try {
                while (gameOn) {

                    try {
                        Thread.sleep(200);
                    } catch (Exception ignore) {}

                    snake.move();

                    if (snakeView.isSnakeBite()) {
                        snakeView.refresh();
                        snake.extend();
                        score++;
                        runOnUiThread(() -> {
                            String textScore = "SCORE: " + score;
                            scoreView.setText(textScore);
                        });
                    }

                    if (snakeView.isDistance()) {
                        gameOn = false;
                        snakeView.setGameState(true);
                        if (score > highScore) {
                            sharedPreferences.edit().putInt("score", score).apply();
                        }
                    }

                    if (snakeView.collision()) {
                        gameOn = false;
                        snakeView.setGameState(true);
                        if (score > highScore) {
                            sharedPreferences.edit().putInt("score", score).apply();
                        }
                    }

                    snakeView.invalidate();

                }

            } catch (Exception ignore) {}

        });
        gameEngine.start();

    }

    @Override
    public void moveUp() {
        if (snake != null) {
            if (!snake.direction.equalsIgnoreCase("down")) {
                snake.moveUp();
            }
        }
    }

    @Override
    public void moveDown() {
        if (snake != null) {
            if (!snake.direction.equalsIgnoreCase("up")) {
                snake.moveDown();
            }
        }
    }

    @Override
    public void moveLeft() {
        if (snake != null) {
            if (!snake.direction.equalsIgnoreCase("right")) {
                snake.moveLeft();
            }
        }
    }

    @Override
    public void moveRight() {
        if (snake != null) {
            if (!snake.direction.equalsIgnoreCase("left")) {
                snake.moveRight();
            }
        }
    }

    @Override
    public SnakeModel getSnake() {
        return snake;
    }
}