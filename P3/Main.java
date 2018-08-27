import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class QuineMcCluskey {

    private Integer n;
    private ArrayList<Integer> minterms;
    private ArrayList<String> binary;
    private ArrayList<String> primeImplicants;
    private int [][] chart;
    private ArrayList<String> essentialPrimeImplicants;
    private int [] kmap;

    public QuineMcCluskey() {

        minterms = new ArrayList<>();
        binary = new ArrayList<>();
        primeImplicants = new ArrayList<>();
    
    }

    private void bin_to_dec() {
        for(Integer minterm : minterms) {
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < this.n; i++) {
                temp.add(Integer.toString(minterm % 2));
                minterm /= 2;
            }
            String reverse = new StringBuilder(String.join("", temp))
                                .reverse().toString();
            this.binary.add(reverse);
        }
    }

    private String compare_string(String a, String b) {
        char [] first = a.toCharArray();
        char [] second = b.toCharArray();
        Integer counter = 0;

        for (int i = 0; i < first.length; i++) {
            if(first[i] != second[i]) {
                counter++;
                first[i] = '_';
            }
        }
        if (counter > 1) {
            return "x";
        }

        return String.valueOf(first);
    }

    private ArrayList<String> check() {

        ArrayList<String> prime = new ArrayList<>();
        ArrayList<String> binary = new ArrayList<>();
        binary.addAll(this.binary);
        for (; ; ) {
            ArrayList<Character> check = new ArrayList<>
                                    (Collections.nCopies(binary.size(), '$'));
            ArrayList<String> temp = new ArrayList<>();

            for (int i = 0; i < binary.size(); i++) {
                for (int j = i+1; j < binary.size(); j++) {

                    String k = compare_string(binary.get(i), binary.get(j));
                    if (k != "x") {
                        temp.add(k);
                        check.set(i, '*');
                        check.set(j, '*');
                    }
                }
            }
            for (int i = 0; i < check.size(); i++) {
                if (check.get(i) == '$') {
                    prime.add(binary.get(i));
                }
            }
            if (temp.size() == 0) {
                return prime;
            }

            Set<String> set = new HashSet<>(temp);
            binary.removeAll(binary);
            binary.addAll(set);
        }
    }

    private boolean is_suitable(String a, String b, int counter) {
        char [] first = a.toCharArray();
        char [] second = b.toCharArray();
        Integer counter_n = 0;

        for (int i = 0; i < first.length; i++) {
            if (first[i] != second[i]) {
                counter_n++;
            }
        }
        if (counter == counter_n) {
            return true;
        }
        return false;
    }

    private void createPrimeImplicantsChart() {
        chart = new int [this.primeImplicants.size()][];
        for (int i = 0; i < this.primeImplicants.size(); i++) {
            chart[i] = new int [this.binary.size()];
        }


        for (int i = 0; i < this.primeImplicants.size(); i++) {
            int counter = (int) primeImplicants.get(i)
                            .chars().filter(ch -> ch == '_').count();
            for (int j = 0; j < this.binary.size(); j++) {
                if (is_suitable(this.primeImplicants.get(i),
                                this.binary.get(j), counter)) {
                    this.chart[i][j] = 1;
                }
            }
        }
    }

    private ArrayList<String> selection() {
        ArrayList<String> eprime = new ArrayList<>();
        ArrayList<Integer> select = new ArrayList<>
                                (Collections.nCopies
                                    (this.primeImplicants.size(), 0));

        for (int i = 0; i < this.binary.size(); i++) {
            Integer count = 0, rem = -1;
            for (int j = 0; j < this.primeImplicants.size(); j++) {
                if (this.chart[j][i] == 1) {
                    count++;
                    rem = j;
                }
            }
            if (count == 1) {
                select.set(rem, 1);
            }
        }

        for (int i = 0; i < this.primeImplicants.size(); i++) {
            if (select.get(i) == 1) {
                for (int j = 0; j < this.binary.size(); j++) {
                    if (chart[i][j] == 1) {
                        for (int k = 0; k < this.primeImplicants.size(); k++) {
                            chart[k][j] = 0;
                        }
                    }
                }
                eprime.add(this.primeImplicants.get(i));
            }
        }
        //remaining terms after the selection of essential prime implicants
        for (; ; ) {

            Integer max_n = 0, rem = -1, count_n = 0;
            for (int i = 0; i < this.primeImplicants.size(); i++) {
                for (int j = 0; j < this.binary.size(); j++) {
                    if (this.chart[i][j] == 1) {
                        count_n++;
                    }
                }
                if (count_n > max_n) {
                    max_n = count_n;
                    rem = i;
                }
                count_n = 0;
            }
            if (max_n == 0) {
                return eprime;
            }

            eprime.add(this.primeImplicants.get(rem));

            for (int i = 0; i < this.binary.size(); i++) {
                if (this.chart[rem][i] == 1) {
                    for (int j = 0; j < this.primeImplicants.size(); j++) {
                        this.chart[j][i] = 0;
                    }
                }
            }
        }
    }

    public void setVariable(String variable) {
        this.n = Integer.valueOf(variable);
    }

    public void setMinterms(Set<Integer> set) {
        this.minterms.addAll(set);
    }

    public Integer getn() {
        return this.n;
    }

    public String getPrintText() {
        String s = new String();
        s += "\nBinary form:\n";
        s += this.binary.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
        s += "\n\nMinterms:\n";
        s += this.minterms.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
        s += "\n\nPrime implicants:\n";
        for (int pi = 0; pi < primeImplicants.size(); pi++) {
            char [] temp = this.primeImplicants.get(pi).toCharArray();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] == '1') {
                    s += getCharForNumber(i + 1);
                } else {
                    if (temp[i] == '0') {
                        s += getCharForNumber(i + 1) + "'";   
                    }
                }
            }
            if (pi != primeImplicants.size() - 1) {
                s += (" + ");    
            }
            
        }
        
        s += "\n\nEssential Prime implicants including minimized terms:\n";
        for (int epi = 0; epi < essentialPrimeImplicants.size(); epi++) {
            char [] temp = essentialPrimeImplicants.get(epi).toCharArray();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] == '1') {
                    s += getCharForNumber(i + 1);
                } else {
                    if (temp[i] == '0') {
                        s += getCharForNumber(i + 1) + "'";   
                    }
                }
            }
            if (epi != essentialPrimeImplicants.size() - 1) {
                s += (" + ");    
            }
        }
        s += "\n";

        return s;
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : null;
    }

    public void createKMap() {
        kmap = new int [(1 << this.n)];
        ArrayList<String> gray_code_v = new ArrayList<>();
        ArrayList<String> gray_code_h = new ArrayList<>();

        for (int i = 0; i < (1 << this.n/2); i++) {
            gray_code_v.add(String.format("%0$"+ this.n/2 + "s",
                            Integer.toBinaryString(i ^ (i >> 1)))
                            .replace(" ", "0"));
        }

        for (int i = 0; i < (1 << (this.n - this.n/2)); i++) {
            gray_code_h.add(String.format("%0$"+ (this.n - this.n/2) + "s",
                            Integer.toBinaryString(i ^ (i >> 1)))
                            .replace(" ", "0"));
        }

        int count = 0;
        for (int i = 0; i < gray_code_v.size(); i++) {
            for (int j = 0; j < gray_code_h.size(); j++) {
                kmap[count] = Integer.parseInt(
                            (gray_code_v.get(i) + gray_code_h.get(j)), 2);
            count++;
            }
        }

    }

    public String getKMapValue(Integer i) {
        return String.valueOf(kmap[i]);
    }

    public void solve() {
        bin_to_dec();
        this.primeImplicants = check();
        createPrimeImplicantsChart();
        this.essentialPrimeImplicants = selection();
    }
}


