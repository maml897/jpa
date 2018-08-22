package common.excel;

/**
 * 常用正则表达式字符串(Unicode)
 * @author wuff
 *
 */
public interface RegexConstants
{
	/**
	 * 汉字
	 */
	public static final String CHINESE = "\\u2E80-\\uFE4F";
	
	/**
	 * 除汉字外的所有字符
	 */
	public static final String NOCHINESE = "[^\u4e00-\u9fa5]+";
	
	/**
	 * 汉字+数字字母下划线
	 */
	public static final String LETTER_NUMBER_CHINESE = CHINESE+"\\w";
	
	/**
	 * 中英文逗号
	 */
	public static final String COMMA = "\\uFF0C\\u002C";
	
	/**
	 * 英文点
	 */
	public static final String POINT = "\\u002E";
	
	/**
	 * 班级：英文点，汉字+数字字母下划线
	 */
	public static final String STRING4CLASS = LETTER_NUMBER_CHINESE+POINT;
	
	/**
	 * 考号、学籍号：数字+字母（0-20）
	 */
	public static final String EXAM_CODE = "^[0-9a-zA-Z]{0,20}$";
	
	/**
	 * 考号、学籍号：数字+字母（1-20）必填
	 */
	public static final String EXAM_CODE1 = "^[0-9a-zA-Z]{1,20}$";
	
	/**
	 * 成绩：整数或一位两位小数
	 */
	public static final String SCORE = "^(([1-9][0-9]*)|0)(\\.[0-9]{1,2})?$";
	
	/**
	 * 数字字母下划线
	 */
	public static final String PASSWORD = "\\w";
	
	/**
	 * 邮箱
	 */
	public static final String EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
	/**
	 * 手机
	 */
	public static final String MOBILE = "^1[3|4|5|7|8][0-9]{9}$";
}
