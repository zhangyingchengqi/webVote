package com.hcq.utils;

/**
 * @author HCQ
 *一些常见正则验证
 */
public class RegexUtils {

	public static final String reg1="[1-9]\\d*";
	public static final String reg2="-[1-9]\\d*";
	public static final String reg3="-?[1-9]\\d*|0";
	public static final String reg4="[1-9]\\d*|0";
	public static final String reg5="-[1-9]\\d*|0";
	public static final String reg6="[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*";//正浮点数
	public static final String reg7="-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)";
	public static final String reg8="-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)";
	public static final String reg9="[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0";
	public static final String reg10="(-([1-9]\\d*\\.\\d*|0\\0\\.\\d*[1-9]\\d*))";
	
	//“yyyy-mm-dd“ 格式的日期校验，已考虑平闰年。
	public static final String reg11="^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?";
	
	//校验18位身份证号码
	public static final String reg12="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
	
	//校验邮箱
	public static final String reg13="[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w]";
	
	//校验15位身份证号码
	public static final String reg14="^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

	//下面是国内 13、15、18开头的手机号正则表达式。
	public static final String reg15="^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
	
	//金额校验，精确到两位小数
	public static final String reg16="^[0-9]+(.[0-9]{2})?$";
	
	//IE版本检查的表达式
	public static final String reg17="^.*MSIE [5-8](?:\\.[0-9]+)?(?!.*Trident\\/[5-9]\\.0).*$";
	
	//校验IP-v4地址
	public static final String reg18="\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\";
	
	//下面的这个表达式可以筛选出一段文本中的URL。
	public static final String reg19="^(f|ht){1}(tp|tps):\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w- ./?%&=]*)?";
	
	//抽取注释
	public static final String reg20="<!--(.*?)-->";
	
	//字符串仅能是中文。
	public static final String reg21="^[\\u4e00-\\u9fa5]{0,}$";
	
	//密码的强度必须是包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间。
	public static final String reg22="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$";
			
}