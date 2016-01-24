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
import java.util.HashMap;

/**
 * Created by tomasz on 11.01.16.
 */
public class MainWindow {


    private String[] labelStrings = {"Radius", "Edge Length", "Visibility Field Width", "Visibility Field Height", "Time Limit", "Population Size"};
    private Double[] defaultValues = {30.0,100.0,800.0,600.0, 400.0, 5.0};
    private JFrame window;
    private JButton setValuesButton;
    private JButton clearButton;

    private JLabel[] labels;
    private JTextField[] params;

    private JPanel panel;
    private JSVGCanvas svgCanvas;
    private JLabel fitness;
    private JLabel population;
    private PSZTGraph ourGraph;

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


    /**
     * Glowne okno programu
     */
    public MainWindow()
    {
        EventQueue.invokeLater(new Runnable(){
            MainWindow m;
            public void run()
            {

                window = new JFrame("PSZT_Algorytm_Ewolucyjny");
                window.setSize(800, 800);
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setLayout(new GridBagLayout());
                // SVG do wyswietlenia w oknie
                svgCanvas = new JSVGCanvas();
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

                        SwingWorker worker = new SwingWorker<Void, Void>()
                        {
                            public SwingWorker init(MainWindow m, HashMap h) {
                                this.m = m;
                                this.h = h;
                                return this;
                            }
                            HashMap h;
                            MainWindow m;
                            @Override
                            protected Void doInBackground()
                            {
                                m.startGraphsGeneration(h);
                                return null;
                            }

                        }.init(m, values);

                        worker.execute();


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
                    params[i] = new JTextField("");
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
                fitness = new JLabel("Fitness:");
                population = new JLabel("Population:");
                panel = new JPanel();
                GridBagConstraints c = new GridBagConstraints();
                // ustawianie komponentow w oknie
                int xoffset, length;
                for(int i = 0; i < labels.length; i++)
                {
                    if(i <= labels.length/2) {
                        xoffset = 1;
                        length = 1;
                    }
                    else {
                        xoffset = 3;
                        length = 2;
                    }
                    this.setConstrainst(c, xoffset+1, i%(labels.length/2 + 1), length, 1, 0, 0, length, 1, GridBagConstraints.HORIZONTAL);
                    window.getContentPane().add(params[i], c);
                    this.setConstrainst(c, xoffset, i%(labels.length/2 + 1), length, 1, 0, 0, length, 1, GridBagConstraints.NONE);
                    window.getContentPane().add(labels[i], c);
                }

                this.setConstrainst(c, 1, labels.length/2 + 1, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(setValuesButton, c);
                this.setConstrainst(c, 2, labels.length/2 +1 , 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(clearButton, c);
                this.setConstrainst(c, 3, labels.length/2 +1 , 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
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

    public void startGraphsGeneration(HashMap<String, Double> map) {
        GraphQualityArguments arguments = new GraphQualityArguments(map.get("Edge Length"), map.get("Radius"));

        GraphEvolutionGenerator generator = new GraphEvolutionGenerator(ourGraph,arguments, map.get("Population Size").intValue(), map.get("Visibility Field Width").intValue(), map.get("Visibility Field Height").intValue(), 2, 1);
        while(true)
        {
            long timeLimit = map.get("Time Limit").longValue();
            long begin = System.currentTimeMillis();
            while(System.currentTimeMillis() - begin < timeLimit)
            {
                generator.generateNextPopulation();
            }
            org.javatuples.Pair<PSZTGraph, Double> bestGraph = generator.getBestGraphFromCurrentPopulation();
//            PSZTGraph graphTest = generator.
            System.out.println("yolo:"+bestGraph.getValue1());
            fitness.setText("Fitness: " + bestGraph.getValue1());
            PSZTGraphToSVGConverter converter = new PSZTGraphToSVGConverter(bestGraph.getValue0(), map.get("Visibility Field Width").intValue(), map.get("Visibility Field Height").intValue(), map.get("Radius"));
            converter.doTheMagic();
            Document doc = converter.getSvgDraw().getDoc();
            this.getSvgCanvas().setDocument(doc);
        }


//            org.javatuples.Pair<PSZTGraph, Double> bestPair = generator.getBestGraphFromCurrentPopulation();
//            bestPair.getValue0();

    }

}
