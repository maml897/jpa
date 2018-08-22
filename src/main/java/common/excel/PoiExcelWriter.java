package common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成（Excel 97-2003 工作簿）
 * @author lifw
 *
 */
public class PoiExcelWriter
{
	/**
	 * 默认第0行的行高
	 */
	public static final int Head_Height = 600;


	private Workbook workBook;

	/**
	 * 初始化一个空的Excel
	 */
	public PoiExcelWriter()
	{
		workBook = new HSSFWorkbook();
	}
	
	/**
	 * 通过读取一个Excel模版，生成一个新的Workbook
	 * @param inputFilePath
	 */
	public PoiExcelWriter(String inputFilePath)
	{
		try(InputStream is = new FileInputStream(new File(inputFilePath)))
		{
			POIFSFileSystem fs = new POIFSFileSystem(is);
			workBook = new HSSFWorkbook(fs);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 根据索引获取一个sheet，如果没有则返回null
	 * @param sheetIndex
	 * @return
	 */
	public PoiExcelSheet getSheet(int sheetIndex)
	{
		try
		{
			//如果没有获取到sheet，那么会抛出异常
			Sheet sheet = workBook.getSheetAt(sheetIndex);
			PoiExcelSheet poiExcelSheet = new PoiExcelSheet(sheet);
			return poiExcelSheet;
		}
		catch (Exception e) {
			//logger.error(e.toString());
			return null;
		}
	}
	
	/**
	 * 根据名字（name）获取一个sheet，如果没有则返回null
	 * @param sheetName
	 * @return
	 */
	public PoiExcelSheet getSheet(String sheetName)
	{
		Sheet sheet = workBook.getSheet(sheetName);
		if(sheet == null)
			return null;

		return new PoiExcelSheet(sheet);
	}
	
	/**
	 * 创建一个sheet（名字为"Export"）
	 * @return
	 * @throws Exception
	 */
	public PoiExcelSheet createSheet()
	{
		return createSheet("Export");
	}
	
	/**
	 * 创建一个sheet
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public PoiExcelSheet createSheet(String sheetName)
	{
		Sheet sheet = workBook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
		PoiExcelSheet poiExcelSheet = new PoiExcelSheet(sheet);
		poiExcelSheet.setRowHeight(0, Head_Height);
		
		return poiExcelSheet;
	}

	/**
	 * 写入文件(文件扩展名为 .xls)
	 * @param outputFilePath 绝对路径
	 */
	public void write(String outputFilePath)
	{
		write(new File(outputFilePath));
	}

	/**
	 * 写入文件(文件扩展名为 .xls)
	 * @param outputFile
	 */
	public void write(File outputFile)
	{
		try (FileOutputStream out = new FileOutputStream(outputFile))
		{
			write(out);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 输出到输出流中
	 * @param os
	 * @throws IOException
	 */
	public void write(OutputStream os) throws IOException
	{
		if (workBook != null)
		{
			workBook.write(os);
		}
	}

	/**
	 * 判读一个字符串中是否全部都是由数字组成
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)
	{
		if (str == null || "".equals(str.trim()) || str.length() > 10)
			return false;
		Pattern pattern = Pattern.compile("^0|[1-9]\\d*(\\.\\d+)?$");
		return pattern.matcher(str).matches();
	}
}
