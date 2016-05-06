package net.crsr.java.pdfsignatures.signing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
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

public class Signer {
	private static final String					digestAlgorithm			= DigestAlgorithms.SHA256;
	private static final String					provider						= BouncyCastleProvider.PROVIDER_NAME;
	private static final CryptoStandard	cryptoStandard			= CryptoStandard.CMS;
	private static final int						certificationLevel	= PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED;

	Certificate[]												certificateChain;
	PrivateKey													privateKey;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public Signer(Certificate[] certificateChain, PrivateKey privateKey) {
		this.certificateChain = certificateChain;
		this.privateKey = privateKey;
	}

	public void signExistingField(InputStream in, OutputStream out, String fieldName, String reason, String location)
			throws IOException, DocumentException, GeneralSecurityException {
		// '\0' => Keep document's version.
		PdfStamper stamper = PdfStamper.createSignature(new PdfReader(in), out, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setCertificationLevel(certificationLevel);
		if (reason != null) {
			appearance.setReason(reason);
		}
		if (location != null) {
			appearance.setLocation(location);
		}
		appearance.setVisibleSignature(fieldName);
		addSignature(appearance);
	}

	public void signNewField(InputStream in, OutputStream out, int pageNumber, Rectangle rectangle, String reason,
			String location) throws IOException, DocumentException, GeneralSecurityException {
		// '\0' => Keep document's version.
		PdfStamper stamper = PdfStamper.createSignature(new PdfReader(in), out, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setCertificationLevel(certificationLevel);
		if (reason != null) {
			appearance.setReason(reason);
		}
		if (location != null) {
			appearance.setLocation(location);
		}
		// null => Automatically generate field name.
		appearance.setVisibleSignature(rectangle, pageNumber, null);
		addSignature(appearance);
	}

	private void addSignature(PdfSignatureAppearance appearance)
			throws IOException, DocumentException, GeneralSecurityException {
		ExternalDigest digest = new BouncyCastleDigest();
		ExternalSignature signature = new PrivateKeySignature(privateKey, digestAlgorithm, provider);
		MakeSignature.signDetached(appearance, digest, signature, certificateChain, null, null, null, 0, cryptoStandard);
	}
}
