package it.unibo.sdwn.node;

import it.unibo.sdwn.app.logger.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNode implements Node
{
    private Address address;
    private List<Link> links = new ArrayList<>();

    public BaseNode(Address nodeAddr)
    {
        this.address = nodeAddr;
    }

    public BaseNode(int intAddr)
    {
        Address addr = new Address(intAddr);
        this.address = addr;
    }

    @Override
    public boolean hasLinkTo(Node node)
    {
        for (int i = 0; i < this.links.size(); ++i) {
            if (this.links.get(i).getDestinationNode() == node)
                return true;
        }
        return false;
    }

    @Override
    public Link getLinkTo(Node node)
    {
        if (hasLinkTo(node)) {
            for (Link link : this.links) {
                if (link.getDestinationNode() == node)
                    return link;
            }
        }
        Log.main().error("Try to get non existence link");
        return null;
    }

    @Override
    public final void addLinkTo(Node node)
    {
        if (hasLinkTo(node)) {
            Log.main().error("Tried to create a duplicated link");
        }
        Link link = new CommunicationLink(node);
        this.links.add(link);
        //add link in opposite direction in case of
        //HALF_DUPLEX OR FULL_DUPLEX
        //AND also we need to check that this link doesn't already exists
        if ((link.getLinkType() == Link.LinkType.FULL_DUPLEX ||
                link.getLinkType() == Link.LinkType.HALF_DUPLEX) &&
                !node.hasLinkTo(this))
            node.addLinkTo(this);
    }

    @Override
    public final List<Link> getLinks()
    {
        return this.links;
    }

    @Override
    public final Address getAddress()
    {
        return this.address;
    }

    @Override
    public final void setAddress(Address address)
    {
        this.address = address;
    }
}
