package artronics.chapar.connection.serialPort;

import artronics.chapar.connection.EstablishConnectionException;
import artronics.chapar.core.configuration.Config;
import artronics.chapar.core.logger.Log;
import gnu.io.CommPortIdentifier;

import java.util.Enumeration;
import java.util.HashMap;

class SerialPortInitializer
{
    private HashMap ports = new HashMap();
    private CommPortIdentifier selectedCommPortIdentifier;

    public CommPortIdentifier getSelectedCommPortIdentifier()
    {
        return this.selectedCommPortIdentifier;
    }

    public void init()
    {
        try {
            searchForPorts();
            this.selectedCommPortIdentifier = getComPortFromConfig();
        }catch (EstablishConnectionException noComPortFound) {
            showLogMessage();
            noComPortFound.printStackTrace();
//            Log.main().error("If you are on Mac, create a /var/lock with 777 permision." +
//                                     " see: http://pizzaprogramming.blogspot.it/2010/05/gnuioportinuseexception
// .html");
            //Dude this is the end! hold your breath and count to ten.
            System.exit(1);
        }
    }

    private void showLogMessage()
    {
        Log.main().error("No Serial-Com-port found. " +
                                 "There might be some workarounds:\n" +
                                 "\t1- Check config file(chapar.config). check if you set property OS correctly\n" +
                                 "\t2- Check the value of SinkCommPort\n" +
                                 "\t3- Delete chapar.config file run program again and set bytes values again.\n" +
                                 "\t4- If non of above worked, well, Happy Debugging!! ;-)");
    }

    private void searchForPorts() throws EstablishConnectionException
    {

        Enumeration ports = CommPortIdentifier.getPortIdentifiers();

        filterPorts(ports);

        if (this.ports.isEmpty()) {
            Log.main().info(
                    "If you get any issue related to comm driver on mac see: http://blog.iharder" +
                            ".net/2009/08/18/rxtx-java-6-and-librxtxserial-jnilib-on-intel-mac-os-x");
            throw new EstablishConnectionException();
        }
    }

    private void filterPorts(Enumeration ports)
    {
        //Filter com ports based on current Operating System.
        String OS = Config.get().getString("OS");

        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();

            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (OS.equals("Mac")) {
                    if (!curPort.getName().contains("Bluetooth") &&
                            curPort.getName().contains("tty")) {

                        this.ports.put(curPort.getName(), curPort);
                        Log.main().debug("Found Com port:[" + this.ports.size() + "] --> " + curPort.getName());
                    }
                }else {
                    //Currently there is no code for windows and linux.
                    //Add filter here
                    //TODO Put windows filtering here.
                    this.ports.put(curPort.getName(), curPort);
                    Log.main().debug("Found Com port:[" + this.ports.size() + "] --> " + curPort.getName());
                }
            }
        }
    }

    private CommPortIdentifier getComPortFromConfig() throws EstablishConnectionException
    {
        CommPortIdentifier sinkPort = null;
        String comName = Config.get().getString("SERIAL_PORT_NAME");
        if (this.ports.containsKey(comName)) {
            sinkPort = (CommPortIdentifier) this.ports.get(comName);
            Log.main().info("Com port matched configuration");
            Log.main().debug("Sink port is associated to " + sinkPort.getName());

            return sinkPort;
        }else {
            Log.main().debug("No available com port(s) matched configuration.\n" +
                                     "Your current configuration for SinkCommPortName is" + comName + "\n" +
                                     "Check SDWN.config file and change property SinkCommPortName");
            //TODO change unibo so unibo automatically change configuration to match
            throw new EstablishConnectionException();
        }
    }
}
