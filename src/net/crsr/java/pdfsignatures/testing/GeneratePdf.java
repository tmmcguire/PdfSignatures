package net.crsr.java.pdfsignatures.testing;

import java.io.OutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePdf {

	public static void generateSimplePdf(OutputStream out, String fieldName) throws DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, out);
		document.open();
		document.add(new Paragraph("Contract"));
		document.add(new Paragraph("Delbert \"Mittens\" Johnson agrees to transfer to the bearer all of the quatloos."));
		PdfFormField field = PdfFormField.createSignature(writer);
		field.setWidget(new Rectangle(72, 732, 400, 760), PdfAnnotation.HIGHLIGHT_INVERT);
		field.setFieldName(fieldName);
		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		field.setPage();
		field.setMKBorderColor(BaseColor.BLACK);
		field.setMKBackgroundColor(BaseColor.WHITE);
		PdfAppearance tp = PdfAppearance.createAppearance(writer, 72, 48);
		tp.rectangle(0.5f, 0.5f, 71.5f, 47.5f);
		tp.stroke();
		field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
		writer.addAnnotation(field);
		document.close();
	}
}
