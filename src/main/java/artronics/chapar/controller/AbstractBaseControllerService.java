package artronics.chapar.controller;

import artronics.chapar.PacketQueue.PacketQueue;
import artronics.chapar.address.AbstractBaseAddress;
import artronics.chapar.address.AddressFactory;
import artronics.chapar.app.config.Config;
import artronics.chapar.app.event.Event;
import artronics.chapar.node.AbstractBaseNode;
import artronics.chapar.node.NodeFactory;
import artronics.chapar.packet.AbstractBasePacket;
import artronics.chapar.packet.PacketFactory;
import artronics.chapar.routing.Routing;
import artronics.chapar.trasport.TransportService;
import artronics.chapar.trasport.events.SinkFoundEvent;
import com.google.common.eventbus.Subscribe;

import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class AbstractBaseControllerService
        <P extends AbstractBasePacket,
        N extends AbstractBaseNode,
        A extends AbstractBaseAddress>
        implements ControllerService, Runnable
{
    protected static final int MAX_QUEUE_CAPACITY = Config.get().getInt("MAX_QUEUE_CAPACITY");
    protected final AddressFactory<A> addressFactory;
    protected final ArrayBlockingQueue<P> inPacketQueue;
    protected final ArrayBlockingQueue<P> outPacketQueue;
    protected Hashtable<A, N> networkMap = new Hashtable();
    protected TransportService transport;
    protected Routing routing;
    protected PacketFactory packetFactory;
    protected NodeFactory<N, A> nodeFactory;

//    @Override
//    public ArrayBlockingQueue<P> getInPacketQueue()
//    {
//        return inPacketQueue;
//    }
//    @Override
//    public ArrayBlockingQueue<P> getOutPacketQueue()
//    {
//        return outPacketQueue;
//    }



    public AbstractBaseControllerService(TransportService transport,
                                         Routing routing,
                                         PacketFactory packetFactory,
                                         NodeFactory nodeFactory,
                                         AddressFactory addressFactory)
    {
        this.transport = transport;
        this.routing = routing;
        this.packetFactory = packetFactory;
        this.nodeFactory = nodeFactory;
        this.addressFactory = addressFactory;
        PacketQueue<P> packetQueue = new PacketQueue<>();
        this.inPacketQueue = packetQueue.getInPacketQueue();
        this.outPacketQueue = packetQueue.getOutPacketQueue();
        Event.mainBus().register(this);
    }

    /**
     * An abstract contract which grantees updating network map. Although HashTable is a thread safe implementation,
     * this is up to programmer to apply additional synchronization mechanism.
     *
     * @param node    the node
     * @param address the address
     */
    protected abstract void putNodeToNetworkMap(N node, A address);


    @Subscribe
    abstract public void sinkFoundEventHandler(SinkFoundEvent event);

    public void init()
    {
        transport.init();
        routing.init();
    }

    @Override
    public void run()
    {
    }
}
