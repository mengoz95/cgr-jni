package input;

import java.io.IOException;

import core.DTNHost;
import core.Message;
import core.PriorityMessage;
import core.World;

public class PriorityMessageCreateEvent extends MessageCreateEvent 
{
	/** Priority of the message (0..2) */
	protected int priority;
	
	/**
	 * Creates a message creation event with a optional response request
	 * @param from The creator of the message
	 * @param to Where the message is destined to
	 * @param id ID of the message
	 * @param size Size of the message
	 * @param responseSize Size of the requested response message or 0 if
	 * no response is requested
	 * @param time Time, when the message is created
	 */
	public PriorityMessageCreateEvent(int from, int to, String id, int size, int responseSize, double time, int priority)
	{
		super(from, to, id, size, responseSize, time);
		this.priority = priority;
	}

	/**
	 * Creates the message this event represents.
	 */
	@Override
	public void processEvent(World world)
	{
		DTNHost to = world.getNodeByAddress(this.toAddr);
		DTNHost from = world.getNodeByAddress(this.fromAddr);
		
		Message m = new PriorityMessage(from, to, this.id, this.size, this.priority);
		m.setResponseSize(this.responseSize);
		from.createNewMessage(m);
	}

}