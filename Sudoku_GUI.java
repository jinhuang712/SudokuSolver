

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

@SuppressWarnings("serial")
public class Sudoku_GUI extends JFrame {
    static Sudoku sudoku;
    static Sudoku sudoku_solution;

    final int WIDTH = 430, HEIGHT = 350;
    public Sudoku_GUI() {
        setSize(WIDTH, HEIGHT);
        setTitle("Sudoku Solver Version. 1");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        sudoku = new Sudoku();
        sudoku_solution = new Sudoku();
        draw();
    }

    JPanel panel;
    JPanel matrix;
    public void draw() {
        setLayout(new BorderLayout());
        drawPanel();
        drawGrid();
    }

    public void drawGrid() {
        matrix = new JPanel();
        matrix.setBackground(Color.BLACK);
        matrix.setLayout(new GridLayout(9,9));
        createGrid();
        this.add(matrix);
    }

    JTextField[][] grid = new JTextField[9][9];
    public void createGrid() {
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                grid[i][j] = new JTextField(1);
                grid[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                grid[i][j].setAlignmentX(CENTER_ALIGNMENT);
                matrix.add(grid[i][j]);
            }
        }
        inputSudoku();
    }

    JPanel importPanel, upperPanel;
    JButton jSolve, jClear, jImport;
    JLabel statusField;
    JComboBox<String> filename;
    public void drawPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout(2, 1));
        jSolve = new JButton("solve");
        jSolve.addActionListener(new upperPanelHandler());
        upperPanel.add(jSolve);
        jClear = new JButton("clear");
        jClear.addActionListener(new upperPanelHandler());
        upperPanel.add(jClear);
        panel.add(upperPanel, BorderLayout.NORTH);
        statusField = new JLabel("WAITING", JLabel.CENTER);
        panel.add(statusField, BorderLayout.CENTER);
        importPanel = new JPanel();
        importPanel.setLayout(new GridLayout(2,1));
        panel.add(importPanel, BorderLayout.SOUTH);
        filename = new JComboBox<String>();
        String[] samples = getSamples();
        Arrays.sort(samples);
        for (String s : samples)
            filename.addItem(s);
        importPanel.add(filename);
        jImport = new JButton("import");
        jImport.addActionListener(new importHandler());
        importPanel.add(jImport);
        //add action listener
        this.add(panel, BorderLayout.EAST);
    }

    public String[] getSamples() {
        File file = new File("Samples");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(file.list()));
        for (int i = 0; i < list.size(); i++) {
            int index = list.get(i).lastIndexOf('/');
            list.set(i, list.get(i).substring(index + 1));
            if (list.get(i).equals("Solutions")) list.remove(i);
        }
        return list.toArray(new String[list.size()]);
    }

    public void inputSudoku() {
        for(int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.sdk[i][j] == 0)
                    grid[i][j].setText("");
                else
                    grid[i][j].setText(sudoku.sdk[i][j] + "");
            }
        }
        changeColor(Color.black);
        statusField.setText("LOADED...");
    }

    public void inputSolution(String correct) {
        for(int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.sdk[i][j] == 0)
                    grid[i][j].setText("");
                else
                    grid[i][j].setText(sudoku.sdk[i][j] + "");
            }
        }
        statusField.setText(correct);
    }

    public void changeColor(Color color) {
        for(int i=0; i<9; i++)
            for (int j = 0; j < 9; j++)
                grid[i][j].setForeground(color);
    }

    public void changeColor(ArrayList<int[]> list, Color color) {
        for(int[] i : list)
            grid[i[0]][i[1]].setForeground(color);
    }

    private class upperPanelHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if(source == jSolve) {
                if (sudoku.getEmpty() == 9*9) return;
                changeColor(sudoku.getEmptyCoordinates(), Color.gray);
                Algorithm.fill(sudoku);
                ArrayList<int[]> difference = sudoku.compare(sudoku_solution);
                String correct;
                if (difference.size() != 0) {
                    changeColor(difference, Color.RED);
                    correct = "incorrect...";
                } else {
                    correct = "CORRECT!";
                }
                inputSolution(correct);
            } if(source == jClear) {
                sudoku = new Sudoku();
                inputSudoku();
                statusField.setText("waiting...");
            }
            repaint();
        }
    }

    private class importHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String file = (String) (filename.getSelectedItem());
            try {
                sudoku.importSudoku("Samples/"+file);
                sudoku_solution.importSudoku("Samples/Solutions/"+file);
                inputSudoku();
                repaint();
            } catch (FileNotFoundException e1) {
                statusField.setText("ERROR");
            }
        }
    }
}