package com.videonetics.report;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.videonetics.sku.logo.VLogo;

/**
 * Class to add a table as header and a two tables the footer.
 */
public class PdfHeaderFooter extends PdfPageEventHelper {
	/** The header text. */
	String reportTitle;
	String company;
	String contacts;
	/** The template with the total number of pages. */
	PdfTemplate total;

	Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLACK);
	Font otherFont = new Font(Font.FontFamily.HELVETICA);

	/**
	 * Allows us to change the content of the header.
	 * 
	 * @param header
	 *            The new header String
	 */
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	/**
	 * @param company
	 *            the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @param contacts
	 *            the contacts to set
	 */
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	/**
	 * Creates the PdfTemplate that will hold the total number of pages.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(30, 16);
	}

	/**
	 * Adds a header and footer to every page
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfPTable table = new PdfPTable(3);
		try {
			table.setWidths(new int[] { 10, 40, 10 });
			table.setTotalWidth(527);
			table.setLockedWidth(true);
			table.getDefaultCell().setFixedHeight(50);
			table.getDefaultCell().setBorder(Rectangle.BOTTOM);

			java.net.URL corporate_logo = new VLogo().getCorporateLogoURL();

			Image image = Image.getInstance(corporate_logo);
			image.scaleAbsolute(150, 50);
			table.addCell(image);

			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(new Paragraph(reportTitle, titleFont));

			table.addCell("");

			table.writeSelectedRows(0, -1, 34, 820, writer.getDirectContent());
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		} catch (IOException de) {
			throw new ExceptionConverter(de);
		} catch (Exception de) {
			throw new ExceptionConverter(de);
		}

		table = new PdfPTable(3);
		try {
			table.setWidths(new int[] { 24, 24, 2 });
			table.setTotalWidth(527);
			table.setLockedWidth(true);
			table.getDefaultCell().setFixedHeight(20);
			table.getDefaultCell().setBorder(Rectangle.TOP);
			table.addCell(new Paragraph(company, otherFont));

			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(new Paragraph(String.format("Page %d of", writer.getPageNumber()), otherFont));

			PdfPCell cell = new PdfPCell(Image.getInstance(total));
			cell.setBorder(Rectangle.TOP);
			table.addCell(cell);

			table.writeSelectedRows(0, -1, 34, 70, writer.getDirectContent());
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}

		table = new PdfPTable(3);
		try {
			table.setWidths(new int[] { 24, 2, 24 });
			table.setTotalWidth(527);
			table.setLockedWidth(true);
			table.getDefaultCell().setFixedHeight(20);
			table.getDefaultCell().setBorder(0);

			table.addCell(new Paragraph(contacts, otherFont));
			table.addCell(new Paragraph());
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(new Paragraph(PdfReporter.UNIQUE_IDENTIFIER, otherFont));

			table.writeSelectedRows(0, -1, 34, 40, writer.getDirectContent());
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	/**
	 * Fills out the total number of pages before the document is closed.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2,
				2, 0);
	}
}
