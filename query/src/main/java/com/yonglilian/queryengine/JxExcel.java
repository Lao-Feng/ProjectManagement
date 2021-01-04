package com.yonglilian.queryengine;

import com.yonglilian.model.SessionUser;
import com.yonglilian.service.impl.QueryControlImpl;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableWorkbook;

import java.io.OutputStream;

public class JxExcel {

	public JxExcel() throws Exception {
	}

	public void WriteExcel(QueryControlImpl queryControlImpl, OutputStream os, String ID, SessionUser User, String P1,
                           String P2, String P3, String P4, String P5, String Strpage, String Strcrow) throws Exception {

		// ---初始化-------------------
		WritableCellFormat wcTHTitle; // 表格表头普通单元格风格
		WritableCellFormat wcTHTitleL; // 表格表头左边单元格风格
		WritableCellFormat wcTHTitleR; // 表格表头右边单元格风格
		WritableCellFormat wcfLeftA; // 左边普通单元格风格
		WritableCellFormat wcfRightA; // 右边普通单元格风格
		WritableCellFormat wcfA; // 普通中间单元格风格
		WritableCellFormat wcfLeftB; // 左边底部单元格风格
		WritableCellFormat wcfRightB; // 左边底部单元格风格
		WritableCellFormat wcfB; // 底部中间单元格风格

		WritableCellFormat wcfTitle; // 标题单元格风格
		jxl.write.Number number = null;

		// 字体风格
		WritableFont wfc = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		// 标题字体风格
		WritableFont wfTilte = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		// 标题单元格风格
		wcfTitle = new WritableCellFormat(wfTilte);
		wcfTitle.setAlignment(jxl.format.Alignment.CENTRE);
		wcfTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

		// 表格表头单元格风格
		wcTHTitle = new WritableCellFormat(wfc);
		wcTHTitle.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.THIN);
		wcTHTitle.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.DOUBLE);
		wcTHTitle.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.THIN);
		wcTHTitle.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.THIN);
		wcTHTitle.setAlignment(jxl.format.Alignment.CENTRE);
		wcTHTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

		// 表格表头左边单元格风格
		wcTHTitleL = new WritableCellFormat(wfc);
		wcTHTitleL.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.DOUBLE);
		wcTHTitleL.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.DOUBLE);
		wcTHTitleL.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.THIN);
		wcTHTitleL.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.THIN);
		wcTHTitleL.setAlignment(jxl.format.Alignment.CENTRE);
		wcTHTitleL.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

		// 表格表头右边单元格风格
		wcTHTitleR = new WritableCellFormat(wfc);
		wcTHTitleR.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.THIN);
		wcTHTitleR.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.DOUBLE);
		wcTHTitleR.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.THIN);
		wcTHTitleR.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.DOUBLE);
		wcTHTitleR.setAlignment(jxl.format.Alignment.CENTRE);
		wcTHTitleR.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

		// 左边普通单元格风格
		wcfLeftA = new jxl.write.WritableCellFormat(wfc);
		wcfLeftA.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.DOUBLE);
		wcfLeftA.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);
		wcfLeftA.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.THIN);
		wcfLeftA.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.THIN);

		// 右边普通单元格风格
		wcfRightA = new jxl.write.WritableCellFormat(wfc);
		wcfRightA.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.THIN);
		wcfRightA.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);
		wcfRightA.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.THIN);
		wcfRightA.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.DOUBLE);

		// 普通中间单元格风格
		wcfA = new jxl.write.WritableCellFormat(wfc);
		wcfA.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

		// 左边底部单元格风格
		wcfLeftB = new jxl.write.WritableCellFormat(wfc);
		wcfLeftB.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.DOUBLE);
		wcfLeftB.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);
		wcfLeftB.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.DOUBLE);
		wcfLeftB.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.THIN);

		// 右边底部单元格风格
		wcfRightB = new jxl.write.WritableCellFormat(wfc);
		wcfRightB.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.THIN);
		wcfRightB.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);
		wcfRightB.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.DOUBLE);
		wcfRightB.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.DOUBLE);

		// 底部中间单元格风格
		wcfB = new jxl.write.WritableCellFormat(wfc);
		wcfB.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.THIN);
		wcfB.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);
		wcfB.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.DOUBLE);
		wcfB.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.THIN);

		// -------初始化结束-----------------

		String ThTitle[];
		int colWidth[];
		jxl.write.Label labelC;

		WritableWorkbook wwb = null;
		wwb = Workbook.createWorkbook(os);
		ThTitle = queryControlImpl.getQueryTHTitle(ID);
		jxl.write.WritableSheet sheet = wwb.createSheet(queryControlImpl.getQueryTitle(ID) + "结果", 0);
		sheet.mergeCells(0, 0, ThTitle.length - 1, 0);

		sheet.setRowView(0, 1000);
		sheet.setRowView(1, 500);

		labelC = new Label(0, 0, queryControlImpl.getQueryTitle(ID));
		labelC.setCellFormat(wcfTitle);
		sheet.addCell(labelC);
		colWidth = new int[ThTitle.length];
		for (int i = 0; i < ThTitle.length; i++) {
			labelC = new Label(i, 1, ThTitle[i]);
			colWidth[i] = ThTitle[i].trim().length() + 7;

			if (i == 0) {
				labelC.setCellFormat(wcTHTitleL);
			} else if (i == ThTitle.length - 1) {
				labelC.setCellFormat(wcTHTitleR);
			} else {
				labelC.setCellFormat(wcTHTitle);
			}

			sheet.addCell(labelC);

		}

		String[][] sData = queryControlImpl.getExcelData(ID, User, P1, P2, P3, P4, P5, Strpage, Strcrow);
		String[] isnumber = queryControlImpl.getIsNumber(ID);
		String isnum = "1";
		if (isnumber == null) {
			isnum = "0";
		}

		jxl.write.WritableCellFormat wcf = null;
		if (sData != null) {

			for (int i = 0; i < sData.length; i++) {
				for (int j = 0; j < sData[i].length; j++) {
					labelC = new Label(j, i + 2, sData[i][j]);

					if (colWidth[j] < sData[i][j].trim().length() + 7) {
						colWidth[j] = sData[i][j].trim().length() + 7;
					}

					if (i < sData.length - 1) {
						if (j == 0) {
							wcf = wcfLeftA;
						} else if (j == sData[i].length - 1) {
							wcf = wcfRightA;
						} else {
							wcf = wcfA;
						}
					} else {
						if (j == 0) {
							wcf = wcfLeftB;
						} else if (j == sData[i].length - 1) {
							wcf = wcfRightB;
						} else {
							wcf = wcfB;
						}
					}

					if (isnum.equals("1")) {
						if (j < isnumber.length) {
							if (isnumber[j].equals("1")) {
								try {
									double sint = Double.parseDouble(sData[i][j]);
									number = new jxl.write.Number(j, i + 2, sint, wcf);
									sheet.addCell(number);
								} catch (Exception ex) {
									labelC.setCellFormat(wcf);
									sheet.addCell(labelC);
								}
							} else {
								labelC.setCellFormat(wcf);
								sheet.addCell(labelC);
							}
						} else {
							labelC.setCellFormat(wcf);
							sheet.addCell(labelC);
						}
					} else {
						labelC.setCellFormat(wcf);
						sheet.addCell(labelC);
					}
				}
			}
		}
		for (int i = 0; i < colWidth.length; i++) {
			sheet.setColumnView(i, colWidth[i]);
		}
		wwb.write();
		wwb.close();
		wwb = null;
	}
}
