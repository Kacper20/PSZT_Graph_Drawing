import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.JSVGComponent;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created by tomasz on 11.01.16.
 */
public class MainWindow {


    private String[] labelStrings = {"param1", "param2", "param3", "param4", "param5"};
    private Double[] defaultValues = {0.0,0.0,0.0,0.0,0.0};
    private JFrame window;
    private JButton setValuesButton;
    private JButton clearButton;
    private JLabel paramLabel1;
    private JLabel paramLabel2;
    private JLabel paramLabel3;
    private JLabel paramLabel4;
    private JLabel paramLabel5;
    private JLabel[] labels;
    private JTextField[] params;
    private JTextField param1 ;
    private JTextField param2 ;
    private JTextField param3 ;
    private JTextField param4 ;
    private JTextField param5 ;
    private JPanel panel;
    private JSVGCanvas svgCanvas;


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



    public MainWindow()
    {
        EventQueue.invokeLater(new Runnable(){
            MainWindow m;
            public void run()
            {
                //temporary name for window
                window = new JFrame("Best Szit Ever");
                window.setSize(800, 800);
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setLayout(new GridBagLayout());
                svgCanvas = new JSVGCanvas();
                // init
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
                panel = new JPanel();
                GridBagConstraints c = new GridBagConstraints();
                // gridx. gridy, gridwidth, gridheight, ipadx, ipady, weightx, weighty, fill
                int xoffset;
                for(int i = 0; i < labels.length; i++)
                {
                    if(i <= labels.length/2)
                        xoffset = 1;
                    else
                        xoffset = 5;
                    this.setConstrainst(c, xoffset+1, i%(labels.length/2 + 1), 3, 1, 0, 0, 3, 1, GridBagConstraints.HORIZONTAL);
                    window.getContentPane().add(params[i], c);
                    this.setConstrainst(c, xoffset, i%(labels.length/2 + 1), 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                    window.getContentPane().add(labels[i], c);
                }

                this.setConstrainst(c, 1, labels.length/2 + 1, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(setValuesButton, c);
                this.setConstrainst(c, 2, labels.length/2 +1 , 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(clearButton, c);
                this.setConstrainst(c, 0, labels.length/2 +2, 9, 1, 0, 0, 40, 60, GridBagConstraints.BOTH);
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


}
