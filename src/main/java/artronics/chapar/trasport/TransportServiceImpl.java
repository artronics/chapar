package artronics.chapar.trasport;

import artronics.chapar.helper.UnsignedByte;
import artronics.chapar.packet.PacketFactory;
import artronics.chapar.packet.protocol.PacketProtocol;
import artronics.chapar.packet.protocol.sdwn.SdwnPacketType;
import artronics.chapar.packet.sdwn.SdwnPacket;
import artronics.chapar.trasport.connection.Connection;
import artronics.chapar.trasport.events.ConnectionDataInAvailableEvent;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public final class TransportServiceImpl extends AbstractBaseTransportService<SdwnPacket, SdwnPacketType>
{

    public TransportServiceImpl(Connection connection,
                                PacketProtocol packetProtocol,
                                PacketFactory packetFactory)
    {
        super(connection, packetProtocol, packetFactory);
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
    public void connectionDataAvailableEventHandler(ConnectionDataInAvailableEvent e)
    {
        final int length = e.getLength();
        final byte[] buff = e.getBuff();
        ArrayList<UnsignedByte> receivedBytes = UnsignedByte.toUnsignedByteArrayList(buff, length);

        synchronized (lock) {
            receivedBytesQueue.addAll(receivedBytes);
        }
    }
}
