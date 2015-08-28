package it.unibo.sdwn.trasport;

import com.google.common.eventbus.Subscribe;
import it.unibo.sdwn.helper.UnsignedByte;
import it.unibo.sdwn.packet.PacketFactory;
import it.unibo.sdwn.packet.protocol.PacketProtocol;
import it.unibo.sdwn.packet.protocol.sdwn.SdwnPacketType;
import it.unibo.sdwn.packet.sdwn.SdwnPacket;
import it.unibo.sdwn.trasport.events.ConnectionDataAvailableEvent;

import java.util.ArrayList;

public final class TransportServiceImpl extends AbstractBaseTransportService<SdwnPacket, SdwnPacketType>
{

    public TransportServiceImpl(InOutQueue packetQueue,
                                Connection connection,
                                PacketProtocol packetProtocol,
                                PacketFactory packetFactory)
    {
        super(packetQueue, connection, packetProtocol, packetFactory);
    }

    /**
     * This handler will handle data that Connection Layer provides. There would be two situations, first Connection
     * Layer provides data byte by byte, second Connection Layer provides an Stream of Bytes. If Stream is considered
     * you just need to put it to PacketQueue, otherwise you need to instantiate a PacketProtocolHelper implementation to deal
     * with each received byte.
     *
     * @param e
     */
    @Subscribe
    public void connectionDataAvailableEventHandler(ConnectionDataAvailableEvent e)
    {
        //At this time we just deal with second situation. Because Serial Com gives us all bytes at once.
        //TODO [Feature] add code for dealing with byte by byte situation.

        //at this point we have a buffer with fixed length
        //first we need to convert byte to unsignedByte
        final int length = e.getLength();
        final byte[] buff = e.getBuff();
        ArrayList<UnsignedByte> receivedBytes = UnsignedByte.toUnsignedByteArrayList(buff, length);

        synchronized (lock) {
            receivedBytesQueue.addAll(receivedBytes);
        }
        //When you're done with creating an ArrayList of a packet we can ask
        // SdwnPacketFactory to generate a packet for us.
//        SdwnPacket packet = SdwnPacketFactory.(packet.Direction.IN,receivedBytes);
        //TODO [Potential Bug] examine synchronization of next line.
//        receivedPacket = packetFactory.createPacket(receivedBytes);
//        packetQueue.putInput(receivedPacket);
    }

    @Override
    public void run()
    {

    }

}
