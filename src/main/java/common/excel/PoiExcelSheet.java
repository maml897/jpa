package common.excel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 具体的一个工作表格处理类
 * @author lifw
 */
public class PoiExcelSheet
{
	private Sheet sheet = null;

	private CellStyle defaultCellFormat = null;

	private Drawing patriarch = null;

	public PoiExcelSheet(Sheet sheet)
	{
		this.sheet = sheet;
		this.defaultCellFormat = getFormatContent();
	}

	/**
	 * 获取当前sheet的索引（index）
	 * @return
	 */
	public int getSheetIndex()
	{
		return sheet.getWorkbook().getSheetIndex(sheet);
	}

	/**
	 * 获取当前的sheet
	 * @return
	 */
	public Sheet getSheet()
	{
		return sheet;
	}

	/**
	 * 设置默认的Cell的样式
	 * @param defaultCellFormat
	 */
	public void setDefaultCellFormat(CellStyle defaultCellFormat)
	{
		this.defaultCellFormat = defaultCellFormat;
	}

	/**
	 * 设置行高
	 * @param rownum
	 *            行编号（以0开始）
	 * @param height
	 */
	public void setRowHeight(int rownum, int height)
	{
		Row row = sheet.getRow(rownum);
		if (row == null)
			row = sheet.createRow(rownum);

		row.setHeight((short) height);
	}

	/**
	 * 设置列宽
	 * @param colnum
	 *            列编号（以0开始）
	 * @param width
	 */
	public void setColumnWidth(int colnum, int width)
	{
		sheet.setColumnWidth(colnum, width * 256);
	}

	/**
	 * 合并单元格
	 * @param firstCol
	 *            开始列
	 * @param firstRow
	 *            开始行
	 * @param lastCol
	 *            结束列
	 * @param lastRow
	 *            结束行
	 */
	public void mergeCells(int firstCol, int firstRow, int lastCol, int lastRow)
	{
		mergeCells(firstCol, firstRow, lastCol, lastRow, getCell(firstCol, firstRow).getCellStyle());
	}
	
	/**
	 * 合并单元格
	 * @param firstCol
	 *            开始列
	 * @param firstRow
	 *            开始行
	 * @param lastCol
	 *            结束列
	 * @param lastRow
	 *            结束行
	 * @param st 合并单元格后的样式
	 */
	public void mergeCells(int firstCol, int firstRow, int lastCol, int lastRow, CellStyle st)
	{
		for(int i=firstCol;i<=lastCol;i++)
		{
			for(int j=firstRow;j<=lastRow;j++)
			{
				setCellStyle(i, j, st);
			}
		}
		
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	}

	/**
	 * 设置单元格的样式为默认样式
	 * @param colnum
	 * @param rownum
	 */
	public void setCellStyleDefault(int colnum, int rownum)
	{
		setCellStyle(colnum, rownum, defaultCellFormat);
	}

	/**
	 * 设置单元格的样式
	 * @param colnum
	 * @param rownum
	 * @param st
	 */
	public void setCellStyle(int colnum, int rownum, CellStyle st)
	{
		Cell cell = getCell(colnum, rownum);
		cell.setCellStyle(st);

		setMergeCellStyle(colnum, rownum, st);
	}

	/**
	 * 添加一个单元格
	 * @param colnum
	 * @param rownum
	 * @param content
	 */
	public void addCell(int colnum, int rownum, String content)
	{
		addCell(colnum, rownum, content, defaultCellFormat);
	}

	/**
	 * 添加一个单元格
	 * @param colnum
	 * @param rownum
	 * @param content
	 * @param st
	 */
	public void addCell(int colnum, int rownum, String content, CellStyle st)
	{
		Cell cell = getCell(colnum, rownum);
		cell.setCellValue(content);
		cell.setCellStyle(st);

		setMergeCellStyle(colnum, rownum, st);
	}

	/**
	 * 添加一个单元格
	 * @param colnum
	 * @param rownum
	 * @param val
	 */
	public void addCell(int colnum, int rownum, int val)
	{
		addCell(colnum, rownum, val, defaultCellFormat);
	}

	/**
	 * 添加一个单元格
	 * @param colnum
	 * @param rownum
	 * @param val
	 * @param st
	 */
	public void addCell(int colnum, int rownum, int val, CellStyle st)
	{
		Cell cell = getCell(colnum, rownum);
		cell.setCellValue(val);
		cell.setCellStyle(st);

		setMergeCellStyle(colnum, rownum, st);
	}

	/**
	 * 添加一个单元格
	 * @param colnum
	 * @param rownum
	 * @param val
	 */
	public void addCell(int colnum, int rownum, float val)
	{
		addCell(colnum, rownum, val, defaultCellFormat);
	}

