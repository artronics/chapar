package it.unibo.sdwn.routing;

import it.unibo.sdwn.map.NetworkMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RoutingFactory
{
    @Autowired
    private static NetworkMap networkMap;
    @Autowired
    private static Routing routing;

    public synchronized Routing getInstance()
    {
        if (routing == null) {
            //get dependencies
            ClassPathXmlApplicationContext xmlContext = new ClassPathXmlApplicationContext(
                    "DependencyInjection.xml");
            networkMap = xmlContext.getBean(NetworkMap.class);
            routing = xmlContext.getBean(Routing.class);
            xmlContext.close();

            routing.setNetworkMap(networkMap);
            return routing;
        }

        return routing;
    }
}
