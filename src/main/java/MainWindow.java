import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.Interactor;
import org.apache.batik.swing.svg.JSVGComponent;
import org.apache.commons.math3.util.Pair;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by tomasz on 11.01.16.
 */
public class MainWindow {


    private String[] labelStrings = {"Radius", "Edge Length", "Visibility Field Width", "Visibility Field Height", "Time Limit", "Population Size", "distance punishment",
                                    "lengthPunishment", "crossingPunishment", "vertexCrossingPunishment", "vertexAnglesPunishment"};
    private Double[] defaultValues = {30.0,100.0,800.0,600.0, 1000.0, 100.0, 1.0, 2.0, 3.0, 5., 1., 0.4, 2.0};
    private JFrame window;
    private JButton startStopButton;
    private JButton resetButton;
    private JButton exportButton;
    private JLabel[] labels;
    private JTextField[] params;

    private JPanel panel;
    private JSVGCanvas svgCanvas;
    private JLabel fitness;
    private JLabel population;
    private PSZTGraph ourGraph;
    private GraphQualityArguments arguments;
    private GraphEvolutionGenerator generator;
    private org.javatuples.Pair<PSZTGraph, Double> bestGraph;

    private long timeLimit;
    private int visibilityFieldWidth;
    private int visibilityFieldHeight;
    private double radius;

    public void setPsztGraph(PSZTGraph psztGraph) {
        this.ourGraph = psztGraph;
    }

    public JTextField[] getParams() {
        return params;
    }
    public JSVGCanvas getSvgCanvas() {
        return svgCanvas;
    }
    public String[] getLabelStrings() {
        return labelStrings;
    }
    public JLabel[] getLabels() {
        return labels;
    }

    class PSZTWorker extends SwingWorker<Void, Void>
    {
        public PSZTWorker(MainWindow m, HashMap h) {
            this.m = m;
            this.h = h;
            run = true;
        }
        HashMap h;
        MainWindow m;
        boolean run;

        public boolean isRun() {
            return run;
        }

        public void setRun(boolean run) {
            this.run = run;
        }