	/**
	 * 添加一个单元格
	 * @param colnum
	 * @param rownum
	 * @param val
	 * @param st
	 */
	public void addCell(int colnum, int rownum, float val, CellStyle st)
	{
		Cell cell = getCell(colnum, rownum);
		cell.setCellValue(val);
		cell.setCellStyle(st);

		setMergeCellStyle(colnum, rownum, st);
	}

	/**
	 * 添加一个单元格
	 * @param colnum
	 * @param rownum
	 * @param val
	 */
	public void addCell(int colnum, int rownum, double val)
	{
		addCell(colnum, rownum, val, defaultCellFormat);
	}

	/**
	 * 添加一个单元格
	 * @param colnum
	 * @param rownum
	 * @param val
	 * @param st
	 */
	public void addCell(int colnum, int rownum, double val, CellStyle st)
	{
		Cell cell = getCell(colnum, rownum);
		cell.setCellValue(val);
		cell.setCellStyle(st);

		setMergeCellStyle(colnum, rownum, st);
	}

	/**
	 * 如果当前cell是合并的单元格，需要设置样式
	 * @param colnum
	 * @param rownum
	 * @param st
	 */
	private void setMergeCellStyle(int colnum, int rownum, CellStyle st)
	{
		CellRangeAddress cellRangeAddress = null;

		int numMergedRegions = sheet.getNumMergedRegions();
		for (int j = numMergedRegions - 1; j >= 0; j--)
		{
			CellRangeAddress c = sheet.getMergedRegion(j);
			if (c.isInRange(rownum, colnum))
			{
				cellRangeAddress = c;
				break;
			}
		}
		if (cellRangeAddress != null)
		{
			int firstRow = cellRangeAddress.getFirstRow();
			int lastRow = cellRangeAddress.getLastRow();
			int firstColumn = cellRangeAddress.getFirstColumn();
			int lastColumn = cellRangeAddress.getLastColumn();

			for (int i = firstRow; i <= lastRow; i++)
			{
				for (int j = firstColumn; j <= lastColumn; j++)
				{
					Cell cell = getCell(j, i);
					cell.setCellStyle(st);
				}
			}
		}
	}

	/**
	 * 私有内部方法，获取一个单元格
	 * @param colnum
	 * @param rownum
	 * @return
	 */
	private Cell getCell(int colnum, int rownum)
	{
		Row row = sheet.getRow(rownum);
		if (row == null)
		{
			row = sheet.createRow(rownum);
		}
		Cell cell = row.getCell(colnum);
		if (cell == null)
		{
			cell = row.createCell(colnum);
		}
		return cell;
	}

	/**
	 * 在sheet中添加一个图片
	 * @param startColnum
	 *            图片左上角的起始位置列
	 * @param startRownum
	 *            图片左上角的起始位置行
	 * @param width
	 *            图片的宽
	 * @param height
	 *            图片的高
	 * @param file
	 *            图片文件
	 */
	public void addImage(int startColnum, int startRownum, int width, int height, File file)
	{
		// 声明一个画图的顶级管理器
		if (patriarch == null)
			patriarch = sheet.createDrawingPatriarch();

		try
		{
			BufferedImage bi = ImageIO.read(file);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ImageIO.write(bi, "PNG", bout);
			byte[] bytes = bout.toByteArray();

			int col2 = startColnum + width;
			int row2 = startRownum + height;
//			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) startColnum, startRownum, (short) col2, row2);
			
			ClientAnchor anchor = sheet.getWorkbook().getCreationHelper().createClientAnchor();
			anchor.setDx1(0);
			anchor.setDy1(0);
			anchor.setDx2(1023);
			anchor.setDy2(255);
			anchor.setCol1(startColnum);
			anchor.setRow1(startRownum);
			anchor.setCol2(col2);
			anchor.setRow2(row2);
			anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);

			patriarch.createPicture(anchor, sheet.getWorkbook().addPicture(bytes, Workbook.PICTURE_TYPE_PNG));
		}
		catch (IOException e)
		{
		}
	}

	/**
	 * 题目格式
	 * @return
	 */
	public CellStyle getFormatHead()
	{
		CellStyle cs = sheet.getWorkbook().createCellStyle();
		return cs;
	}

	/**
	 * 标头格式
	 * @return
	 */
	public CellStyle getFormatTitle()
	{
		CellStyle cs = sheet.getWorkbook().createCellStyle();
		return cs;
	}

	/**
	 * 内容格式
	 * @return
	 */
	public CellStyle getFormatContent()
	{
		CellStyle cs = sheet.getWorkbook().createCellStyle();
		return cs;
	}

	/**
	 * 内容居左
	 * @return
	 */
	public CellStyle getFormatContentLeft()
	{
		CellStyle cs = sheet.getWorkbook().createCellStyle();
		return cs;
	}
}
