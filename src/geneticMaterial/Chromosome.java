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
     * Broj tonova u hromozomu (jednak je broju tonova u trazenom hromozomu).
     * Trazeni hromozom -> deo originalne pesme koji zelimo da reprodukujemo.
     */
    private int chromosomeLength;

    /** Jedan hromozom se sastoji od chromosomeLength tonova. **/
    private  MyTone[] chromosomeTones;
    /**
     * Bazen sa svim tonovima iz midi fajla koji je prosledjen.
     */
    private  ArrayList<MyTone> tonesPool;
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

    /**
     * Pravi novi hromozom sa praznim nizom genetskog materijala(tonova). Fitnes hromozoma se default-no postavlja na -1.
     * @param length -> duzina hromozoma (koliko tonova ce imati svaki hromozom)
     * @param pool -> bazen sa svim tonovima ucitanih iz midi fajla. {@link Population#fillTonesPool(String)}
     */
    public Chromosome(int length,  ArrayList<MyTone> pool) {
        chromosomeTones = new MyTone[length];
        currentIndex = -1;
        chromosomeLength = length;
        fitness = -1;
        tonesPool = pool;
    }

    public Chromosome(MyMidiEvent[] chromosomeMidiEvents, ArrayList<MyTone> pool) {
        this.chromosomeMidiEvents = chromosomeMidiEvents;
        chromosomeLength = chromosomeMidiEvents.length;
        currentIndex = chromosomeLength - 1;
        fitness = -1;
        tonesPool = pool;
    }
    /**
     * Dodaje tonove u hromozom
     * @param tone -> ton koji ce biti dodat.
     * @return true ukoliko je bilo mesta u hromozomu i ukoliko nije prosledjeni ton nije bio null.
     */
    public boolean addTones(MyTone tone) {
        if (tone == null || currentIndex == chromosomeTones.length - 1) return false;
        chromosomeTones[++currentIndex] = tone;
        return true;
    }

    /**
     * Hromozomu dodaje random tonove iz bazena svih tonova.
     * Ukoliko tonovi nisu lepo ucitani metoda postavlja chromosomeTones na null vrednost.
     * TODO napraviti exeption za nepravilan unos midiEventova
     */
    public void makeItRandom() {
        if(tonesPool.size() == 0) {
            System.err.println("Nema učitanih midiEventova");
            chromosomeTones = null;
            return;
        }
        Random random = new Random();
        for (int i = 0; i < chromosomeLength; i++) {
            int randomIndex = random.nextInt(tonesPool.size() - 1);
            chromosomeTones[i] = tonesPool.get(randomIndex);
        }
    }

    /**
     * Metoda racuna fitnes za hromozom tako sto uporedjuje tonove.
     * Izrazava se u broju istih tonova na 2 stepen + brpj tonova koji se pojavljuju i u targetu.
     * @param target -> hromozom koji trazimo (deo pesme)
     */
    public double calculateFitness(Chromosome target) {
        numOfSame = 0;
        int numOfContains = 0;
        for(int i = 0; i < chromosomeLength; i++) {
            if(this.chromosomeTones[i].equals(target.chromosomeTones[i])) {
                numOfSame++;
                chromosomeTones[i].setOnThePlace(true);
                continue;
            }
            for (int j = 0; j < chromosomeLength; j++) {
                if( chromosomeTones[i].equals(target.chromosomeTones[j])){
                    numOfContains++;
                    //  chromosomeMidiEvents[i].setInRightChromosome(true);
                }
            }

        }
        fitness = Math.pow(2.0, numOfSame) + numOfContains;
        return fitness;
    }

    /**
     * Metoda koja pravi novi hromozom tako sto ukrsta genetski materijal od random tacke sa prosledjenim hromozomom.
     * @param father -> hromozom sa kojim se ukrsta genetski materijal.
     * @param target -> TODO naporaviti bolje ukrstanje
     * @return novi hromozom.
     */
    public Chromosome makeLove(Chromosome father, Chromosome target) {
        Random random = new Random();
        int rundomNum = random.nextInt(chromosomeLength - 1);
        Chromosome child = new Chromosome(chromosomeLength, tonesPool);
        for(int i = 0; i < chromosomeLength; i++) {
           /*if(this.chromosomeMidiEvents[i].isOnThePlace())
                childMidiEvents[i] = this.chromosomeMidiEvents[i];
            else if(father.chromosomeMidiEvents[i].isOnThePlace())
                childMidiEvents[i] = father.chromosomeMidiEvents[i];
            else*/
            if (i < rundomNum)  {
                this.chromosomeTones[i].setOnThePlace(false);
                child.addTones(this.chromosomeTones[i]);
            }
            else  {
                father.chromosomeTones[i].setOnThePlace(false);
                child.addTones(father.chromosomeTones[i]);
            }
        }
        return child;
    }

    /**
     * Proverava da li ce svaki deo hromozoma mutirati. Ukoliko je random broj manji od mutationRate onda se taj ton
     * menja (mutira) sa nekim iz tonesPool.
     * Ukoliko se ton nalazi na pravom mestu u hromozomu on nece mutirati.
     * @param mutationRate -> procenat mutacije izrazen u double.
     */
    public void mutate(double mutationRate) {
        Random random = new Random();
        for(int i = 0; i < chromosomeLength; i++) {
            if(chromosomeTones[i].isOnThePlace() || random.nextDouble() > mutationRate) continue;
            int randomToneIndex = random.nextInt(tonesPool.size() - 1);
            MyTone randomTone = tonesPool.get(randomToneIndex);
            chromosomeTones[i] = randomTone;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chromosome)) return false;
        Chromosome that = (Chromosome) o;
        if(chromosomeLength != that.chromosomeLength) return false;
        for(int i = 0; i < chromosomeLength; i++) {
            if(!this.chromosomeTones[i].equals(that.chromosomeTones[i]))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(chromosomeLength);
        result = 31 * result + Arrays.hashCode(chromosomeTones);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < chromosomeLength; i++) {
            sb.append(i + 1);
            sb.append(".pocinje: @");
            sb.append(chromosomeTones[i].getStart());
            sb.append(" nota: ");
            sb.append(chromosomeTones[i].getNote());
            sb.append(" na jačini: ");
            sb.append(chromosomeTones[i].getVelocity());
            sb.append('\n');
        }
        return "Hromozom: " + "\n" +
                sb.toString() +
                "Broj pogodjenih hromozoma = " + numOfSame;
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

    public MyTone[] getChromosomeTones() {
        return chromosomeTones;
    }

    public void setChromosomeTones(MyTone[] chromosomeTones) {
        this.chromosomeTones = chromosomeTones;
    }

    public ArrayList<MyTone> getTonesPool() {
        return tonesPool;
    }

    public void setTonesPool(ArrayList<MyTone> tonesPool) {
        this.tonesPool = tonesPool;
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
