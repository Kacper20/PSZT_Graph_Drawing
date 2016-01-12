import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import java.awt.*;

/**
 * Created by tomasz on 11.01.16.
 */
public class MainWindow {



    private JFrame window;
    private JButton setValuesButton;
    private JButton clearButton;
    private JLabel paramLabel1;
    private JLabel paramLabel2;
    private JLabel paramLabel3;
    private JLabel paramLabel4;
    private JLabel paramLabel5;
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
                param1 = new JTextField("default");
                param2 = new JTextField("default");
                param3 = new JTextField("default");
                param4 = new JTextField("default");
                param5 = new JTextField("default");
                paramLabel1 = new JLabel("Parametr 1");
                paramLabel2 = new JLabel("Parametr 2");
                paramLabel3 = new JLabel("Parametr 3");
                paramLabel4 = new JLabel("Parametr 4");
                paramLabel5 = new JLabel("Parametr 5");
                panel = new JPanel();
                GridBagConstraints c = new GridBagConstraints();
                // gridx. gridy, gridwidth, gridheight, ipadx, ipady, weightx, weighty, fill
                this.setConstrainst(c, 2,       1,      3,          1,      0,      0,      3,      1,      GridBagConstraints.HORIZONTAL);
                window.getContentPane().add(param1, c);
                this.setConstrainst(c, 1, 1, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(paramLabel1, c);
                this.setConstrainst(c, 2, 2, 3, 1, 0, 0, 3, 1, GridBagConstraints.HORIZONTAL);
                window.getContentPane().add(param2, c);
                this.setConstrainst(c, 1, 2, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(paramLabel2, c);
                this.setConstrainst(c, 2, 3, 3, 1, 0, 0, 3, 1, GridBagConstraints.HORIZONTAL);
                window.getContentPane().add(param3, c);
                this.setConstrainst(c, 1, 3, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(paramLabel3, c);
                this.setConstrainst(c, 6, 1, 3, 1, 0, 0, 3, 1, GridBagConstraints.HORIZONTAL);
                window.getContentPane().add(param4, c);
                this.setConstrainst(c, 5, 1, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(paramLabel4, c);
                this.setConstrainst(c, 6, 2, 3, 1, 0, 0, 3, 1, GridBagConstraints.HORIZONTAL);
                window.getContentPane().add(param5, c);
                this.setConstrainst(c, 5, 2, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(paramLabel5, c);
                this.setConstrainst(c, 1, 4, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(setValuesButton, c);
                this.setConstrainst(c, 2, 4, 1, 1, 0, 0, 1, 1, GridBagConstraints.NONE);
                window.getContentPane().add(clearButton, c);
                this.setConstrainst(c, 2, 5, 1, 60, 0, 0, 1, 60, GridBagConstraints.NONE);
                window.getContentPane().add(panel, c);
                panel.setLayout(new GridLayout(1,1));
                panel.add(svgCanvas);

                window.setVisible(true);




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
        });
    }


}
