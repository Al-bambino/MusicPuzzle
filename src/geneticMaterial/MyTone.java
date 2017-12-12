package geneticMaterial;

import java.util.Objects;

public class MyTone {

    /* Gleda da li je ton na pravom mestu u hromozomu */
    private boolean onThePlace = false;
    /* trajanje tona izrazena u tickovima  */
    private long length;
    /* Tick na kom pocinje ton da se svira*/
    private long start;
    /* Jacina na kojoj se nota */
    private int velocity;
    /* Nota(ton) koja se svira */
    private int note;

    /**
     * Pravi novi ton.
     * @param length -> duzina trajanja tona izrazena u tickovima
     * @param start -> kad ton pocinje da se cuje
     * @param velocity -> jacina na kojoj se cuje taj ton
     * @param note -> nota (ton) koji se svira
     */
    public MyTone(long length, long start, int velocity, int note) {
        this.length = length;
        this.start = start;
        this.velocity = velocity;
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyTone)) return false;
        MyTone myTone = (MyTone) o;
        return length == myTone.length &&
                start == myTone.start &&
                velocity == myTone.velocity &&
                note == myTone.note;
    }


    @Override
    public String toString() {
        return "@" + start + ", duzina: " + length +  ", note=" + note + ", velocity=" + velocity;

    }

    @Override
    public int hashCode() {

        return Objects.hash(length, start, velocity, note);
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public int getVelocity() {
        return velocity;
    }

    public boolean isOnThePlace() {
        return onThePlace;
    }

    public void setOnThePlace(boolean onThePlace) {
        this.onThePlace = onThePlace;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }
}
