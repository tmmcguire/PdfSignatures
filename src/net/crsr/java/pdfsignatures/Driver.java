package net.crsr.java.pdfsignatures;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import com.itextpdf.text.DocumentException;

import net.crsr.java.pdfsignatures.examples.Listing1213;
import net.crsr.java.pdfsignatures.examples.Listing1214;
import net.crsr.java.pdfsignatures.signing.Signer;
import net.crsr.java.pdfsignatures.testing.GeneratePdf;

public class Driver {
	private static final String	usage								= "Driver (" + "create-example filename" + " | "
			+ "sign-example [in out keystore store-pass key-pass]" + " | " + "create-testing [filename]" + " | "
			+ "sign-testing [in out keystore store-pass key-pass]" + ")";

	private static final String	signatureFieldName	= "cryptographicSignature";

	public static void main(String[] args) {
		try {
			if (args.length == 0) {

				System.err.println("Usage: " + usage);
				System.exit(-1);

			} else if (args[0].equals("create-example")) {

				String filename = (args.length > 1) ? args[1] : "test.pdf";
				new Listing1213().createPdf(filename);

			} else if (args[0].equals("sign-example")) {

				String inputFilename = (args.length > 1) ? args[1] : "test.pdf";
				String outputFilename = (args.length > 2) ? args[2] : "test-signed.pdf";
				String keystoreName = (args.length > 3) ? args[3] : "keystore";
				String keystorePass = (args.length > 4) ? args[4] : "keystorePass";
				String keyPass = (args.length > 5) ? args[5] : "keyPass";
				Listing1214.setupBouncyCastle();
				new Listing1214(keystoreName, keystorePass, keyPass).signPdf(inputFilename, outputFilename);

			} else if (args[0].equals("create-testing")) {

				String outputFilename = (args.length > 1) ? args[1] : "poc.pdf";
				FileOutputStream out = new FileOutputStream(outputFilename);
				GeneratePdf.generateSimplePdf(out, signatureFieldName);

			} else if (args[0].equals("sign-testing")) {

				String inputFilename = (args.length > 1) ? args[1] : "poc.pdf";
				String outputFilename = (args.length > 2) ? args[2] : "poc-signed.pdf";

				String keystoreName = (args.length > 3) ? args[3] : "keystore";
				String keystorePassword = (args.length > 4) ? args[4] : "keystorePass";
				String keyPassword = (args.length > 5) ? args[5] : "keyPass";
				KeyStore ks = KeyStore.getInstance("JKS");
				ks.load(new FileInputStream(keystoreName), keystorePassword.toCharArray());
				String alias = (String) ks.aliases().nextElement();
				PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPassword.toCharArray());
				Certificate[] certificateChain = ks.getCertificateChain(alias);

				Signer signer = new Signer(certificateChain, privateKey);
				signer.signExistingField(new FileInputStream(inputFilename), new FileOutputStream(outputFilename),
						signatureFieldName, null, null);

			} else {

				System.err.println("Unknown command: " + args[0]);
				System.exit(-1);

			}
		} catch (IOException | DocumentException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

}
