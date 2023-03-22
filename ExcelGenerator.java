package com.videonetics.report;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.crypt.dsig.SignatureConfig;
import org.apache.poi.poifs.crypt.dsig.SignatureInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.videonetics.sku.logo.VLogo;


public class ExcelGenerator {
	
	private static final Logger LOGGER= Logger.getLogger(ExcelGenerator.class.getSimpleName());


    @SuppressWarnings({"unused", "resource", "deprecation"})
    public static String createExcel(String fileName, String reportTitle, Map<String, String> bodycontent, List<String> tableColumnName, Map<String, String> getterNReturn, List<?> tableContent)
            throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        File statisticFile = new File(fileName);
        String parentPath = statisticFile.getParent();
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        CellStyle header_style_center, header_style_left_body, header_style_right_body, header_style_left_Space, header_style_right_Space;

        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet(reportTitle);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        Font font = workBook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_center = workBook.createCellStyle();
        header_style_center.setFont(font);
        header_style_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_center.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        header_style_center.setAlignment(CellStyle.ALIGN_CENTER);
        header_style_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_center.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center.setBorderTop(CellStyle.BORDER_THIN);
        header_style_center.setBorderRight(CellStyle.BORDER_THIN);
        header_style_center.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_left_body = workBook.createCellStyle();
        header_style_left_body.setFont(font);
        header_style_left_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_body.setFillForegroundColor(new HSSFColor.CORAL().getIndex());
        header_style_left_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_left_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_left_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderLeft(CellStyle.BORDER_THIN);
        header_style_left_Space = workBook.createCellStyle();

        header_style_left_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_Space.setFillForegroundColor(new HSSFColor.CORAL().getIndex());
        header_style_left_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_Space.setBorderTop(CellStyle.BORDER_THIN);

        header_style_right_body = workBook.createCellStyle();
        header_style_right_body.setFont(font);
        header_style_right_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_body.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_right_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_right_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_Space = workBook.createCellStyle();
        header_style_right_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_Space.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_Space.setBorderTop(CellStyle.BORDER_THIN);

        HSSFCellStyle body_style = workBook.createCellStyle();
        font = workBook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        body_style.setFont(font);
        body_style.setAlignment(CellStyle.ALIGN_CENTER);
        body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        body_style.setBorderBottom(CellStyle.BORDER_THIN);
        body_style.setBorderTop(CellStyle.BORDER_THIN);
        body_style.setBorderRight(CellStyle.BORDER_THIN);
        body_style.setBorderLeft(CellStyle.BORDER_THIN);

