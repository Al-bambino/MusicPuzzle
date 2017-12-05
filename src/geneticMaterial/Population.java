package geneticMaterial;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    /**
     * Verovatnoca da hromozom mutira
     */
    private double mutationRate;
    /**
     * Trenutna populacija
     */
    private Chromosome[] population;
    /**
     * Deo pesme koji treba da dobijemo ovom populacijom
     * TODO primeniti na celu pesmu
     */
    private Chromosome target;
    /**
     * Niz sa svim hromozomima popunjen po verovatnoci da prezivi (fitnessu)
     * @see {@link #naturalSelection()}
     */
    private ArrayList<Chromosome> possbilityPool;
    /**
     * Trenutni broj generacije
     */
    private int generationNumber;
    /**
     * Velicina populacije -> broj hromozoma u generaciji
     */
    private int populationSize;
    /**
     * Najbolji hromozom u generaciji (Djak generacije)
     */
    private Chromosome ourBest;
    /**
     * Proverava da li imamo hromozom koji se potouno poklapa sa targetom
     */
    private  boolean weHaveAWinner;

    public Population(double mutationRate, int populationSize, int chromosomeLength, Chromosome target) {
        this.mutationRate = mutationRate;
        this.target = target;
        this.populationSize = populationSize;
        population = new Chromosome[populationSize];
        makeRandomGeneration(chromosomeLength);
    }

    /**
     * Pravi generaciju praznih hromozoma, i potom zove metodu makeItRandom za svaki hromozom,
     * kako bi on na random nacin odbrao svoje delove (midiEvent-ove u ovom slucaju).
     * Takdoje, buduci da se samo prvi put generacija bira na random nacin, redni broj generacije se postavlja na 0.
     * @param chromosomeLength -> duzina hromozoma(dela pesme koji treba da prepoznamo).
     * @see {@link Chromosome#makeItRandom()}
     */
    private void makeRandomGeneration(int chromosomeLength) {
        for(int i = 0; i < populationSize; i++){
            population[i] = new Chromosome(chromosomeLength);
            population[i].makeItRandom();
            generationNumber = 0;
        }
    }

    /**
     * Metoda koja racuna fitnes svakog hromozoma u populaciji, i proverava da li se pri tom racunanju promenio
     * nas najbolji hromozom.
     * Metodi calculateFitness se proseldjuje parametar target, kako bi se svaki hromozom uporedio sa ciljnim hromozomom.
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
        if((int)population[i].getFitness() == 1 ) {
            ourBest = population[i];
            weHaveAWinner = true;
        } else if(ourBest == null || population[i].getFitness() > ourBest.getFitness())
            ourBest = population[i];
    }

    /**
     * Metoda koja pravi bazen za odabir roditelja.
     * Sto veci fitnes ima hromozom to ce se vise puta naci u bazenu(sesiru za izvalcenje),
     * te ce imati i vece sanse da bude izabran za ukrstanje.
     * Fitnes se mnozi sa 10 cisto da bi se napravio veci broj primeraka hrommozoma. Broj sa kojim se mnozi je proizvoljan.
     */
    public void naturalSelection() {
        possbilityPool.clear();
        for(int i = 0; i < populationSize; i++) {
            int n = (int)population[i].getFitness() * 10;
            for(int j = 0; j < n; j++)
                possbilityPool.add(population[i]);
        }
    }

    /**
     * Metoda koja pravi novu generaciju hromozoma. Uzima dva random roditelja iz possbilityPool-a i ukrsta ih.
     * Dobijeni hromozom se dodaje u novu generaciju. To radimo populationSize puta, kako bi svaka generacija imala
     * isti broj hromozoma.
     */
    public void makeNewGeneration() {
        Random random = new Random(possbilityPool.size() - 1);
        for(int i = 0; i < populationSize; i++) {
            int firstParentIndex = random.nextInt();
            int secondParentIndex = random.nextInt();
            Chromosome firstParent = possbilityPool.get(firstParentIndex);
            Chromosome secondParent = possbilityPool.get(secondParentIndex);
            Chromosome child = firstParent.makeLove(secondParent);
            population[i] = child;
        }
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

    public void setTarget(Chromosome target) {
        this.target = target;
    }

    public ArrayList<Chromosome> getPossbilityPool() {
        return possbilityPool;
    }

    public void setPossbilityPool(ArrayList<Chromosome> possbilityPool) {
        this.possbilityPool = possbilityPool;
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