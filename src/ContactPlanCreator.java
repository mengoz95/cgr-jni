*
 * Copyright 2017 University of Bologna
 * Released under GPLv3. See LICENSE.txt for details.



import java.io.*;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * The contact plan converter consists of a stand - alone Java application
 * including two classes, ContactPlanConverter.java and ContactPlanLine.java. This
 * class creates an ION contact plan file starting from a ONE report file, namely
 * the CPEventLogReport created by ONE after a simulation.
 * The ION contact file obtrained can be used in a variety of ways. In particualr it can 
 * be used to let CGR know in advance the "pseudo" random contacts generated by ONE. To this end
 * it is necessary to run the same simulation twice, with the same seed. The former just to collect the 
 * psudeo random contact data; the latter to carry out a simulation with CGR provided with 
 * a contact plan that corresponds exactly to pseudo random contacts enforced by ONE. It is 
 * crucial that the two simulations have the same configuration and seed. 
 * First of all, the user needs to enter the input
 * file (the ONE report file) and the output file (the ION contact plan) full
 * paths. Then the application scans the input file, considering only connection events: 
 * the nodes involved in the contact, the start time and the Tx speed are derived from "UP" events; 
 * the stop time from "DOWN" ones. For every contact the application writes down in the contact plan 
*  file three lines: the "range" line, with the smallest ipn node first, in order to
 * intend a two-way range, and then the two contact lines, one for each way.
 *  
 *
 * @author Jako Jo Messina
 *
 */
class ContactPlanConverter {
	public static void main(String args[]) throws IOException {

		FileReader fr;
		String inputFilePath, outputPath;
		int datarate = 0;
		int node1, node2;
		int start = 0;
		int stop = 0;
		BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("--> Insert the full path of the input file for the Contact Plan Converter");
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
		System.out.println("Finished! all done!");

	}
}
