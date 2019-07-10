package app;

/**
 * Created by Innokentiy on 28.06.2019.
 */
public class EntrancePosition {
    private long line;
    private long position;
    public EntrancePosition(long line, long position){
        this.line = line;
        this.position = position;
    }

    public long getLine() {
        return line;
    }

    public long getPosition() {
        return position;
    }
}
