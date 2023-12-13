package cf.arjun.dev.snake;

import android.graphics.Color;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Random;

public class SnakeModel {

    ArrayList<Position> startingPosition;
    ArrayList<SnakePiece> segments;
    public final int cellSide = 65;
    public String direction = "right";
    public Paint color;

    public SnakeModel () {

        Random random = new Random();

        int col = random.nextInt(5-3)+3;
        int row = random.nextInt(7-2)+2;

        int x = cellSide*col;
        int y = cellSide*row;

        String[] directions = {"up", "down", "right"};

        direction = directions[random.nextInt(directions.length)];

        startingPosition = new ArrayList<>();
        startingPosition.add(new Position(x, y));
        startingPosition.add(new Position(x - cellSide, y));
        startingPosition.add(new Position(x - (2*cellSide), y));

        segments = new ArrayList<>();

        createSnake();

        color = new Paint();
        color.setColor(Color.WHITE);

    }

    private void createSnake () {
        for (Position position : startingPosition) {
            addSegment(position);
        }
    }

    public void addSegment (Position position) {
        segments.add(new SnakePiece( position));
    }

    public void extend () {
        addSegment(new Position(segments.get(segments.size()-1).position));
    }

    public void move () {
        for (int i=segments.size()-1; i>0; i--) {
            segments.get(i).position = new Position(segments.get(i-1).position);
        }
        SnakePiece Head = segments.get(0);
        Position pos = Head.position;
        switch (direction) {
            case "up": {
                pos.y -= cellSide;
                break;
            }
            case "down": {
                pos.y += cellSide;
                break;
            }
            case "left": {
                pos.x -= cellSide;
                break;
            }
            case "right": {
                pos.x += cellSide;
                break;
            }
        }
        Head.position = new Position(pos);
    }

    public void moveUp() {
        direction = "up";
    }

    public void moveDown() {
        direction = "down";
    }

    public void moveLeft() {
        direction = "left";
    }

    public void moveRight() {
        direction = "right";
    }

}
