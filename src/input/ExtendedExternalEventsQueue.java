package input;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.Settings;

/**
 * Queue of extended external events. It handles the specific case of external
 * ContactPlan events read by a "contact.txt" file, using the methods from
 * input.CPEventsReader.
 * 
 * The constructors are exactly the same as the superclass, ExternalEventsQueue,
 * but the "init" method has been modified, in order to support the reading with
 * input.CPEventsReader.
 * 
 * @author Federico Fiorini
 * 
 */
public class ExtendedExternalEventsQueue extends ExternalEventsQueue {
	private File eventsFile;
	private CPEventsReader reader;
	private int nrofPreload;
	private boolean allEventsRead = false;

	public ExtendedExternalEventsQueue(Settings s) {
		super(s);
	}

	public ExtendedExternalEventsQueue(String filePath, int nrofPreload) {
		super(filePath, nrofPreload);
		init(filePath);
	}
	

	/**
	 * This method creates an association between an instance of this class and
	 * one reading class; it has been modified to allow reading from
	 * CPEventsReader. This method calls the next one. For the sake of
	 * simplicity, it is assumed that the input file cannot be binary, thus
	 * omitting one if instruction.
	 * 
	 * @param eeFilePath
	 */
	protected void init(String eeFilePath) {
		this.eventsFile = new File(eeFilePath);

		/** Used for external Contact Plan reading */
		this.reader = new CPEventsReader(eventsFile);

		this.queue = readEvents(nrofPreload);
		this.nrofPreload = 0;
	}

	/**
	 * This method is called inside init(), it reads the events by means of the
	 * associated reader and adds them to the queue of the external events,
	 * defined as a field of the class.
	 * 
	 * @param nrof
	 * @return
	 */
	protected List<ExternalEvent> readEvents(int nrof) {
		if (allEventsRead) {
			return new ArrayList<ExternalEvent>(0);
		}

		List<ExternalEvent> events = reader.readEvents(nrof);

		if (nrof > 0 && events.size() == 0) {
			reader.close();
			allEventsRead = true;
		}

		return events;
	}

}