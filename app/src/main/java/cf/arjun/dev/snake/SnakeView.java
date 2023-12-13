package cf.arjun.dev.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;
import androidx.annotation.Nullable;

public class SnakeView extends View {

    public Delegate delegate;

    private Random random;
    public final int cellSide = 65;
    public int x;
    public int y;
    public int width;
    public int height;
    private final Paint bg, color, font;
    public volatile boolean over;

    public SnakeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        color = new Paint();
        color.setColor(Color.CYAN);
        bg = new Paint();
        bg.setColor(Color.BLACK);
        font = new Paint();
        font.setColor(Color.RED);
        random = new Random();

        x = random.nextInt(5)*cellSide;
        y = random.nextInt(5)*cellSide;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();

        canvas.drawRect(0, 0, width, height, bg);

        canvas.drawOval((float) x, (float) y, (float) x+cellSide-15, (float) y+cellSide-15, color);

        if (delegate != null) {

            SnakeModel snake = delegate.getSnake();
            for (SnakePiece piece : snake.segments) {
                canvas.drawRect(piece.position.x, piece.position.y,
                        piece.position.x + snake.cellSide, piece.position.y + snake.cellSide, snake.color);
            }

            if (over) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Typeface face = getResources().getFont(R.font.courier_bold);
                    font.setTypeface(face);
                    font.setTextSize(100f);
                    canvas.drawText("Game Over.", width - (width*0.79f), width - (width*0.80f)/2, font);
                }
            }

        }

    }

    public boolean isSnakeBite () {

        if (delegate != null) {
            SnakeModel snake = delegate.getSnake();
            Position pos = snake.segments.get(0).position;
            int distX = Integer.max(pos.x , x) - Integer.min(pos.x, x);
            int distY = Integer.max(pos.y , y) - Integer.min(pos.y, y);
            return distX <= 40 && distY <= 40;
        }

        return false;

    }

    public boolean isDistance () {

        if (delegate != null) {

            SnakeModel snake = delegate.getSnake();
            Position pos = snake.segments.get(0).position;
            return pos.x < 0 || pos.x > width - (2*snake.cellSide)/2 || pos.y < 0 || pos.y > height - (2*snake.cellSide)/2;

        }

        return false;
    }

    public boolean collision () {

        if (delegate != null) {

            SnakeModel snake = delegate.getSnake();
            int size = snake.segments.size();
            Position pos = snake.segments.get(0).position;
            for (int i=2; i<size; i++) {
                Position p = snake.segments.get(i).position;
                int distX = Integer.max(pos.x , p.x) - Integer.min(pos.x, p.x);
                int distY = Integer.max(pos.y , p.y) - Integer.min(pos.y, p.y);
                if (distX <= 30 && distY <= 30) {
                    return true;
                }
            }

        }

        return false;

    }

    public void setGameState (boolean state) {
        this.over = state;
    }

    public void refresh () {

        int availWidth = width-cellSide;
        int availHeight = height-cellSide;

        int col = random.nextInt(availWidth/cellSide);
        int row = random.nextInt(availHeight/cellSide);

        int newX = col*cellSide;
        int newY = row*cellSide;

        if (newX == x) newX = random.nextInt(availWidth/cellSide)*cellSide;
        else if (newY == y) newY = random.nextInt(availHeight/cellSide)*cellSide;

        this.x = newX;
        this.y = newY;

        if (delegate != null) {
            SnakeModel snake = delegate.getSnake();
            for (SnakePiece piece : snake.segments) {
                Position pos = piece.position;
                if (pos.x / snake.cellSide == col && pos.y / snake.cellSide == row) {
                    refresh();
                    break;
                }
            }
        }

    }

}
