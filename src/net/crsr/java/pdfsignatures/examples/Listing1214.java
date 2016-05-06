package net.crsr.java.pdfsignatures.examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

public class Listing1214 {
	String											keystorePath;
	String											keystorePassword;
	String											keyPassword;

	private static final String	digestAlgorithm	= DigestAlgorithms.SHA256;

	public Listing1214(String keystorePath, String keystorePassword, String keyPassword) {
		this.keystorePath = keystorePath;
		this.keystorePassword = keystorePassword;
		this.keyPassword = keyPassword;
	}

	public static void setupBouncyCastle() {
		Security.addProvider(new BouncyCastleProvider());
	}

	public void signPdf(String inputFilename, String outputFilename)
			throws FileNotFoundException, IOException, DocumentException, GeneralSecurityException {
		KeyStore ks = KeyStore.getInstance(/* "pkcs12" */ "JKS");
		ks.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());
		String alias = (String) ks.aliases().nextElement();
		PrivateKey pk = (PrivateKey) ks.getKey(alias, keyPassword.toCharArray());
		Certificate[] chain = ks.getCertificateChain(alias);
		String provider = BouncyCastleProvider.PROVIDER_NAME;
		// Creating the reader and the stamper
		PdfReader reader = new PdfReader(inputFilename);
		FileOutputStream os = new FileOutputStream(outputFilename);
		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		// Creating the appearance
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
		appearance.setReason("It's personal");
		appearance.setLocation("Foobar");
		appearance.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, null /* "mySig" */);
		// Creating the signature
		ExternalDigest digest = new BouncyCastleDigest();
		ExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, provider);
		MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, CryptoStandard.CMS);
	}
}
