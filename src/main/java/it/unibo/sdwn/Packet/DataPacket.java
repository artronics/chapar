package it.unibo.sdwn.Packet;

import it.unibo.sdwn.Packet.sdwn.SdwnPacket;

public final class DataPacket implements SdwnPacket//extends SdwnPacket
{
    /*
    private Address destNodeAddress = new Address();
    private ArrayList<UnsignedByte> data = new ArrayList<>();
    private ArrayList<UnsignedByte> header = new ArrayList<>();

    //See PacketFactory
    protected DataPacket(ArrayList<UnsignedByte> dataPayload,
                      Address destNodeAddres)
    {
        int packetLeng = dataPayload.size() + DATA_PACKET_HEADER_LENGTH.intValue();
        UnsignedByte paketLength = UnsignedByte.of(packetLeng);

        setHeader(destNodeAddres, paketLength);
        data = new ArrayList<>(dataPayload);
        data.addAll(0, header);
    }

    protected void setHeader(Address destNodeAddress, UnsignedByte packetLength)
    {
        header.add(ByteMeaning.LENGTH.value, packetLength);
        header.add(ByteMeaning.NET_ID.value, UnsignedByte.of(Config.get().getByte("NET_ID")));
        header.add(ByteMeaning.SOURCE_L.value, UnsignedByte.of(0));
        header.add(ByteMeaning.SOURCE_H.value, UnsignedByte.of(0));
        header.add(ByteMeaning.DESTINATION_L.value, destNodeAddress.getAddress()[0]);
        header.add(ByteMeaning.DESTINATION_H.value, destNodeAddress.getAddress()[1]);
        header.add(ByteMeaning.TYPE.value, Type.DATA.value);
        header.add(ByteMeaning.TTL.value, UnsignedByte.of(Config.get().getByte("TTL")));
        header.add(ByteMeaning.NEXT_HOP_L.value, UnsignedByte.of(0));
        header.add(ByteMeaning.NEXT_HOP_H.value, UnsignedByte.of(0));
    }
    */
}
