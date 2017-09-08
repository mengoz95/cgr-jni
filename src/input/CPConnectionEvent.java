/*
 * Copyright 2017 University of Bologna
 * Released under GPLv3. See LICENSE.txt for details.
 */
package input;

import core.DTNHost;
import core.World;

/**
 * Class used to define a specific ConnectionEvent, made by the ContactPlan
 * reading class. It extends ConnectionEvent adding the transmit datarate
 * between hosts (param: speed)
 * The constructor calls the superclass' one and extends it by adding the speed
 * parameter.
 * 
 * @author Federico Fiorini
 * 
 */

public class CPConnectionEvent extends ConnectionEvent {

	private static final long serialVersionUID = 1L;


	/**
	 * Speed of the connection between two nodes. It's simply the datarate used
	 * by the network interface
	 */
	private int[] speed = new int[2];

	public CPConnectionEvent(int from, int to, String interf, boolean up, double time, int txSpeed, int rxSpeed) {
		super(from, to, interf, up, time);
		this.speed[0] = txSpeed;
		this.speed[1] = rxSpeed;
	}

	@Override
	public void processEvent(World world) {
		DTNHost from =  world.getNodeByAddress(this.fromAddr);
		DTNHost to =  world.getNodeByAddress(this.toAddr);
		from.getInterface(1).setTransmitSpeed(this.speed[0]);
		to.getInterface(1).setTransmitSpeed(this.speed[1]);
		from.forceConnection(to, null, this.isUp);
	}

	@Override
	public String toString() {
		return "CONN " + (isUp ? "up" : "down") + " @" + this.time + " " + this.fromAddr + "<->" + this.toAddr + " . c"
				+ this.speed;
	}
}
