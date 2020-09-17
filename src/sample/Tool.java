package sample;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    public static String SEPERATOR_REGEX_OR = "\\|";
    public static String SEPERATOR_REGEX_COMMA = "\\,";
    private static Logger logger = Logger.getLogger(Tool.class);

    public static String listToString(List<String> strList) {
        return Tool.combineArgsListBySymbol(strList, ",");
    }

    public static String listToStringBySymbol(List<String> strList, String Symbol) {
        return Tool.combineArgsListBySymbol(strList, Symbol);
    }

    public static String[] seperateBySymbol(String targetStr, String seperator) {
        return targetStr.split(targetStr);
    }

    public static String combineArgsListBySymbol(List args, String symbol) {
        return Tool.listToStringBySymbol(args, symbol);
    }

    public static String combineArgsListByComma(List args) {
        return Tool.listToStringBySymbol(args, SEPERATOR_REGEX_COMMA);
    }

    public static String combineArgsListByOr(List args) {
        return Tool.listToStringBySymbol(args, SEPERATOR_REGEX_OR);
    }

    public static String[] seperateByComma(String targetStr) {
        return targetStr.split(SEPERATOR_REGEX_COMMA);
    }

    public static String[] seperateByOr(String targetStr) {
        return targetStr.split(SEPERATOR_REGEX_OR);
    }

    public static boolean hasEmptyStr(String str) {
        if (str != null && !"".equals(str)) {
            return false;
        }
        return true;
    }

    public static boolean validStr(String str) {
        if (str != null && !"".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isNum(String str) {
        String regExp = "^\\d+$";
        if (validStr(str) && str.matches(regExp)) {
            return true;
        }
        return false;
    }

    public static String findStrByRegEx(String content, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println(findStrByRegEx("", ""));
    }

    public static String findStrByRegExMul(String content, String regEx, int index) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        if (m.find(index)) {
            System.out.println(m.end());
            return m.group(index);
        }
        return null;
    }

    public static boolean matchStrByRegEx(String content, String regEx) {
        return findStrByRegEx(content, regEx) != null;
    }

    public static boolean isPhone(String content) {
        String regExp = "^\\d{11}$";
        return content.matches(regExp);

    }

    public static Date getNowDateTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    public static String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getNowDate(String format) {
        Date currentTime = new Date();
        if (!validStr(format)) {
            format = "yyyyMMdd";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static void fileWrite(String filePath, List<String> fileContent) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        StringBuffer sb = new StringBuffer(4096);
        for (String temp : fileContent) {
            if (validStr(temp.trim())) {
                sb.append(temp.trim()).append("\r\n");
            }
        }
        bw.write(sb.toString());
        bw.close();
    }

    public static List<String> fileRead(String filePath) throws IOException {
        List<String> fileContent = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String temp = null;
        while ((temp = br.readLine()) != null) {
            fileContent.add(temp.trim());
        }
        br.close();
        return fileContent;
    }

    public static String readFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String temp = null;
        StringBuffer sb = new StringBuffer();
        while ((temp = br.readLine()) != null) {
            sb.append(temp.trim());
        }
        br.close();
        return sb.toString();
    }

    public static String getProjectPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 读取XML
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static Document readXml(String filename) throws IOException {
        File f = new File(filename);
        if (f.exists()) {
            logger.info("配置文件路径:" + filename);
        }

        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        Document dt = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            dt = db.parse(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获取几天前的日期
     *
     * @param nDays
     * @param format
     * @return
     */
    public static String getNDaysDate(int nDays, String format) {
        Calendar calendar2 = Calendar.getInstance();
        String thisFormat = Tool.validStr(format) ? format : "yyyyMMdd";
        SimpleDateFormat sdf2 = new SimpleDateFormat(thisFormat);
        calendar2.add(Calendar.DATE, nDays);
        String three_days_after = sdf2.format(calendar2.getTime());
        return three_days_after;
    }

    /**
     * unicode转换字符串
     */
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * 计算方法使用时间__未完成
     *
     * @param method
     */
    public static void CalculateMethodRunningTime(Method method) {
        long endTime;
        long starTime;
        long Time;
        starTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis();
        Time = endTime - starTime;
        System.out.println(Time);
    }

    /**
     * "yyyy-MM-dd HH:mm:ss" 根据格式返回当前时间
     *
     * @param pattern
     * @return
     */
    // private List<String> DatePatternList = null;
    public static String getNowDateStringByPattern(String pattern) {
        // 字符串是否合法
        if (!Tool.validStr(pattern))
            return null;
        Date currentTime = new Date();
        // 此处应该作格式合法判断
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 根据时区和日期转换
     *
     * @param currentDate
     * @param toTimeZone
     * @return
     */
    public static String transferTimeZone(Date currentDate, String toTimeZone) {
        return null;
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static String getPros(String key, String filePath) throws IOException {
        Properties prop = new Properties();// 属性集合对象
        FileInputStream fis = new FileInputStream(filePath);// 属性文件流
        prop.load(fis);// 将属性文件流装载到Properties对象中
        return prop.getProperty(key);
    }

    public static void trimStrs(String... temp) {
        for (String str : temp) {
            str = str.trim();
        }

    }

}
