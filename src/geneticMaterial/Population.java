package geneticMaterial;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.util.Random;

public class Population {
    /**
     * Verovatnoca da hromozom mutira
     */
    private double mutationRate;
    /**
     * Populacija koju cine jedinke(hromozomi). Jedna populacija je jedna generacija zapravo.
     */
    private Chromosome[] population;
    /**
     * Deo pesme koji treba da nadjemo ovom populacijom.
     */
    private Chromosome target;
    /**
     * Niz sa svim hromozomima popunjen po verovatnoci da prezivi (fitnessu)
     * @see {@link #naturalSelection()}
     */
    private ArrayList<Chromosome> possibilityPool = new ArrayList<>();
    /**
     * Redni broj generacije
     */
    private int generationNumber;
    /**
     * Velicina populacije -> broj jedinki(hromozoma) u generaciji
     */
    private int populationSize;
    /**
     * Najbolji hromozom u generaciji (Djak generacije)
     */
    private Chromosome ourBest;
    /**
     * Proverava da li imamo hromozom koji se potpuno poklapa sa targetom
     */
    private  boolean weHaveAWinner;
    /** Bazen sa svim midiEventovima **/
    private ArrayList<MyMidiEvent> midiPool = new ArrayList<>();
    /** Bazen sa svim tonovima ucitanih iz midi fajla. **/
    private ArrayList<MyTone> tonePool = new ArrayList<>();
    private int chromosomeLength;
    /**
     * Pravi novu populaciju, popunjenu random hromozomima. Takdoje, popunjava tonePool svim tonovima.
     * @param mutationRate -> šansa da se javi mutacija u hromozomu, posle ukraštanja.
     * @param populationSize -> veličina populacije, broj jedinki (hromozoma) koji hocemo da imamo.
     * @param targetStart -> redni broj tona od kog pocinje target (Pr. zelimo da nadjemo prvih 10 tonova,
     *                    targetStart = 0.
     * @param targetLength -> duzina hromozoma koji tražimo (broj tonova u hromzomu).
     * @param filePath -> putanja do midi fajla.
     */
    public Population(double mutationRate, int populationSize, int targetStart, int targetLength, String filePath) {
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        chromosomeLength = targetLength;
        population = new Chromosome[populationSize];
        fillTonesPool(filePath);
        target = makeTarget(targetStart, targetLength);
        makeRandomGeneration(targetLength);
    }

    /**
     * TODO poboljsati metodu, izbaciti mozda {@link #makeTones(ArrayList)}
     * @param filePath
     */
    private void fillTonesPool(String filePath){
        Sequence sequence = null;
        try {
            sequence = MidiSystem.getSequence(new File(filePath));
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Niste uneli dobar fajl");
            e.printStackTrace();
        }
        for (Track track: sequence.getTracks()) {
            for(int i = 0; i < track.size(); i++) {
                MidiEvent midiEvent = track.get(i);
                MidiMessage midiMessage = midiEvent.getMessage();
                if(midiMessage instanceof ShortMessage) {
                    ShortMessage shortMessage = (ShortMessage)midiMessage;
                    if(shortMessage.getCommand() == 144 || shortMessage.getCommand() == 128) {
                            MyMidiEvent myMidiEvent = new MyMidiEvent(midiEvent.getTick(), shortMessage.getData1(), shortMessage.getData2());
                            midiPool.add(myMidiEvent);
                    }
                }
            }
        }
        tonePool = makeTones(midiPool);
    }

    /**
     * Pravi tonove od midi eventova. Prolazi kroz sve midiEventove i za svaki proverava da li je velocity 0,
     * ako jeste onda je to taj tick(time) oznacava kraj svrianja te note. Razlika izmedju dva eventa je duzina trajanja tona.
     * @see {@link MyTone}
     * @param midiPool -> Lista svih midiEventova, poredjanih po redosledu.
     * @return -> Listu tonova koji se pojavljuju u prosledjenoj kompoziciji.
     */
    private ArrayList<MyTone> makeTones(ArrayList<MyMidiEvent> midiPool ) {
        ArrayList<MyTone> tonesPool = new ArrayList<>();
        for(MyMidiEvent startMidiEvent : midiPool) {
            if (startMidiEvent.getVelocity() == 0) continue;
            int index = midiPool.indexOf(startMidiEvent);
            for(int i = index + 1; i < midiPool.size(); i++) {
                MyMidiEvent endMidiEvent = midiPool.get(i);
                if( endMidiEvent.getVelocity() == 0 &&
                        startMidiEvent.getNote() == endMidiEvent.getNote()) {
                    int note = startMidiEvent.getNote();
                    int velocity = startMidiEvent.getVelocity();
                    long start = startMidiEvent.getTime();
                    long length = endMidiEvent.getTime() - startMidiEvent.getTime();
                    MyTone myTone = new MyTone(length, start, velocity, note);
                    tonesPool.add(myTone);
                    break;
                }

            }
        }
        return tonesPool;
    }

