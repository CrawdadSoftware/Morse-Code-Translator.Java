import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUI extends JFrame implements KeyListener
{
    private MorseCodeController morseCodeController;

    // textInputArea -> Text to be Translated
    // morseCodeArea -> Translated text into morse code
    private JTextArea textInputArea, morseCodeArea;

    public GUI()
    {
        // Adds text to the title bar
        super("Morse Code Translator");

        // size of frame 540x760
        setSize(new Dimension(540,760));

        // GUI is no longer able to be resized
        setResizable(false);

        // Setting the Layout to null
        setLayout(null);

        // Exits program when GUI is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Background color
        getContentPane().setBackground(Color.decode("#347aeb"));

        // Set the GUI in the center of the screen
        setLocationRelativeTo(null);

        morseCodeController = new MorseCodeController();
        addGuiComponents();
    }

    private void addGuiComponents()
    {
        // title
        JLabel titleLabel = new JLabel("Morse Code Translator");

        // Font size and Weight
        titleLabel.setFont(new Font("Dialog",Font.BOLD,32));

        // Font color
        titleLabel.setForeground(Color.white);

        // Center text
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // X, Y position and Width, Height dimentions
        titleLabel.setBounds(0, 0, 540, 100);

        // Text input
        JLabel textInputLabel = new JLabel("Text:");
        textInputLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        textInputLabel.setForeground(Color.WHITE);
        textInputLabel.setBounds(20, 100, 200, 30);

        textInputArea = new JTextArea();
        textInputArea.setFont(new Font("Dialog", Font.PLAIN, 18));
        textInputArea.addKeyListener(this);
        textInputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textInputArea.setLineWrap(true);
        textInputArea.setWrapStyleWord(true);

        // Scrolling ability to input text area
        JScrollPane textInputScroll = new JScrollPane(textInputArea);
        textInputScroll.setBounds(20, 132, 484, 236);

        // Morse Code Input
        JLabel morseCodeInputLabel = new JLabel("Morse Code:");
        morseCodeInputLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        morseCodeInputLabel.setForeground(Color.WHITE);
        morseCodeInputLabel.setBounds(20, 390, 200, 30);

        morseCodeArea = new JTextArea();
        morseCodeArea.setFont(new Font("Dialog", Font.PLAIN, 18));
        morseCodeArea.setEditable(false);
        morseCodeArea.setLineWrap(true);
        morseCodeArea.setWrapStyleWord(true);
        morseCodeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane morseCodeScroll = new JScrollPane(morseCodeArea);
        morseCodeScroll.setBounds(20, 430, 484, 236);

        // play the sound button
        JButton playSoundButton = new JButton("Play Sound");
        playSoundButton.setBounds(210, 680, 100, 30);
        playSoundButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // disable the play button
                playSoundButton.setEnabled(false);

                Thread playMorseCodeThread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // attemp to play the morse code sound
                        try
                        {
                            String[] morseCodeMessage = morseCodeArea.getText().split(" ");
                            morseCodeController.playSound(morseCodeMessage);
                        }
                        catch (LineUnavailableException lineUnavailableException)
                        {
                            lineUnavailableException.printStackTrace();
                        }
                        catch (InterruptedException interruptedException)
                        {
                            interruptedException.printStackTrace();
                        }
                        finally
                        {
                            // enable play sound button
                            playSoundButton.setEnabled(true);
                        }
                    }
                });playMorseCodeThread.start();
            }
        });

        // Add to GUI
        add(titleLabel);
        add(textInputLabel);
        add(textInputScroll);
        add(morseCodeInputLabel);
        add(morseCodeScroll);
        add(playSoundButton);
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() != KeyEvent.VK_SHIFT)
        {
            String inputText = textInputArea.getText();

            // GUI update
            morseCodeArea.setText(morseCodeController.translateToMorse(inputText));
        }
    }
}