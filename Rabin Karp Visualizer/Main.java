import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


/*
 * 
 * Rabin Karp algorithm demonstration with GUI 
 * 
 * k163805
 * k163803
 * k163763
 * 
 */






public class Main extends JFrame implements Runnable{

	//learnd enough data structues for life time
	//enough
	String text = "" ;
	String text2 = "";
	String pattern = "" ;
	int width = 1200;
	int height = 600;
	int boxHeight = 60;
	int position = 30;
	int PatternHash;
	boolean _found = false;
	private int i;
	private boolean done;
	private String found = "Found at index : ";
	private long time = 0;        // 1 sec delay animation adjust to your requirement
	int timer1;
	int timer2;
	int timer3;
	int timer4;
	
	
	Main(String orig,String text1,String text2,String pattern,int time){
		this.time = time;
		this.text = text1;
		this.text2 = text2;
		this.pattern = pattern;
		setTitle("Rabin Karp Algo");
		setSize(width, height);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		PatternHash = calHash(pattern.trim());
		rabinKarp(orig, pattern.trim());
		timer2 = pattern.trim().length();
		timer4 = pattern.trim().length();
		//getContentPane().setBackground(new Color(66, 134, 244));
	}
	
	long getBiggerPrime(int m) {
	    BigInteger prime = BigInteger.probablePrime(getNumberOfBits(m) + 1, new Random());
	    return prime.longValue();
	}
	
	int getNumberOfBits(int number) {
	    return Integer.SIZE - Integer.numberOfLeadingZeros(number);
	}
	
	int simpleTextSearch(char[] pattern, char[] text) {
	    int patternSize = pattern.length;
	    int textSize = text.length;
	 
	    int i = 0;
	 
	    while ((i + patternSize) <= textSize) {
	        int j = 0;
	        while (text[i + j] == pattern[j]) {
	            j += 1;
	            if (j >= patternSize)
	                return i;
	        }
	        i += 1;
	    }
	    return -1;
	}
	
	int RabinKarpMethod2(char[] pattern, char[] text) {
	    int patternSize = pattern.length;
	    int textSize = text.length;      
	 
	    long prime = getBiggerPrime(patternSize);
	 
	    long r = 1;
	    for (int i = 0; i < patternSize - 1; i++) {
	        r *= 2;
	        r = r % prime;
	    }
	 
	    long[] t = new long[textSize];
	    t[0] = 0;
	 
	    long pfinger = 0;
	 
	    for (int j = 0; j < patternSize; j++) {
	        t[0] = (2 * t[0] + text[j]) % prime;
	        pfinger = (2 * pfinger + pattern[j]) % prime;
	    }
	 
	    int i = 0;
	    boolean passed = false;
	 
	    int diff = textSize - patternSize;
	    for (i = 0; i <= diff; i++) {
	        if (t[i] == pfinger) {
	            passed = true;
	            for (int k = 0; k < patternSize; k++) {
	                if (text[i + k] != pattern[k]) {
	                    passed = false;
	                    break;
	                }
	            }
	 
	            if (passed) {
	                return i;
	            }
	        }
	 
	        if (i < diff) {
	            long value = 2 * (t[i] - r * text[i]) + text[i + patternSize];
	            t[i + 1] = ((value % prime) + prime) % prime;
	        }
	    }
	    return -1;
	 
	}
	
	void rabinKarp(String text,String pattern) {
		for (int i = 0; i <= text.length()-pattern.length(); i++) {
			int hash = calHash(text.substring(i, i+pattern.length()));
			if(hash == PatternHash){
				int j;
				for (j = 0; j < pattern.length(); j++) {
					if(text.charAt(i+j) != pattern.charAt(j)) break;
				}
				if(j == pattern.length()){
					_found = true;
					found += i + " , ";
				}
			}
		}
	}

