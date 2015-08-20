package it.unibo.sdwn.trasport;

import it.unibo.sdwn.app.config.Config;
import it.unibo.sdwn.trasport.exceptions.MalformedPacketException;
import it.unibo.sdwn.trasport.exceptions.PacketNotReadyException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PacketByteStreamAdapterTest
{
    private final byte startByte = Config.get().getByte("startByte");
    private final byte stopByte = Config.get().getByte("stopByte");
    private ArrayList goodPacket = new ArrayList<Byte>();
    private ArrayList malformedPacket = new ArrayList<Byte>();
    private PacketByteStreamAdapter packetByteStreamAdapter;

    @Before
    public void setUp()
    {
        packetByteStreamAdapter = new PacketByteStreamAdapter();
        //lets say the packet is something like this
        //startByte lengthOfPacket someDate stopByte
        goodPacket.add((byte) 4); //packet length
        goodPacket.add((byte) 1);
        goodPacket.add((byte) 2);
        goodPacket.add((byte) 3);

        malformedPacket.add(startByte);
        malformedPacket.add((byte) 2); //packet length
        malformedPacket.add((byte) 2);
        malformedPacket.add((byte) 3);
    }

    private void setBytes() throws MalformedPacketException, PacketNotReadyException
    {
        packetByteStreamAdapter.add(startByte);
        packetByteStreamAdapter.add((byte) 4);
        packetByteStreamAdapter.add((byte) 1);
        packetByteStreamAdapter.add((byte) 2);
        packetByteStreamAdapter.add((byte) 3);
        packetByteStreamAdapter.add(stopByte);

    }

    @Test
    public void It_should_construct_packet() throws PacketNotReadyException, MalformedPacketException
    {
        setBytes();
        ArrayList<Byte> actualPacket = packetByteStreamAdapter.getPacket();

        assertEquals(goodPacket, actualPacket);
    }

    @Test
    public void It_should_ignore_non_sense_bytes_BEFORE_getting_start_byte() throws MalformedPacketException,
            PacketNotReadyException
    {
        //lets add some in appropriate bytes
        packetByteStreamAdapter.add((byte) 7);
        packetByteStreamAdapter.add((byte) 4);
        //then appropriate bytes
        setBytes();
        ArrayList<Byte> actualPacket = packetByteStreamAdapter.getPacket();
        assertEquals(goodPacket, actualPacket);
    }

    @Test
    public void If_packet_is_ready_and_we_send_unapproprited_bytes_it_should_give_last_packet() throws
            PacketNotReadyException, MalformedPacketException
    {
        //first send appropriate bytes
        setBytes();
        //then send unappropriated bytes
        packetByteStreamAdapter.add((byte) 7);
        packetByteStreamAdapter.add((byte) 4);

        ArrayList<Byte> actualPacket = packetByteStreamAdapter.getPacket();

        assertEquals(goodPacket, actualPacket);
    }

    @Test
    public void It_should_construct_a_new_packet_after_finishig_previous_one() throws PacketNotReadyException,
            MalformedPacketException
    {
        //now lets send two sequence of good packets
        It_should_construct_packet();
        It_should_construct_packet();

        //now lets mixed above methods
        If_packet_is_ready_and_we_send_unapproprited_bytes_it_should_give_last_packet();
        It_should_ignore_non_sense_bytes_BEFORE_getting_start_byte();
    }

    @Test
    public void It_should_be_clear_when_instantiated()
    {
        //if packet is not ready it should throw packetNotReady
        boolean thrown = false;
        try {
            packetByteStreamAdapter.getPacket();
        }catch (PacketNotReadyException e) {
            thrown = true;
        }

        assertTrue(thrown);
        //isReady also should return false
        assertFalse(packetByteStreamAdapter.isReady());

    }

    @Test
    public void It_should_be_clear_after_geting_the_packet()
    {
        try {
            setBytes();
            ArrayList<Byte> getThePacket = packetByteStreamAdapter.getPacket();
        }catch (PacketNotReadyException packetNotReady) {
        }catch (MalformedPacketException malformedPacket1) {
        }
        //then
        It_should_be_clear_when_instantiated();
    }

    @Test(expected = MalformedPacketException.class)
    public void It_should_throw_exp_for_malformed_packet() throws PacketNotReadyException, MalformedPacketException
    {
        packetByteStreamAdapter.add((Byte) malformedPacket.get(0));
        packetByteStreamAdapter.add((Byte) malformedPacket.get(1));
        packetByteStreamAdapter.add((Byte) malformedPacket.get(2));
        packetByteStreamAdapter.add((Byte) malformedPacket.get(3));
        ArrayList<Byte> actualPacket = packetByteStreamAdapter.getPacket();
    }

    @Test
    public void It_shoud_be_clear_after_throwing_exceptions()
    {
        try {

            packetByteStreamAdapter.getPacket();
        }catch (PacketNotReadyException packetNotReady) {
            assertFalse(packetByteStreamAdapter.isReady());
        }
        try {
            packetByteStreamAdapter.add((Byte) malformedPacket.get(0));
            packetByteStreamAdapter.add((Byte) malformedPacket.get(1));
            packetByteStreamAdapter.add((Byte) malformedPacket.get(2));
            packetByteStreamAdapter.add((Byte) malformedPacket.get(3));
            ArrayList<Byte> actualPacket = packetByteStreamAdapter.getPacket();
        }catch (MalformedPacketException malformedPacket1) {
            assertFalse(packetByteStreamAdapter.isReady());
        }catch (PacketNotReadyException packetNotReady) {
        }
    }

}