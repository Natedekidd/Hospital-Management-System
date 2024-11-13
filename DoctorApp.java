import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoctorApp extends JFrame {
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = size.width, height = size.height;
    private String[] iconlocation = {
            "C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0015.jpg",
            "C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0022.jpg",
            "C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0019.jpg",
            "C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0020.jpg",
            "C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0018.jpg",
            "C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0017.jpg"
    };

    private String[] buttonsNames = {"Home",  "View Appointments", "View Patient Records", "View Treatment Details"};
    private String[] iconForClosing = {
            "C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0016.jpg",
            "C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0015.jpg"
    };

    private JButton[] buttons = new JButton[buttonsNames.length];
    private JPanel right;
    private JButton close;
    private int doctorID;
    private String doctorName;

    public DoctorApp(int doctorID, String doctorName) {
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        setVisible(true);
        setBounds(0, 0, width, height);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        add(Top(doctorName));
        add(Left());
        add(right = homeP());

        ActionListener leftButtonClicked = e -> {
            Object from = e.getSource();
            if (from == buttons[0]) {
                clicked(buttons[0]);
                right.removeAll();
                right.repaint();
                right.revalidate();
                right.add(homeP());
                right.repaint();
                right.revalidate();
            } else if (from == buttons[1]) {
                clicked(buttons[1]);
                right.removeAll();
                right.repaint();
                right.revalidate();
                right.add(new ViewAppointments(doctorID, doctorName));
                right.repaint();
                right.revalidate();
            } else if (from == buttons[2]) {
                clicked(buttons[2]);
                right.removeAll();
                right.repaint();
                right.revalidate();
                right.add(new ViewPatientRecords(doctorID, doctorName));
                right.repaint();
                right.revalidate();
            } else if (from == buttons[3]) {
                clicked(buttons[3]);
                right.removeAll();
                right.repaint();
                right.revalidate();
                right.add(new ViewPatientTreatment(doctorID, doctorName));
                right.repaint();
                right.revalidate();
            }
        };

        for (JButton button : buttons) {
            button.addActionListener(leftButtonClicked);
        }
        close.addActionListener(leftButtonClicked);

    }

    public JPanel Top(String doctorName) {
        JPanel top = new JPanel();
        top.setBounds(0, 0, width, height / 7);
        top.setBackground(new Color(3, 19, 255, 255));
        top.setLayout(null);
        Image icon1 = Toolkit.getDefaultToolkit().getImage("C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\WhatsApp Image 2024-06-12 at 19.05.03_575447c4.jpg").getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        ImageIcon icone1 = new ImageIcon(icon1);
        JLabel topdisplay1 = new JLabel("", icone1, JLabel.CENTER);
        topdisplay1.setBounds(20, 20, 80, 80);
        top.add(topdisplay1);

        JLabel username = new JLabel("Welcome Dr. " + doctorName);
        username.setBounds(110, 30, 450, 75);
        username.setFont(new Font(Font.SERIF, Font.BOLD, 35));
        username.setForeground(Color.WHITE);
        top.add(username);

        Image iconSecond = Toolkit.getDefaultToolkit().getImage(iconForClosing[0]).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon iconeSecond = new ImageIcon(iconSecond);
        close = new JButton(iconeSecond);
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.setBounds(width - 200, 25, 50, 50);
        close.setText("true");
        close.setFont(new Font("Ariel", Font.BOLD, 0));
        top.add(close);

        Image icont = Toolkit.getDefaultToolkit().getImage("C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0014.jpg").getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon icone = new ImageIcon(icont);
        JLabel topdisplay = new JLabel("", icone, JLabel.CENTER);
        topdisplay.setBounds(width - 130, 0, 100, 100);
        top.add(topdisplay);


        return top;
    }

    public JPanel Left() {
        JPanel left = new JPanel();
        left.setBounds(0, height / 7, width / 5, height - (height / 5));
        left.setBackground(Color.WHITE);
        left.setLayout(new GridLayout(buttonsNames.length, 1));
        Border lineBorder = BorderFactory.createLineBorder(new Color(3, 190, 255, 255), 3);

        for (int i = 0; i < buttonsNames.length; i++) {
            Image iconww = Toolkit.getDefaultToolkit().getImage(iconlocation[i]).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            ImageIcon iconw = new ImageIcon(iconww);
            buttons[i] = new JButton(iconw);
            buttons[i].setLayout(new GridLayout());
            buttons[i].setText(buttonsNames[i]);
            buttons[i].setFont(new Font("Ariel", Font.ITALIC, 15));
            buttons[i].setForeground(new Color(0, 0, 250, 255));
            buttons[i].setBackground(new Color(250, 250, 255, 255));
            buttons[i].setContentAreaFilled(false);
            buttons[i].setBorder(lineBorder);
            buttons[i].setOpaque(true);
            left.add(buttons[i]);
        }

        return left;
    }

    public JPanel homeP() {
        JPanel homeP = new JPanel();
        homeP.setBounds(width / 5, height / 7, width - (width / 5), height - (height / 7));
        homeP.setBackground(Color.white);
        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0014.jpg");
        ImageIcon homei = new ImageIcon(icon);
        JLabel homedisplay = new JLabel("", homei, JLabel.LEFT);
        homedisplay.setBounds(0, 0, 3 * (width / 4), height - (height / 7));
//        homeP.add(homedisplay);

        JLabel welcome = new JLabel("Welcome to New Hope Hospital");
        welcome.setFont(new Font("Ariel", Font.ITALIC, 35));
        welcome.setBounds(100, 50, 1000, 200);
        welcome.setForeground(Color.black);
        homedisplay.add(welcome);
        homeP.add(homedisplay);
        return homeP;
    }

    public void clicked(Object clickedButton) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == clickedButton) {
                buttons[i].setBackground(new Color(3, 190, 255, 255));
            } else {
                buttons[i].setBackground(Color.WHITE);
            }
        }
    }
}