class Gui {
    private Integer n;
    private JFrame frame;
    private static JButton [] button;
    private JButton enter, compute, restart, quit;
    private JLabel label, tempLabel;
    private JTextArea output;
    private JTextField input;
    private JPanel panel1, panel2;
    private GridLayout grid;
    private JScrollPane jsp;
    private Set<Integer> set;
    private QuineMcCluskey qm;

    public Gui() {
        qm = new QuineMcCluskey();        
        set = new HashSet<Integer>();
        frame = new JFrame("K-Map");
        
        panel1 = new JPanel();
        panel2 = new JPanel();
        tempLabel = new JLabel("K-Map");
        tempLabel.setFont(new Font("Monaco", 1, 200));
        panel1.add(tempLabel);
        panel2.setLayout(new FlowLayout());
        
        JScrollPane scroller1 = new JScrollPane(panel1);
        scroller1.setViewportBorder(null);
        scroller1.setVerticalScrollBarPolicy(ScrollPaneConstants
                                            .VERTICAL_SCROLLBAR_ALWAYS);
        scroller1.setHorizontalScrollBarPolicy(ScrollPaneConstants
                                            .HORIZONTAL_SCROLLBAR_ALWAYS);

        panel1.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10,
                         new Color(169, 169, 169)));
        panel2.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10,
                         new Color(169, 169, 169)));
        createLowerPanel();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setLocationRelativeTo(null);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1));
        frame.add(scroller1);
        frame.add(panel2);
        frame.setVisible(true);
    }

    public void addButtons() {
        panel1.remove(tempLabel);
        button = new JButton [(1 << qm.getn())];

        grid =  new GridLayout((1 << qm.getn() / 2),
                                 (1 << (qm.getn() - qm.getn()/2)));
        panel1.setLayout(grid);

        for (int i = 0; i < (1 << qm.getn()); i++) {
            button[i] = new JButton();
            String s = qm.getKMapValue(i);
            JButton  temp = new JButton(s);
            button[i] = temp;
            panel1.add(button[i]);
            temp.setBackground(new Color(67, 216, 211));
            temp.setBorder(BorderFactory.createLoweredBevelBorder());

            temp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent changeColor) {
                    set.add(Integer.valueOf(changeColor.getActionCommand()));
                    temp.setBackground(new Color(169, 169, 169));
                    temp.setBorder(BorderFactory.createRaisedBevelBorder());
                }
            });
        }
    }

    public void createLowerPanel() {

        label = new JLabel("Enter the no. of Variables");
        input = new JTextField(20);
        input.setSize(100, 20);
        enter = new JButton("Create K-Map");
        enter.setActionCommand("Enter");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                qm.setVariable(input.getText());
                qm.createKMap();
                addButtons();
                enter.setEnabled(false);
            }
        });

        output = new JTextArea("RESULT:\n", 10, 80);
        output.setFont(new Font("Monaco", 1, 20));
        output.setEditable(false);
        JScrollPane scroller2 = new JScrollPane(output);
        scroller2.setVerticalScrollBarPolicy(ScrollPaneConstants
                                            .VERTICAL_SCROLLBAR_ALWAYS);
        scroller2.setHorizontalScrollBarPolicy(ScrollPaneConstants
                                            .HORIZONTAL_SCROLLBAR_ALWAYS);

        compute = new JButton("COMPUTE");
        compute.setActionCommand("Enter");
        compute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                qm.setMinterms(set);
                qm.solve();
                compute.setEnabled(false);
                output.append(qm.getPrintText());
            }
        });

        quit = new JButton("Exit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        restart = new JButton("Reset");
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Gui();
            }
        });

        panel2.add(label);
        panel2.add(input);
        panel2.add(enter);
        panel2.add(compute);
        panel2.add(restart);
        panel2.add(quit);
        panel2.add(scroller2);
    }
}

public class Main {
    public static void main(String[] args) {
        new Gui();
    }
}
