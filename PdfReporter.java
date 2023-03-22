package com.videonetics.report;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.security.GraphicMode;
import com.spire.pdf.security.PdfCertificate;
import com.spire.pdf.security.PdfCertificationFlags;
import com.spire.pdf.security.PdfSignature;

public class PdfReporter {
	private static final Logger LOGGER= Logger.getLogger(PdfReporter.class.getSimpleName());

    public static String UNIQUE_IDENTIFIER = "";

    public static String createPdf(String fullDestination, String reportTitle, String[] details, String company, String contacts,
            Map<String, String> bodyContent, List<String> tableColoumName, Map<String, String> getterNreturntype,
            List<?> tableContent, String userPassword, String ownerPassword, String certificatePassword) throws MalformedURLException, IOException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, DocumentException {

       
        Rectangle pageSize = new Rectangle(PageSize.A4);

        Document document = new Document(pageSize, 0, 0, 90, 70);

        File destinationFile = new File(fullDestination);
        String parentPath = destinationFile.getParent();
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFile));
        if (userPassword != null && ownerPassword != null && userPassword.length() > 0 && ownerPassword.length() > 0) {
                writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            }
        

        PdfHeaderFooter eventHeaderFooter = new PdfHeaderFooter();
        eventHeaderFooter.setReportTitle("");
        eventHeaderFooter.setCompany(company);
        eventHeaderFooter.setContacts(contacts);
        writer.setPageEvent(eventHeaderFooter);

        document.open();

        PdfPTable titlePdfTable = new PdfPTable(1);

        PdfPCell titleTablecell = new PdfPCell(new Phrase(reportTitle, FontFactory.getFont("Arial", 14.0f, Font.BOLD)));
        titleTablecell.setBorder(0);
        titleTablecell.setHorizontalAlignment(Element.ALIGN_CENTER);

        titlePdfTable.addCell(titleTablecell);
        
        document.add(titlePdfTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        
        PdfPTable cameraDetailsPdfTable = new PdfPTable(1);

        PdfPCell cameraDetailsTablecell = new PdfPCell(new Phrase(details[0] + details[1] + details[2], FontFactory.getFont("Arial", 10.0f, Font.BOLD)));
        cameraDetailsTablecell.setBorder(0);
        cameraDetailsTablecell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cameraDetailsPdfTable.addCell(cameraDetailsTablecell);

        document.add(cameraDetailsPdfTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);       

        PdfPTable headerPdfTable = new PdfPTable(2);

        if ((bodyContent.size() % 2) == 1) {
            bodyContent.put(null, null);
        }

        PdfPCell headerTablecell;

        int count = 0;
        Set<Entry<String, String>> entires = bodyContent.entrySet();
        for (Entry<String, String> ent : entires) {

            if (ent.getKey() == null || ent.getValue() == null) {
                headerTablecell = new PdfPCell(new Paragraph());
            } else {
                headerTablecell = new PdfPCell(new Phrase(ent.getKey() + "  :  " + ent.getValue(), FontFactory.getFont("Arial", 12.0f, Font.NORMAL)));
            }

            headerTablecell.setBorder(0);

            headerTablecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (count > 1) {
                headerTablecell.setPaddingLeft(5);
            } else {
                headerTablecell.setPaddingLeft(20);
            }

            headerPdfTable.addCell(headerTablecell);

            count++;
        }

        document.add(headerPdfTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        PdfPTable pSpaceTable = new PdfPTable(3);
        PdfPCell spacecell = new PdfPCell(new Paragraph(" "));
        spacecell.setColspan(3);
        spacecell.setBorder(0);
        pSpaceTable.addCell(spacecell);
        document.add(pSpaceTable);
        document.add(Chunk.NEWLINE);

        int headercolsCount = tableColoumName.size();

        Font fontTablecontentbody = FontFactory.getFont("Arial", 6.0f, Font.NORMAL);


        Font fontTablecontent = FontFactory.getFont("Arial", 8.0f, Font.NORMAL);

        PdfPTable headerOfTable = new PdfPTable(headercolsCount + 1);

        PdfPCell headerOfTablecell;
        PdfPCell headerOfTableSNcell;
        headerOfTableSNcell = new PdfPCell(new Phrase("SL", fontTablecontent));
        headerOfTableSNcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerOfTableSNcell.setGrayFill(0.75f);
        headerOfTable.addCell(headerOfTableSNcell);

        for (int i = 0; i < headercolsCount; i++) {

            headerOfTablecell = new PdfPCell(new Phrase(tableColoumName.get(i), fontTablecontent));

            headerOfTablecell.setPaddingLeft(10);

            headerOfTablecell.setGrayFill(0.75f);

            headerOfTablecell.setHorizontalAlignment(Element.ALIGN_CENTER);

            headerOfTable.addCell(headerOfTablecell);

        }
        document.add(headerOfTable);

        @SuppressWarnings("unused")
        Font fontValue = new Font(Font.FontFamily.COURIER, 8, Font.ITALIC);

        PdfPTable addValueTable = new PdfPTable(headercolsCount + 1);

        PdfPCell addValueCell;

        int countSerial = 1;

        Iterator<?> itaddvalue = tableContent.iterator();
        while (itaddvalue.hasNext()) {
            PdfPCell addValueSNCell = new PdfPCell(new Phrase(Integer.toString(countSerial), fontTablecontentbody));
            addValueSNCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            addValueTable.addCell(addValueSNCell);
            countSerial++;
            Object row = itaddvalue.next();
            Class<?> cl = row.getClass();
            Set<Entry<String, String>> entiresMethodNReturnType = getterNreturntype.entrySet();
            for (Entry<String, String> ent : entiresMethodNReturnType) {
                Method m = cl.getMethod(ent.getKey());
                Object obj = m.invoke(row);
                Class<?> cls = Class.forName(ent.getValue());

                if (cls.cast(obj) instanceof Font) {
                    fontTablecontentbody = (Font) cls.cast(obj);
                } else if (cls.cast(obj) instanceof BaseColor) {

                    fontTablecontentbody = FontFactory.getFont("Arial", 6.0f, Font.NORMAL, (BaseColor) cls.cast(obj));
                } else if (cls.cast(obj) instanceof List) {
                    @SuppressWarnings("unchecked")
                    ArrayList<byte[]> arrOfbyte = (ArrayList<byte[]>) cls.cast(obj);
                    if (arrOfbyte.size() == 0) {
                        addValueTable.addCell(new Phrase(arrOfbyte.size()));
                    } else if (arrOfbyte.size() == 1) {
                        addValueTable.addCell(Image.getInstance(arrOfbyte.get(0)));
                    } else {
                        addValueCell = new PdfPCell(new Phrase(arrOfbyte.size() + "", fontTablecontentbody));
                        addValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        addValueTable.addCell(addValueCell);

                        int size = arrOfbyte.size();

                        for (int i = 0; i < size; i++) {
                            Image imageOftable = Image.getInstance(arrOfbyte.get(i));
                            imageOftable.scaleToFit(35, 80);
                            addValueCell = new PdfPCell(imageOftable);
                            addValueCell.setPadding(3f);
                            addValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                            if (i % (headercolsCount + 1) == 0) {

                                addValueCell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
                            } else if ((i) % (headercolsCount + 1) == headercolsCount) {

                                addValueCell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);

                            } else {
                                addValueCell.setBorder(Rectangle.BOTTOM);
                            }

                            addValueTable.addCell(addValueCell);
                        }
                        int left = 0;
                        if (size < (headercolsCount + 1) && size > 0) {
                            left = (headercolsCount + 1) - size;
                        } else {
                            left = size % ((headercolsCount + 1) + 1);
                        }
                        

                        for (int i = 1; i <= left; i++) {
                            addValueCell = new PdfPCell(new Phrase());

                            if (i == left) {
                                addValueCell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
                            } else {
                                addValueCell.setBorder(Rectangle.BOTTOM);
                            }
                            addValueTable.addCell(addValueCell);
                        }

                    }
                } else {
                    fontTablecontentbody = FontFactory.getFont("Arial", 6.0f, Font.NORMAL);
                    if (cls.cast(obj) != null) {
                        addValueCell = new PdfPCell(new Paragraph(cls.cast(obj).toString(), fontTablecontentbody));

                    } else {
                        addValueCell = new PdfPCell(new Paragraph("", fontTablecontentbody));

                    }

                    addValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    addValueTable.addCell(addValueCell);
                }

            }
        }

        document.add(addValueTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        PdfPTable disclaimerTitlePdfTable = new PdfPTable(1);

        PdfPCell disclaimerTitleTablecell = new PdfPCell(new Phrase("Disclaimer:", FontFactory.getFont("Arial", 9.0f, Font.BOLD)));
        disclaimerTitleTablecell.setBorder(0);
        disclaimerTitleTablecell.setHorizontalAlignment(Element.ALIGN_LEFT);

        disclaimerTitlePdfTable.addCell(disclaimerTitleTablecell);

        document.add(disclaimerTitlePdfTable);
        document.add(Chunk.NEWLINE);

        PdfPTable disclaimerPdfTable = new PdfPTable(1);

        PdfPCell disclaimerTablecell = new PdfPCell(new Phrase("This is a confidential report and should not be shared without the consent of the owner.",
                FontFactory.getFont("Arial", 9.0f, Font.NORMAL)));
        disclaimerTablecell.setBorder(0);
        disclaimerTablecell.setHorizontalAlignment(Element.ALIGN_LEFT);

        disclaimerPdfTable.addCell(disclaimerTablecell);

        document.add(disclaimerPdfTable);

        document.close();
        writer.close();

        setSignature(fullDestination, userPassword, certificatePassword);
        
        return destinationFile.getAbsolutePath();

    }

    public static String createPdf(String fullDestination, String reportTitle, String company, String contacts,
            Map<String, String> bodyContent, List<String> tableColoumName, Map<String, String> getterNreturntype,
            List<?> tableContent, String userPassword, String ownerPassword, String certificatePassword) throws MalformedURLException, IOException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, DocumentException {

       
        Rectangle pageSize = new Rectangle(PageSize.A4);

        Document document = new Document(pageSize, 0, 0, 90, 70);

        File destinationFile = new File(fullDestination);
        String parentPath = destinationFile.getParent();
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFile));
        if (userPassword != null && ownerPassword != null && userPassword.length() > 0 && ownerPassword.length() > 0) {
                writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            }
        

        PdfHeaderFooter eventHeaderFooter = new PdfHeaderFooter();
        eventHeaderFooter.setReportTitle("");
        eventHeaderFooter.setCompany(company);
        eventHeaderFooter.setContacts(contacts);
        writer.setPageEvent(eventHeaderFooter);

        document.open();

        PdfPTable titlePdfTable = new PdfPTable(1);

        PdfPCell titleTablecell = new PdfPCell(new Phrase(reportTitle, FontFactory.getFont("Arial", 14.0f, Font.BOLD)));
        titleTablecell.setBorder(0);
        titleTablecell.setHorizontalAlignment(Element.ALIGN_CENTER);

        titlePdfTable.addCell(titleTablecell);

        document.add(titlePdfTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        PdfPTable headerPdfTable = new PdfPTable(2);

        if ((bodyContent.size() % 2) == 1) {
            bodyContent.put(null, null);
        }

        PdfPCell headerTablecell;

        int count = 0;
        Set<Entry<String, String>> entires = bodyContent.entrySet();
        for (Entry<String, String> ent : entires) {

            if (ent.getKey() == null || ent.getValue() == null) {
                headerTablecell = new PdfPCell(new Paragraph());
            } else {
                headerTablecell = new PdfPCell(new Phrase(ent.getKey() + "  :  " + ent.getValue(), FontFactory.getFont("Arial", 12.0f, Font.NORMAL)));
            }

            headerTablecell.setBorder(0);

            headerTablecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (count > 1) {
                headerTablecell.setPaddingLeft(5);
            } else {
                headerTablecell.setPaddingLeft(20);
            }

            headerPdfTable.addCell(headerTablecell);

            count++;
        }

        document.add(headerPdfTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        PdfPTable pSpaceTable = new PdfPTable(3);
        PdfPCell spacecell = new PdfPCell(new Paragraph(" "));
        spacecell.setColspan(3);
        spacecell.setBorder(0);
        pSpaceTable.addCell(spacecell);
        document.add(pSpaceTable);
        document.add(Chunk.NEWLINE);

        int headercolsCount = tableColoumName.size();

        Font fontTablecontentbody = FontFactory.getFont("Arial", 6.0f, Font.NORMAL);


        Font fontTablecontent = FontFactory.getFont("Arial", 8.0f, Font.NORMAL);

        PdfPTable headerOfTable = new PdfPTable(headercolsCount + 1);

        PdfPCell headerOfTablecell;
        PdfPCell headerOfTableSNcell;
        headerOfTableSNcell = new PdfPCell(new Phrase("SL", fontTablecontent));
        headerOfTableSNcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerOfTableSNcell.setGrayFill(0.75f);
        headerOfTable.addCell(headerOfTableSNcell);

        for (int i = 0; i < headercolsCount; i++) {

            headerOfTablecell = new PdfPCell(new Phrase(tableColoumName.get(i), fontTablecontent));

            headerOfTablecell.setPaddingLeft(10);

            headerOfTablecell.setGrayFill(0.75f);

            headerOfTablecell.setHorizontalAlignment(Element.ALIGN_CENTER);

            headerOfTable.addCell(headerOfTablecell);

        }
        document.add(headerOfTable);

        @SuppressWarnings("unused")
        Font fontValue = new Font(Font.FontFamily.COURIER, 8, Font.ITALIC);

        PdfPTable addValueTable = new PdfPTable(headercolsCount + 1);

        PdfPCell addValueCell;

        int countSerial = 1;

        Iterator<?> itaddvalue = tableContent.iterator();
        while (itaddvalue.hasNext()) {
            PdfPCell addValueSNCell = new PdfPCell(new Phrase(Integer.toString(countSerial), fontTablecontentbody));
            addValueSNCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            addValueTable.addCell(addValueSNCell);
            countSerial++;
            Object row = itaddvalue.next();
            Class<?> cl = row.getClass();
            Set<Entry<String, String>> entiresMethodNReturnType = getterNreturntype.entrySet();
            for (Entry<String, String> ent : entiresMethodNReturnType) {
                Method m = cl.getMethod(ent.getKey());
                Object obj = m.invoke(row);
                Class<?> cls = Class.forName(ent.getValue());

                if (cls.cast(obj) instanceof Font) {
                    fontTablecontentbody = (Font) cls.cast(obj);
                } else if (cls.cast(obj) instanceof BaseColor) {

                    fontTablecontentbody = FontFactory.getFont("Arial", 6.0f, Font.NORMAL, (BaseColor) cls.cast(obj));
                } else if (cls.cast(obj) instanceof List) {
                    @SuppressWarnings("unchecked")
                    ArrayList<byte[]> arrOfbyte = (ArrayList<byte[]>) cls.cast(obj);
                    if (arrOfbyte.size() == 0) {
                        addValueTable.addCell(new Phrase(arrOfbyte.size()));
                    } else if (arrOfbyte.size() == 1) {
                        addValueTable.addCell(Image.getInstance(arrOfbyte.get(0)));
                    } else {
                        addValueCell = new PdfPCell(new Phrase(arrOfbyte.size() + "", fontTablecontentbody));
                        addValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        addValueTable.addCell(addValueCell);

                        int size = arrOfbyte.size();

                        for (int i = 0; i < size; i++) {
                            Image imageOftable = Image.getInstance(arrOfbyte.get(i));
                            imageOftable.scaleToFit(35, 80);
                            addValueCell = new PdfPCell(imageOftable);
                            addValueCell.setPadding(3f);
                            addValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                            if (i % (headercolsCount + 1) == 0) {

                                addValueCell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
                            } else if ((i) % (headercolsCount + 1) == headercolsCount) {

                                addValueCell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);

                            } else {
                                addValueCell.setBorder(Rectangle.BOTTOM);
                            }

                            addValueTable.addCell(addValueCell);
                        }
                        int left = 0;
                        if (size < (headercolsCount + 1) && size > 0) {
                            left = (headercolsCount + 1) - size;
                        } else {
                            left = size % ((headercolsCount + 1) + 1);
                        }
                        

                        for (int i = 1; i <= left; i++) {
                            addValueCell = new PdfPCell(new Phrase());

                            if (i == left) {
                                addValueCell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
                            } else {
                                addValueCell.setBorder(Rectangle.BOTTOM);
                            }
                            addValueTable.addCell(addValueCell);
                        }

                    }
                } else {
                    fontTablecontentbody = FontFactory.getFont("Arial", 6.0f, Font.NORMAL);
                    if (cls.cast(obj) != null) {
                        addValueCell = new PdfPCell(new Paragraph(cls.cast(obj).toString(), fontTablecontentbody));

                    } else {
                        addValueCell = new PdfPCell(new Paragraph("", fontTablecontentbody));

                    }

                    addValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    addValueTable.addCell(addValueCell);
                }

            }
        }

        document.add(addValueTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        PdfPTable disclaimerTitlePdfTable = new PdfPTable(1);

        PdfPCell disclaimerTitleTablecell = new PdfPCell(new Phrase("Disclaimer:", FontFactory.getFont("Arial", 9.0f, Font.BOLD)));
        disclaimerTitleTablecell.setBorder(0);
        disclaimerTitleTablecell.setHorizontalAlignment(Element.ALIGN_LEFT);

        disclaimerTitlePdfTable.addCell(disclaimerTitleTablecell);

        document.add(disclaimerTitlePdfTable);
        document.add(Chunk.NEWLINE);

        PdfPTable disclaimerPdfTable = new PdfPTable(1);

        PdfPCell disclaimerTablecell = new PdfPCell(new Phrase("This is a confidential report and should not be shared without the consent of the owner.",
                FontFactory.getFont("Arial", 9.0f, Font.NORMAL)));
        disclaimerTablecell.setBorder(0);
        disclaimerTablecell.setHorizontalAlignment(Element.ALIGN_LEFT);

        disclaimerPdfTable.addCell(disclaimerTablecell);

        document.add(disclaimerPdfTable);

        document.close();
        writer.close();

        setSignature(fullDestination, userPassword, certificatePassword);
        
        return destinationFile.getAbsolutePath();

    }

    public static String createPdf(String fullDestination, String reportTitle, String company, String contacts,
            Map<String, String> bodyContent, List<String> tableColoumName, Map<String, String> getterNreturntype,
            List<?> tableContent, List<String> summaryContent) throws MalformedURLException, IOException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, DocumentException {
        return createPdf(fullDestination, reportTitle, company, contacts, bodyContent, tableColoumName, getterNreturntype, tableContent, summaryContent, null, null, null);
    }

    public static String createPdf(String fullDestination, String reportTitle, String company, String contacts,
            Map<String, String> bodyContent, List<String> tableColoumName, Map<String, String> getterNreturntype,
            List<?> tableContent, List<String> summaryContent, String userPassword, String ownerPassword, String certificatePassword) throws MalformedURLException, IOException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, DocumentException {

       
        Rectangle pageSize = new Rectangle(PageSize.A4);


        Document document = new Document(pageSize, 0, 0, 90, 70);

        File destinationFile = new File(fullDestination);
        String parentPath = destinationFile.getParent();
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFile));
        if (userPassword != null && ownerPassword != null && userPassword.length() > 0 && ownerPassword.length() > 0) {
                writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            }
        

        PdfHeaderFooter eventHeaderFooter = new PdfHeaderFooter();
        eventHeaderFooter.setReportTitle("");
        eventHeaderFooter.setCompany(company);
        eventHeaderFooter.setContacts(contacts);
        writer.setPageEvent(eventHeaderFooter);

        document.open();

        PdfPTable titlePdfTable = new PdfPTable(1);

        PdfPCell titleTablecell = new PdfPCell(new Phrase(reportTitle, FontFactory.getFont("Arial", 14.0f, Font.BOLD)));
        titleTablecell.setBorder(0);
        titleTablecell.setHorizontalAlignment(Element.ALIGN_CENTER);

        titlePdfTable.addCell(titleTablecell);

        document.add(titlePdfTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        PdfPTable headerPdfTable = new PdfPTable(2);

        if ((bodyContent.size() % 2) == 1) {
            bodyContent.put(null, null);
        }

        PdfPCell headerTablecell;

        Set<Entry<String, String>> entires = bodyContent.entrySet();
        for (Entry<String, String> ent : entires) {

            if (ent.getKey() == null || ent.getValue() == null) {
                headerTablecell = new PdfPCell(new Paragraph());
            } else {
                headerTablecell = new PdfPCell(new Paragraph(ent.getKey() + "  :  " + ent.getValue()));
            }

            headerTablecell.setBorder(0);
            headerTablecell.setPaddingLeft(10);
            headerTablecell.setHorizontalAlignment(Element.ALIGN_LEFT);

            headerPdfTable.addCell(headerTablecell);
        }

        document.add(headerPdfTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        PdfPTable pSpaceTable = new PdfPTable(3);
        PdfPCell spacecell = new PdfPCell(new Paragraph(" "));
        spacecell.setColspan(3);
        spacecell.setBorder(0);
        pSpaceTable.addCell(spacecell);
        document.add(pSpaceTable);
        document.add(Chunk.NEWLINE);

        int headercolsCount = tableColoumName.size();

        Font fontTablecontentbody = FontFactory.getFont("Arial", 6.0f, Font.NORMAL);


        Font fontTablecontent = FontFactory.getFont("Arial", 8.0f, Font.NORMAL);

        @SuppressWarnings("unused")
        Font fontValue = new Font(Font.FontFamily.COURIER, 8, Font.ITALIC);

        PdfPTable addValueTable = new PdfPTable(headercolsCount + 1);

        PdfPCell headerOfTablecell;
        PdfPCell headerOfTableSNcell;
        headerOfTableSNcell = new PdfPCell(new Phrase("SL", fontTablecontent));
        headerOfTableSNcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerOfTableSNcell.setGrayFill(0.75f);
        addValueTable.addCell(headerOfTableSNcell);

        for (int i = 0; i < headercolsCount; i++) {

            headerOfTablecell = new PdfPCell(new Phrase(tableColoumName.get(i), fontTablecontent));

            headerOfTablecell.setPaddingLeft(10);

            headerOfTablecell.setGrayFill(0.75f);

            headerOfTablecell.setHorizontalAlignment(Element.ALIGN_CENTER);

            addValueTable.addCell(headerOfTablecell);

        }

        addValueTable.setHeaderRows(1);

        PdfPCell addValueCell;

        int countSerial = 1;

        Iterator<?> itaddvalue = tableContent.iterator();
        while (itaddvalue.hasNext()) {
            PdfPCell addValueSNCell = new PdfPCell(new Phrase(Integer.toString(countSerial), fontTablecontentbody));
            addValueSNCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            addValueTable.addCell(addValueSNCell);
            countSerial++;
            Object row = itaddvalue.next();
            Class<?> cl = row.getClass();
            Set<Entry<String, String>> entiresMethodNReturnType = getterNreturntype.entrySet();
            for (Entry<String, String> ent : entiresMethodNReturnType) {
                Method m = cl.getMethod(ent.getKey());
                Object obj = m.invoke(row);
                Class<?> cls = Class.forName(ent.getValue());

                if (cls.cast(obj) instanceof Font) {
                    fontTablecontentbody = (Font) cls.cast(obj);
                } else if (cls.cast(obj) instanceof BaseColor) {
                    fontTablecontentbody = FontFactory.getFont("Arial", 6.0f, Font.NORMAL, (BaseColor) cls.cast(obj));

                } else if (cls.cast(obj) instanceof List) {
                    @SuppressWarnings("unchecked")
                    ArrayList<byte[]> arrOfbyte = (ArrayList<byte[]>) cls.cast(obj);
                    if (arrOfbyte.size() == 0) {
                        addValueTable.addCell(new Phrase(arrOfbyte.size()));
                    } else if (arrOfbyte.size() == 1) {
                        addValueTable.addCell(Image.getInstance(arrOfbyte.get(0)));
                    } else {
                        addValueCell = new PdfPCell(new Phrase(arrOfbyte.size() + "", fontTablecontentbody));
                        addValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        addValueTable.addCell(addValueCell);

                        int size = arrOfbyte.size();

                        for (int i = 0; i < size; i++) {
                            Image imageOftable = Image.getInstance(arrOfbyte.get(i));
                            imageOftable.scaleToFit(35, 80);
                            addValueCell = new PdfPCell(imageOftable);
                            addValueCell.setPadding(3f);
                            addValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                            if (i % (headercolsCount + 1) == 0) {

                                addValueCell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
                            } else if ((i) % (headercolsCount + 1) == headercolsCount) {

                                addValueCell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);

                            } else {
                                addValueCell.setBorder(Rectangle.BOTTOM);
                            }

                            addValueTable.addCell(addValueCell);
                        }
                        int left = 0;
                        if (size < (headercolsCount + 1) && size > 0) {
                            left = (headercolsCount + 1) - size;
                        } else {
                            left = size % ((headercolsCount + 1) + 1);
                        }
                       

                        for (int i = 1; i <= left; i++) {
                            addValueCell = new PdfPCell(new Phrase());

                            if (i == left) {
                                addValueCell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
                            } else {
                                addValueCell.setBorder(Rectangle.BOTTOM);
                            }
                            addValueTable.addCell(addValueCell);
                        }

                    }
                } else {
                    fontTablecontentbody = FontFactory.getFont("Arial", 6.0f, Font.NORMAL);
                    if (cls.cast(obj) != null) {
                        addValueCell = new PdfPCell(new Paragraph(cls.cast(obj).toString(), fontTablecontentbody));

                    } else {
                        addValueCell = new PdfPCell(new Paragraph("", fontTablecontentbody));

                    }

                    addValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    addValueTable.addCell(addValueCell);
                }

            }
        }

        document.add(addValueTable);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        PdfPTable summaryValueTable = new PdfPTable(2);
        fontTablecontent = FontFactory.getFont("Arial", 9.0f, Font.BOLD);

        PdfPCell summaryOfTablecell;

        for (int i = 0; i < summaryContent.size(); i++) {
            summaryOfTablecell = new PdfPCell(new Phrase(summaryContent.get(i), fontTablecontent));
            summaryOfTablecell.disableBorderSide(Rectangle.BOX);
            summaryOfTablecell.setPaddingRight(40);
            summaryOfTablecell.setGrayFill(0.80f);
            summaryOfTablecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            summaryValueTable.addCell(summaryOfTablecell);
        }

        document.add(summaryValueTable);

        PdfPTable disclaimerTitlePdfTable = new PdfPTable(1);

        PdfPCell disclaimerTitleTablecell = new PdfPCell(new Phrase("Disclaimer:", FontFactory.getFont("Arial", 9.0f, Font.BOLD)));
        disclaimerTitleTablecell.setBorder(0);
        disclaimerTitleTablecell.setHorizontalAlignment(Element.ALIGN_LEFT);

        disclaimerTitlePdfTable.addCell(disclaimerTitleTablecell);

        document.add(disclaimerTitlePdfTable);
        document.add(Chunk.NEWLINE);

        PdfPTable disclaimerPdfTable = new PdfPTable(1);

        PdfPCell disclaimerTablecell = new PdfPCell(new Phrase("This is a confidential report and should not be shared without the consent of the owner.",
                FontFactory.getFont("Arial", 9.0f, Font.NORMAL)));
        disclaimerTablecell.setBorder(0);
        disclaimerTablecell.setHorizontalAlignment(Element.ALIGN_LEFT);

        disclaimerPdfTable.addCell(disclaimerTablecell);

        document.add(disclaimerPdfTable);

        document.close();
        writer.close();

        setSignature(fullDestination, userPassword, certificatePassword);
        
        return destinationFile.getAbsolutePath();

    }

    public static void setSignature(String fileName, String filePassword, String certificatePassword){

    	PdfDocument doc = null;
    	try {
    		doc = new PdfDocument();
    		if (filePassword != null && filePassword.length() > 0) {
    			doc.loadFromFile(fileName, filePassword);
    		}else{
    			doc.loadFromFile(fileName);
    		}

    		// Get the first Page
    		PdfPageBase page = doc.getPages().get(0);
    		Rectangle2D.Float rec = new Rectangle2D.Float(330, 2, 220, 70);

    		PdfCertificate certificate = new PdfCertificate("lib/export/ivmsexport.pfx", certificatePassword);
    		PdfSignature signature = new PdfSignature(doc, page, certificate, "Videonetics", rec);
    		signature.setNameLabel("Signer:  ");
    		signature.setName("Videonetics");
    		signature.setDateLabel("Date:  ");
    		signature.setDate(new Date());
    		signature.setGraphicMode(GraphicMode.Sign_Name_And_Sign_Detail);
    		signature.setDocumentPermissions(PdfCertificationFlags.Allow_Form_Fill);
    		doc.saveToFile(fileName);

    		
    	} catch (Exception e) {
    		LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
    	} finally{
    		if(doc != null){
    			try { doc.close(); } catch (Exception e) {
    				LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
    			}
    		}
    	}
    }
    
}
