package myApp;

import geneticMaterial.Chromosome;
import geneticMaterial.MyMidiEvent;
import geneticMaterial.Population;

import javax.sound.midi.*;

import javax.sound.midi.MidiSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        public static void main(String[] args) {

            //System.out.println(c);

            Population p = new Population(0.1, 1000,  10, "D:\\Downloads\\mzAllaTurca.mid");
            System.out.println(p.getTarget());
            int l = 0;
            while(!p.isWeHaveAWinner() ) {
                p.calculateFitness();
                p.naturalSelection();
                p.makeNewGeneration();
                System.out.println(p.getGenerationNumber() + ". " +p.getOurBest());
            }

            System.out.println();
            for(int i =0; i < p.getTarget().getChromosomeLength(); i++)
            System.out.println(p.getOurBest().getChromosomeMidiEvents()[i].isOnThePlace());

       /*  for(Chromosome chromosome : p.getPopulation()) {
             System.out.println(chromosome);
         }*/


        /*FileWriter fw = null;
        Sequence sequence = null;
        try {
           fw = new FileWriter(new File("D:\\Downloads\\reci.txt"));
            sequence = MidiSystem.getSequence(new File("D:\\Downloads\\mzAllaTurca.mid"));
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Niste uneli dobar fajl");
            e.printStackTrace();
        }

        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) {
                javax.sound.midi.MidiEvent event = track.get(i);
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();


                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;

                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();

                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        try {
                            fw.write("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
                        System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
        }*/
    }
}
