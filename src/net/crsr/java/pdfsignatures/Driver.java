package net.crsr.java.pdfsignatures;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.itextpdf.text.DocumentException;

import net.crsr.java.pdfsignatures.examples.Listing1213;
import net.crsr.java.pdfsignatures.examples.Listing1214;

public class Driver {

	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				System.err
						.println("Usage: Driver (create <filename> | sign-created <in> <out> <keystore> <store-pass> <key-pass>)");
				System.exit(-1);
			} else if (args[0].equals("create")) {
				String filename = (args.length > 1) ? args[1] : "test.pdf";
				new Listing1213().createPdf(filename);
			} else if (args[0].equals("sign-created")) {
				String inputFilename = (args.length > 1) ? args[1] : "test.pdf";
				String outputFilename = (args.length > 2) ? args[2] : "test-signed.pdf";
				String keystoreName = (args.length > 3) ? args[3] : "keystore";
				String keystorePass = (args.length > 4) ? args[4] : "keystorePass";
				String keyPass = (args.length > 5) ? args[5] : "keyPass";
				Listing1214.setupBouncyCastle();
				new Listing1214(keystoreName, keystorePass, keyPass).signPdf(inputFilename, outputFilename);
				;
			} else {
				System.err.println("Unknown command: " + args[0]);
			}
		} catch (IOException | DocumentException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

}
