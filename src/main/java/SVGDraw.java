
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.JSVGComponent;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.DOMImplementation;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.*;
import org.apache.batik.transcoder.image.*;

import javax.xml.transform.dom.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;


public class SVGDraw {
    Document doc;
    private final String svgNS;
    private final int width;
    private final int height;
    private final String name;
    private final String circleColor;
    private final String lineColor;
    private final double strokeWidth;

    public Document getDoc() {
        return doc;
    }

    public SVGDraw(int width, int height, String name, String circleColor, String lineColor, double strokeWidth) {
        this.width = width;
        this.height = height;
        this.name = name;
        this.circleColor = circleColor;
        this.lineColor = lineColor;
        this.strokeWidth = strokeWidth;
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        doc = impl.createDocument(svgNS, "svg", null);

        Element svgRoot = doc.getDocumentElement();
        svgRoot.setAttributeNS(svgNS, "width", String.valueOf(width));
        svgRoot.setAttributeNS(svgNS, "height", String.valueOf(height));
    }

    public void show() {
        final JPanel panel = new JPanel(new BorderLayout());

        JSVGCanvas svgCanvas = new JSVGCanvas();
        panel.add(svgCanvas, BorderLayout.CENTER);

        JFrame f = new JFrame(name);
        f.getContentPane().add(panel);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        int dx = 5;
        int dy = 35;
        f.setSize(width + dx, height + dy);
        f.setVisible(true);
        svgCanvas.setSize(panel.getSize());
        svgCanvas.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);
        svgCanvas.setDocument(doc);
    }

    public void toSVG(OutputStream os) throws IOException {
        org.apache.xml.serialize.OutputFormat of =
                new org.apache.xml.serialize.OutputFormat("XML", "ISO-8859-1", true);
        of.setIndent(1);
        of.setIndenting(true);
        org.apache.xml.serialize.XMLSerializer serializer =
                new org.apache.xml.serialize.XMLSerializer(os, of);
        //   As a DOM Serializer
        serializer.asDOMSerializer();
        serializer.serialize(doc);
    }

    public void toPNG(OutputStream os) throws TranscoderException {
        Transcoder transcoder = new PNGTranscoder();
        TranscoderOutput output = new TranscoderOutput(os);
        TranscoderInput input = new TranscoderInput(doc);
        transcoder.transcode(input, output);
    }

    private static String q(double i) {
        return Double.toString(i);
    }

    public Element circle(double cx, double cy, double radius) {
        Element circle = doc.createElementNS(svgNS, "circle");
        circle.setAttributeNS(null, "cx", q(cx));
        circle.setAttributeNS(null, "cy", q(cy));
        circle.setAttributeNS(null, "r", q(radius));
        circle.setAttributeNS(null, "style", "fill:" + circleColor);
        return circle;
    }

    public Element line(double x1, double y1, double x2, double y2) {
        Element line = doc.createElementNS(svgNS, "line");
        line.setAttributeNS(null, "x1", q(x1));
        line.setAttributeNS(null, "y1", q(y1));
        line.setAttributeNS(null, "x2", q(x2));
        line.setAttributeNS(null, "y2", q(y2));
        line.setAttributeNS(null, "style", "stroke-width:" + q(strokeWidth) + "; stroke:" + lineColor);
        return line;
    }

    public Element textCentered(double x, double y, double size, String t) {
        Element text = doc.createElementNS(svgNS, "text");
        text.setAttributeNS(null, "x", q(x));
        text.setAttributeNS(null, "y", q(y + size / 3.));
        text.setAttributeNS(null, "style",
                "text-anchor: middle; fill:black; stroke:none; font-size: " + size + "px");
        Text message = doc.createTextNode(t);
        text.appendChild(message);
        return text;
    }
}