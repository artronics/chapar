package artronics.chapar.routing;

import artronics.chapar.map.BaseNetworkMap;
import artronics.chapar.map.NetworkMap;
import artronics.chapar.node.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RouterTest
{
    NetworkMap networkMap;
    Router router ;

    Node node0;
    Node node1;
    Node node2;
    Node node3;

    @Before
    public void setUp() throws Exception
    {
        networkMap = new BaseNetworkMap<SimpleNode>();

        router = new Router(networkMap);

        /*
            A simple graph for testing Router
            node0 --30--> node1
            node2 --05--> node1
            node2 --10--> node0
            node3 --50--> node1

            The shortest path from node0 to node3 is:
                node0
                node2
                node1
                node3
            Remember NetworkMap is not a directed graph.
         */

        node0 = new SimpleNode(0);
        node1 = new SimpleNode(1);
        node2 = new SimpleNode(2);
        node3 = new SimpleNode(3);

        networkMap.addNode(node0);
        networkMap.addNode(node1);
        networkMap.addNode(node2);
        networkMap.addNode(node3);

        networkMap.addLink(node0, node1, 30);
        networkMap.addLink(node2, node1, 5);
        networkMap.addLink(node2, node0, 10);
        networkMap.addLink(node3, node1, 50);
    }

    @Test
    public void It_should_give_the_shortest_path()
    {
        List<SimpleNode> path = router.getShortestPath(node0, node3);

        assertThat(path.size(),equalTo(3));
    }

    @Test
    public void It_should_give_a_list_of_nodes_rigth_order_except_source()
    {
        List<SimpleNode> path = router.getShortestPath(node0, node3);

        SimpleNode targetNode2 = path.get(0);
        SimpleNode targetNode1 = path.get(1);
        SimpleNode targetNode3 = path.get(2);

        assertThat(targetNode2, equalTo(node2));
        assertThat(targetNode1, equalTo(node1));
        assertThat(targetNode3, equalTo(node3));
    }
}