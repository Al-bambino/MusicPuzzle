package geneticMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Chromosome {

    /**
     * TODO Za allMidiEvents smisli nacin za ucitavanje
     * TODO DA li je static?
     */
    private  ArrayList<MyMidiEvent> allMidiEvents;
    /**
     * Broj midiEvent-ova u hromozomu, odnosno broj midiEventova u trazenom hromozomu.
     * Trazeni hromozom -> deo originalne pesme koji zelimo da reprodukujemo.
     */

    private int chromosomeLength;
    /**
     * Jedan hromozom se sastoji od chromosomeLength midiEvent-ova
     */
    private MyMidiEvent[] chromosomeMidiEvents;
    /**
     * TODO Proveriti da li je potrebno
     */
    private int currentIndex;
    /**
     * Sanse da hromozom prezivi
     */
    private double fitness;
    public int numOfSame;
    public Chromosome(int length,  ArrayList<MyMidiEvent>pool) {
        chromosomeMidiEvents = new MyMidiEvent[length];
        currentIndex = -1;
        chromosomeLength = length;
        fitness = -1;
        allMidiEvents = pool;
    }

    public Chromosome(MyMidiEvent[] chromosomeMidiEvents, ArrayList<MyMidiEvent> pool) {
        this.chromosomeMidiEvents = chromosomeMidiEvents;
        chromosomeLength = chromosomeMidiEvents.length;
        currentIndex = chromosomeLength - 1;
        fitness = -1;
        allMidiEvents = pool;
    }

    /**
     * Dodaje midiEventove u hromozom.
     */
    public boolean addMidiEvent(MyMidiEvent midiEvent){
        if(midiEvent == null || currentIndex == chromosomeMidiEvents.length - 1 ) return false;
        chromosomeMidiEvents[++currentIndex] = midiEvent;
        return true;
    }

    /**
     * Hromozomu dodaje random midiEventove iz bazena svih midiEventovima date kompozicije.
     * Ukoliko midiEventovi nisu lepo ucitani metoda postavlja chromosomeMidiEvents na null vrednost.
     * TODO napraviti exeption za nepravilan unos midiEventova
     */
    public void makeItRandom() {
        if(allMidiEvents.size() == 0) {
            System.err.println("Nema učitanih midiEventova");
            chromosomeMidiEvents = null;
            return;
        }
        Random random = new Random();
        for (int i = 0; i < chromosomeLength; i++) {
            chromosomeMidiEvents[i] = allMidiEvents.get(random.nextInt(allMidiEvents.size() - 1));
        }
    }

    /**
     * Metoda racuna fitnes za hromozom tako sto uporedjuje midiEvetnove da li su isti.
     * Izrazava se u broju istih hromozoma na 4 stepen. Dizem na 4 kako bi razlika izmedju dva vrlo slicna bila sto veca.
     * @param target -> hromozom koji trazimo (deo pesme)
     */
    public double calculateFitness(Chromosome target) {
        numOfSame = 0;
        int numOfContains = 0;
        for(int i = 0; i < chromosomeLength; i++) {
            if( chromosomeMidiEvents[i].equals(target.getChromosomeMidiEvents()[i])) {
                numOfSame++;
                chromosomeMidiEvents[i].setOnThePlace(true);
            } else {
                for (int j = 0; j < chromosomeLength; j++) {
                    if( chromosomeMidiEvents[i].equals(target.getChromosomeMidiEvents()[j])){
                        numOfContains++;
                      //  chromosomeMidiEvents[i].setInRightChromosome(true);
                    }
                }
            }
        }
        fitness = Math.pow(2.0, numOfSame) + numOfContains ;
        return fitness;
    }

    /**
     * Metoda koja pravi novi hromozom tako sto ukrsta genetski materijal od random tacke sa prosledjenim hromozomom.
     * @param secondParent -> hromozom sa kojim se ukrsta genetski materijal.
     * @return novi hromozom.
     */
    public Chromosome makeLove(Chromosome secondParent, Chromosome target) {
        Random random = new Random();
        int n = random.nextInt(chromosomeLength - 1);
        Chromosome child = new Chromosome(chromosomeLength, allMidiEvents);
        for(int i = 0; i < chromosomeLength; i++) {
           /*if(this.chromosomeMidiEvents[i].isOnThePlace())
                childMidiEvents[i] = this.chromosomeMidiEvents[i];
            else if(secondParent.chromosomeMidiEvents[i].isOnThePlace())
                childMidiEvents[i] = secondParent.chromosomeMidiEvents[i];
            else*/
            if (i < n)  {
                this.chromosomeMidiEvents[i].setOnThePlace(false);
                child.addMidiEvent(this.chromosomeMidiEvents[i]);
            }
            else  {
                secondParent.chromosomeMidiEvents[i].setOnThePlace(false);
                child.addMidiEvent(secondParent.chromosomeMidiEvents[i]);
            }
        }
        return child;
    }

    /**
     * Proverava da li ce svaki deo hromozoma mutirati. Ukoliko je random broj manji od mutationRate onda se taj hromozom
     * menja (mutira) sa nekim iz allMidiEvents.
     * @param mutationRate -> procenat mutacije izrazen u double.
     */
    public void mutate(double mutationRate) {
        Random random = new Random();
        for(int i = 0; i < chromosomeLength; i++){
            if(chromosomeMidiEvents[i].isOnThePlace()) continue;
            if(random.nextDouble() <= mutationRate){
                int indexOfRandomMidiEvent = random.nextInt(allMidiEvents.size() - 1);
                MyMidiEvent randomMidiEvent = allMidiEvents.get(indexOfRandomMidiEvent);
                chromosomeMidiEvents[i] = randomMidiEvent;
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chromosome)) return false;
        Chromosome that = (Chromosome) o;
        if(chromosomeLength != that.chromosomeLength) return false;
        for(int i = 0; i < chromosomeLength; i++) {
            if(!this.chromosomeMidiEvents[i].equals(that.chromosomeMidiEvents[i]))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(chromosomeLength);
        result = 31 * result + Arrays.hashCode(chromosomeMidiEvents);
        return result;
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
                "Broj pogodjenih hromozoma=" + numOfSame;
    }


    public MyMidiEvent[] getChromosomeMidiEvents() {
        return chromosomeMidiEvents;
    }

    public void setChromosomeMidiEvents(MyMidiEvent[] chromosomeMidiEvents) {
        this.chromosomeMidiEvents = chromosomeMidiEvents;
    }

    public ArrayList<MyMidiEvent> getAllMidiEvents() {
        return allMidiEvents;
    }

    public void setAllMidiEvents(ArrayList<MyMidiEvent> allMidiEvents) {
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
