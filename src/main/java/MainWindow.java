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
    private JButton setValuesButton;
    private JButton clearButton;

    private JLabel[] labels;
    private JTextField[] params;
    private JButton stopButton;
    private JPanel panel;
    private JSVGCanvas svgCanvas;
    private JLabel fitness;
    private JLabel population;
    private PSZTGraph ourGraph;

    private org.javatuples.Pair<PSZTGraph, Double> bestGraph;
    private GraphQualityArguments arguments;
    private GraphEvolutionGenerator generator;


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
                stopButton = new JButton("Stop");
                setValuesButton = new JButton("Set Values");
                clearButton = new JButton("Clear");

                setValuesButton.addActionListener((new ActionListener()
                {
                    MainWindow m;
                    public void actionPerformed(ActionEvent e)
                    {
                        HashMap<String, Double> values = new HashMap<String, Double>();

                        for (int i = 0; i < m.getLabelStrings().length; i++) {
                            if(!m.getParams()[i].getText().equals(""))
                                values.put(getLabelStrings()[i], Double.parseDouble(getParams()[i].getText()));
                            else
                                values.put(getLabelStrings()[i], defaultValues[i]);

                        }
                        if(isRunning)
                        {
                            worker.setRun(false);
                            worker.cancel(true);
                        }

                        worker = new PSZTWorker(m, values);

                        worker.execute();
                        isRunning = true;
//                        System.out.println("click");


                    }
                    public ActionListener init(MainWindow mm)
                    {
                        m = mm;
                        return this;
                    }
                }).init(m));
                params = new JTextField[labelStrings.length];
                labels = new JLabel[labelStrings.length];
                for(int i = 0; i < labelStrings.length; i++)
                {
                    params[i] = new JTextField(Double.toString(defaultValues[i]));
                    labels[i] = new JLabel(labelStrings[i]);

                }

                clearButton.addActionListener((new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for(JTextField t : m.getParams())
                        {
                            t.setText("");
                        }
                    }
                    public ActionListener init(MainWindow mm)
                    {
                        m = mm;
                        return this;
                    }
                }).init(m));

                stopButton.addActionListener(new ActionListener() {
                    MainWindow m;
                    @Override

                    public void actionPerformed(ActionEvent e) {
                        worker.cancel(true);
                        worker.setRun(false);

                    }
                    public ActionListener init(MainWindow mm)
                    {
                        m = mm;
                        return this;
                    }
                }.init(m));
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
                window.getContentPane().add(setValuesButton, c);
                this.setConstrainst(c, 2, labels.length/2 +1 , 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(clearButton, c);
                this.setConstrainst(c, 3, labels.length/2 +1 , 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(stopButton, c);
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
        arguments = new GraphQualityArguments(map.get("distance punishment"), map.get("lengthPunishment"), map.get("lengthPunishment"), map.get("vertexCrossingPunishment"), map.get("vertexAnglesPunishment"), map.get("Edge Length"), map.get("Radius"));

        generator = new GraphEvolutionGenerator(ourGraph,arguments, map.get("Population Size").intValue(), map.get("Visibility Field Width").intValue(), map.get("Visibility Field Height").intValue(), map.get("Radius"), 1);

        org.javatuples.Pair<PSZTGraph, Double> bestGraphFromCurrentPopulation;
        if (bestGraph == null) {
            bestGraphFromCurrentPopulation = generator.getBestGraphFromCurrentPopulation();
            bestGraph = new org.javatuples.Pair<>((PSZTGraph) bestGraphFromCurrentPopulation.getValue0().clone(), bestGraphFromCurrentPopulation.getValue1());
        }

        while(worker.isRun())
        {
            long timeLimit = map.get("Time Limit").longValue();
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

//            org.javatuples.Pair<PSZTGraph, Double> bestPair = generator.getBestGraphFromCurrentPopulation();
//            bestPair.getValue0();

    }

}