        @Override
        protected Void doInBackground()
        {
            m.startGraphsGeneration(h, this);
            return null;
        }

    };
    /**
     * Glowne okno programu
     */
    public MainWindow()
    {
        EventQueue.invokeLater(new Runnable(){


            PSZTWorker worker;
            MainWindow m;
            boolean isRunning;
            public void run()
            {

                isRunning = false;
                window = new JFrame("PSZT_Algorytm_Ewolucyjny");
                window.setSize(800, 800);
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setLayout(new GridBagLayout());
                // SVG do wyswietlenia w oknie
                svgCanvas = new JSVGCanvas();

                startStopButton = new JButton("Start");
                resetButton = new JButton("Reset");
                resetButton.setEnabled(false);
                exportButton = new JButton("Export");
                exportButton.setEnabled(false);

                startStopButton.addActionListener(e -> {
                    if(((JButton)(e.getSource())).getText().equals("Start")) {
                        ((JButton)(e.getSource())).setText("Stop");
                        HashMap<String, Double> values = new HashMap<String, Double>();
                        for (int i = 0; i < m.getLabelStrings().length; i++) {
                            if (!m.getParams()[i].getText().equals(""))
                                values.put(getLabelStrings()[i], Double.parseDouble(getParams()[i].getText()));
                            else
                                values.put(getLabelStrings()[i], defaultValues[i]);
                        }

                        worker = new PSZTWorker(m, values);
                        worker.execute();
                        exportButton.setEnabled(false);
                        resetButton.setEnabled(false);
                    }
                    else
                    {
                        ((JButton)(e.getSource())).setText("Start");
                        worker.setRun(false);
                        exportButton.setEnabled(true);
                        resetButton.setEnabled(true);
                    }

                });
                params = new JTextField[labelStrings.length];
                labels = new JLabel[labelStrings.length];
                for(int i = 0; i < labelStrings.length; i++)
                {
                    params[i] = new JTextField(Double.toString(defaultValues[i]));
                    labels[i] = new JLabel(labelStrings[i]);

                }

                resetButton.addActionListener(e -> {
                    arguments = null;
                    generator = null;
                    bestGraph = null;
                });



                exportButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser();
                        File file;
                        int returnVal = fc.showOpenDialog(new JFrame());
                        if (returnVal == JFileChooser.APPROVE_OPTION) {

                            file = fc.getSelectedFile();
                            System.out.println(file.getAbsolutePath());
                            PSZTGraphToSVGConverter converter = new PSZTGraphToSVGConverter(bestGraph.getValue0(), visibilityFieldWidth, visibilityFieldHeight, radius);
                            converter.doTheMagic();

                            if(!file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                converter.getSvgDraw().toSVG(fileOutputStream);
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                        }

                    }
                });
                fitness = new JLabel("Fitness:");
                population = new JLabel("Population:");
                panel = new JPanel();
                GridBagConstraints c = new GridBagConstraints();
                // ustawianie komponentow w oknie
                int xoffset, length, secondElementOffset;
                for(int i = 0; i < labels.length; i++)
                {
                    if(i <= labels.length/2) {
                        xoffset = 1;
                        length = 1;
                        secondElementOffset = 1;
                    }
                    else {
                        xoffset = 3;
                        length = 2;
                        secondElementOffset = 2;
                    }
                    this.setConstrainst(c, xoffset+secondElementOffset, i%(labels.length/2 + 1), length, 1, 0, 0, length, 1, GridBagConstraints.HORIZONTAL);
                    window.getContentPane().add(params[i], c);
                    this.setConstrainst(c, xoffset, i%(labels.length/2 + 1), length, 1, 0, 0, length, 1, GridBagConstraints.NONE);
                    window.getContentPane().add(labels[i], c);
                }

                this.setConstrainst(c, 1, labels.length/2 + 1, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(startStopButton, c);
                this.setConstrainst(c, 2, labels.length/2 +1 , 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(resetButton, c);
                this.setConstrainst(c, 3, labels.length/2 +1 , 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(exportButton, c);
                this.setConstrainst(c, 4, labels.length/2 +1 , 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(fitness, c);
                this.setConstrainst(c, 5, labels.length/2 +1 , 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(population, c);
                this.setConstrainst(c, 0, labels.length/2 +2, 10, 1, 0, 0, 40, 60, GridBagConstraints.BOTH);
                window.getContentPane().add(panel, c);
                panel.setLayout(new GridBagLayout());
                this.setConstrainst(c, 1, 1, 1, 1, 0, 0, 1, 1, GridBagConstraints.BOTH);

                svgCanvas.setSize(panel.getSize());
                svgCanvas.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);

                System.out.println(panel.getSize());
                panel.add(svgCanvas, c);

                window.setVisible(true);




            }

            public Runnable init(MainWindow mm)
            {
                m = mm;
                return this;
            }

            public void setConstrainst(GridBagConstraints c, int gridx, int gridy, int gridwidth, int gridheight, int ipadx, int ipady, double weightx, double weighty, int fill)
            {
                c.gridx = gridx;
                c.gridy = gridy;
                c.gridwidth = gridwidth;
                c.gridheight = gridheight;
                c.ipadx = ipadx;
                c.ipady = ipady;
                c.weightx = weightx;
                c.weighty = weighty;
                c.fill = fill;
            }
            // funkcja zmieniająca wyświetlany obraz svg
            public void updateCanvas(Document d)
            {
                svgCanvas.setDocument(d);
            }
        }.init(this));
    }

    public void startGraphsGeneration(HashMap<String, Double> map, PSZTWorker worker) {
        if(arguments == null) arguments = new GraphQualityArguments(map.get("distance punishment"), map.get("lengthPunishment"), map.get("lengthPunishment"), map.get("vertexCrossingPunishment"), map.get("vertexAnglesPunishment"), map.get("Radius"), map.get("Edge Length"));

        if(generator == null){
            generator = new GraphEvolutionGenerator(ourGraph,arguments, map.get("Population Size").intValue(), map.get("Visibility Field Width").intValue(), map.get("Visibility Field Height").intValue(), map.get("Radius"), 1);
            visibilityFieldWidth = map.get("Visibility Field Width").intValue();
            visibilityFieldHeight = map.get("Visibility Field Height").intValue();
            radius = map.get("Radius");

        }
        org.javatuples.Pair<PSZTGraph, Double> bestGraphFromCurrentPopulation;
        if(bestGraph == null)
        {
            bestGraphFromCurrentPopulation = generator.getBestGraphFromCurrentPopulation();
            bestGraph = new org.javatuples.Pair<>((PSZTGraph) bestGraphFromCurrentPopulation.getValue0().clone(), bestGraphFromCurrentPopulation.getValue1());
        }

        while(worker.isRun())
        {
            if(timeLimit == 0) timeLimit = map.get("Time Limit").longValue();
            long begin = System.currentTimeMillis();
            while(System.currentTimeMillis() - begin < timeLimit)
            {
                generator.generateNextPopulation();
                bestGraphFromCurrentPopulation = generator.getBestGraphFromCurrentPopulation();
                if (bestGraphFromCurrentPopulation.getValue1() > bestGraph.getValue1())
                    bestGraph = new org.javatuples.Pair<>((PSZTGraph) bestGraphFromCurrentPopulation.getValue0().clone(), bestGraphFromCurrentPopulation.getValue1());
            }
            System.out.println("yolo:"+bestGraph.getValue1());

            GraphQualityEvaluator evaluator = generator.getEvaluator();
            PSZTGraph graph = bestGraph.getValue0();
            System.out.println("Crossings EE: " + evaluator.numberOfCrossings(graph));
            System.out.println("Crossings EV: " + evaluator.numberOfVerticesWithEdgesCrossings(graph));
            System.out.println("Crossings VV: " + evaluator.numberOfVerticesWithVerticesCrossings(graph));
            System.out.println("Rel error: " + evaluator.relativeErrorOfEdgeLengths(graph));

            System.out.println("Punishment (percentage):");
            System.out.println("EE: " + evaluator.crossingEECumulativePunishment(graph));
            System.out.println("EV: " + evaluator.crossingVECumulativePunishment(graph));
            System.out.println("VV: " + evaluator.crossingVVCumulativePunishment(graph));
            System.out.println("angles: " + evaluator.anglesCumulativePunishment(graph));
            System.out.println("lengths: " + evaluator.lengthCumulativePunishment(graph));

            fitness.setText("Fitness: " + bestGraph.getValue1());
            PSZTGraphToSVGConverter converter = new PSZTGraphToSVGConverter(graph, map.get("Visibility Field Width").intValue(), map.get("Visibility Field Height").intValue(), map.get("Radius"));
            converter.doTheMagic();
            Document doc = converter.getSvgDraw().getDoc();
            panel.setSize(map.get("Visibility Field Width").intValue(), map.get("Visibility Field Height").intValue());
            svgCanvas.setSize(map.get("Visibility Field Width").intValue(), map.get("Visibility Field Height").intValue());
            this.getSvgCanvas().setDocument(doc);
        }

        // tutaj odszarzanie przycisku do eksportu pliku
    }

}
