package cf.arjun.dev.snake;

public interface Delegate {
    void moveUp ();
    void moveDown ();
    void moveLeft ();
    void moveRight ();

    SnakeModel getSnake ();

}
