package it.unibo.sdwn.controller.artronics;

import it.unibo.sdwn.controller.BaseController;
import it.unibo.sdwn.routing.Routing;
import it.unibo.sdwn.trasport.TransportService;

public class SdwnController extends BaseController
{
    public SdwnController(TransportService transport, Routing routing)
    {
        super(transport, routing);
    }

    @Override
    public void init()
    {
        super.init();
    }
}
