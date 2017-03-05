package cp_creator;

import java.io.*;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * The contact plan creation consists of a stand - alone Java application
 * including two classes, ContactPlanCreator.java and ContactPlanLine.java. This
 * class creates the contact plan starting from an input file, in our case this
 * is the CPEventLogReport created by ONE after we run a simulation: it is of
 * crucial importance that the simulation we want to run using the CGR, and
 * consequently the contact plan, has the same configuration and seed of the
 * simulation we run in order to obtain the contact plan, otherwise we are going
 * to have unexpected results. First of all, the user needs to enter the input
 * file full path (corresponding with the report file) and the output file full
 * path (which will be our contact plan). Then the application will read the
 * whole input file, line after line, selecting only the connection events, and,
 * in particular, extrapolating from the UP events the start time of the
 * contact, nodes involved in that contact, start time and speed; then, stop
 * time will be added when the DOWN event will be read. So, for every contact
 * read, the application will write down in the contact plan file three lines:
 * first, the range line, with the smallest address node as from, in order to
 * intend a two ways contact, and then the two contact lines, one for each way.
 *
 * @author Jako Jo Messina
 *
 */
class ContactPlanCreator {
	public static void main(String args[]) throws IOException {

		FileReader fr;
		String inputFilePath, outputPath;
		int datarate = 0;
		int node1, node2;
		int start = 0;
		int stop = 0;
		BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("--> Insert the full path of the input file for the Contact Plan Creator\n");
		inputFilePath = br2.readLine();

		fr = new FileReader(inputFilePath);
		BufferedReader br = new BufferedReader(fr);

		SortedSet<ContactPlanLine> contactPlan = new TreeSet<>();
		ContactPlanLine riga;
		String fileLine;
		while ((fileLine = br.readLine()) != null) {
			StringTokenizer tokenizer = new StringTokenizer(fileLine);

			while (tokenizer.hasMoreTokens()) {
				start = (int) (Math.floor(Double.parseDouble(tokenizer.nextToken())));

				if (tokenizer.nextToken().equalsIgnoreCase("CONN")) {
					String temp1, temp2;
					temp1 = tokenizer.nextToken().substring(1);
					temp2 = tokenizer.nextToken().substring(1);
					node1 = Integer.parseInt(temp1);
					node2 = Integer.parseInt(temp2);
					if (tokenizer.nextToken().equalsIgnoreCase("UP")) {
						// SP mod
						datarate = Integer.parseInt(tokenizer.nextToken());
						stop = 0;
						riga = new ContactPlanLine(start, stop, node1, node2, datarate);
						contactPlan.add(riga);
					} else {
						for (ContactPlanLine cpl : contactPlan) {
							if (cpl.connectionUp(node1, node2)) {
								cpl.setStop((int) Math.floor(start));
								break;
							}
						}
					}
				} else
					break;

			}

		}
		br.close();
		fr.close();
		System.out.println("--> Insert the output contact plan path");
		outputPath = br2.readLine();
		PrintWriter pw = new PrintWriter(outputPath);
		BufferedWriter bw = new BufferedWriter(pw);
		for (ContactPlanLine c : contactPlan) {

			if (c.getStop() == 0) {
				c.setStop(start);

				if (c.getStart() == c.getStop())
					c.setStop(start + 1);
			}

			bw.write(c.toStringRange());
			bw.newLine();
			bw.write(c.toString());
			bw.newLine();
			bw.write(c.toStringTwoWays());
			bw.newLine();
		}
		bw.close();
		pw.close();

	}
}