        HSSFCellStyle headerCellStyle = workBook.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(new HSSFColor.LIGHT_GREEN().getIndex());
        
        
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment((short) 10);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THICK);
        headerCellStyle.setBorderTop(CellStyle.BORDER_THICK);
        headerCellStyle.setBorderRight(CellStyle.BORDER_THICK);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_THICK);
        HSSFFont hSSFFont = workBook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 20);
        hSSFFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.BLACK.index);
        headerCellStyle.setFont(hSSFFont);

        int headerRow = 0;
        {

            HSSFRow titleRow = sheet.createRow(headerRow++);
            titleRow.setHeightInPoints(100);
            HSSFCell[] firsttitleCell = new HSSFCell[tableColumnName.size() + 1];
            firsttitleCell[0] = titleRow.createCell(0);

            BufferedImage originalImageByteHeader = new VLogo().getCorporateLogo();
            ByteArrayOutputStream baosByteheader = new ByteArrayOutputStream();
            ImageIO.write(originalImageByteHeader, "jpg", baosByteheader);
            baosByteheader.flush();
            byte[] imageInByteSingleHeader = baosByteheader.toByteArray();
            baosByteheader.close();

            HSSFPatriarch patriarchHeader = sheet.createDrawingPatriarch();

            HSSFClientAnchor anchorImage = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 1, 1);

            int imageIndexHeader = workBook.addPicture(imageInByteSingleHeader, HSSFWorkbook.PICTURE_TYPE_JPEG);

            patriarchHeader.createPicture(anchorImage, imageIndexHeader);

            firsttitleCell[1] = titleRow.createCell(1);
            firsttitleCell[1].setCellValue(reportTitle);
            firsttitleCell[1].setCellStyle(headerCellStyle);
            sheet.addMergedRegion(CellRangeAddress.valueOf("$B$1:$E$1"));

            headerRow++;
            HSSFRow spacerow = sheet.createRow(headerRow);

            int noOfRowInBody = bodycontent.size() / 2;
            int noOfCellInBody = noOfRowInBody * 2;

            int tableheaderNo = tableColumnName.size();
            int noOfHeaderNeed = tableheaderNo + 1;
            int noOfHeaderForFirstCol = (noOfHeaderNeed) / 2;
            int noOfHeaderForSecCol = noOfHeaderNeed - noOfHeaderForFirstCol;

            Set<String> keySet = bodycontent.keySet();

            List<String> list = new ArrayList<String>(keySet);
            HSSFRow row = null;
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                String value = bodycontent.get(key);
                
                if (i % 2 == 0) {

                    headerRow++;
                    row = sheet.createRow(headerRow);
                    HSSFCell[] cells = new HSSFCell[noOfHeaderNeed];

                    for (int l = 0; l < noOfHeaderForFirstCol; l++) {
                        cells[l] = row.createCell(l);
                        cells[l].setCellValue(" ");
                       

                    }
                    cells[(noOfHeaderForFirstCol / 2)].setCellValue(key + "  :  " + value);
                    cells[(noOfHeaderForFirstCol / 2)].setCellStyle(header_style_left_body);

                } else if (i % 2 == 1) {
                    HSSFCell[] cell = new HSSFCell[noOfHeaderNeed];
                    for (int t = noOfHeaderForFirstCol; t < noOfHeaderNeed; t++) {
                        cell[t] = row.createCell(t);
                        cell[t].setCellValue(" ");
                       

                    }

                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellValue(key + "  :  " + value);
                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellStyle(header_style_right_body);

                }
            }

            int tableColumnCount = tableColumnName.size();

            headerRow++;
            headerRow++;
            HSSFRow columnrow = sheet.createRow(headerRow);

            columnrow.setHeightInPoints(20);
            HSSFCell[] cellsInTableheader = new HSSFCell[tableColumnCount + 1];
            int count = 0;
            Iterator<String> it = tableColumnName.iterator();
            while (it.hasNext()) {
                if (count == 0) {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue("SL");
                    cellsInTableheader[count].setCellStyle(header_style_center);

                    sheet.setColumnWidth(count, 8000);

                    count++;
                } else {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue(it.next());
                    cellsInTableheader[count].setCellStyle(header_style_center);
                    sheet.setColumnWidth(count, 8000);
                    count++;
                }
            }

            int countNoOfRow = 0;

            Iterator<?> itaddvalue = tableContent.iterator();
            while (itaddvalue.hasNext()) {

                int columnNo = 0;
                headerRow++;
                HSSFRow datarow = sheet.createRow(headerRow);
                datarow.setHeightInPoints(30);
                HSSFCell[] cellsInTableContent = new HSSFCell[tableColumnCount + 1];
                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                cellsInTableContent[columnNo].setCellValue(countNoOfRow + 1);
                cellsInTableContent[columnNo].setCellStyle(body_style);

                countNoOfRow++;
                columnNo++;

                Object rowValue = itaddvalue.next();
                Class<?> cl = rowValue.getClass();
                Set<Entry<String, String>> entiresMethodNReturnType = getterNReturn.entrySet();
                for (Entry<String, String> ent : entiresMethodNReturnType) {
                    Method m = cl.getMethod(ent.getKey());
                    Object obj = m.invoke(rowValue);
                    Class<?> cls = Class.forName(ent.getValue());

                    if (cls.cast(obj) instanceof byte[]) {
                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                        cellsInTableContent[columnNo].setCellStyle(body_style);

                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                headerRow, (short) (columnNo + 1), headerRow + 1);
                        anchor.setAnchorType(2);
                        byte[] byteA = (byte[]) cls.cast(obj);
                        int imageIndexSingle = workBook.addPicture(byteA, HSSFWorkbook.PICTURE_TYPE_JPEG);

                        patriarch.createPicture(anchor, imageIndexSingle);
                        columnNo++;

                    } else if (cls.cast(obj) instanceof ArrayList) {
                        ArrayList<byte[]> snapListofImage = (ArrayList<byte[]>) cls.cast(obj);
                        int countofRow = 0;

                        if (snapListofImage.size() > 1) {
                            if (snapListofImage.size() <= (tableColumnName.size() + 1)) {

                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                cellsInTableContent[columnNo].setCellStyle(body_style);
                                headerRow++;
                                HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                dataMultipleRow.setHeightInPoints(100);
                                HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];

                                HSSFClientAnchor anchor = null;

                                for (int i = 0; i < snapListofImage.size(); i++) {

                                    anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + i),
                                            headerRow, (short) (countofRow + i + 1), headerRow + 1);

                                    int imageIndex = workBook.addPicture(snapListofImage.get(i), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                    patriarch.createPicture(anchor, imageIndex);
                                    cellsInTableContentImage[i] = dataMultipleRow.createCell(i);

                                    countNoOfRow++;

                                }

                            } else {
                                int noOfRow = snapListofImage.size() / 5;
                                int noOfRowNeed = snapListofImage.size() % 5;
                                HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];
                                for (int i = 0; i < noOfRow; i++) {

                                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                    cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                    cellsInTableContent[columnNo].setCellStyle(body_style);

                                    headerRow++;
                                    HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                    dataMultipleRow.setHeightInPoints(100);

                                    HSSFClientAnchor anchor = null;

                                    for (int j = 0; j < (tableColumnName.size() + 1); j++) {

                                        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);
                                        cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                    }

                                }

                                headerRow++;
                                HSSFRow dataMultipleRow = sheet.createRow(headerRow);
                                HSSFCell titleCell;
                                dataMultipleRow.setHeightInPoints(100);

                                HSSFClientAnchor anchor = null;

                                for (int j = 0; j < noOfRowNeed; j++) {

                                    anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                            headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                    int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                    patriarch.createPicture(anchor, imageIndex);

                                    cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                }

                            }
                        } else if (snapListofImage.size() == 1) {
                            datarow.setHeightInPoints(100);
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                    headerRow, (short) (columnNo + 1), headerRow + 1);
                            anchor.setAnchorType(2);

                            int imageIndexSingle = workBook.addPicture(snapListofImage.get(0), HSSFWorkbook.PICTURE_TYPE_JPEG);

                            patriarch.createPicture(anchor, imageIndexSingle);

                        } else {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                        }
                    } else {
                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                        cellsInTableContent[columnNo].setCellValue(obj != null ? cls.cast(obj).toString() : "");
                        cellsInTableContent[columnNo].setCellStyle(body_style);

                        columnNo++;

                    }
                }
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(fileName);
            workBook.write(fos);
            fos.close();
        }
        return statisticFile.getAbsolutePath();

    }

    @SuppressWarnings({"unused", "resource", "deprecation"})
    public static String createExcel(String fileName, String reportTitle, Map<String, String> bodycontent, List<String> tableColumnName, Map<String, String> getterNReturn, List<?> tableContent,
            List<String> summaryContent) throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        File statisticFile = new File(fileName);

        CellStyle header_style_center, header_style_left_body, header_style_right_body, header_style_left_Space, header_style_right_Space;

        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet(reportTitle);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        Font font = workBook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_center = workBook.createCellStyle();
        header_style_center.setFont(font);
        header_style_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_center.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        header_style_center.setAlignment(CellStyle.ALIGN_CENTER);
        header_style_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_center.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center.setBorderTop(CellStyle.BORDER_THIN);
        header_style_center.setBorderRight(CellStyle.BORDER_THIN);
        header_style_center.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_left_body = workBook.createCellStyle();
        header_style_left_body.setFont(font);
        header_style_left_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_body.setFillForegroundColor(new HSSFColor.CORAL().getIndex());
        header_style_left_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_left_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_left_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderLeft(CellStyle.BORDER_THIN);
        header_style_left_Space = workBook.createCellStyle();

        header_style_left_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_Space.setFillForegroundColor(new HSSFColor.CORAL().getIndex());
        header_style_left_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_Space.setBorderTop(CellStyle.BORDER_THIN);

        header_style_right_body = workBook.createCellStyle();
        header_style_right_body.setFont(font);
        header_style_right_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_body.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_right_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_right_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_Space = workBook.createCellStyle();
        header_style_right_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_Space.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_Space.setBorderTop(CellStyle.BORDER_THIN);

        HSSFCellStyle body_style = workBook.createCellStyle();
        font = workBook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        body_style.setFont(font);
        body_style.setAlignment(CellStyle.ALIGN_CENTER);
        body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        body_style.setBorderBottom(CellStyle.BORDER_THIN);
        body_style.setBorderTop(CellStyle.BORDER_THIN);
        body_style.setBorderRight(CellStyle.BORDER_THIN);
        body_style.setBorderLeft(CellStyle.BORDER_THIN);

        HSSFCellStyle headerCellStyle = workBook.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(new HSSFColor.LIGHT_GREEN().getIndex());
       
        
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment((short) 10);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THICK);
        headerCellStyle.setBorderTop(CellStyle.BORDER_THICK);
        headerCellStyle.setBorderRight(CellStyle.BORDER_THICK);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_THICK);
        HSSFFont hSSFFont = workBook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 20);
        hSSFFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.BLACK.index);
        headerCellStyle.setFont(hSSFFont);

        int headerRow = 0;
        {

            HSSFRow titleRow = sheet.createRow(headerRow++);
            titleRow.setHeightInPoints(100);
            HSSFCell[] firsttitleCell = new HSSFCell[tableColumnName.size() + 1];
            firsttitleCell[0] = titleRow.createCell(0);

            BufferedImage originalImageByteHeader = new VLogo().getCorporateLogo();
            ByteArrayOutputStream baosByteheader = new ByteArrayOutputStream();
            ImageIO.write(originalImageByteHeader, "jpg", baosByteheader);
            baosByteheader.flush();
            byte[] imageInByteSingleHeader = baosByteheader.toByteArray();
            baosByteheader.close();

            HSSFPatriarch patriarchHeader = sheet.createDrawingPatriarch();

            HSSFClientAnchor anchorImage = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 1, 1);

            int imageIndexHeader = workBook.addPicture(imageInByteSingleHeader, HSSFWorkbook.PICTURE_TYPE_JPEG);

            patriarchHeader.createPicture(anchorImage, imageIndexHeader);

            firsttitleCell[1] = titleRow.createCell(1);
            firsttitleCell[1].setCellValue(reportTitle);
            firsttitleCell[1].setCellStyle(headerCellStyle);
            sheet.addMergedRegion(CellRangeAddress.valueOf("$B$1:$E$1"));

            headerRow++;
            HSSFRow spacerow = sheet.createRow(headerRow);

            int noOfRowInBody = bodycontent.size() / 2;
            int noOfCellInBody = noOfRowInBody * 2;

            int tableheaderNo = tableColumnName.size();
            int noOfHeaderNeed = tableheaderNo + 1;
            int noOfHeaderForFirstCol = (noOfHeaderNeed) / 2;
            int noOfHeaderForSecCol = noOfHeaderNeed - noOfHeaderForFirstCol;

            Set<String> keySet = bodycontent.keySet();

            List<String> list = new ArrayList<String>(keySet);
            HSSFRow row = null;
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                String value = bodycontent.get(key);
                
                if (i % 2 == 0) {

                    headerRow++;
                    row = sheet.createRow(headerRow);
                    HSSFCell[] cells = new HSSFCell[noOfHeaderNeed];

                    for (int l = 0; l < noOfHeaderForFirstCol; l++) {
                        cells[l] = row.createCell(l);
                        cells[l].setCellValue(" ");
                        

                    }
                    cells[(noOfHeaderForFirstCol / 2)].setCellValue(key + "  :  " + value);
                    cells[(noOfHeaderForFirstCol / 2)].setCellStyle(header_style_left_body);

                } else if (i % 2 == 1) {
                    HSSFCell[] cell = new HSSFCell[noOfHeaderNeed];
                    for (int t = noOfHeaderForFirstCol; t < noOfHeaderNeed; t++) {
                        cell[t] = row.createCell(t);
                        cell[t].setCellValue(" ");
                        

                    }

                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellValue(key + "  :  " + value);
                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellStyle(header_style_right_body);

                }
            }

            int tableColumnCount = tableColumnName.size();

            headerRow++;
            headerRow++;
            HSSFRow columnrow = sheet.createRow(headerRow);

            columnrow.setHeightInPoints(20);
            HSSFCell[] cellsInTableheader = new HSSFCell[tableColumnCount + 1];
            int count = 0;
            Iterator<String> it = tableColumnName.iterator();
            while (it.hasNext()) {
                if (count == 0) {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue("SL");
                    cellsInTableheader[count].setCellStyle(header_style_center);

                    sheet.setColumnWidth(count, 8000);

                    count++;
                } else {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue(it.next());
                    cellsInTableheader[count].setCellStyle(header_style_center);
                    sheet.setColumnWidth(count, 12000);
                    count++;
                }
            }

            int countNoOfRow = 0;

            Iterator<?> itaddvalue = tableContent.iterator();
            while (itaddvalue.hasNext()) {

                int columnNo = 0;
                headerRow++;
                HSSFRow datarow = sheet.createRow(headerRow);
                datarow.setHeightInPoints(30);
                HSSFCell[] cellsInTableContent = new HSSFCell[tableColumnCount + 1];
                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                cellsInTableContent[columnNo].setCellValue(countNoOfRow + 1);
                cellsInTableContent[columnNo].setCellStyle(body_style);

                countNoOfRow++;
                columnNo++;

                Object rowValue = itaddvalue.next();
                Class<?> cl = rowValue.getClass();
                Set<Entry<String, String>> entiresMethodNReturnType = getterNReturn.entrySet();
                for (Entry<String, String> ent : entiresMethodNReturnType) {
                    Method m = cl.getMethod(ent.getKey());
                    Object obj = m.invoke(rowValue);
                    Class<?> cls = Class.forName(ent.getValue());

                    if (cls.cast(obj) instanceof byte[]) {
                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                        cellsInTableContent[columnNo].setCellStyle(body_style);

                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                headerRow, (short) (columnNo + 1), headerRow + 1);
                        anchor.setAnchorType(2);
                        byte[] byteA = (byte[]) cls.cast(obj);
                        int imageIndexSingle = workBook.addPicture(byteA, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);

                        patriarch.createPicture(anchor, imageIndexSingle);
                        columnNo++;

                    } else if (cls.cast(obj) instanceof ArrayList) {
                        ArrayList<byte[]> snapListofImage = (ArrayList<byte[]>) cls.cast(obj);
                        int countofRow = 0;

                        if (snapListofImage.size() > 1) {
                            if (snapListofImage.size() <= (tableColumnName.size() + 1)) {

                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                cellsInTableContent[columnNo].setCellStyle(body_style);
                                headerRow++;
                                HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                dataMultipleRow.setHeightInPoints(100);
                                HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];

                                HSSFClientAnchor anchor = null;

                                for (int i = 0; i < snapListofImage.size(); i++) {

                                    anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + i),
                                            headerRow, (short) (countofRow + i + 1), headerRow + 1);

                                    int imageIndex = workBook.addPicture(snapListofImage.get(i), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                    patriarch.createPicture(anchor, imageIndex);
                                    cellsInTableContentImage[i] = dataMultipleRow.createCell(i);

                                    countNoOfRow++;

                                }

                            } else {
                                int noOfRow = snapListofImage.size() / 5;
                                int noOfRowNeed = snapListofImage.size() % 5;
                                HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];
                                for (int i = 0; i < noOfRow; i++) {

                                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                    cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                    cellsInTableContent[columnNo].setCellStyle(body_style);

                                    headerRow++;
                                    HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                    dataMultipleRow.setHeightInPoints(100);

                                    HSSFClientAnchor anchor = null;

                                    for (int j = 0; j < (tableColumnName.size() + 1); j++) {

                                        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);
                                        cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                    }

                                }

                                headerRow++;
                                HSSFRow dataMultipleRow = sheet.createRow(headerRow);
                                HSSFCell titleCell;
                                dataMultipleRow.setHeightInPoints(100);

                                HSSFClientAnchor anchor = null;

                                for (int j = 0; j < noOfRowNeed; j++) {

                                    anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                            headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                    int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                    patriarch.createPicture(anchor, imageIndex);

                                    cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                }

                            }
                        } else if (snapListofImage.size() == 1) {
                            datarow.setHeightInPoints(100);
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                    headerRow, (short) (columnNo + 1), headerRow + 1);
                            anchor.setAnchorType(2);

                            int imageIndexSingle = workBook.addPicture(snapListofImage.get(0), HSSFWorkbook.PICTURE_TYPE_JPEG);

                            patriarch.createPicture(anchor, imageIndexSingle);

                        } else {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                        }
                    } else {
                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                        cellsInTableContent[columnNo].setCellValue(cls.cast(obj).toString());
                        cellsInTableContent[columnNo].setCellStyle(body_style);

                        columnNo++;

                    }
                }
            }

            headerRow++;
            headerRow++;

            int summaryCount = 0;
            Iterator<String> summartIt = summaryContent.iterator();
            HSSFRow summaryContentrow = sheet.createRow(headerRow);
            HSSFCell[] summaryContentcellsInTableheader = new HSSFCell[tableColumnCount + 1];

            summaryCount = ((tableColumnCount + 1) - 2);
            while (summartIt.hasNext()) {

                if (summaryCount == ((tableColumnCount + 1) - 2)) {

                    summaryContentcellsInTableheader[summaryCount] = summaryContentrow.createCell(summaryCount);
                    summaryContentcellsInTableheader[summaryCount].setCellValue(summartIt.next());
                    summaryContentcellsInTableheader[summaryCount].setCellStyle(body_style);
                    summaryCount++;
                } else if (summaryCount == tableColumnCount) {

                    summaryContentcellsInTableheader[summaryCount] = summaryContentrow.createCell(summaryCount);
                    summaryContentcellsInTableheader[summaryCount].setCellValue(summartIt.next());
                    summaryContentcellsInTableheader[summaryCount].setCellStyle(body_style);

                    summaryCount = ((tableColumnCount + 1) - 2);
                    headerRow++;
                    summaryContentrow = sheet.createRow(headerRow);
                }
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(fileName);
            workBook.write(fos);
            fos.close();

        }
        return statisticFile.getAbsolutePath();

    }

    @SuppressWarnings({"unused", "resource", "deprecation"})
    public static String createCartExcel(String fileName, String reportTitle, Map<String, String> bodycontent, List<String> tableColumnName, Map<String, String> getterNReturn, List<?> tableContent) throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        File statisticFile = new File(fileName);
        String parentPath = statisticFile.getParent();
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        CellStyle header_style_center, header_style_left_body, header_style_right_body, header_style_left_Space, header_style_right_Space, hlink_style;

        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet(reportTitle);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        CreationHelper createHelper = workBook.getCreationHelper();

        Font font = workBook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_center = workBook.createCellStyle();
        header_style_center.setFont(font);
        header_style_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_center.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        header_style_center.setAlignment(CellStyle.ALIGN_CENTER);
        header_style_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_center.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center.setBorderTop(CellStyle.BORDER_THIN);
        header_style_center.setBorderRight(CellStyle.BORDER_THIN);
        header_style_center.setBorderLeft(CellStyle.BORDER_THIN);

        hlink_style = workBook.createCellStyle();
        Font hlink_font = workBook.createFont();
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        hlink_style.setAlignment(CellStyle.ALIGN_CENTER);
        hlink_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        hlink_style.setBorderBottom(CellStyle.BORDER_THIN);
        hlink_style.setBorderTop(CellStyle.BORDER_THIN);
        hlink_style.setBorderRight(CellStyle.BORDER_THIN);
        hlink_style.setBorderLeft(CellStyle.BORDER_THIN);
        hlink_style.setFont(hlink_font);

        font = workBook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_left_body = workBook.createCellStyle();
        header_style_left_body.setFont(font);
        header_style_left_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_left_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_left_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_left_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_left_Space = workBook.createCellStyle();
        header_style_left_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_Space.setFillForegroundColor(new HSSFColor.CORAL().getIndex());
        header_style_left_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_Space.setBorderTop(CellStyle.BORDER_THIN);

        header_style_right_body = workBook.createCellStyle();
        header_style_right_body.setFont(font);
        header_style_right_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_right_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_right_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_right_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_Space = workBook.createCellStyle();
        header_style_right_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_Space.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_Space.setBorderTop(CellStyle.BORDER_THIN);

        HSSFCellStyle body_style = workBook.createCellStyle();
        font = workBook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        body_style.setFont(font);
        body_style.setAlignment(CellStyle.ALIGN_CENTER);
        body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        body_style.setBorderBottom(CellStyle.BORDER_THIN);
        body_style.setBorderTop(CellStyle.BORDER_THIN);
        body_style.setBorderRight(CellStyle.BORDER_THIN);
        body_style.setBorderLeft(CellStyle.BORDER_THIN);

        HSSFCellStyle headerCellStyle = workBook.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
       
       
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment((short) 10);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderTop(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderRight(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
        HSSFFont hSSFFont = workBook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 14);
        hSSFFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.WHITE.index);
        headerCellStyle.setFont(hSSFFont);

        int headerRow = 0;
        {

            HSSFRow titleRow = sheet.createRow(headerRow++);
            titleRow.setHeightInPoints(100);

            HSSFCell firsttitleCell = titleRow.createCell(0);

            BufferedImage originalImageByteHeader = new VLogo().getCorporateLogo();

            ByteArrayOutputStream baosByteheader = new ByteArrayOutputStream();
            ImageIO.write(originalImageByteHeader, "jpg", baosByteheader);
            baosByteheader.flush();
            byte[] imageInByteSingleHeader = baosByteheader.toByteArray();
            baosByteheader.close();

            final int imageIndexHeader = workBook.addPicture(imageInByteSingleHeader, HSSFWorkbook.PICTURE_TYPE_JPEG);


            CreationHelper helper = workBook.getCreationHelper();
            Drawing drawingPatriarch = sheet.createDrawingPatriarch();
            final ClientAnchor imageAnchor = helper.createClientAnchor();
            imageAnchor.setAnchorType(ClientAnchor.DONT_MOVE_AND_RESIZE);
            imageAnchor.setCol1(0);
            imageAnchor.setCol2(1);
            imageAnchor.setRow1(0);
            imageAnchor.setRow2(1);

            HSSFPicture picture = patriarch.createPicture(imageAnchor, imageIndexHeader);
            picture.resize(2.0, 1);

            HSSFRow secondRow = sheet.createRow(headerRow);


            HSSFCell secondtitleCell = secondRow.createCell(0);
            secondtitleCell.setCellValue(reportTitle);
            secondtitleCell.setCellStyle(headerCellStyle);
            

            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, tableColumnName.size()));

            headerRow++;
            HSSFRow spacerow = sheet.createRow(headerRow);

            int noOfRowInBody = bodycontent.size() / 2;
            int noOfCellInBody = noOfRowInBody * 2;

            int tableheaderNo = tableColumnName.size();
            int noOfHeaderNeed = tableheaderNo + 1;
            int noOfHeaderForFirstCol = (noOfHeaderNeed) / 2;
            int noOfHeaderForSecCol = noOfHeaderNeed - noOfHeaderForFirstCol;

            Set<String> keySet = bodycontent.keySet();

            List<String> list = new ArrayList<String>(keySet);
            HSSFRow row = null;
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                String value = bodycontent.get(key);
                
                if (i % 2 == 0) {

                    headerRow++;
                    row = sheet.createRow(headerRow);
                    HSSFCell[] cells = new HSSFCell[noOfHeaderNeed];

                    for (int l = 0; l < noOfHeaderForFirstCol; l++) {
                        cells[l] = row.createCell(l);
                        cells[l].setCellValue(" ");
                       
                    }
                    cells[(noOfHeaderForFirstCol / 2)].setCellValue(key + "  :  " + value);
                    cells[(noOfHeaderForFirstCol / 2)].setCellStyle(header_style_left_body);

                } else if (i % 2 == 1) {
                    HSSFCell[] cell = new HSSFCell[noOfHeaderNeed];
                    for (int t = noOfHeaderForFirstCol; t < noOfHeaderNeed; t++) {
                        cell[t] = row.createCell(t);
                        cell[t].setCellValue(" ");
                        

                    }

                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellValue(key + "  :  " + value);
                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellStyle(header_style_right_body);

                }
            }

            int tableColumnCount = tableColumnName.size();

            headerRow++;
            headerRow++;
            HSSFRow columnrow = sheet.createRow(headerRow);

            columnrow.setHeightInPoints(20);
            HSSFCell[] cellsInTableheader = new HSSFCell[tableColumnCount + 1];
            int count = 0;
            Iterator<String> it = tableColumnName.iterator();
            while (it.hasNext()) {
                if (count == 0) {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue("SL");
                    cellsInTableheader[count].setCellStyle(header_style_center);

                    sheet.setColumnWidth(count, 2000);

                    count++;
                } else {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue(it.next());
                    cellsInTableheader[count].setCellStyle(header_style_center);
                    sheet.setColumnWidth(count, 8000);
                    count++;
                }
            }

            int countNoOfRow = 0;

            Iterator<?> itaddvalue = tableContent.iterator();
            while (itaddvalue.hasNext()) {

                int columnNo = 0;
                headerRow++;
                HSSFRow datarow = sheet.createRow(headerRow);
                datarow.setHeightInPoints(30);
                HSSFCell[] cellsInTableContent = new HSSFCell[tableColumnCount + 1];
                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                cellsInTableContent[columnNo].setCellValue(countNoOfRow + 1);
                cellsInTableContent[columnNo].setCellStyle(body_style);

                countNoOfRow++;
                columnNo++;

                Object rowValue = itaddvalue.next();
                Class<?> cl = rowValue.getClass();
                Set<Entry<String, String>> entiresMethodNReturnType = getterNReturn.entrySet();
                for (Entry<String, String> ent : entiresMethodNReturnType) {
                    Method m = cl.getMethod(ent.getKey());
                    Object obj = m.invoke(rowValue);
                    Class<?> cls = Class.forName(ent.getValue());

                    if (cls.cast(obj) instanceof byte[]) {
                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                        cellsInTableContent[columnNo].setCellStyle(body_style);

                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                headerRow, (short) (columnNo + 1), headerRow + 1);
                        anchor.setAnchorType(2);
                        byte[] byteA = (byte[]) cls.cast(obj);
                        int imageIndexSingle = workBook.addPicture(byteA, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);

                        patriarch.createPicture(anchor, imageIndexSingle);
                        columnNo++;

                    } else if (cls.cast(obj) instanceof ArrayList) {
                        ArrayList<byte[]> snapListofImage = (ArrayList<byte[]>) cls.cast(obj);
                        int countofRow = 0;

                        if (snapListofImage.size() > 1) {
                            if (snapListofImage.size() <= (tableColumnName.size() + 1)) {

                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                cellsInTableContent[columnNo].setCellStyle(body_style);
                                headerRow++;
                                HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                dataMultipleRow.setHeightInPoints(100);
                                HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];

                                HSSFClientAnchor anchor = null;

                                for (int i = 0; i < snapListofImage.size(); i++) {

                                    anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + i),
                                            headerRow, (short) (countofRow + i + 1), headerRow + 1);

                                    int imageIndex = workBook.addPicture(snapListofImage.get(i), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                    patriarch.createPicture(anchor, imageIndex);
                                    cellsInTableContentImage[i] = dataMultipleRow.createCell(i);

                                    countNoOfRow++;

                                }

                            } else {
                                int noOfRow = snapListofImage.size() / 5;
                                int noOfRowNeed = snapListofImage.size() % 5;
                                HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];
                                for (int i = 0; i < noOfRow; i++) {

                                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                    cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                    cellsInTableContent[columnNo].setCellStyle(body_style);

                                    headerRow++;
                                    HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                    dataMultipleRow.setHeightInPoints(100);

                                    HSSFClientAnchor anchor = null;

                                    for (int j = 0; j < (tableColumnName.size() + 1); j++) {

                                        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);
                                        cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                    }

                                }

                                headerRow++;
                                HSSFRow dataMultipleRow = sheet.createRow(headerRow);
                                HSSFCell titleCell;
                                dataMultipleRow.setHeightInPoints(100);

                                HSSFClientAnchor anchor = null;

                                for (int j = 0; j < noOfRowNeed; j++) {

                                    anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                            headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                    int imageIndex = workBook.addPicture(snapListofImage.get(j), org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);
                                    patriarch.createPicture(anchor, imageIndex);

                                    cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                }

                            }
                        } else if (snapListofImage.size() == 1) {
                            datarow.setHeightInPoints(100);
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                    headerRow, (short) (columnNo + 1), headerRow + 1);
                            anchor.setAnchorType(2);

                            int imageIndexSingle = workBook.addPicture(snapListofImage.get(0), HSSFWorkbook.PICTURE_TYPE_JPEG);

                            patriarch.createPicture(anchor, imageIndexSingle);

                        } else {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                        }
                    } else {
                        if (entiresMethodNReturnType.size() != columnNo) {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue(cls.cast(obj).toString());
                            cellsInTableContent[columnNo].setCellStyle(body_style);
                        } else {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue("PLAY");
                            cellsInTableContent[columnNo].setCellStyle(hlink_style);
                            Hyperlink hyperLink = createHelper.createHyperlink(Hyperlink.LINK_FILE);
                            hyperLink.setAddress(obj != null ? cls.cast(obj).toString() : "");
                            cellsInTableContent[columnNo].setHyperlink(hyperLink);
                        }

                        LOGGER.log(Level.INFO, "entiresMethodNReturnType ={}" ,entiresMethodNReturnType.size());
                        LOGGER.log(Level.INFO, "columnNo= {}" ,columnNo);
                        columnNo++;

                    }
                }
            }

            // add Disclaimer:
            HSSFCellStyle disclaimer_header_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            disclaimer_header_style.setFont(font);
            disclaimer_header_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_header_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_header_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderLeft(CellStyle.BORDER_NONE);

            HSSFCellStyle disclaimer_body_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
            disclaimer_body_style.setFont(font);
            disclaimer_body_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_body_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderLeft(CellStyle.BORDER_NONE);

            headerRow++;
            headerRow++;
            HSSFRow disclaimerRow = sheet.createRow(headerRow);

            HSSFCell disclaimerRowCell = disclaimerRow.createCell(0);
            disclaimerRowCell.setCellValue("Disclaimer:");
            disclaimerRowCell.setCellStyle(disclaimer_header_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 1));

            headerRow++;

            HSSFRow disclaimerDataRow = sheet.createRow(headerRow);

            HSSFCell disclaimerDataRowCell = disclaimerDataRow.createCell(0);
            disclaimerDataRowCell.setCellValue("This is a confidential report and should not be shared without the consent of the owner.");
            disclaimerDataRowCell.setCellStyle(disclaimer_body_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 3));

            FileOutputStream fos = new FileOutputStream(fileName);
            workBook.write(fos);
            fos.close();
        }
        return statisticFile.getAbsolutePath();

    }

    @SuppressWarnings({"unused", "resource", "deprecation"})
    public static String createExcel(String fileName, String reportTitle, Map<String, String> bodycontent, List<String> tableColumnName, Map<String, String> getterNReturn, List<?> tableContent,
            List<String> summaryContent, String password, String noDataMsg) throws IOException, NoSuchMethodException, SecurityException,
            ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        File statisticFile = new File(fileName);

        CellStyle header_style_center, header_style_left_body, header_style_right_body, header_style_left_Space, header_style_right_Space;

        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet(reportTitle);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        Font font = workBook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_center = workBook.createCellStyle();
        header_style_center.setFont(font);
        header_style_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_center.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        header_style_center.setAlignment(CellStyle.ALIGN_CENTER);
        header_style_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_center.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center.setBorderTop(CellStyle.BORDER_THIN);
        header_style_center.setBorderRight(CellStyle.BORDER_THIN);
        header_style_center.setBorderLeft(CellStyle.BORDER_THIN);

        font = workBook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_left_body = workBook.createCellStyle();
        header_style_left_body.setFont(font);
        header_style_left_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_left_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_left_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_left_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_body = workBook.createCellStyle();
        header_style_right_body.setFont(font);
        header_style_right_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_right_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_right_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_right_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_Space = workBook.createCellStyle();
        header_style_right_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_Space.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_Space.setBorderTop(CellStyle.BORDER_THIN);

        HSSFCellStyle body_style = workBook.createCellStyle();
        font = workBook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        body_style.setFont(font);
        body_style.setAlignment(CellStyle.ALIGN_CENTER);
        body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        body_style.setBorderBottom(CellStyle.BORDER_THIN);
        body_style.setBorderTop(CellStyle.BORDER_THIN);
        body_style.setBorderRight(CellStyle.BORDER_THIN);
        body_style.setBorderLeft(CellStyle.BORDER_THIN);

        HSSFCellStyle headerCellStyle = workBook.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        
      
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment((short) 10);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderTop(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderRight(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
        HSSFFont hSSFFont = workBook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 14);
        hSSFFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.WHITE.index);
        headerCellStyle.setFont(hSSFFont);

        int headerRow = 0;
        {

            HSSFRow titleRow = sheet.createRow(headerRow++);
            titleRow.setHeightInPoints(100);

            HSSFCell firsttitleCell = titleRow.createCell(0);

            BufferedImage originalImageByteHeader = new VLogo().getCorporateLogo();

            ByteArrayOutputStream baosByteheader = new ByteArrayOutputStream();
            ImageIO.write(originalImageByteHeader, "jpg", baosByteheader);
            baosByteheader.flush();
            byte[] imageInByteSingleHeader = baosByteheader.toByteArray();
            baosByteheader.close();

            final int imageIndexHeader = workBook.addPicture(imageInByteSingleHeader, HSSFWorkbook.PICTURE_TYPE_JPEG);


            CreationHelper helper = workBook.getCreationHelper();
            Drawing drawingPatriarch = sheet.createDrawingPatriarch();
            final ClientAnchor imageAnchor = helper.createClientAnchor();
            imageAnchor.setAnchorType(ClientAnchor.DONT_MOVE_AND_RESIZE);
            imageAnchor.setCol1(0);
            imageAnchor.setCol2(1);
            imageAnchor.setRow1(0);
            imageAnchor.setRow2(1);

            HSSFPicture picture = patriarch.createPicture(imageAnchor, imageIndexHeader);
            picture.resize(2.0, 1);

            HSSFRow secondRow = sheet.createRow(headerRow);


            HSSFCell secondtitleCell = secondRow.createCell(0);
            secondtitleCell.setCellValue(reportTitle);
            secondtitleCell.setCellStyle(headerCellStyle);


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, tableColumnName.size()));

            headerRow++;
            HSSFRow spacerow = sheet.createRow(headerRow);

            int noOfRowInBody = bodycontent.size() / 2;
            int noOfCellInBody = noOfRowInBody * 2;

            int tableheaderNo = tableColumnName.size();
            int noOfHeaderNeed = tableheaderNo + 1;
            int noOfHeaderForFirstCol = (noOfHeaderNeed) / 2;
            int noOfHeaderForSecCol = noOfHeaderNeed - noOfHeaderForFirstCol;

            Set<String> keySet = bodycontent.keySet();

            List<String> list = new ArrayList<String>(keySet);
            HSSFRow row = null;
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                String value = bodycontent.get(key);
                
                if (i % 2 == 0) {

                    headerRow++;
                    row = sheet.createRow(headerRow);
                    HSSFCell[] cells = new HSSFCell[noOfHeaderNeed];

                    for (int l = 0; l < noOfHeaderForFirstCol; l++) {
                        cells[l] = row.createCell(l);
                        cells[l].setCellValue(" ");
                        

                    }
                    cells[(noOfHeaderForFirstCol / 2)].setCellValue(key + "  :  " + value);
                    cells[(noOfHeaderForFirstCol / 2)].setCellStyle(header_style_left_body);

                } else if (i % 2 == 1) {
                    HSSFCell[] cell = new HSSFCell[noOfHeaderNeed];
                    for (int t = noOfHeaderForFirstCol; t < noOfHeaderNeed; t++) {
                        cell[t] = row.createCell(t);
                        cell[t].setCellValue(" ");
                        

                    }

                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellValue(key + "  :  " + value);
                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellStyle(header_style_right_body);

                }
            }

            int tableColumnCount = tableColumnName.size();

            headerRow++;
            headerRow++;
            HSSFRow columnrow = sheet.createRow(headerRow);

            columnrow.setHeightInPoints(20);
            HSSFCell[] cellsInTableheader = new HSSFCell[tableColumnCount + 1];
            int count = 0;
            Iterator<String> it = tableColumnName.iterator();
            while (it.hasNext()) {
                if (count == 0) {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue("SL");
                    cellsInTableheader[count].setCellStyle(header_style_center);

                    sheet.setColumnWidth(count, 2000);

                } else {

                    String columsValue = it.next();
                    int colomnWidth = 6;
                    try {
                        colomnWidth = Math.max(Math.min(columsValue.trim().length(), 8), 4);
                    } catch (Exception e) {
                    	LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
                    }

                    if (columsValue.trim().toUpperCase().contains("TIME") || columsValue.trim().toUpperCase().contains("DATE")
                            || columsValue.trim().toUpperCase().contains("IP")) {
                        colomnWidth = 8;
                    }

                    if (columsValue.trim().equals("Camera Availability(%)") || columsValue.trim().equals("Camera Recording Availability(%)")) {
                        colomnWidth = 10;
                    }

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue(columsValue);
                    cellsInTableheader[count].setCellStyle(header_style_center);
                    sheet.setColumnWidth(count, colomnWidth * 1000);

                }

                count++;
            }

            int countNoOfRow = 0;

            Iterator<?> itaddvalue = tableContent.iterator();
            if (tableContent.size() > 0) {
                while (itaddvalue.hasNext()) {

                    int columnNo = 0;
                    headerRow++;
                    HSSFRow datarow = sheet.createRow(headerRow);
                    datarow.setHeightInPoints(30);
                    HSSFCell[] cellsInTableContent = new HSSFCell[tableColumnCount + 1];
                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                    cellsInTableContent[columnNo].setCellValue(countNoOfRow + 1);
                    cellsInTableContent[columnNo].setCellStyle(body_style);

                    countNoOfRow++;
                    columnNo++;

                    Object rowValue = itaddvalue.next();
                    Class<?> cl = rowValue.getClass();
                    Set<Entry<String, String>> entiresMethodNReturnType = getterNReturn.entrySet();
                    for (Entry<String, String> ent : entiresMethodNReturnType) {
                        Method m = cl.getMethod(ent.getKey());
                        Object obj = m.invoke(rowValue);
                        Class<?> cls = Class.forName(ent.getValue());

                        if (cls.cast(obj) instanceof byte[]) {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                    headerRow, (short) (columnNo + 1), headerRow + 1);
                            anchor.setAnchorType(2);
                            byte[] byteA = (byte[]) cls.cast(obj);
                            int imageIndexSingle = workBook.addPicture(byteA, HSSFWorkbook.PICTURE_TYPE_JPEG);

                            patriarch.createPicture(anchor, imageIndexSingle);
                            columnNo++;

                        } else if (cls.cast(obj) instanceof ArrayList) {
                            ArrayList<byte[]> snapListofImage = (ArrayList<byte[]>) cls.cast(obj);
                            int countofRow = 0;

                            if (snapListofImage.size() > 1) {
                                if (snapListofImage.size() <= (tableColumnName.size() + 1)) {

                                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                    cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                    cellsInTableContent[columnNo].setCellStyle(body_style);
                                    headerRow++;
                                    HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                    dataMultipleRow.setHeightInPoints(100);
                                    HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];

                                    HSSFClientAnchor anchor = null;

                                    for (int i = 0; i < snapListofImage.size(); i++) {

                                        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + i),
                                                headerRow, (short) (countofRow + i + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(i), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);
                                        cellsInTableContentImage[i] = dataMultipleRow.createCell(i);

                                        countNoOfRow++;

                                    }

                                } else {
                                    int noOfRow = snapListofImage.size() / 5;
                                    int noOfRowNeed = snapListofImage.size() % 5;
                                    HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];
                                    for (int i = 0; i < noOfRow; i++) {

                                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                        cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                        cellsInTableContent[columnNo].setCellStyle(body_style);

                                        headerRow++;
                                        HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                        dataMultipleRow.setHeightInPoints(100);

                                        HSSFClientAnchor anchor = null;

                                        for (int j = 0; j < (tableColumnName.size() + 1); j++) {

                                            anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                    headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                            int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                            patriarch.createPicture(anchor, imageIndex);
                                            cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                        }

                                    }

                                    headerRow++;
                                    HSSFRow dataMultipleRow = sheet.createRow(headerRow);
                                    HSSFCell titleCell;
                                    dataMultipleRow.setHeightInPoints(100);

                                    HSSFClientAnchor anchor = null;

                                    for (int j = 0; j < noOfRowNeed; j++) {

                                        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);

                                        cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                    }

                                }
                            } else if (snapListofImage.size() == 1) {
                                datarow.setHeightInPoints(100);
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                        headerRow, (short) (columnNo + 1), headerRow + 1);
                                anchor.setAnchorType(2);

                                int imageIndexSingle = workBook.addPicture(snapListofImage.get(0), HSSFWorkbook.PICTURE_TYPE_JPEG);

                                patriarch.createPicture(anchor, imageIndexSingle);

                            } else {
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                            }
                        } else {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue(cls.cast(obj).toString());
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            columnNo++;

                        }
                    }
                }
            } else {
                headerRow++;
                HSSFRow emptyRow = sheet.createRow(headerRow);
                HSSFCell emptyCell = emptyRow.createCell(0);
                emptyCell.setCellValue(noDataMsg);
                emptyCell.setCellStyle(body_style);

                sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, tableColumnName.size()));
            }

            headerRow++;
            headerRow++;

            int summaryCount = 0;
            Iterator<String> summartIt = summaryContent.iterator();
            HSSFRow summaryContentrow = sheet.createRow(headerRow);
            HSSFCell[] summaryContentcellsInTableheader = new HSSFCell[tableColumnCount + 1];

            summaryCount = ((tableColumnCount + 1) - 2);
            while (summartIt.hasNext()) {

                if (summaryCount == ((tableColumnCount + 1) - 2)) {

                    summaryContentcellsInTableheader[summaryCount] = summaryContentrow.createCell(summaryCount);
                    summaryContentcellsInTableheader[summaryCount].setCellValue(summartIt.next());
                    summaryContentcellsInTableheader[summaryCount].setCellStyle(body_style);
                    summaryCount++;
                } else if (summaryCount == tableColumnCount) {

                    summaryContentcellsInTableheader[summaryCount] = summaryContentrow.createCell(summaryCount);
                    summaryContentcellsInTableheader[summaryCount].setCellValue(summartIt.next());
                    summaryContentcellsInTableheader[summaryCount].setCellStyle(body_style);

                    summaryCount = ((tableColumnCount + 1) - 2);
                    headerRow++;
                    summaryContentrow = sheet.createRow(headerRow);
                }
            }

            // add Disclaimer:
            HSSFCellStyle disclaimer_header_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            disclaimer_header_style.setFont(font);
            disclaimer_header_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_header_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_header_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderLeft(CellStyle.BORDER_NONE);

            HSSFCellStyle disclaimer_body_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
            disclaimer_body_style.setFont(font);
            disclaimer_body_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_body_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderLeft(CellStyle.BORDER_NONE);

            headerRow++;
            headerRow++;
            HSSFRow disclaimerRow = sheet.createRow(headerRow);

            HSSFCell disclaimerRowCell = disclaimerRow.createCell(0);
            disclaimerRowCell.setCellValue("Disclaimer:");
            disclaimerRowCell.setCellStyle(disclaimer_header_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 1));

            headerRow++;

            HSSFRow disclaimerDataRow = sheet.createRow(headerRow);

            HSSFCell disclaimerDataRowCell = disclaimerDataRow.createCell(0);
            disclaimerDataRowCell.setCellValue("This is a confidential report and should not be shared without the consent of the owner.");
            disclaimerDataRowCell.setCellStyle(disclaimer_body_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 3));

            FileOutputStream fos = new FileOutputStream(fileName);
            workBook.write(fos);
            fos.close();

            if (password != null) {
                setPassword(fileName, password);
            }
        }
        return statisticFile.getAbsolutePath();

    }

    @SuppressWarnings({"unused", "resource", "deprecation"})
    public static String createExcel(String fileName, String reportTitle, Map<String, String> bodycontent, List<String> tableColumnName,
            Map<String, String> getterNReturn, List<?> tableContent, String password, String noDataMsg)
            throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        File statisticFile = new File(fileName);
        String parentPath = statisticFile.getParent();
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        CellStyle header_style_center, header_style_left_body, header_style_right_body, header_style_left_Space, header_style_right_Space;

        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet(reportTitle);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        Font font = workBook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_center = workBook.createCellStyle();
        header_style_center.setFont(font);
        header_style_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_center.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        header_style_center.setAlignment(CellStyle.ALIGN_CENTER);
        header_style_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_center.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center.setBorderTop(CellStyle.BORDER_THIN);
        header_style_center.setBorderRight(CellStyle.BORDER_THIN);
        header_style_center.setBorderLeft(CellStyle.BORDER_THIN);

        font = workBook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_left_body = workBook.createCellStyle();
        header_style_left_body.setFont(font);
        header_style_left_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_left_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_left_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_left_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_left_Space = workBook.createCellStyle();
        header_style_left_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_Space.setFillForegroundColor(new HSSFColor.CORAL().getIndex());
        header_style_left_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_Space.setBorderTop(CellStyle.BORDER_THIN);

        header_style_right_body = workBook.createCellStyle();
        header_style_right_body.setFont(font);
        header_style_right_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_right_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_right_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_right_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_Space = workBook.createCellStyle();
        header_style_right_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_Space.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_Space.setBorderTop(CellStyle.BORDER_THIN);

        HSSFCellStyle body_style = workBook.createCellStyle();
        font = workBook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        body_style.setFont(font);
        body_style.setAlignment(CellStyle.ALIGN_CENTER);
        body_style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        body_style.setBorderBottom(CellStyle.BORDER_THIN);
        body_style.setBorderTop(CellStyle.BORDER_THIN);
        body_style.setBorderRight(CellStyle.BORDER_THIN);
        body_style.setBorderLeft(CellStyle.BORDER_THIN);

        HSSFCellStyle headerCellStyle = workBook.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        
       
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment((short) 10);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderTop(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderRight(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
        HSSFFont hSSFFont = workBook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 14);
        hSSFFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.WHITE.index);
        headerCellStyle.setFont(hSSFFont);

        int headerRow = 0;
        {

            HSSFRow titleRow = sheet.createRow(headerRow++);
            titleRow.setHeightInPoints(100);

            HSSFCell firsttitleCell = titleRow.createCell(0);

            BufferedImage originalImageByteHeader = new VLogo().getCorporateLogo();

            ByteArrayOutputStream baosByteheader = new ByteArrayOutputStream();
            ImageIO.write(originalImageByteHeader, "jpg", baosByteheader);
            baosByteheader.flush();
            byte[] imageInByteSingleHeader = baosByteheader.toByteArray();
            baosByteheader.close();

            final int imageIndexHeader = workBook.addPicture(imageInByteSingleHeader, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);


            CreationHelper helper = workBook.getCreationHelper();
            Drawing drawingPatriarch = sheet.createDrawingPatriarch();
            final ClientAnchor imageAnchor = helper.createClientAnchor();
            imageAnchor.setAnchorType(ClientAnchor.DONT_MOVE_AND_RESIZE);
            imageAnchor.setCol1(0);
            imageAnchor.setCol2(1);
            imageAnchor.setRow1(0);
            imageAnchor.setRow2(1);

            HSSFPicture picture = patriarch.createPicture(imageAnchor, imageIndexHeader);
            picture.resize(2.0, 1);

            HSSFRow secondRow = sheet.createRow(headerRow);
            HSSFCell secondtitleCell = secondRow.createCell(0);
            secondtitleCell.setCellValue(reportTitle);
            secondtitleCell.setCellStyle(headerCellStyle);


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, tableColumnName.size()));

            headerRow++;
            HSSFRow spacerow = sheet.createRow(headerRow);

            int noOfRowInBody = bodycontent.size() / 2;
            int noOfCellInBody = noOfRowInBody * 2;

            int tableheaderNo = tableColumnName.size();
            int noOfHeaderNeed = tableheaderNo + 1;
            int noOfHeaderForFirstCol = (noOfHeaderNeed) / 2;
            int noOfHeaderForSecCol = noOfHeaderNeed - noOfHeaderForFirstCol;

            Set<String> keySet = bodycontent.keySet();

            List<String> list = new ArrayList<String>(keySet);
            HSSFRow row = null;
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                String value = bodycontent.get(key);
                
                if (i % 2 == 0) {

                    headerRow++;
                    row = sheet.createRow(headerRow);
                    HSSFCell[] cells = new HSSFCell[noOfHeaderNeed];

                    for (int l = 0; l < noOfHeaderForFirstCol; l++) {
                        cells[l] = row.createCell(l);
                        cells[l].setCellValue(" ");
                        

                    }
                    cells[(noOfHeaderForFirstCol / 2)].setCellValue(key + "  :  " + value);
                    cells[(noOfHeaderForFirstCol / 2)].setCellStyle(header_style_left_body);

                } else if (i % 2 == 1) {
                    HSSFCell[] cell = new HSSFCell[noOfHeaderNeed];
                    for (int t = noOfHeaderForFirstCol; t < noOfHeaderNeed; t++) {
                        cell[t] = row.createCell(t);
                        cell[t].setCellValue(" ");
                        

                    }

                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellValue(key + "  :  " + value);
                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellStyle(header_style_right_body);

                }
            }

            int tableColumnCount = tableColumnName.size();

            headerRow++;
            headerRow++;
            HSSFRow columnrow = sheet.createRow(headerRow);

            columnrow.setHeightInPoints(20);
            HSSFCell[] cellsInTableheader = new HSSFCell[tableColumnCount + 1];
            int count = 0;
            Iterator<String> it = tableColumnName.iterator();
            while (it.hasNext()) {
                if (count == 0) {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue("SL");
                    cellsInTableheader[count].setCellStyle(header_style_center);

                    sheet.setColumnWidth(count, 2000);

                } else {

                    String columsValue = it.next();

                    int colomnWidth = 6;
                    try {
                        colomnWidth = Math.max(Math.min(columsValue.trim().length(), 8), 4);
                    } catch (Exception e) {
                    	LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
                    }

                    if (columsValue.trim().toUpperCase().contains("TIME") || columsValue.trim().toUpperCase().contains("DATE")
                            || columsValue.trim().toUpperCase().contains("IP")) {
                        colomnWidth = 8;
                    }

                    if (columsValue.trim().equals("Network Storage [DOWN] [UP]") || columsValue.trim().equals("Local Storage [DOWN] [UP]")) {
                        colomnWidth = 10;
                    }

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue(columsValue);
                    cellsInTableheader[count].setCellStyle(header_style_center);
                    sheet.setColumnWidth(count, colomnWidth * 1000);

                }

                count++;
            }

            int countNoOfRow = 0;

            Iterator<?> itaddvalue = tableContent.iterator();
            if (tableContent.size() > 0) {
                while (itaddvalue.hasNext()) {

                    int columnNo = 0;
                    headerRow++;
                    HSSFRow datarow = sheet.createRow(headerRow);
                    datarow.setHeightInPoints(30);
                    HSSFCell[] cellsInTableContent = new HSSFCell[tableColumnCount + 1];
                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                    cellsInTableContent[columnNo].setCellValue(countNoOfRow + 1);
                    cellsInTableContent[columnNo].setCellStyle(body_style);

                    countNoOfRow++;
                    columnNo++;

                    Object rowValue = itaddvalue.next();
                    Class<?> cl = rowValue.getClass();
                    Set<Entry<String, String>> entiresMethodNReturnType = getterNReturn.entrySet();
                    for (Entry<String, String> ent : entiresMethodNReturnType) {
                        Method m = cl.getMethod(ent.getKey());
                        Object obj = m.invoke(rowValue);
                        Class<?> cls = Class.forName(ent.getValue());

                        if (cls.cast(obj) instanceof byte[]) {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                    headerRow, (short) (columnNo + 1), headerRow + 1);
                            anchor.setAnchorType(2);
                            byte[] byteA = (byte[]) cls.cast(obj);
                            int imageIndexSingle = workBook.addPicture(byteA, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);

                            patriarch.createPicture(anchor, imageIndexSingle);
                            columnNo++;

                        } else if (cls.cast(obj) instanceof ArrayList) {
                            ArrayList<byte[]> snapListofImage = (ArrayList<byte[]>) cls.cast(obj);
                            int countofRow = 0;

                            if (snapListofImage.size() > 1) {
                                if (snapListofImage.size() <= (tableColumnName.size() + 1)) {

                                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                    cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                    cellsInTableContent[columnNo].setCellStyle(body_style);
                                    headerRow++;
                                    HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                    dataMultipleRow.setHeightInPoints(100);
                                    HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];

                                    HSSFClientAnchor anchor = null;

                                    for (int i = 0; i < snapListofImage.size(); i++) {

                                        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + i),
                                                headerRow, (short) (countofRow + i + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(i), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);
                                        cellsInTableContentImage[i] = dataMultipleRow.createCell(i);

                                        countNoOfRow++;

                                    }

                                } else {
                                    int noOfRow = snapListofImage.size() / 5;
                                    int noOfRowNeed = snapListofImage.size() % 5;
                                    HSSFCell[] cellsInTableContentImage = new HSSFCell[snapListofImage.size()];
                                    for (int i = 0; i < noOfRow; i++) {

                                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                        cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                        cellsInTableContent[columnNo].setCellStyle(body_style);

                                        headerRow++;
                                        HSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                        dataMultipleRow.setHeightInPoints(100);

                                        HSSFClientAnchor anchor = null;

                                        for (int j = 0; j < (tableColumnName.size() + 1); j++) {

                                            anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                    headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                            int imageIndex = workBook.addPicture(snapListofImage.get(j), org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);
                                            patriarch.createPicture(anchor, imageIndex);
                                            cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                        }

                                    }

                                    headerRow++;
                                    HSSFRow dataMultipleRow = sheet.createRow(headerRow);
                                    HSSFCell titleCell;
                                    dataMultipleRow.setHeightInPoints(100);

                                    HSSFClientAnchor anchor = null;

                                    for (int j = 0; j < noOfRowNeed; j++) {

                                        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);

                                        cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                    }

                                }
                            } else if (snapListofImage.size() == 1) {
                                datarow.setHeightInPoints(100);
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                        headerRow, (short) (columnNo + 1), headerRow + 1);
                                anchor.setAnchorType(2);

                                int imageIndexSingle = workBook.addPicture(snapListofImage.get(0), org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);

                                patriarch.createPicture(anchor, imageIndexSingle);

                            } else {
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                            }
                        } else {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue(obj != null ? cls.cast(obj).toString() : "");
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            columnNo++;

                        }
                    }
                }
            } else {

                headerRow++;
                HSSFRow emptyRow = sheet.createRow(headerRow);
                HSSFCell emptyCell = emptyRow.createCell(0);
                emptyCell.setCellValue(noDataMsg);
                emptyCell.setCellStyle(body_style);

                sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, tableColumnName.size()));

            }

            // add Disclaimer:
            HSSFCellStyle disclaimer_header_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            disclaimer_header_style.setFont(font);
            disclaimer_header_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_header_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_header_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderLeft(CellStyle.BORDER_NONE);

            HSSFCellStyle disclaimer_body_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
            disclaimer_body_style.setFont(font);
            disclaimer_body_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_body_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderLeft(CellStyle.BORDER_NONE);

            headerRow++;
            headerRow++;
            HSSFRow disclaimerRow = sheet.createRow(headerRow);

            HSSFCell disclaimerRowCell = disclaimerRow.createCell(0);
            disclaimerRowCell.setCellValue("Disclaimer:");
            disclaimerRowCell.setCellStyle(disclaimer_header_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 1));

            headerRow++;

            HSSFRow disclaimerDataRow = sheet.createRow(headerRow);

            HSSFCell disclaimerDataRowCell = disclaimerDataRow.createCell(0);
            disclaimerDataRowCell.setCellValue("This is a confidential report and should not be shared without the consent of the owner.");
            disclaimerDataRowCell.setCellStyle(disclaimer_body_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 3));

            FileOutputStream fos = null;
            fos = new FileOutputStream(fileName);
            workBook.write(fos);
            fos.close();

            if (password != null) {
                setPassword(fileName, password);
            }
        }
        return statisticFile.getAbsolutePath();

    }

    @SuppressWarnings({"unused", "resource", "deprecation"})
    public static String createExcelX(String fileName, String reportTitle, Map<String, String> bodycontent, List<String> tableColumnName, Map<String, String> getterNReturn, List<?> tableContent,
            List<String> summaryContent, String password, String noDataMsg, String certificatePassword, String certificateAlias) throws IOException, NoSuchMethodException, SecurityException,
            ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
    	String tempFileName = fileName;
    	if (password != null) {
    		try {
        		String[] splitedString = tempFileName.split("\\.");
        		tempFileName = splitedString[0] + "_" + new Date().getTime() + "." + splitedString[1];
        		
    		} catch (Exception e) { 
    			LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
    		}
    	}
    	
    	File statisticFile = new File(tempFileName);

        CellStyle header_style_center, header_style_left_body, header_style_right_body, header_style_left_Space, header_style_right_Space;

        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet(reportTitle);
        XSSFDrawing patriarch = sheet.createDrawingPatriarch();

        Font font = workBook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_center = workBook.createCellStyle();
        header_style_center.setFont(font);
        header_style_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_center.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        header_style_center.setAlignment(CellStyle.ALIGN_CENTER);
        header_style_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_center.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center.setBorderTop(CellStyle.BORDER_THIN);
        header_style_center.setBorderRight(CellStyle.BORDER_THIN);
        header_style_center.setBorderLeft(CellStyle.BORDER_THIN);

        font = workBook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_left_body = workBook.createCellStyle();
        header_style_left_body.setFont(font);
        header_style_left_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_left_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_left_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_left_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_body = workBook.createCellStyle();
        header_style_right_body.setFont(font);
        header_style_right_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_right_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_right_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_right_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_Space = workBook.createCellStyle();
        header_style_right_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_Space.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_Space.setBorderTop(CellStyle.BORDER_THIN);

        XSSFCellStyle body_style = workBook.createCellStyle();
        font = workBook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        body_style.setFont(font);
        body_style.setAlignment(CellStyle.ALIGN_CENTER);
        body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        body_style.setBorderBottom(CellStyle.BORDER_THIN);
        body_style.setBorderTop(CellStyle.BORDER_THIN);
        body_style.setBorderRight(CellStyle.BORDER_THIN);
        body_style.setBorderLeft(CellStyle.BORDER_THIN);

        XSSFCellStyle headerCellStyle = workBook.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
       
        
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderTop(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderRight(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
        XSSFFont hSSFFont = workBook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 14);
        hSSFFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.WHITE.index);
        headerCellStyle.setFont(hSSFFont);

        int headerRow = 0;
        {

            XSSFRow titleRow = sheet.createRow(headerRow++);
            titleRow.setHeightInPoints(100);

            XSSFCell firsttitleCell = titleRow.createCell(0);

            BufferedImage originalImageByteHeader = new VLogo().getCorporateLogo();

            ByteArrayOutputStream baosByteheader = new ByteArrayOutputStream();
            ImageIO.write(originalImageByteHeader, "jpg", baosByteheader);
            baosByteheader.flush();
            byte[] imageInByteSingleHeader = baosByteheader.toByteArray();
            baosByteheader.close();

            final int imageIndexHeader = workBook.addPicture(imageInByteSingleHeader, HSSFWorkbook.PICTURE_TYPE_JPEG);



            CreationHelper helper = workBook.getCreationHelper();
            Drawing drawingPatriarch = sheet.createDrawingPatriarch();
            final ClientAnchor imageAnchor = helper.createClientAnchor();
            imageAnchor.setAnchorType(ClientAnchor.DONT_MOVE_AND_RESIZE);
            imageAnchor.setCol1(0);
            imageAnchor.setCol2(1);
            imageAnchor.setRow1(0);
            imageAnchor.setRow2(1);

            XSSFPicture picture = patriarch.createPicture(imageAnchor, imageIndexHeader);
            picture.resize(2.0, 1);

            XSSFRow secondRow = sheet.createRow(headerRow);


            XSSFCell secondtitleCell = secondRow.createCell(0);
            secondtitleCell.setCellValue(reportTitle);
            secondtitleCell.setCellStyle(headerCellStyle);


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, tableColumnName.size()));

            headerRow++;
            XSSFRow spacerow = sheet.createRow(headerRow);

            int noOfRowInBody = bodycontent.size() / 2;
            int noOfCellInBody = noOfRowInBody * 2;

            int tableheaderNo = tableColumnName.size();
            int noOfHeaderNeed = tableheaderNo + 1;
            int noOfHeaderForFirstCol = (noOfHeaderNeed) / 2;
            int noOfHeaderForSecCol = noOfHeaderNeed - noOfHeaderForFirstCol;

            Set<String> keySet = bodycontent.keySet();

            List<String> list = new ArrayList<String>(keySet);
            XSSFRow row = null;
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                String value = bodycontent.get(key);
                
                if (i % 2 == 0) {

                    headerRow++;
                    row = sheet.createRow(headerRow);
                    XSSFCell[] cells = new XSSFCell[noOfHeaderNeed];

                    for (int l = 0; l < noOfHeaderForFirstCol; l++) {
                        cells[l] = row.createCell(l);
                        cells[l].setCellValue(" ");
                        

                    }
                    cells[(noOfHeaderForFirstCol / 2)].setCellValue(key + "  :  " + value);
                    cells[(noOfHeaderForFirstCol / 2)].setCellStyle(header_style_left_body);

                } else if (i % 2 == 1) {
                    XSSFCell[] cell = new XSSFCell[noOfHeaderNeed];
                    for (int t = noOfHeaderForFirstCol; t < noOfHeaderNeed; t++) {
                        cell[t] = row.createCell(t);
                        cell[t].setCellValue(" ");
                        

                    }

                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellValue(key + "  :  " + value);
                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellStyle(header_style_right_body);

                }
            }

            int tableColumnCount = tableColumnName.size();

            headerRow++;
            headerRow++;
            XSSFRow columnrow = sheet.createRow(headerRow);

            columnrow.setHeightInPoints(20);
            XSSFCell[] cellsInTableheader = new XSSFCell[tableColumnCount + 1];
            int count = 0;
            Iterator<String> it = tableColumnName.iterator();
            while (it.hasNext()) {
                if (count == 0) {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue("SL");
                    cellsInTableheader[count].setCellStyle(header_style_center);

                    sheet.setColumnWidth(count, 2000);

                } else {

                    String columsValue = it.next();
                    int colomnWidth = 6;
                    try {
                        colomnWidth = Math.max(Math.min(columsValue.trim().length(), 8), 4);
                    } catch (Exception e) {
                    	LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
                    }

                    if (columsValue.trim().toUpperCase().contains("TIME") || columsValue.trim().toUpperCase().contains("DATE")
                            || columsValue.trim().toUpperCase().contains("IP")) {
                        colomnWidth = 8;
                    }

                    if (columsValue.trim().equals("Camera Availability(%)") || columsValue.trim().equals("Camera Recording Availability(%)")) {
                        colomnWidth = 10;
                    }

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue(columsValue);
                    cellsInTableheader[count].setCellStyle(header_style_center);
                    sheet.setColumnWidth(count, colomnWidth * 1000);

                }

                count++;
            }

            int countNoOfRow = 0;

            Iterator<?> itaddvalue = tableContent.iterator();
            if (tableContent.size() > 0) {
                while (itaddvalue.hasNext()) {

                    int columnNo = 0;
                    headerRow++;
                    XSSFRow datarow = sheet.createRow(headerRow);
                    datarow.setHeightInPoints(30);
                    XSSFCell[] cellsInTableContent = new XSSFCell[tableColumnCount + 1];
                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                    cellsInTableContent[columnNo].setCellValue(countNoOfRow + 1);
                    cellsInTableContent[columnNo].setCellStyle(body_style);

                    countNoOfRow++;
                    columnNo++;

                    Object rowValue = itaddvalue.next();
                    Class<?> cl = rowValue.getClass();
                    Set<Entry<String, String>> entiresMethodNReturnType = getterNReturn.entrySet();
                    for (Entry<String, String> ent : entiresMethodNReturnType) {
                        Method m = cl.getMethod(ent.getKey());
                        Object obj = m.invoke(rowValue);
                        Class<?> cls = Class.forName(ent.getValue());

                        if (cls.cast(obj) instanceof byte[]) {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                    headerRow, (short) (columnNo + 1), headerRow + 1);
                            anchor.setAnchorType(2);
                            byte[] byteA = (byte[]) cls.cast(obj);
                            int imageIndexSingle = workBook.addPicture(byteA, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);

                            patriarch.createPicture(anchor, imageIndexSingle);
                            columnNo++;

                        } else if (cls.cast(obj) instanceof ArrayList) {
                            ArrayList<byte[]> snapListofImage = (ArrayList<byte[]>) cls.cast(obj);
                            int countofRow = 0;

                            if (snapListofImage.size() > 1) {
                                if (snapListofImage.size() <= (tableColumnName.size() + 1)) {

                                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                    cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                    cellsInTableContent[columnNo].setCellStyle(body_style);
                                    headerRow++;
                                    XSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                    dataMultipleRow.setHeightInPoints(100);
                                    XSSFCell[] cellsInTableContentImage = new XSSFCell[snapListofImage.size()];

                                    HSSFClientAnchor anchor = null;

                                    for (int i = 0; i < snapListofImage.size(); i++) {

                                        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + i),
                                                headerRow, (short) (countofRow + i + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(i), org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);
                                        cellsInTableContentImage[i] = dataMultipleRow.createCell(i);

                                        countNoOfRow++;

                                    }

                                } else {
                                    int noOfRow = snapListofImage.size() / 5;
                                    int noOfRowNeed = snapListofImage.size() % 5;
                                    XSSFCell[] cellsInTableContentImage = new XSSFCell[snapListofImage.size()];
                                    for (int i = 0; i < noOfRow; i++) {

                                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                        cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                        cellsInTableContent[columnNo].setCellStyle(body_style);

                                        headerRow++;
                                        XSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                        dataMultipleRow.setHeightInPoints(100);

                                        HSSFClientAnchor anchor = null;

                                        for (int j = 0; j < (tableColumnName.size() + 1); j++) {

                                            anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                    headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                            int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                            patriarch.createPicture(anchor, imageIndex);
                                            cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                        }

                                    }

                                    headerRow++;
                                    XSSFRow dataMultipleRow = sheet.createRow(headerRow);
                                    HSSFCell titleCell;
                                    dataMultipleRow.setHeightInPoints(100);

                                    HSSFClientAnchor anchor = null;

                                    for (int j = 0; j < noOfRowNeed; j++) {

                                        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);

                                        cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                    }

                                }
                            } else if (snapListofImage.size() == 1) {
                                datarow.setHeightInPoints(100);
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                        headerRow, (short) (columnNo + 1), headerRow + 1);
                                anchor.setAnchorType(2);

                                int imageIndexSingle = workBook.addPicture(snapListofImage.get(0), HSSFWorkbook.PICTURE_TYPE_JPEG);

                                patriarch.createPicture(anchor, imageIndexSingle);

                            } else {
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                            }
                        } else {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue(cls.cast(obj).toString());
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            columnNo++;

                        }
                    }
                }
            } else {
                headerRow++;
                XSSFRow emptyRow = sheet.createRow(headerRow);
                XSSFCell emptyCell = emptyRow.createCell(0);
                emptyCell.setCellValue(noDataMsg);
                emptyCell.setCellStyle(body_style);

                sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, tableColumnName.size()));
            }

            headerRow++;
            headerRow++;

            int summaryCount = 0;
            Iterator<String> summartIt = summaryContent.iterator();
            XSSFRow summaryContentrow = sheet.createRow(headerRow);
            XSSFCell[] summaryContentcellsInTableheader = new XSSFCell[tableColumnCount + 1];

            summaryCount = ((tableColumnCount + 1) - 2);
            while (summartIt.hasNext()) {

                if (summaryCount == ((tableColumnCount + 1) - 2)) {

                    summaryContentcellsInTableheader[summaryCount] = summaryContentrow.createCell(summaryCount);
                    summaryContentcellsInTableheader[summaryCount].setCellValue(summartIt.next());
                    summaryContentcellsInTableheader[summaryCount].setCellStyle(body_style);
                    summaryCount++;
                } else if (summaryCount == tableColumnCount) {

                    summaryContentcellsInTableheader[summaryCount] = summaryContentrow.createCell(summaryCount);
                    summaryContentcellsInTableheader[summaryCount].setCellValue(summartIt.next());
                    summaryContentcellsInTableheader[summaryCount].setCellStyle(body_style);

                    summaryCount = ((tableColumnCount + 1) - 2);
                    headerRow++;
                    summaryContentrow = sheet.createRow(headerRow);
                }
            }

            // add Disclaimer:
            XSSFCellStyle disclaimer_header_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            disclaimer_header_style.setFont(font);
            disclaimer_header_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_header_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_header_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderLeft(CellStyle.BORDER_NONE);

            XSSFCellStyle disclaimer_body_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
            disclaimer_body_style.setFont(font);
            disclaimer_body_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_body_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderLeft(CellStyle.BORDER_NONE);

            headerRow++;
            headerRow++;
            XSSFRow disclaimerRow = sheet.createRow(headerRow);

            XSSFCell disclaimerRowCell = disclaimerRow.createCell(0);
            disclaimerRowCell.setCellValue("Disclaimer:");
            disclaimerRowCell.setCellStyle(disclaimer_header_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 1));

            headerRow++;

            XSSFRow disclaimerDataRow = sheet.createRow(headerRow);

            XSSFCell disclaimerDataRowCell = disclaimerDataRow.createCell(0);
            disclaimerDataRowCell.setCellValue("This is a confidential report and should not be shared without the consent of the owner.");
            disclaimerDataRowCell.setCellStyle(disclaimer_body_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 3));

            FileOutputStream fos = new FileOutputStream(tempFileName);
            workBook.write(fos);
            fos.close();

            setSignature(tempFileName, certificatePassword, certificateAlias);
            if (password != null) {
            	setPasswordXLSX(tempFileName, fileName, password);
            	
            	 File delFile = new File(tempFileName);
                 try {Files.delete(Paths.get(tempFileName));} catch (Exception e) { try { delFile.deleteOnExit(); } catch (Exception e2) { 
                	 LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
                 } }
            }
            
           
        }
        return statisticFile.getAbsolutePath();

    }

    
    @SuppressWarnings({"unused", "resource", "deprecation"})
    public static String createExcelX(String fileName, String reportTitle, Map<String, String> bodycontent, List<String> tableColumnName,
            Map<String, String> getterNReturn, List<?> tableContent, String password, String noDataMsg, String certificatePassword, String certificateAlias)
            throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    	
    	String tempFileName = fileName;
    	if (password != null) {
    		try {
        		String[] splitedString = tempFileName.split("\\.");
        		tempFileName = splitedString[0] + "_" + new Date().getTime() + "." + splitedString[1];
        		
    		} catch (Exception e) { 
    			LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
    		}
    	}
    	
        File statisticFile = new File(tempFileName);
        String parentPath = statisticFile.getParent();
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        CellStyle header_style_center, header_style_left_body, header_style_right_body, header_style_left_Space, header_style_right_Space;

        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet(reportTitle);
        XSSFDrawing patriarch = sheet.createDrawingPatriarch();

        Font font = workBook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_center = workBook.createCellStyle();
        header_style_center.setFont(font);
        header_style_center.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        header_style_center.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        header_style_center.setAlignment(CellStyle.ALIGN_CENTER);
        header_style_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_center.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center.setBorderTop(CellStyle.BORDER_THIN);
        header_style_center.setBorderRight(CellStyle.BORDER_THIN);
        header_style_center.setBorderLeft(CellStyle.BORDER_THIN);

        font = workBook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_left_body = workBook.createCellStyle();
        header_style_left_body.setFont(font);
        header_style_left_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_left_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_left_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_left_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_left_Space = workBook.createCellStyle();
        header_style_left_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_Space.setFillForegroundColor(new HSSFColor.CORAL().getIndex());
        header_style_left_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_Space.setBorderTop(CellStyle.BORDER_THIN);

        header_style_right_body = workBook.createCellStyle();
        header_style_right_body.setFont(font);
        header_style_right_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_right_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_right_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_right_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_Space = workBook.createCellStyle();
        header_style_right_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_Space.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_Space.setBorderTop(CellStyle.BORDER_THIN);

        XSSFCellStyle body_style = workBook.createCellStyle();
        font = workBook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        body_style.setFont(font);
        body_style.setAlignment(CellStyle.ALIGN_CENTER);
        body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        body_style.setBorderBottom(CellStyle.BORDER_THIN);
        body_style.setBorderTop(CellStyle.BORDER_THIN);
        body_style.setBorderRight(CellStyle.BORDER_THIN);
        body_style.setBorderLeft(CellStyle.BORDER_THIN);

        XSSFCellStyle headerCellStyle = workBook.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        
        
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderTop(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderRight(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
        XSSFFont hSSFFont = workBook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 14);
        hSSFFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.WHITE.index);
        headerCellStyle.setFont(hSSFFont);

        int headerRow = 0;
        {

            XSSFRow titleRow = sheet.createRow(headerRow++);
            titleRow.setHeightInPoints(100);

            XSSFCell firsttitleCell = titleRow.createCell(0);

            BufferedImage originalImageByteHeader = new VLogo().getCorporateLogo();

            ByteArrayOutputStream baosByteheader = new ByteArrayOutputStream();
            ImageIO.write(originalImageByteHeader, "jpg", baosByteheader);
            baosByteheader.flush();
            byte[] imageInByteSingleHeader = baosByteheader.toByteArray();
            baosByteheader.close();

            final int imageIndexHeader = workBook.addPicture(imageInByteSingleHeader, HSSFWorkbook.PICTURE_TYPE_JPEG);


            CreationHelper helper = workBook.getCreationHelper();
            Drawing drawingPatriarch = sheet.createDrawingPatriarch();
            final ClientAnchor imageAnchor = helper.createClientAnchor();
            imageAnchor.setAnchorType(ClientAnchor.DONT_MOVE_AND_RESIZE);
            imageAnchor.setCol1(0);
            imageAnchor.setCol2(1);
            imageAnchor.setRow1(0);
            imageAnchor.setRow2(1);

            XSSFPicture picture = patriarch.createPicture(imageAnchor, imageIndexHeader);
            picture.resize(2.0, 1);

            XSSFRow secondRow = sheet.createRow(headerRow);
            XSSFCell secondtitleCell = secondRow.createCell(0);
            secondtitleCell.setCellValue(reportTitle);
            secondtitleCell.setCellStyle(headerCellStyle);


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, tableColumnName.size()));

            headerRow++;
            XSSFRow spacerow = sheet.createRow(headerRow);

            int noOfRowInBody = bodycontent.size() / 2;
            int noOfCellInBody = noOfRowInBody * 2;

            int tableheaderNo = tableColumnName.size();
            int noOfHeaderNeed = tableheaderNo + 1;
            int noOfHeaderForFirstCol = (noOfHeaderNeed) / 2;
            int noOfHeaderForSecCol = noOfHeaderNeed - noOfHeaderForFirstCol;

            Set<String> keySet = bodycontent.keySet();

            List<String> list = new ArrayList<String>(keySet);
            XSSFRow row = null;
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                String value = bodycontent.get(key);
                
                if (i % 2 == 0) {

                    headerRow++;
                    row = sheet.createRow(headerRow);
                    XSSFCell[] cells = new XSSFCell[noOfHeaderNeed];

                    for (int l = 0; l < noOfHeaderForFirstCol; l++) {
                        cells[l] = row.createCell(l);
                        cells[l].setCellValue(" ");
                       
                    }
                    cells[(noOfHeaderForFirstCol / 2)].setCellValue(key + "  :  " + value);
                    cells[(noOfHeaderForFirstCol / 2)].setCellStyle(header_style_left_body);

                } else if (i % 2 == 1) {
                    XSSFCell[] cell = new XSSFCell[noOfHeaderNeed];
                    for (int t = noOfHeaderForFirstCol; t < noOfHeaderNeed; t++) {
                        cell[t] = row.createCell(t);
                        cell[t].setCellValue(" ");
                        

                    }

                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellValue(key + "  :  " + value);
                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellStyle(header_style_right_body);

                }
            }

            int tableColumnCount = tableColumnName.size();

            headerRow++;
            headerRow++;
            XSSFRow columnrow = sheet.createRow(headerRow);

            columnrow.setHeightInPoints(20);
            XSSFCell[] cellsInTableheader = new XSSFCell[tableColumnCount + 1];
            int count = 0;
            Iterator<String> it = tableColumnName.iterator();
            while (it.hasNext()) {
                if (count == 0) {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue("SL");
                    cellsInTableheader[count].setCellStyle(header_style_center);

                    sheet.setColumnWidth(count, 2000);

                } else {

                    String columsValue = it.next();

                    int colomnWidth = 6;
                    try {
                        colomnWidth = Math.max(Math.min(columsValue.trim().length(), 8), 4);
                    } catch (Exception e) {
                    	LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
                    }

                    if (columsValue.trim().toUpperCase().contains("TIME") || columsValue.trim().toUpperCase().contains("DATE")
                            || columsValue.trim().toUpperCase().contains("IP")) {
                        colomnWidth = 8;
                    }

                    if (columsValue.trim().equals("Network Storage [DOWN] [UP]") || columsValue.trim().equals("Local Storage [DOWN] [UP]")) {
                        colomnWidth = 10;
                    }

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue(columsValue);
                    cellsInTableheader[count].setCellStyle(header_style_center);
                    sheet.setColumnWidth(count, colomnWidth * 1000);

                }

                count++;
            }

            int countNoOfRow = 0;

            Iterator<?> itaddvalue = tableContent.iterator();
            if (tableContent.size() > 0) {
                while (itaddvalue.hasNext()) {

                    int columnNo = 0;
                    headerRow++;
                    XSSFRow datarow = sheet.createRow(headerRow);
                    datarow.setHeightInPoints(30);
                    XSSFCell[] cellsInTableContent = new XSSFCell[tableColumnCount + 1];
                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                    cellsInTableContent[columnNo].setCellValue(countNoOfRow + 1);
                    cellsInTableContent[columnNo].setCellStyle(body_style);

                    countNoOfRow++;
                    columnNo++;

                    Object rowValue = itaddvalue.next();
                    Class<?> cl = rowValue.getClass();
                    Set<Entry<String, String>> entiresMethodNReturnType = getterNReturn.entrySet();
                    for (Entry<String, String> ent : entiresMethodNReturnType) {
                        Method m = cl.getMethod(ent.getKey());
                        Object obj = m.invoke(rowValue);
                        Class<?> cls = Class.forName(ent.getValue());

                        if (cls.cast(obj) instanceof byte[]) {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                    headerRow, (short) (columnNo + 1), headerRow + 1);
                            anchor.setAnchorType(2);
                            byte[] byteA = (byte[]) cls.cast(obj);
                            int imageIndexSingle = workBook.addPicture(byteA, HSSFWorkbook.PICTURE_TYPE_JPEG);

                            patriarch.createPicture(anchor, imageIndexSingle);
                            columnNo++;

                        } else if (cls.cast(obj) instanceof ArrayList) {
                            ArrayList<byte[]> snapListofImage = (ArrayList<byte[]>) cls.cast(obj);
                            int countofRow = 0;

                            if (snapListofImage.size() > 1) {
                                if (snapListofImage.size() <= (tableColumnName.size() + 1)) {

                                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                    cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                    cellsInTableContent[columnNo].setCellStyle(body_style);
                                    headerRow++;
                                    XSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                    dataMultipleRow.setHeightInPoints(100);
                                    XSSFCell[] cellsInTableContentImage = new XSSFCell[snapListofImage.size()];

                                    XSSFClientAnchor anchor = null;

                                    for (int i = 0; i < snapListofImage.size(); i++) {

                                        anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + i),
                                                headerRow, (short) (countofRow + i + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(i), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);
                                        cellsInTableContentImage[i] = dataMultipleRow.createCell(i);

                                        countNoOfRow++;

                                    }

                                } else {
                                    int noOfRow = snapListofImage.size() / 5;
                                    int noOfRowNeed = snapListofImage.size() % 5;
                                    XSSFCell[] cellsInTableContentImage = new XSSFCell[snapListofImage.size()];
                                    for (int i = 0; i < noOfRow; i++) {

                                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                        cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                        cellsInTableContent[columnNo].setCellStyle(body_style);

                                        headerRow++;
                                        XSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                        dataMultipleRow.setHeightInPoints(100);

                                        XSSFClientAnchor anchor = null;

                                        for (int j = 0; j < (tableColumnName.size() + 1); j++) {

                                            anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                    headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                            int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                            patriarch.createPicture(anchor, imageIndex);
                                            cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                        }

                                    }

                                    headerRow++;
                                    XSSFRow dataMultipleRow = sheet.createRow(headerRow);
                                    XSSFCell titleCell;
                                    dataMultipleRow.setHeightInPoints(100);

                                    XSSFClientAnchor anchor = null;

                                    for (int j = 0; j < noOfRowNeed; j++) {

                                        anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(j), HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);

                                        cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                    }

                                }
                            } else if (snapListofImage.size() == 1) {
                                datarow.setHeightInPoints(100);
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                                XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                        headerRow, (short) (columnNo + 1), headerRow + 1);
                                anchor.setAnchorType(2);

                                int imageIndexSingle = workBook.addPicture(snapListofImage.get(0), HSSFWorkbook.PICTURE_TYPE_JPEG);

                                patriarch.createPicture(anchor, imageIndexSingle);

                            } else {
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                            }
                        } else {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue(obj != null ? cls.cast(obj).toString() : "");
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            columnNo++;

                        }
                    }
                }
            } else {

                headerRow++;
                XSSFRow emptyRow = sheet.createRow(headerRow);
                XSSFCell emptyCell = emptyRow.createCell(0);
                emptyCell.setCellValue(noDataMsg);
                emptyCell.setCellStyle(body_style);

                sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, tableColumnName.size()));

            }

            // add Disclaimer:
            XSSFCellStyle disclaimer_header_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            disclaimer_header_style.setFont(font);
            disclaimer_header_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_header_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_header_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderLeft(CellStyle.BORDER_NONE);

            XSSFCellStyle disclaimer_body_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
            disclaimer_body_style.setFont(font);
            disclaimer_body_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_body_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderLeft(CellStyle.BORDER_NONE);

            headerRow++;
            headerRow++;
            XSSFRow disclaimerRow = sheet.createRow(headerRow);

            XSSFCell disclaimerRowCell = disclaimerRow.createCell(0);
            disclaimerRowCell.setCellValue("Disclaimer:");
            disclaimerRowCell.setCellStyle(disclaimer_header_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 1));

            headerRow++;

            XSSFRow disclaimerDataRow = sheet.createRow(headerRow);

            XSSFCell disclaimerDataRowCell = disclaimerDataRow.createCell(0);
            disclaimerDataRowCell.setCellValue("This is a confidential report and should not be shared without the consent of the owner.");
            disclaimerDataRowCell.setCellStyle(disclaimer_body_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 3));

            FileOutputStream fos = null;
            fos = new FileOutputStream(tempFileName);
            workBook.write(fos);
            fos.close();
            
            setSignature(tempFileName, certificatePassword, certificateAlias);
            if (password != null) {
            	setPasswordXLSX(tempFileName, fileName, password);
            	
            	File delFile = new File(tempFileName);
                try {Files.delete(Paths.get(tempFileName));} catch (Exception e) { try { delFile.deleteOnExit(); } catch (Exception e2) { 
                	LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
                } }
            }
            
        }
        return statisticFile.getAbsolutePath();

    }
    
	public static void setPassword(String fileName, String password) {

		POIFSFileSystem poiFileSystem = null;
		
		try (FileInputStream fileInput = new FileInputStream(fileName);
				BufferedInputStream bufferInput = new BufferedInputStream(fileInput);) {

			poiFileSystem = new POIFSFileSystem(bufferInput);

			Biff8EncryptionKey.setCurrentUserPassword(password);
			
		} catch (Exception ex) {

			LOGGER.log(Level.WARNING, "error :{}", ex.getMessage());
		}
		
		if(poiFileSystem != null) {
			try(FileOutputStream fileOut = new FileOutputStream(fileName);
					HSSFWorkbook workbook = new HSSFWorkbook(poiFileSystem, true);){
				
				workbook.writeProtectWorkbook(Biff8EncryptionKey.getCurrentUserPassword(), "");
				workbook.write(fileOut);
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "error :{}", e.getMessage());
			}
		}
	}
    
    public static void setPasswordXLSX(String tempFileName, String fileName, String password) {
        
        try(FileInputStream fileInput = new FileInputStream(tempFileName);
        	FileOutputStream  fileOut = new FileOutputStream(new File(fileName))) {
           
            encrypt(fileInput, fileOut, password);
            
        } catch (Exception ex) {
        	LOGGER.log(Level.WARNING, "error:{} " , ex.getMessage());
        } 
    }
    
    public static void encrypt(InputStream input, OutputStream output, String password)
    		throws IOException, GeneralSecurityException {

    	try {
    		POIFSFileSystem fs = new POIFSFileSystem();
    		EncryptionInfo info = new EncryptionInfo(fs, EncryptionMode.standard);
    		Encryptor enc = info.getEncryptor();
    		enc.confirmPassword(password);

    		OPCPackage opc = OPCPackage.open(input);
    		OutputStream os = enc.getDataStream(fs);
    		opc.save(os);
    		opc.close();

    		fs.writeFilesystem(output);
    		output.close();

    	} catch (Exception e) {
        	LOGGER.log(Level.WARNING, "error:{} " , e.getMessage());
    	} 

    }
    
    public static void setSignature(String fileName, String certificatePassword, String certificateAlias){
    	try {
			
    		String certificateFile = "lib/export/ivmsexport.pfx";
    		
    		
    		char password[] = certificatePassword.toCharArray();
			File file = new File(certificateFile);
			KeyStore keystore = KeyStore.getInstance("PKCS12");
			FileInputStream fis = new FileInputStream(file);
			
			keystore.load(fis, password);
			fis.close();

			X509Certificate x509 = (X509Certificate) keystore.getCertificate(certificateAlias);

			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			// filling the SignatureConfig entries (minimum fields, more options are available ...)
			SignatureConfig signatureConfig = new SignatureConfig();
			signatureConfig.setKey(keyPair.getPrivate());
			signatureConfig.setSigningCertificateChain(Collections.singletonList(x509));
			
            OPCPackage pkg = OPCPackage.open(fileName, PackageAccess.READ_WRITE);
			signatureConfig.setOpcPackage(pkg);
			SignatureInfo signatureInfo = new SignatureInfo();
			signatureInfo.setSignatureConfig(signatureConfig);
			
			try {
				signatureInfo.confirmSignature();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "error:{} " , e.getMessage());
			}

			boolean b = signatureInfo.verifySignature();
			assert (b);
			
			pkg.close();
    		
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "error:{} " , e.getMessage());
		}
    }

    public static String createExcelX(String fileName, String reportTitle, String[] details, Map<String, String> bodycontent, List<String> tableColumnName,
            Map<String, String> getterNReturn, List<?> tableContent, String password, String noDataMsg, String certificatePassword, String certificateAlias)
            throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    	
    	String tempFileName = fileName;
    	if (password != null) {
    		try {
        		String[] splitedString = tempFileName.split("\\.");
        		tempFileName = splitedString[0] + "_" + new Date().getTime() + "." + splitedString[1];
        		
    		} catch (Exception e) { 
    			LOGGER.log(Level.WARNING, "error:{} " , e.getMessage());
    		}
    	}
    	
        File statisticFile = new File(tempFileName);
        String parentPath = statisticFile.getParent();
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        CellStyle header_style_center, header_style_left_body, header_style_right_body, header_style_center_body, header_style_left_Space, header_style_right_Space, header_style_center_space;

        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet(reportTitle);
        XSSFDrawing patriarch = sheet.createDrawingPatriarch();

        Font font = workBook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_center = workBook.createCellStyle();
        header_style_center.setFont(font);
        header_style_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_center.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        header_style_center.setAlignment(CellStyle.ALIGN_CENTER);
        header_style_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_center.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center.setBorderTop(CellStyle.BORDER_THIN);
        header_style_center.setBorderRight(CellStyle.BORDER_THIN);
        header_style_center.setBorderLeft(CellStyle.BORDER_THIN);

        font = workBook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        header_style_left_body = workBook.createCellStyle();
        header_style_left_body.setFont(font);
        header_style_left_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_left_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_left_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_left_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_left_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_left_Space = workBook.createCellStyle();
        header_style_left_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_left_Space.setFillForegroundColor(new HSSFColor.CORAL().getIndex());
        header_style_left_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_left_Space.setBorderTop(CellStyle.BORDER_THIN);

        header_style_right_body = workBook.createCellStyle();
        header_style_right_body.setFont(font);
        header_style_right_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_right_body.setAlignment(CellStyle.ALIGN_LEFT);
        header_style_right_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_right_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_right_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_right_Space = workBook.createCellStyle();
        header_style_right_Space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_right_Space.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_right_Space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_right_Space.setBorderTop(CellStyle.BORDER_THIN);
        
        header_style_center_body = workBook.createCellStyle();
        header_style_center_body.setFont(font);
        header_style_center_body.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_center_body.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        header_style_center_body.setAlignment(CellStyle.ALIGN_CENTER);
        header_style_center_body.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        header_style_center_body.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center_body.setBorderTop(CellStyle.BORDER_THIN);
        header_style_center_body.setBorderRight(CellStyle.BORDER_THIN);
        header_style_center_body.setBorderLeft(CellStyle.BORDER_THIN);

        header_style_center_space = workBook.createCellStyle();
        header_style_center_space.setFillPattern(CellStyle.SOLID_FOREGROUND);
        header_style_center_space.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        header_style_center_space.setBorderBottom(CellStyle.BORDER_THIN);
        header_style_center_space.setBorderTop(CellStyle.BORDER_THIN);

        XSSFCellStyle body_style = workBook.createCellStyle();
        font = workBook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        body_style.setFont(font);
        body_style.setAlignment(CellStyle.ALIGN_CENTER);
        body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        body_style.setBorderBottom(CellStyle.BORDER_THIN);
        body_style.setBorderTop(CellStyle.BORDER_THIN);
        body_style.setBorderRight(CellStyle.BORDER_THIN);
        body_style.setBorderLeft(CellStyle.BORDER_THIN);

        XSSFCellStyle headerCellStyle = workBook.createCellStyle();
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
        
    
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderTop(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderRight(CellStyle.BORDER_NONE);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
        XSSFFont hSSFFont = workBook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 14);
        hSSFFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.WHITE.index);
        headerCellStyle.setFont(hSSFFont);

        int headerRow = 0;
        {

            XSSFRow titleRow = sheet.createRow(headerRow++);
            titleRow.setHeightInPoints(100);

            
            BufferedImage originalImageByteHeader = new VLogo().getCorporateLogo();

            ByteArrayOutputStream baosByteheader = new ByteArrayOutputStream();
            ImageIO.write(originalImageByteHeader, "jpg", baosByteheader);
            baosByteheader.flush();
            byte[] imageInByteSingleHeader = baosByteheader.toByteArray();
            baosByteheader.close();

            final int imageIndexHeader = workBook.addPicture(imageInByteSingleHeader, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);


            CreationHelper helper = workBook.getCreationHelper();
           
            final ClientAnchor imageAnchor = helper.createClientAnchor();
            imageAnchor.setAnchorType(ClientAnchor.DONT_MOVE_AND_RESIZE);
            imageAnchor.setCol1(0);
            imageAnchor.setCol2(1);
            imageAnchor.setRow1(0);
            imageAnchor.setRow2(1);

            XSSFPicture picture = patriarch.createPicture(imageAnchor, imageIndexHeader);
            picture.resize(2.0, 1);

            XSSFRow secondRow = sheet.createRow(headerRow);
            XSSFCell secondtitleCell = secondRow.createCell(0);
            secondtitleCell.setCellValue(reportTitle);
            secondtitleCell.setCellStyle(headerCellStyle);
            
            headerRow++;
            headerRow++;
            
            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 4 , 6));
            XSSFRow thirdRow = sheet.createRow(headerRow);
            XSSFCell thirdtitleCell = thirdRow.createCell(4);
            thirdtitleCell.setCellValue(details[0]);
            thirdtitleCell.setCellStyle(headerCellStyle);
            
            headerRow++;
            
            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 4 , 6));
            thirdRow = sheet.createRow(headerRow);
            thirdtitleCell = thirdRow.createCell(4);
            thirdtitleCell.setCellValue(details[1]);
            thirdtitleCell.setCellStyle(headerCellStyle);
            
            headerRow++;
            
            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 4 , 6));
            thirdRow = sheet.createRow(headerRow);
            thirdtitleCell = thirdRow.createCell(4);
            thirdtitleCell.setCellValue(details[2]);
            thirdtitleCell.setCellStyle(headerCellStyle);

            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, tableColumnName.size()));

            headerRow++;
            headerRow++;

            

            
            

            int tableheaderNo = tableColumnName.size();
            int noOfHeaderNeed = tableheaderNo + 1;
            int noOfHeaderForFirstCol = (noOfHeaderNeed) / 2;
          

            Set<String> keySet = bodycontent.keySet();

            List<String> list = new ArrayList<String>(keySet);
            XSSFRow row = null;
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                String value = bodycontent.get(key);
               
                if (i % 2 == 0) {

                    headerRow++;
                    row = sheet.createRow(headerRow);
                    XSSFCell[] cells = new XSSFCell[noOfHeaderNeed];

                    for (int l = 0; l < noOfHeaderForFirstCol; l++) {
                        cells[l] = row.createCell(l);
                        cells[l].setCellValue(" ");
                       

                    }
                    cells[(noOfHeaderForFirstCol / 2)].setCellValue(key + "  :  " + value);
                    cells[(noOfHeaderForFirstCol / 2)].setCellStyle(header_style_left_body);

                } else if (i % 2 == 1) {
                    XSSFCell[] cell = new XSSFCell[noOfHeaderNeed];
                    for (int t = noOfHeaderForFirstCol; t < noOfHeaderNeed; t++) {
                        cell[t] = row.createCell(t);
                        cell[t].setCellValue(" ");
                       

                    }

                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellValue(key + "  :  " + value);
                    cell[(noOfHeaderForFirstCol + (noOfHeaderNeed - noOfHeaderForFirstCol) / 2)].setCellStyle(header_style_right_body);

                }
            }

            int tableColumnCount = tableColumnName.size();

            headerRow++;
            headerRow++;
            XSSFRow columnrow = sheet.createRow(headerRow);

            columnrow.setHeightInPoints(20);
            XSSFCell[] cellsInTableheader = new XSSFCell[tableColumnCount + 1];
            int count = 0;
            Iterator<String> it = tableColumnName.iterator();
            while (it.hasNext()) {
                if (count == 0) {

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue("SL");
                    cellsInTableheader[count].setCellStyle(header_style_center);

                    sheet.setColumnWidth(count, 2000);

                } else {

                    String columsValue = it.next();

                    int colomnWidth = 6;
                    try {
                        colomnWidth = Math.max(Math.min(columsValue.trim().length(), 8), 4);
                    } catch (Exception e) {
                    	LOGGER.log(Level.INFO,"error: {}", e.getCause().getMessage());
                    }

                    if (columsValue.trim().toUpperCase().contains("TIME") || columsValue.trim().toUpperCase().contains("DATE")
                            || columsValue.trim().toUpperCase().contains("IP")) {
                        colomnWidth = 8;
                    }

                    if (columsValue.trim().equals("Network Storage [DOWN] [UP]") || columsValue.trim().equals("Local Storage [DOWN] [UP]")) {
                        colomnWidth = 10;
                    }

                    cellsInTableheader[count] = columnrow.createCell(count);
                    cellsInTableheader[count].setCellValue(columsValue);
                    cellsInTableheader[count].setCellStyle(header_style_center);
                    sheet.setColumnWidth(count, colomnWidth * 1000);

                }

                count++;
            }

            int countNoOfRow = 0;

            Iterator<?> itaddvalue = tableContent.iterator();
            if (tableContent.size() > 0) {
                while (itaddvalue.hasNext()) {

                    int columnNo = 0;
                    headerRow++;
                    XSSFRow datarow = sheet.createRow(headerRow);
                    datarow.setHeightInPoints(30);
                    XSSFCell[] cellsInTableContent = new XSSFCell[tableColumnCount + 1];
                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                    cellsInTableContent[columnNo].setCellValue(countNoOfRow + 1);
                    cellsInTableContent[columnNo].setCellStyle(body_style);

                    countNoOfRow++;
                    columnNo++;

                    Object rowValue = itaddvalue.next();
                    Class<?> cl = rowValue.getClass();
                    Set<Entry<String, String>> entiresMethodNReturnType = getterNReturn.entrySet();
                    for (Entry<String, String> ent : entiresMethodNReturnType) {
                        Method m = cl.getMethod(ent.getKey());
                        Object obj = m.invoke(rowValue);
                        Class<?> cls = Class.forName(ent.getValue());

                        if (cls.cast(obj) instanceof byte[]) {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                    headerRow, (short) (columnNo + 1), headerRow + 1);
                            anchor.setAnchorType(2);
                            byte[] byteA = (byte[]) cls.cast(obj);
                            int imageIndexSingle = workBook.addPicture(byteA, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);

                            patriarch.createPicture(anchor, imageIndexSingle);
                            columnNo++;

                        } else if (cls.cast(obj) instanceof ArrayList) {
                            ArrayList<byte[]> snapListofImage = (ArrayList<byte[]>) cls.cast(obj);
                            int countofRow = 0;

                            if (snapListofImage.size() > 1) {
                                if (snapListofImage.size() <= (tableColumnName.size() + 1)) {

                                    cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                    cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                    cellsInTableContent[columnNo].setCellStyle(body_style);
                                    headerRow++;
                                    XSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                    dataMultipleRow.setHeightInPoints(100);
                                    XSSFCell[] cellsInTableContentImage = new XSSFCell[snapListofImage.size()];

                                    XSSFClientAnchor anchor = null;

                                    for (int i = 0; i < snapListofImage.size(); i++) {

                                        anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + i),
                                                headerRow, (short) (countofRow + i + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(i), org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);
                                        cellsInTableContentImage[i] = dataMultipleRow.createCell(i);

                                        countNoOfRow++;

                                    }

                                } else {
                                    int noOfRow = snapListofImage.size() / 5;
                                    int noOfRowNeed = snapListofImage.size() % 5;
                                    XSSFCell[] cellsInTableContentImage = new XSSFCell[snapListofImage.size()];
                                    for (int i = 0; i < noOfRow; i++) {

                                        cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                        cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                        cellsInTableContent[columnNo].setCellStyle(body_style);

                                        headerRow++;
                                        XSSFRow dataMultipleRow = sheet.createRow(headerRow);

                                        dataMultipleRow.setHeightInPoints(100);

                                        XSSFClientAnchor anchor = null;

                                        for (int j = 0; j < (tableColumnName.size() + 1); j++) {

                                            anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                    headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                            int imageIndex = workBook.addPicture(snapListofImage.get(j), org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);
                                            patriarch.createPicture(anchor, imageIndex);
                                            cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                        }

                                    }

                                    headerRow++;
                                    XSSFRow dataMultipleRow = sheet.createRow(headerRow);
                                    XSSFCell titleCell;
                                    dataMultipleRow.setHeightInPoints(100);

                                    XSSFClientAnchor anchor = null;

                                    for (int j = 0; j < noOfRowNeed; j++) {

                                        anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) (countofRow + j),
                                                headerRow, (short) (countofRow + j + 1), headerRow + 1);

                                        int imageIndex = workBook.addPicture(snapListofImage.get(j), org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);
                                        patriarch.createPicture(anchor, imageIndex);

                                        cellsInTableContentImage[j] = dataMultipleRow.createCell(j);

                                    }

                                }
                            } else if (snapListofImage.size() == 1) {
                                datarow.setHeightInPoints(100);
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                                XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) columnNo,
                                        headerRow, (short) (columnNo + 1), headerRow + 1);
                                anchor.setAnchorType(2);

                                int imageIndexSingle = workBook.addPicture(snapListofImage.get(0), org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_JPEG);

                                patriarch.createPicture(anchor, imageIndexSingle);

                            } else {
                                cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                                cellsInTableContent[columnNo].setCellValue(snapListofImage.size());
                                cellsInTableContent[columnNo].setCellStyle(body_style);

                            }
                        } else {
                            cellsInTableContent[columnNo] = datarow.createCell(columnNo);
                            cellsInTableContent[columnNo].setCellValue(obj != null ? cls.cast(obj).toString() : "");
                            cellsInTableContent[columnNo].setCellStyle(body_style);

                            columnNo++;

                        }
                    }
                }
            } else {

                headerRow++;
                XSSFRow emptyRow = sheet.createRow(headerRow);
                XSSFCell emptyCell = emptyRow.createCell(0);
                emptyCell.setCellValue(noDataMsg);
                emptyCell.setCellStyle(body_style);

                sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, tableColumnName.size()));

            }

            // add Disclaimer:
            XSSFCellStyle disclaimer_header_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            disclaimer_header_style.setFont(font);
            disclaimer_header_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_header_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_header_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_header_style.setBorderLeft(CellStyle.BORDER_NONE);

            XSSFCellStyle disclaimer_body_style = workBook.createCellStyle();
            font = workBook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
            disclaimer_body_style.setFont(font);
            disclaimer_body_style.setAlignment(CellStyle.ALIGN_LEFT);
            disclaimer_body_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            disclaimer_body_style.setBorderBottom(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderTop(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderRight(CellStyle.BORDER_NONE);
            disclaimer_body_style.setBorderLeft(CellStyle.BORDER_NONE);

            headerRow++;
            headerRow++;
            XSSFRow disclaimerRow = sheet.createRow(headerRow);

            XSSFCell disclaimerRowCell = disclaimerRow.createCell(0);
            disclaimerRowCell.setCellValue("Disclaimer:");
            disclaimerRowCell.setCellStyle(disclaimer_header_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 1));

            headerRow++;

            XSSFRow disclaimerDataRow = sheet.createRow(headerRow);

            XSSFCell disclaimerDataRowCell = disclaimerDataRow.createCell(0);
            disclaimerDataRowCell.setCellValue("This is a confidential report and should not be shared without the consent of the owner.");
            disclaimerDataRowCell.setCellStyle(disclaimer_body_style);

            sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, 0, 3));

            FileOutputStream fos = null;
            fos = new FileOutputStream(tempFileName);
            workBook.write(fos);
            fos.close();
            
            setSignature(tempFileName, certificatePassword, certificateAlias);
			if (password != null) {
				setPasswordXLSX(tempFileName, fileName, password);

				Files.delete(Paths.get(tempFileName));

			}
            
        }
        return statisticFile.getAbsolutePath();

    }

}
