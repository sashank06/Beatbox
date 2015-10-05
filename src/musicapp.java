package box.BeatBox.src;

import javax.swing.*;
import javax.sound.midi.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class musicapp{
	
	String[] instruments = {"Bass Drums","Closed Hi-Hat", "Open Hi-Hat", "accoustic snare","Crash Cymbal","HandClap","High Tom","Hi Bongo","Maracas", " Whistle", "Low Congs","Cowbell","VibrasLap","low-mid Tom","High Agogo","Open Hi-Conga"};
	JPanel mainpanel;
	ArrayList<JCheckBox> box;
	Sequencer player;
	Sequence sequence;
	Track track;
	JFrame frame;
	int[] instrumentno ={35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	
	public static void main(String[] args){
		musicapp musicbox = new musicapp();
		musicbox.creategui();
	}
	
	public void creategui(){
		frame = new JFrame("My player");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//BorderLayout outline = new BorderLayout();
		JPanel panel = new JPanel(new BorderLayout());
		box = new ArrayList<JCheckBox>();
		Box buttons = new Box(BoxLayout.Y_AXIS);
		
		JButton start = new JButton("start");
		start.addActionListener(new mystartListener());
		buttons.add(start);
		JButton stop = new JButton("stop");
		stop.addActionListener(new mystopListener());
		buttons.add(stop);
		JButton tempoUp = new JButton("Tempo Up");
		start.addActionListener(new mytempoupListener());
		buttons.add(tempoUp);
		JButton tempoDown = new JButton("Tempo Down");
		stop.addActionListener(new mytempodownListener());
		buttons.add(tempoDown);
		
		Box names = new Box(BoxLayout.Y_AXIS);
		for (int i=0;i<instruments.length;i++){
			names.add(new Label(instruments[i]));
		}
		panel.add(BorderLayout.EAST,buttons);
		panel.add(BorderLayout.WEST,names);
		frame.getContentPane().add(panel);
		
		GridLayout grid = new GridLayout(16,16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainpanel = new JPanel(grid);
		panel.add(BorderLayout.CENTER,mainpanel);
		for (int i=0;i<256;i++) {
			JCheckBox cbox =  new JCheckBox();
			cbox.setSelected(false);
			box.add(cbox);
			mainpanel.add(cbox);
		}
		 setupmidi();
		 frame.setBounds(50,50,50,50);
		 frame.pack();
		 frame.setVisible(true);
		 
	}
	public void setupmidi(){
		try {
			player = MidiSystem.getSequencer();
			player.open();
			sequence = new Sequence(Sequence.PPQ,4);
			track = sequence.createTrack();
			player.setTempoInBPM(120); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void buildtrackandstart(){
	int[] tracklist = null;
	sequence.deleteTrack(track);
	track = sequence.createTrack();
	for (int i=0;i<16;i++){
		tracklist = new int[16];
		
		int key = instrumentno[i];
			for ( int j=0;j<16;j++){
				JCheckBox jc = (JCheckBox) box.get(+ (16*i));
				if ( jc.isSelected()){
					tracklist[j] = key;
				}
				else
				{
					tracklist[j] = 0;
				}
			}	
		 makeTracks(tracklist);
		 track.add(makeEvent(176,1,127,0,16));
		}
		track.add(makeEvent(192,9,1,0,15));
		try{
			player.setSequence(sequence);
			player.setLoopCount(player.LOOP_CONTINUOUSLY);
			player.start();
			player.setTempoInBPM(120);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public class mystartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			buildtrackandstart();
		}
	}
	public class mystopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			player.stop();
		}
	}	
	public class mytempoupListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			float tempfactor = player.getTempoFactor();
			player.setTempoFactor((float) (tempfactor * 1.05));
		}
	}
	public class mytempodownListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			float tempfactor = player.getTempoFactor();
			player.setTempoFactor((float) (tempfactor * 0.95));
		}
	}
	public void makeTracks(int[] list) {
		for ( int i=0;i<16;i++){
			int key = list[i];
			if(key!=0) {
				track.add(makeEvent(144,9,key,100,i));
				track.add(makeEvent(128,9,key,100,i + 1 ));
			}
		}
	}
	public static MidiEvent makeEvent(int cmd,int channel,int one,int two,int tick){
		MidiEvent event = null;
		
		try {
			ShortMessage a =new ShortMessage();
			a.setMessage(cmd, channel, one, two);
			event = new MidiEvent(a,tick);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return event;
	}
}