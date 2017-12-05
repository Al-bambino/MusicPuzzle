package myApp;

import com.leff.midi.MidiFile;
import geneticMaterial.Chromosome;
import geneticMaterial.MidiEvent;

import javax.sound.midi.*;

import javax.sound.midi.MidiSystem;
import java.io.File;
import java.io.IOException;

public class Main {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        public static void main(String[] args) {
            Chromosome c = new Chromosome(4);
            c.addMidiEvent(new MidiEvent(480,71,66));
            c.addMidiEvent(new MidiEvent(600,71,0));
            c.addMidiEvent(new MidiEvent(600,69,64));
            c.addMidiEvent(new MidiEvent(720,69,0));
            System.out.println(c);
/*
        MidiFile m = new MidiFile();

        Sequence sequence = null;
        try {
            sequence = MidiSystem.getSequence(new File("D:\\Downloads\\mzAllaTurca.mid"));
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
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
