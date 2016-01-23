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

        Document testingDocument = PSZTGraphToSVGConverter.getTestingDocument();

        w.getSvgCanvas().setDocument(testingDocument);


        try {
            Graph graphToDraw = TinkerGraph.open();
            graphToDraw.io(IoCore.graphml()).readGraph("input.xml");

            PSZTGraph ourGraph = new PSZTGraph(graphToDraw);




            GraphQualityArguments arguments = new GraphQualityArguments(20);

            GraphEvolutionGenerator generator = new GraphEvolutionGenerator(ourGraph, 100, 800, 600, 2, 1);

            PSZTGraph firstPop = generator.getPopulation().get(1);

            Graph graph = PSZTGraph.GraphFromPSZTGraph(firstPop);
            GraphToSVGConverter converter = new GraphToSVGConverter(graph, 800, 600, 12.0);

//            GraphToSVGConverter graphToSVGConverter = new GraphToSVGConverter(graph, 800, 600, 30.);

//randomowy comment ^^.
            //drugi randomowy comment ^_^
            //i trzeci komencik < 3
            //xDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDxDXdDxDXDXDxDXDxDXDxDxDxDxDxDxDxDxDxDxD
            converter.doTheMagic();
            converter.getSvgDraw().show();












            Graph converted = PSZTGraph.GraphFromPSZTGraph(ourGraph);
            converted.io(IoCore.graphml()).writeGraph("converted.xml");
            System.out.println(ourGraph.getVertices().size());




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void testTriangle (){

    }
}
