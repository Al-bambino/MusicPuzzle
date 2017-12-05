package geneticMaterial;

import java.util.Arrays;
import java.util.Random;

public class Chromosome {

    /**
     * TODO Za allMidiEvents smisli nacin za ucitavanje
     */
    private MidiEvent[] allMidiEvents;
    /**
     * Broj midiEvent-ova u hromozomu, odnosno broj midiEventova u trazenom hromozomu.
     * Trazeni hromozom -> deo originalne pesme koji zelimo da reprodukujemo.
     */
    private int chromosomeLength;
    /**
     * Jedan hromozom se sastoji od chromosomeLength midiEvent-ova
     */
    private MidiEvent[] chromosomeMidiEvents;
    /**
     * TODO Proveriti da li je potrebno
     */
    private int currentIndex;
    /**
     * Sanse da hromozom prezivi
     */
    private double fitness;

    public Chromosome(int length) {
        chromosomeMidiEvents = new MidiEvent[length];
        currentIndex = -1;
        chromosomeLength = length;
        fitness = -1;
    }

    public Chromosome(MidiEvent[] chromosomeMidiEvents) {
        this.chromosomeMidiEvents = chromosomeMidiEvents;
        chromosomeLength = chromosomeMidiEvents.length;
        fitness = -1;
    }

    /**
     * TODO Da li treba??
     */
    public boolean addMidiEvent(MidiEvent midiEvent){
        if(midiEvent == null || currentIndex == chromosomeMidiEvents.length - 1 ) return false;
        chromosomeMidiEvents[++currentIndex] = midiEvent;
        return true;
    }

    /**
     * Hromozomu dodaje random midiEventove iz bazena svih midiEventovima date kompozicije.
     * Ukoliko midiEventovi nisu lepo ucitani metoda postavlja chromosomeMidiEvents na null vrednost.
     * TODO napraviti exeption za nepravilan unos midiEventova
     */
    public void makeItRandom(){
        if(allMidiEvents.length == 0){
            System.err.println("Nema ucitanih midiEventova");
            chromosomeMidiEvents = null;
            return;
        }
        Random random = new Random(allMidiEvents.length - 1);
        for (int i = 0; i < chromosomeLength; i++) {
            chromosomeMidiEvents[i] = allMidiEvents[random.nextInt()];
        }
    }

    /**
     * Metoda racuna fitnes za hromozom tako sto uporedjuje midiEvetnove da li su isti.
     * Izrazava se u procentima. Trazeni hromozom ce imati fitnes 1.0.
     * U ovom trenutku ne radim nikakvu optimizaciju, najporstija calculateFitness metoda.
     * @param target -> hromozom koji trazimo (deo pesme)
     */
    public void calculateFitness(Chromosome target) {
        int numOfSame = 0;
        for(int i = 0; i < chromosomeLength; i++) {
            if(chromosomeMidiEvents[i].equals(target.getChromosomeMidiEvents()[i]))
                numOfSame++;
        }
        fitness = (numOfSame * 1.0) / chromosomeLength;

    }

    public Chromosome makeLove(Chromosome secondParent) {
        /**
         * TODO
         */
        return null;
    }

    private void mutate() {
        /**
         * TODO
         */
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < chromosomeLength; i++) {
            sb.append(i + 1);
            sb.append(".midiEvent: @");
            sb.append(chromosomeMidiEvents[i].getTime());
            sb.append(" ključ: ");
            sb.append(chromosomeMidiEvents[i].getNote());
            sb.append(" na jačini: ");
            sb.append(chromosomeMidiEvents[i].getVelocity());
            sb.append('\n');
        }
        return "Hromozom: " + "\n" +
                sb.toString() +
                "Broj pogodjenih hromozoma=" + (int)fitness * chromosomeLength;
    }

    public MidiEvent[] getChromosomeMidiEvents() {
        return chromosomeMidiEvents;
    }

    public void setChromosomeMidiEvents(MidiEvent[] chromosomeMidiEvents) {
        this.chromosomeMidiEvents = chromosomeMidiEvents;
    }

    public MidiEvent[] getAllMidiEvents() {
        return allMidiEvents;
    }

    public void setAllMidiEvents(MidiEvent[] allMidiEvents) {
        this.allMidiEvents = allMidiEvents;
    }

    public int getChromosomeLength() {
        return chromosomeLength;
    }

    public void setChromosomeLength(int chromosomeLength) {
        this.chromosomeLength = chromosomeLength;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