	int calHash(String text){
		int hash=0;
		for (int i = 0; i < text.trim().length(); i++) {
			hash += text.charAt(i);
		}
		hash %= 13;
		return hash;
	}
	
	
	public void run(){
		while(true){
			Graphics2D g = (Graphics2D) getContentPane().getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			Stroke stroke = new BasicStroke((float) 2);
			g.setStroke(stroke);
			int size = width/text.length();
			for (int i = 0; i < text.length(); i++) {
				g.setColor(Color.BLACK);
				g.drawRect(size*i, position, size, boxHeight);
				g.setFont(new Font(Font.SERIF,Font.PLAIN,size));
				g.drawString(" "+text.charAt(i), size*i, boxHeight+position);
			}
			int position2 = position + boxHeight + 20;
			int size2 = width/text2.length();
			for (int i = 0; i < text2.length(); i++) {
				g.setColor(Color.BLACK);
				g.drawRect(size2*i, position2, size2, boxHeight);
				g.setFont(new Font(Font.SERIF,Font.PLAIN,size2));
				g.drawString(" "+text2.charAt(i), size2*i, boxHeight+position2);
			}
			int position3 = position2 + boxHeight + 20;
			int size3 = width/pattern.length();
			for (int i = 0; i < pattern.length(); i++) {
				g.setColor(Color.black);
				g.drawRect(size3*i, position3, size3, boxHeight);
				g.setFont(new Font(Font.SERIF,Font.PLAIN,size3));
				g.drawString(" "+pattern.charAt(i), size3*i, boxHeight+position3);
			}
			i++;
			///////////////////////////////////////////////////////////////////////////////
				if(i > 3 && !done){	
				String t = text.substring(timer1, timer2);
				int hash = calHash(t);
				g.setColor(Color.black);
				g.setFont(new Font(Font.SERIF,Font.PLAIN,50));
				g.drawString("Pattern hash : " + PatternHash, 300-200, 500-150);
				
				
				
				g.setColor(Color.white);
				g.fillRect(530-200, 460-100, 70, 50);
				g.setColor(Color.black);
				g.setFont(new Font(Font.SERIF,Font.PLAIN,50));
				g.drawString("Text hash : " + hash, 300-200, 500-100);
				
				for (int i = timer1; i < timer2; i++) {
					if(hash == PatternHash) g.setColor(Color.green);
					else g.setColor(Color.red);
					g.drawRect(size*i, position, size, boxHeight);
				}
				if(hash == PatternHash){
				
			
					for (int i = timer1; i < timer2; i++) {
						if(text.charAt(i) != pattern.charAt(i-timer1)){
							for (int j = timer1; j < timer2; j++) {
								g.setColor(Color.white);
								g.fillRect(size*i, position, size, boxHeight);
								g.setColor(Color.black);
								g.drawRect(size*i, position, size, boxHeight);
								g.setFont(new Font(Font.SERIF,Font.PLAIN,size));
								g.drawString(" "+text.charAt(i), size*i, boxHeight+position);
							}
					
							break;
						}else{
							g.setColor(Color.cyan);
							g.fillRect(size*i, position, size, boxHeight);
							g.setColor(Color.black);
							g.drawRect(size*i, position, size, boxHeight);
							g.setFont(new Font(Font.SERIF,Font.PLAIN,size));
							g.drawString(" "+text.charAt(i), size*i, boxHeight+position);
							try {
								Thread.sleep(time);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				if(timer2 < text.trim().length()){
					timer1++;
					timer2++;
				}else done = true;
				
				}if(done){
					String t2 = text2.substring(timer3, timer4);
					int hash2 = calHash(t2);
					
					g.setColor(Color.white);
					g.fillRect(530-200, 460-100, 70, 50);
					g.setColor(Color.black);
					g.setFont(new Font(Font.SERIF,Font.PLAIN,50));
					g.drawString("Text hash : " + hash2, 300-200, 500-100);
					
					
					for (int i = timer3; i < timer4; i++) {
						if(hash2 == PatternHash) g.setColor(Color.green);
						else g.setColor(Color.red);
						g.drawRect(size2*i, position2, size2, boxHeight);
					}
					if(hash2 == PatternHash){
		
						for (int i = timer3; i < timer4; i++) {
							if(text2.charAt(i) != pattern.charAt(i-timer3)){
								for (int j = timer3; j < timer4; j++) {
									g.setColor(Color.white);
									g.fillRect(size2*i, position2, size2, boxHeight);
									g.setColor(Color.black);
									g.drawRect(size2*i, position2, size2, boxHeight);
									g.setFont(new Font(Font.SERIF,Font.PLAIN,size2));
									g.drawString(" "+text2.charAt(i), size2*i, boxHeight+position2);
								}
							
								break;
							}else{
							 
								g.setColor(Color.cyan);
								g.fillRect(size2*i, position2, size2, boxHeight);
								g.setColor(Color.black);
								g.drawRect(size2*i, position2, size2, boxHeight);
								g.setFont(new Font(Font.SERIF,Font.PLAIN,size2));
								g.drawString(" "+text2.charAt(i), size2*i, boxHeight+position2);
								g.fillRect(size3*i, position3, size3, boxHeight);
								try {
									Thread.sleep(time);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				
					if(timer4 < text2.trim().length()){
						timer3++;
						timer4++;
					}
					if(timer4 == text2.trim().length()){
						if(_found){
							g.setColor(Color.black);
							g.setFont(new Font(Font.SERIF,Font.PLAIN,50));
							int len = found.length();
							g.drawString(found.trim().substring(0, len-2),500,500);
						}else{
							g.setColor(Color.black);
							g.setFont(new Font(Font.SERIF,Font.PLAIN,50));
							found = "Pattern Not Found";
							int len = found.length();
							g.drawString(found.trim().substring(0, len),500,500);
						}
					}
					
				}
				
				
			
				
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		
		} // end of while
		
	}
	
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Prompt prompt = new Prompt();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}


class Prompt extends JFrame implements ActionListener{
	
	private JPanel contentPane;
	private JTextField text;
	private JTextField pattern;
	private JTextField time;
	private JButton Submit;
	
	private int _time;
	private String _text , _pattern;
	
	Prompt() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		super("Prompt");
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setType(Type.UTILITY);
		setSize(350,500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new TitledBorder(new LineBorder(Color.black,2), "Rabin Karp", TitledBorder.CENTER, TitledBorder.TOP, new Font("Serif",Font.PLAIN,25), new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		text = new JTextField();
		text.setFont(new Font("Serif", Font.PLAIN, 18));
		text.setBounds(72, 80, 200, 30);
		contentPane.add(text);
		text.setColumns(10);
		
		pattern = new JTextField();
		pattern.setFont(new Font("Serif", Font.PLAIN, 18));
		pattern.setColumns(10);
		pattern.setBounds(72, 200, 200, 30);
		contentPane.add(pattern);
		
		time = new JTextField();
		time.setFont(new Font("Serif", Font.PLAIN, 18));
		time.setColumns(10);
		time.setBounds(72, 320, 200, 30);
		contentPane.add(time);
		
		Submit = new JButton("Submit");
		Submit.setFont(new Font("Serif", Font.PLAIN, 17));
		Submit.setBounds(126, 400, 91, 28);
		contentPane.add(Submit);
		Submit.addActionListener(this);
		
		JLabel textLabel = new JLabel("Enter Text");
		textLabel.setFont(new Font("Serif", Font.PLAIN, 17));
		textLabel.setLabelFor(text);
		textLabel.setBounds(72, 60, 360, 16);
		contentPane.add(textLabel);
		
		JLabel patternLabel = new JLabel("Enter Pattern");
		patternLabel.setFont(new Font("Serif", Font.PLAIN, 17));
		patternLabel.setBounds(72, 180, 360, 16);
		contentPane.add(patternLabel);
		
		JLabel timeLabel = new JLabel("Enter time delay for animation");
		timeLabel.setFont(new Font("Serif", Font.PLAIN, 17));
		timeLabel.setBounds(72, 300, 360, 16);
		contentPane.add(timeLabel);
		
		//contentPane.setBackground(new Color(66, 134, 244));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		
		String text1;
		String text2;
		String orig;
		
		_text = text.getText();
		orig  = _text;
		_pattern = pattern.getText();
		_time = Integer.parseInt(time.getText());
		
		if(_text.length() < 50){
			for (int i = _text.length(); i < 50; i++) _text += " ";
		}
		
		text1 = _text.substring(0, 25);
		text2 = _text.substring(25,50);
		
		if(_pattern.length() < 15){
			for (int i = _pattern.length(); i < 15; i++) _pattern += " ";
		}
		
		
		dispose();
		new Thread(new Main(orig,text1,text2,_pattern,_time)).start();

		
	}
	
	
	
	
}
















