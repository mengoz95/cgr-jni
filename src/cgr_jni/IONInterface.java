package cgr_jni;

import routing.ContactGraphRouter;
import routing.ContactGraphRouter.Outduct;
import core.DTNHost;
import core.Message;

public class IONInterface {	

	private static DTNHost getNodeFromNbr(long nodeNbr){
		return Utils.getHostFromNumber(nodeNbr);
	}
	
	//// STATIC METHODS ACCESSED FROM JNI /////
	
	static long getMessageSenderNbr(Message message){
		return message.getFrom().getAddress();
	}
	
	static long getMessageDestinationNbr(Message message)
	{
		return message.getTo().getAddress();
	}
	
	static long getMessageCreationTime(Message message){
		return (int) message.getCreationTime();
	}
	
	static long getMessageTTL(Message message){
		long result = (long) message.getTtl();
		return result;
	}
	static long getMessageSize(Message message){
		return message.getSize();		
	}
	static void updateMessageForfeitTime(Message message, long forfeitTime)
	{
		message.updateProperty(ContactGraphRouter.ROUTE_FORWARD_TIMELIMIT_PROP, forfeitTime);
	}
	
	static boolean isOutductBlocked(Outduct jOutduct)
	{
		return false;
	}
	
	static int getMaxPayloadLen(Outduct jOutduct)
	{
		return 1024*1024*1024;
	}
	
	static String getOutductName(Outduct jOutduct)
	{
		return "" + jOutduct.getHost().getAddress();
	}
	
	static long getOutductTotalEnququedBytes(Outduct jOutduct)
	{
		return jOutduct.getTotalEnqueuedBytes();
	}
	
	static Outduct getONEOutductToNode(long localNodeNbr, long toNodeNbr){
		DTNHost local = getNodeFromNbr(localNodeNbr);
		DTNHost to= getNodeFromNbr(toNodeNbr);
		
		ContactGraphRouter localRouter = (ContactGraphRouter) local.getRouter();
		Outduct result = localRouter.getOutducts().get(to);
		return result;
		
	}
	static int insertBundleIntoOutduct(long localNodeNbr, Message message, long toNodeNbr)
	{
		DTNHost local = getNodeFromNbr(localNodeNbr);
		DTNHost to = getNodeFromNbr(toNodeNbr);
		ContactGraphRouter localRouter = (ContactGraphRouter) local.getRouter();
		if(localRouter.getOutducts().containsKey(to)){
			localRouter.getOutducts().get(to).insertMessageIntoOutduct(message);
			return 0;
		}
		return -1;
	}

	static int insertBundleIntoLimbo(long localNodeNbr, Message message)
	{
		DTNHost local = getNodeFromNbr(localNodeNbr);
		ContactGraphRouter localRouter = (ContactGraphRouter) local.getRouter();
		localRouter.putMessageIntoLimbo(message);
		return 0;	
	}
	
	static void cloneMessage(long localNodeNbr, Message message)
	{
		DTNHost local = getNodeFromNbr(localNodeNbr);
		ContactGraphRouter localRouter = (ContactGraphRouter) local.getRouter();
		localRouter.createNewMessage(message.replicate());
	}
}
