package com.job.assignment.crossborder.service.contractor.impl;

import com.job.assignment.crossborder.service.ServiceConstant;
import com.job.assignment.crossborder.service.algorithm.dijkstra.Dijkstra;
import com.job.assignment.crossborder.service.algorithm.dijkstra.Graph;
import com.job.assignment.crossborder.service.algorithm.dijkstra.JsonCountry;
import com.job.assignment.crossborder.service.algorithm.dijkstra.Node;
import com.job.assignment.crossborder.service.contractor.RouteService;
import com.job.assignment.crossborder.service.exception.WrongSourceWordException;
import com.job.assignment.crossborder.service.exception.WrongTargetWordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    LinkedList<JsonCountry> preparedData;

    public RouteServiceImpl(@Autowired LinkedList<JsonCountry> preparedData){
        this.preparedData = preparedData;
    }

    public Node getborders(String source, String destination) throws RuntimeException {

        Graph graph = prepareGraphData();
        Node resultNode = calculateShortestPathFromSourceToTarget(graph, source, destination);

        printShortestPath(resultNode);
        /**
         * Add add itsef as last node, if already it has adjacentcy
         */
        if(resultNode.getShortestPath().stream().count() != 0)resultNode.getShortestPath().add(resultNode);

        return resultNode;
    }

    private Graph prepareGraphData() {
        Graph finalGraph = new Graph();
        preparedData.stream().forEach(jsonCountry -> {
                    Node mainNode;
                    mainNode = prepareMainNodeAndBindAdjacentNodes(finalGraph, jsonCountry);
                }
        );
        return finalGraph;
    }

    private void printShortestPath(Node resultNode) {
        if(resultNode != null ){
            if(resultNode.getShortestPath().size() == 0){
                System.out.print("400 - rout not calculated");
            }else{
                resultNode.getShortestPath().
                        stream().
                        forEach(c -> System.out.print(c.getName() + " "));
            }
        }
    }

    private Node calculateShortestPathFromSourceToTarget(Graph graph, String source, String target) {
        graph = Dijkstra.calculateShortestPathFromSource(graph, graph.getNodes().
                stream().
                filter(c -> c.getName().equals(source)).
                findAny().
                orElseThrow(() -> new WrongSourceWordException(source)));
        /**
         * Check the target entered right
         */
       return  graph.getNodes().stream().filter(node -> node.getName().equals(target)).findAny().orElseThrow(() -> new WrongTargetWordException(target));
    }

    private Integer printNodesInfo(Graph graph) {
        Integer total=0;
        for (Node node : graph.getNodes()) {

            System.out.print(node.getName() + " , Distances : " +  node.getAdjacentNodes().values() + " | ");
            node.getAdjacentNodes().keySet().
                    stream().
                    forEach(c -> System.out.print("Adjacentcies : " + c.getName() + " "));
            System.out.println();
            total = total + node.getAdjacentNodes().size();
        }
        return total;
    }

    private Node prepareMainNodeAndBindAdjacentNodes(Graph finalGraph, JsonCountry jsonCountry) {
        Node mainNode;
        mainNode = prepareNode(finalGraph, jsonCountry);
        mainNode = bindAdjacentNodes(jsonCountry, mainNode, finalGraph, preparedData);
        return mainNode;
    }

    private Node prepareNode(Graph finalGraph, JsonCountry jsonCountry) {
        Node mainNode;
        if (finalGraph.getNodes().
                stream().
                filter(node -> node.getName().equals(jsonCountry.getId())).
                count() == 0){
            mainNode = createMainNodeThenAddToGraph(finalGraph,  jsonCountry);

        }else{
            mainNode = finalGraph.
                    getNodes().
                    stream().
                    filter(node -> node.getName().equals(jsonCountry.getId())).
                    findAny().
                    orElse(null);
        }
        return mainNode;
    }

    private Node createMainNodeThenAddToGraph(Graph graph, JsonCountry jsonCountry) {
        Node newMainNode;
        newMainNode =  createMainNode(jsonCountry);
        graph.addNode(newMainNode);
        return newMainNode;
    }

    /**
     * Graph has not included the item, create the main nodes.
     * 1.create a main node,
     * 2.bind with adj,
     * 3.remove the main node from adj of main of ajd
     */
    private Node createMainNode(JsonCountry jsonCountry) {
        List JSONLatlng = new ArrayList<>();
        jsonCountry.getLatlng().stream().forEach(jsonLatlng -> {
            JSONLatlng.add(jsonLatlng);
        });
        String JSONName = jsonCountry.getId();
        Node node = new Node(JSONName);
        node.setJsoNlatlng(JSONLatlng);
        return node;
    }

    private Node bindAdjacentNodes(JsonCountry jsonCountry, Node newMainNode, Graph graph, LinkedList<JsonCountry> preparedData)  {
        List<String> JSONBorders = jsonCountry.getBorders();
        if(JSONBorders == null || JSONBorders.size() == 0) return newMainNode;
        else if(JSONBorders.size() < 0 ) throw new RuntimeException(ServiceConstant.NEGATIVE_BORDER_SIZE_EXCEPTION_MESSAGE);
        else if(JSONBorders.size() > 0){
            if(newMainNode.getAdjacentNodes().size() == JSONBorders.size()) return newMainNode;
            else if(newMainNode.getAdjacentNodes() == null || newMainNode.getAdjacentNodes().size() == 0) {
                /**
                 *  All Adjacent nodes must be bind, but check the graph to detect created before .
                 *  if not created completely, add new ones.
                 *  remove main node adjacentcy from all adjacent node adjacentcy.
                 */
                JSONBorders.stream().forEach(jsonBorder -> {

                    Node newMainNodeofAdj = graph.
                            getNodes().
                            stream().
                            filter(nodeOfGraph -> nodeOfGraph.getName().equals(jsonBorder)).
                            findAny().
                            orElse(null);

                    if (newMainNodeofAdj != null) {
                        newMainNode.addDestination(newMainNodeofAdj, calculateDistanceBetweenTwoNode(newMainNode, newMainNodeofAdj).intValue());
                        return;
                    } else {
                        /**
                         *   BUrası tek sefer çalıştırılabilir mi?
                         */
                        JsonCountry adjacentcyMainItem = preparedData.
                                stream().
                                filter(mainjsonCountry -> mainjsonCountry.getId().equals(jsonBorder)).
                                findAny().
                                orElse(null);

                        Node node = createMainNodeThenAddToGraph(graph, adjacentcyMainItem);
                        newMainNode.addDestination(node, calculateDistanceBetweenTwoNode(newMainNode, node).intValue());
                        return;
                    }
                });
            }else {
                System.out.println("Unused place, nothing! " + newMainNode.getAdjacentNodes().size() + JSONBorders.size());
            }
        }
        return newMainNode;
    }

    private BigDecimal calculateDistanceBetweenTwoNode(Node newMainNode, Node node) {
        BigDecimal x1,x2,y1,y2,distance;
        MathContext mathContext = new MathContext(0, RoundingMode.HALF_EVEN);
        x1 =  newMainNode.getJsoNlatlng().get(0).round(mathContext);
        x2 =  node.getJsoNlatlng().get(0).round(mathContext);
        y1 =  newMainNode.getJsoNlatlng().get(1).round(mathContext);
        y2 =  node.getJsoNlatlng().get(1).round(mathContext);

        distance = BigDecimal.valueOf(Math.sqrt(x1.subtract(x2).multiply(x1.subtract(x2)).add(y1.subtract(y2).multiply(y1.subtract(y2))).doubleValue()));

        return distance.round(mathContext);
    }
}