    /**
     * Pravi objekat kalse {@link Chromosome} koji ce predstavljati trazeni hromozm.
     * TODO uraditi za vise traka?
     * @param targetLength -> duzina hromzoma
     * @return -> hromozom koji sadrzi targetLength tonova pocevsi od targetStart-og tona u komopoziciji.
     */
    private Chromosome makeTarget(int targetStart, int targetLength) {
        Chromosome target = new Chromosome(targetLength, tonePool);
        for(int i = 0; i < targetLength; i++)
            target.addTones(tonePool.get(i + targetStart));
        return target;
    }
    /**
     * Pravi generaciju praznih hromozoma, i potom zove metodu {@link Chromosome#makeItRandom()} za svaki hromozom,
     * kako bi on na random nacin odbrao svoj genetski materijal (tonove u ovom slucaju).
     * Takdoje, buduci da se samo prvi put generacija bira na random nacin, redni broj generacije se postavlja na 0.
     * @param chromosomeLength -> duzina hromozoma (dela pesme koji treba da prepoznamo).
     * @see {@link Chromosome#makeItRandom()}
     */
    private void makeRandomGeneration(int chromosomeLength) {
        for(int i = 0; i < populationSize; i++) {
            population[i] = new Chromosome(chromosomeLength, tonePool);
            population[i].makeItRandom();
            generationNumber = 0;
        }
    }
    /**
     * Metoda koja racuna fitnes svakog hromozoma u populaciji, i proverava da li se pri tom racunanju promenio
     * nas najbolji hromozom.
     * Metodi {@link Chromosome#calculateFitness(Chromosome)} se proseldjuje parametar target,
     * kako bi se svaki hromozom uporedio sa ciljnim hromozomom.
     * @see  {@link Chromosome#calculateFitness(Chromosome)}
     */
    public void calculateFitness() {
        for(int i = 0; i < populationSize; i++) {
            population[i].calculateFitness(target);
            updateOurBest(i);
        }
    }

    /**
     * Proverava da li smo nasli trazeni hromozom i apdejtuje trenutni najbolji.
     * @param i -> index hromozoma u populacij za koji proveravmo da li je mozda ourBest.
     */
    private void  updateOurBest(int i) {
        if(population[i].getFitness() == Math.pow(2.0, chromosomeLength)) {
            ourBest = population[i];
            weHaveAWinner = true;
        } else if(ourBest == null || population[i].getFitness() > ourBest.getFitness())
            ourBest = population[i];
    }

    /**
     * Metoda koja pravi "bazen" sa svim roditeljima. Roditelji unutra su skalirani po fitnesu pa potom ubacivani.
     * Sto veci fitnes ima hromozom to ce se vise puta naci u bazenu(sesiru za izvalcenje),
     * te ce imati i vece sanse da bude izabran za ukrstanje.
     * TODO Proveriti da li treba "normalizovti" naci max pa onda.... Ili naci bolje resenje.
     */
    public void naturalSelection() {
        possibilityPool.clear();
        for(int i = 0; i < populationSize; i++) {
            int n = (int)(population[i].getFitness());
            for(int j = 0; j < n; j++)
                possibilityPool.add(population[i]);
        }
    }

    /**
     * Metoda koja pravi novu generaciju hromozoma. Uzima dva random roditelja iz possbilityPool-a i ukrsta ih.
     * Dobijeni hromozom se dodaje u novu generaciju. To radimo populationSize puta, kako bi svaka generacija imala
     * isti broj hromozoma.
     * @see {@link #possibilityPool}
     * TODO izbaciti ovo newGeneration?
     */
    public void makeNewGeneration() {
        Chromosome[] newGeneration = new Chromosome[populationSize];
        Random random = new Random();
        for(int i = 0; i < populationSize; i++) {
            int indexOfMother = random.nextInt(possibilityPool.size() - 1);
            int indexOfFather = random.nextInt(possibilityPool.size() - 1);
            Chromosome mother = possibilityPool.get(indexOfMother);
            Chromosome father = possibilityPool.get(indexOfFather);
            Chromosome child = mother.makeLove(father, target);
            child.mutate(mutationRate);
            newGeneration[i] = child;
        }
        population = newGeneration;
        generationNumber++;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public Chromosome[] getPopulation() {
        return population;
    }

    public void setPopulation(Chromosome[] population) {
        this.population = population;
    }

    public Chromosome getTarget() {
        return target;
    }

    public ArrayList<MyMidiEvent> getMidiPool() {
        return midiPool;
    }

    public void setMidiPool(ArrayList<MyMidiEvent> midiPool) {
        this.midiPool = midiPool;
    }

    public ArrayList<MyTone> getTonePool() {
        return tonePool;
    }

    public void setTonePool(ArrayList<MyTone> tonePool) {
        this.tonePool = tonePool;
    }

    public int getChromosomeLength() {
        return chromosomeLength;
    }

    public void setChromosomeLength(int chromosomeLength) {
        this.chromosomeLength = chromosomeLength;
    }

    public void setTarget(Chromosome target) {
        this.target = target;
    }

    public ArrayList<Chromosome> getPossbilityPool() {
        return possibilityPool;
    }

    public void setPossbilityPool(ArrayList<Chromosome> possbilityPool) {
        this.possibilityPool = possbilityPool;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public void setGenerationNumber(int generationNumber) {
        this.generationNumber = generationNumber;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public Chromosome getOurBest() {
        return ourBest;
    }

    public void setOurBest(Chromosome ourBest) {
        this.ourBest = ourBest;
    }

    public boolean isWeHaveAWinner() {
        return weHaveAWinner;
    }

    public void setWeHaveAWinner(boolean weHaveAWinner) {
        this.weHaveAWinner = weHaveAWinner;
    }
}
