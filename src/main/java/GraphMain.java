import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.w3c.dom.Document;

import java.io.IOException;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphMain {


    public static void main(String[] args) throws IOException {

        MainWindow w = new MainWindow();
//
//        Document testingDocument = PSZTGraphToSVGConverter.getTestingDocument();
//
//        w.getSvgCanvas().setDocument(testingDocument);


        try {
            Graph graphToDraw = TinkerGraph.open();
            graphToDraw.io(IoCore.graphml()).readGraph("converted.xml");

            PSZTGraph ourGraph = new PSZTGraph(graphToDraw);

            w.setPsztGraph(ourGraph);





//
//
//            GraphQualityArguments arguments = new GraphQualityArguments();
//
//            GraphEvolutionGenerator generator = new GraphEvolutionGenerator(ourGraph,arguments, 100, 800, 600, 2, 1);
//
//            PSZTGraph firstPop = generator.getPopulation().get(1);
//
//            PSZTGraphToSVGConverter converter = new PSZTGraphToSVGConverter(firstPop, 800, 600, 12.0);
//
//            converter.doTheMagic();
//            converter.getSvgDraw().show();











//
//            Graph converted = PSZTGraph.GraphFromPSZTGraph(ourGraph);
//            converted.io(IoCore.graphml()).writeGraph("converted.xml");
//            System.out.println(ourGraph.getVertices().size());




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void testTriangle (){

    }
}
