//SP
package report;

import core.Connection;
import core.DTNHost;
import core.Message;
import input.StandardEventsReader;


public class SpeedEventLogReport extends EventLogReport {

	private void processEvent(final String action, final DTNHost host1,
			final DTNHost host2, final Message message, final String extra) {
		write(getSimTime() + " " + action + " " + (host1 != null ? host1 : "")
				+ (host2 != null ? (" " + host2) : "")
				+ (message != null ? " " + message : "")
				+ (extra != null ? " " + extra : ""));
	}
	
	@Override
	public void hostsConnected(DTNHost host1, DTNHost host2) {
		String speed = "";
		for(Connection c : host1.getConnections()){
			if(c.getOtherNode(host1).equals(host2)){
				speed = "" + c.getOtherInterface(null).getTransmitSpeed(null);
			}
		}
		processEvent(StandardEventsReader.CONNECTION, host1, host2, null,
				StandardEventsReader.CONNECTION_UP + " " +speed);
	}

	@Override
	public void messageTransferStarted(Message m, DTNHost from, DTNHost to) {
		String extra = "" + from.getInterface(1).getTransmitSpeed(null);	
		processEvent(StandardEventsReader.SEND, from, to, m, extra);		
	}
}