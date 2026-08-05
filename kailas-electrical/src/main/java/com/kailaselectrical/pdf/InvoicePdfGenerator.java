package com.kailaselectrical.pdf;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Currency;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.kailaselectrical.entity.Invoice;
import com.kailaselectrical.entity.InvoiceItem;
import com.kailaselectrical.service.TranslationService;
import com.kailaselectrical.util.amountwords.AmountInWordsFactory;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoicePdfGenerator {
	
	private final TranslationService translationService;
	
	private final MessageSource messageSource;
	
	private final AmountInWordsFactory amountInWordsFactory;

	public byte[] generate(Invoice invoice) {
		
		try {
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			Document document = new Document();
			
			PdfWriter.getInstance(document, outputStream);
			
			document.open();
			
			ClassPathResource regularFont = new ClassPathResource("fonts/NotoSansDevanagari-Regular.ttf");
			
			BaseFont regularBase = BaseFont.createFont(
					regularFont.getPath(),
					BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			
			ClassPathResource bolddFont = new ClassPathResource("fonts/NotoSansDevanagari-Bold.ttf");
			
			BaseFont boldBase = BaseFont.createFont(
					bolddFont.getPath(),
					BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			
			Font companyFont = new Font(boldBase, 22);
			Font headingFont = new Font(boldBase, 14);
			Font normalFont = new Font(regularBase, 11);
			Font boldFont = new Font(boldBase, 11);
			
			
			Paragraph company = new Paragraph(message("company.name"), companyFont);			
			company.setAlignment(Element.ALIGN_CENTER);			
			document.add(company);
			
			Paragraph subtitle = new Paragraph(message("company.tagline"), normalFont);
			subtitle.setAlignment(Element.ALIGN_CENTER);
			document.add(subtitle);
			
			Paragraph address = new Paragraph(
					"Suryadarshan Bldg,. CIDCO Colony,Boisar, Tal. Dist. Palghar - 401501\n"
					+ "Mobile: " + message("company.mobile"),
					normalFont);
			address.setAlignment(Element.ALIGN_CENTER);
			document.add(address);
			
			document.add(new Paragraph("______________________________________________________________________________"));
			
			
			PdfPTable invoiceTable = new PdfPTable(2);
			

			DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
					.withLocale(LocaleContextHolder.getLocale());
			
			String formattedDate = invoice.getInvoiceDate().format(formatter);
			
			String paymentStatus = message("payment.status." + invoice.getPaymentStatus().name().toLowerCase());
			
			invoiceTable.setWidthPercentage(100);
			invoiceTable.setSpacingBefore(15);
			invoiceTable.setSpacingAfter(15);
			
			invoiceTable.addCell(new Phrase(message("invoice.number"), boldFont));
			invoiceTable.addCell(new Phrase(invoice.getInvoiceNumber(), normalFont));
			
			invoiceTable.addCell(new Phrase(message("invoice.date"), boldFont));
			
			invoiceTable.addCell(new Phrase(formattedDate, normalFont));
			
			invoiceTable.addCell(new Phrase(message("invoice.payment.status"), boldFont));
				
			invoiceTable.addCell(new Phrase(paymentStatus, normalFont));
			
			document.add(invoiceTable);
			
			
			
			
			PdfPTable customerTable = new PdfPTable(2);
			
			customerTable.setWidthPercentage(100);
			customerTable.setSpacingBefore(15);
			customerTable.setSpacingAfter(15);
			customerTable.setWidths(new float[] {30f, 70f});
			
			
			PdfPCell customerHeader = new PdfPCell(new Phrase(message("customer.details"), headingFont));
			
			customerHeader.setColspan(2);
			customerHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			customerHeader.setPadding(8);
			
			customerTable.addCell(customerHeader);
			
			customerTable.addCell(new Phrase(message("customer.name"), boldFont));
			customerTable.addCell(new Phrase(invoice.getBooking().getCustomer().getFullName(), normalFont));
			
			customerTable.addCell(new Phrase(message("customer.phone"), boldFont));
			customerTable.addCell(new Phrase(invoice.getBooking().getCustomer().getPhoneNumber(), normalFont));
			
			customerTable.addCell(new Phrase(message("customer.email"), boldFont));
			customerTable.addCell(new Phrase(invoice.getBooking().getCustomer().getEmail(), normalFont));
			
			customerTable.addCell(new Phrase(message("customer.address"), boldFont));
			customerTable.addCell(new Phrase(invoice.getBooking().getCustomer().getAddress(), normalFont));

			customerTable.addCell(new Phrase(message("booking.number"), boldFont));
			customerTable.addCell(new Phrase(invoice.getBooking().getBookingNumber(), normalFont));
			
			customerTable.addCell(new Phrase(message("booking.date"), boldFont));
			
			String bookingDate = invoice.getBooking().getBookingDate().format(formatter);
			
			customerTable.addCell(new Phrase(bookingDate, normalFont));
			
			document.add(customerTable);
			
			
			
			
			PdfPTable serviceTable = new PdfPTable(5);
			
			serviceTable.setWidthPercentage(100);
			serviceTable.setSpacingBefore(15);
			serviceTable.setSpacingAfter(20);
			serviceTable.setWidths(new float[] {8f, 50f, 10f, 15f, 17f});
			
			serviceTable.addCell(headerCell(message("serial.no"), boldFont));
			serviceTable.addCell(headerCell(message("service"), boldFont));
			serviceTable.addCell(headerCell(message("quantity"), boldFont));
			serviceTable.addCell(headerCell(message("unit.price"), boldFont));
			serviceTable.addCell(headerCell(message("total"), boldFont));
			
			int srNo = 1;

			NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
			currency.setCurrency(Currency.getInstance("INR"));
			
			for (InvoiceItem item : invoice.getInvoiceItems()) {
				
				String translatedName = translationService.getServiceName(item.getElectricalService());

			    serviceTable.addCell(String.valueOf(srNo++));

			    serviceTable.addCell(new Phrase(translatedName, normalFont));

			    serviceTable.addCell(String.valueOf(item.getQuantity()));

			    serviceTable.addCell(currency.format(item.getUnitPrice()));

			    serviceTable.addCell(currency.format(item.getLineTotal()));
			}


			serviceTable.addCell("");
			serviceTable.addCell("");
			serviceTable.addCell("");

			PdfPCell totalLabel = new PdfPCell(new Phrase(message("subtotal"), headingFont));
			totalLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
			serviceTable.addCell(totalLabel);
			
			PdfPCell totalAmount = new PdfPCell(new Phrase(currency.format(invoice.getSubtotal()), headingFont));
			totalAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
			serviceTable.addCell(totalAmount);
			
			document.add(serviceTable);
			
			
			Paragraph amountWords = new Paragraph(message("invoice.amount.words") + " : " +
					amountInWordsFactory.convert(invoice.getSubtotal()),
			        boldFont);
			
			amountWords.setSpacingBefore(15);
			amountWords.setSpacingAfter(15);
			
			document.add(amountWords);
			
			
			Paragraph footer = new Paragraph(
					message("footer.note") 
					+ "\n\n"
					+ message("footer.signature") 
					+ "\n"
					+ "Mobile: " + message("company.mobile"), 
		    normalFont);
			
			footer.setAlignment(Element.ALIGN_CENTER);
			
			footer.setSpacingBefore(40);
			
			document.add(footer);
			
			
			
			document.close();
			
			return outputStream.toByteArray();
			
		} catch (Exception e) {
			
			throw new RuntimeException("Unable to generate PDF", e);
		}
	}
	
	private static PdfPCell headerCell(String text, Font font) {
		
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		cell.setPadding(8);
		
		return cell;
	}
	
	private String message(String key) {
		
		return messageSource.getMessage(
				key,
				null,
				LocaleContextHolder.getLocale());
	}
}
