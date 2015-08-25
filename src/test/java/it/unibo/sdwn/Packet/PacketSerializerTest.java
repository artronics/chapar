package it.unibo.sdwn.Packet;

import it.unibo.sdwn.Packet.sdwn.FakeSdwnPacketFactory;
import it.unibo.sdwn.Packet.sdwn.SdwnPacketType;
import it.unibo.sdwn.Packet.sdwn.SdwnPacketFactory;
import it.unibo.sdwn.app.analyser.Analysable;
import it.unibo.sdwn.helper.UnsignedByte;
import it.unibo.sdwn.node.Address;
import it.unibo.sdwn.node.sdwn.SdwnAddress;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
public class PacketSerializerTest
{
    private static final String regex = "\\s*\\d++[;:]"; //See Test_regex() last method
    private ArrayList<UnsignedByte> pck = new ArrayList<>();
    private Address add = new SdwnAddress(10);
    private Analysable packet;
    private String csv = "";
    private String expCsv;

    @Before
    public void setUp()
    {
        pck = FakeSdwnPacketFactory.buildGoodPacket();
        expCsv = FakeSdwnPacketFactory.FakeCsv;

        packet = SdwnPacketFactory.build(pck, SdwnPacketType.Direction.IN);
        csv = packet.toCsv();
        //we delete PacketSerialNumber because there is no
        //way to test this static variable.
        csv = csv.replaceFirst(regex, "");
    }

    @Test
    public void It_should_create_csv_string()
    {
        assertEquals(expCsv, csv);
    }

    @Test
    //
    public void Test_static_behaviour()
    {
        ArrayList p1 = FakeSdwnPacketFactory.buildGoodPacket();
        SdwnPacketType pk1 = SdwnPacketFactory.build(p1, SdwnPacketType.Direction.IN);
        String actCsv = pk1.toCsv();
        actCsv = actCsv.replaceFirst(regex, "");
        assertEquals(expCsv, actCsv);

        ArrayList p2 = FakeSdwnPacketFactory.buildGoodPacket(SdwnPacketType.Type.BEACON);
        SdwnPacketType pk2 = SdwnPacketFactory.build(p2, SdwnPacketType.Direction.IN);
        actCsv = pk2.toCsv();
        actCsv = actCsv.replaceFirst(regex, "");
        expCsv = FakeSdwnPacketFactory.FakeCsvForBeacon;
        assertEquals(expCsv, actCsv);
    }

    //it's for testing regex in other methods. it has nothing to do with class test
    @Test
    public void Test_regex()
    {
        String act1 = "DATA; IN; 12; 0xFF; 0x12; 11";
        String exp = "DATA; IN; 0xFF; 0x12; 11";
        String regex = "\\s*\\d++[;:]";
//        String act = act1.replace(regex,"");
        String act = act1.replaceFirst(regex, "");
        assertEquals(exp, act);
    }

}