package net.crsr.java.pdfsignatures;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import net.crsr.java.pdfsignatures.examples.Listing1213;

public class Driver {

	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				System.err.println("Usage: Driver create filename");
				System.exit(-1);
			} else if (args[0].equals("create")) {
				new Listing1213().createPdf(args[1]);
			}
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
	}

}
