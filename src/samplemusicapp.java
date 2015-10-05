package box.BeatBox.src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.sound.midi.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
class myPanel extends JPanel implements ControllerEventListener{
	boolean msg = false;
	@Override
	public void controlChange(ShortMessage event) {
		// TODO Auto-generated method stub
		msg=true;
		repaint();
	}
	
	public void paintComponent(Graphics g){
		if (msg){
			Graphics2D g2 = (Graphics2D) g;
			int r = (int) (Math.random() * 250) ;
			int gr = (int) (Math.random() * 250);
			int b = (int) (Math.random() * 250);
			g.setColor(new Color(r,gr,b));
			g.fillRect(60, 80, 100, 100);
			msg = false;
		}
		
	}
	
}
public class samplemusicapp implements ControllerEventListener {
	public void play() throws InvalidMidiDataException {
		try {
			JFrame frame = new JFrame("beatbox");
			myPanel m1 = new myPanel();
			frame.setContentPane(m1);
			frame.setSize(300, 300);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			int[] events = {127};
			player.addControllerEventListener(this,events);
			Sequence seq = new Sequence(Sequence.PPQ,4);
			Track track = seq.createTrack();
			for (int i=0;i<61;i+=4){
				track.add(makeEvent(144,1,i,100,i));
				track.add(makeEvent(176,1,i,127,i));
				track.add(makeEvent(128,1,i,100,i+2));
			}
			player.setSequence(seq);
			player.setTempoInBPM(220);
			player.start();
			/*ShortMessage a = new ShortMessage();
			a.setMessage(144,1,44,100);
			MidiEvent NoteOn = new MidiEvent(a,1);
			track.add(NoteOn);
			ShortMessage b = new ShortMessage();
			b.setMessage(128,5,44,100);
			MidiEvent NoteOff = new MidiEvent(b,4);
			track.add(NoteOff);
			player.setSequence(seq);
			player.start();*/
		} catch (MidiUnavailableException e) {
			
			e.printStackTrace();
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
	
	public static void main(String args[]) throws InvalidMidiDataException{
		samplemusicapp ma= new samplemusicapp();
		ma.play();
	}
	@Override
	public void controlChange(ShortMessage event) {
		// TODO Auto-generated method stub
		System.out.println("la");
		
	}

}

