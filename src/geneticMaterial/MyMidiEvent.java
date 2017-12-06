package geneticMaterial;

import java.util.Objects;

public class MyMidiEvent {

    private boolean onThePlace = false;
    private boolean inRightChromosome = false;
    /*
    Vreme kada se pojavljuje ovaj ton
     */
    private long time;
    /*
    Broj te "note"
     */
    private int note;
    /*
    Jacina tog zvuka, ako je 0 tad taj zvuk prestaje da se cuje
     */
    private int velocity;

    public MyMidiEvent(long time, int note, int velocity) {
        this.time = time;
        this.note = note;
        this.velocity = velocity;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    @Override
    public String toString() {
        return "@" + time + ", note=" + note + ", velocity=" + velocity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyMidiEvent)) return false;
        MyMidiEvent midiEvent = (MyMidiEvent) o;
        return time == midiEvent.time &&
                note == midiEvent.note &&
                velocity == midiEvent.velocity;
    }

    @Override
    public int hashCode() {

        return Objects.hash(time, note, velocity);
    }

    public boolean isOnThePlace() {
        return onThePlace;
    }

    public void setOnThePlace(boolean onThePlace) {
        this.onThePlace = onThePlace;
    }

    public boolean isInRightChromosome() {
        return inRightChromosome;
    }

    public void setInRightChromosome(boolean inRightChromosome) {
        this.inRightChromosome = inRightChromosome;
    }
}
