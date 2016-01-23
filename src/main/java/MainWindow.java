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


    public String[] getLabelStrings() {
        return labelStrings;
    }
    
    private String[] labelStrings = {"param1", "param2", "param3", "param4", "param5"};
    private JFrame window;
    private JButton setValuesButton;
    private JButton clearButton;
    private JLabel paramLabel1;
    private JLabel paramLabel2;
    private JLabel paramLabel3;
    private JLabel paramLabel4;
    private JLabel paramLabel5;
    private JLabel[] labels;

    public JLabel[] getLabels() {
        return labels;
    }

    public JTextField[] getParams() {
        return params;
    }

    private JTextField[] params;
    private JTextField param1 ;
    private JTextField param2 ;
    private JTextField param3 ;
    private JTextField param4 ;
    private JTextField param5 ;
    private JPanel panel;
    private JSVGCanvas svgCanvas;

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
                setValuesButton.addActionListener(new ActionListener()
                {
                    MainWindow m;
                    public void actionPerformed(ActionEvent e)
                    {
                        HashMap<String, Double> values = new HashMap<String, Double>();

                        for (int i = 0; i < m.getLabelStrings().length; i++) {
                            values.put(getLabelStrings()[i], Double.parseDouble(getParams()[i].getText()));

                        }
                    }
                    public void init(MainWindow mm)
                    {
                        m = mm;
                    }
                });
                params = new JTextField[labelStrings.length];
                labels = new JLabel[labelStrings.length];
                for(int i = 0; i < labelStrings.length; i++)
                {
                    params[i] = new JTextField("");
                    labels[i] = new JLabel(labelStrings[i]);

                }

                param1 = new JTextField("");
                param2 = new JTextField("");
                param3 = new JTextField("");
                param4 = new JTextField("");
                param5 = new JTextField("");
                paramLabel1 = new JLabel("Parametr 1");
                paramLabel2 = new JLabel("Parametr 2");
                paramLabel3 = new JLabel("Parametr 3");
                paramLabel4 = new JLabel("Parametr 4");
                paramLabel5 = new JLabel("Parametr 5");
                panel = new JPanel();
                GridBagConstraints c = new GridBagConstraints();
                // gridx. gridy, gridwidth, gridheight, ipadx, ipady, weightx, weighty, fill
                int xoffset;
                for(int i = 1; i <= labels.length; i++)
                {
                    if(i <= labels.length/2)
                        xoffset = 1;
                    else
                        xoffset = 5;
                    this.setConstrainst(c, xoffset+1, i%(labels.length/2 + 1), 3, 1, 0, 0, 3, 1, GridBagConstraints.HORIZONTAL);
                    window.getContentPane().add(params[i-1], c);
                    this.setConstrainst(c, xoffset, i%(labels.length/2 + 1), 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                    window.getContentPane().add(labels[i-1], c);
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

            public void init(MainWindow mm)
            {
                m = mm;
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
        });
    }


}
