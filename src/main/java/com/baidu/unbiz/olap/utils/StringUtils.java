package com.baidu.unbiz.olap.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 字符串工具方法集
 */
public class StringUtils {

    public static final boolean isLegalConfValue(String value) {
        if (value == null) {
            return false;
        }
        if (value.matches("\\$\\{.*\\}")) {
            return false;
        }
        return true;
    }

    public static final boolean isLegalUrl(String url) {
        return url.matches("^(http://)" + "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" + "|" + "([0-9a-z_!~*'()-]+\\.)*"
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." + "[a-z]{2,6})" + "(:[0-9]{1,4})?" + "((/?)|"
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$");
    }

    /**
     * 判断字符串是否为空
     * 
     */
    public static final boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否逻辑为空，trim后的长度是否为0
     * 
     */
    public static final boolean isLogicEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static void main(String[] args) {
        isLegalIpAddress("aaa");
    }

    /**
     * 是否是合法的ip地址
     * 
     * @param ip
     * @return 上午10:26:30 created by Darwin(Tianxin)
     */
    public static final boolean isLegalIpAddress(String ip) {
        if (isEmpty(ip)) {
            return false;
        }

        return ip.matches(IP4PATTERN) || ip.matches(IP6PATTERN);
    }

    /**
     * IPV4的地址正则
     */
    private static final String IP4PATTERN = "^(([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\."
            + "((0)|([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\."
            + "((0)|([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\."
            + "((0)|([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))$";

    /**
     * IPV6的地址的正则
     */
    private static final String IP6PATTERN =
            "^\\s*((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4})|:))|(([0-9A-Fa-f]{1,4}:){6}"
                    + "(:|((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]"
                    + "?\\d{1,2})){3})|(:[0-9A-Fa-f]{1,4})))|(([0-9A-Fa-f]{1,4}:){5}((:((25" 
                    + "[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2}))" 
                    + "{3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]" 
                    + "{1,4}){0,1}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|" 
                    + "[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:)" 
                    + "{3}(:[0-9A-Fa-f]{1,4}){0,2}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25" 
                    + "[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|" 
                    + "(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}((:((25[0-5]|2[0-4]\\d|" 
                    + "[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]" 
                    + "{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:)(:[0-9A-Fa-f]{1,4}){0,4}((:((25[0-5]|" 
                    + "2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:" 
                    + "[0-9A-Fa-f]{1,4}){1,2})))|(:(:[0-9A-Fa-f]{1,4}){0,5}((:((25[0-5]|2[0-4]\\d|" 
                    + "[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]" 
                    + "{1,4}){1,2})))|(((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|" 
                    + "[01]?\\d{1,2})){3})))(%.+)?\\s*$";

    private static char[] md5Chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static MessageDigest messagedigest;

    /**
     * 获取一个字符串的md5
     * 
     */
    public static String getStringMD5String(String str) throws Exception {
        messagedigest = MessageDigest.getInstance("MD5");
        messagedigest.update(str.getBytes());
        return bufferToHex(messagedigest.digest());
    }

    /**
     * 验证一个字符串的MD5
     * 
     * @param str
     * @param md5
     * @return
     */
    public static boolean checkMd5(String str, String md5) throws Exception {
        if (getStringMD5String(str).equals(md5)) {
            return true;
        } else {
            return false;
        }
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = md5Chars[(bt & 0xf0) >> 4];
        char c1 = md5Chars[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * GBK验证
     */
    public static boolean isGBK(String input) {
        try {
            if (input.equals(new String(input.getBytes("GBK"), "GBK"))) {
                return true;
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    /**
     * 将一个list组装成字符串，list各个元素之间以split分割
     * 
     * @param collection
     * @param split 分隔符, 如果为null, 则返回长度为0的字符串
     * @return
     */
    public static String makeStrFromCollection(final Collection<? extends Object> collection, final String split) {

        if (collection == null || collection.isEmpty()) {
            return "";
        }
        if (split == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (Object element : collection) {
            builder.append(element).append(split);
        }
        builder.delete(builder.length() - split.length(), builder.length());

        return builder.toString();
    }

    /**
     * 把以分割的字符串切割成字符串列表，每个字符串进行trim,列表中不包含空字符串
     * 
     * 默认忽略空字符串，不忽略值为"NULL"的字符串
     * 
     * @param listString
     * @param split 连接的字符串，正则表达式
     * @return
     */
    public static List<String> splitToList(final String listString, final String split) {
        return splitToList(listString, split, false);
    }

    /**
     * 把以分割的字符串切割成字符串列表，每个字符串进行trim,列表中不包含空字符串
     * 
     * @param listString
     * @param split 连接的字符串，正则表达式
     * @param ignoreNullString 是否忽略值为"NULL"的字符串
     * @return
     */
    public static List<String> splitToList(final String listString, final String split, boolean ignoreNullString) {
        if (listString == null) {
            return new ArrayList<String>(0);
        }
        if (split == null || split.length() == 0) {
            return new ArrayList<String>(0);
        }
        List<String> result = new ArrayList<String>();

        String tmpListString = listString.trim();

        String[] array = tmpListString.split(split);
        for (String str : array) {
            str = str.trim();
            if (!"".equals(str)) {
                if (ignoreNullString && "NULL".equals(str)) {
                    continue;
                }
                result.add(str);
            }
        }
        return result;
    }

    /**
     * 把以分割的字符串切割成字符串列表，每个字符串进行trim,列表中不包含空字符串，返回的是一个整形列表
     * 
     * 默认忽略空字符串，不忽略值为"NULL"的字符串
     * 
     * @param listString
     * @param split 连接的字符串，正则表达式
     * @return
     */
    public static List<Integer> splitToIntList(final String listString, final String split) {
        return splitToIntList(listString, split, false);
    }

    /**
     * 把以分割的字符串切割成字符串列表，每个字符串进行trim,列表中不包含空字符串，返回的是一个整形列表
     * 
     * @param listString
     * @param split 连接的字符串，正则表达式
     * @param ignoreNullString 是否忽略值为"NULL"的字符串
     * @return
     */
    public static List<Integer> splitToIntList(final String listString, final String split, boolean ignoreNullString) {
        if (listString == null) {
            return new ArrayList<Integer>(0);
        }
        if (split == null || split.length() == 0) {
            return new ArrayList<Integer>(0);
        }

        List<Integer> result = new ArrayList<Integer>();

        String tmpListString = listString.trim();

        String[] array = tmpListString.split(split);
        for (String str : array) {
            str = str.trim();
            if (!"".equals(str)) {
                if (ignoreNullString && "NULL".equals(str)) {
                    continue;
                }
                result.add(Integer.parseInt(str));
            }
        }
        return result;
    }

    public static String fileNameEncode(String fileName) {
        if (fileName == null) {
            return null;
        }
        try {
            return new String(fileName.getBytes("gbk"), "ISO-8859-1");
        } catch (Exception ex) {
            return fileName;
        }
    }

    /**
     * 把string转化成Integer,如果不符合Integer的值,返回null
     * 
     * @param value
     * @return
     */
    public static Integer getInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 把string转化成Long,如果不符合Long的值,返回null
     * 
     * @param value
     * @return
     */
    public static Long getLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 把string转化成Long,如果不符合Long的值,返回null
     * 
     * @param value
     * @return
     */
    public static Double getDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 将一个list组装成字符串,可以在sql的in中使用
     * 
     * @param idList
     * @return
     */
    public static String makeStrCollection(List<?> idList) {

        if (idList == null || idList.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (; i < idList.size() - 1; i++) {
            builder.append(idList.get(i)).append(",");
        }
        builder.append(idList.get(i));

        return builder.toString();
    }

    /**
     * 将一个Collection&lt;? extends String&gt; 组装成字符串,可以在sql的in中使用
     * 
     * @param collection
     * @return
     */
    public static String makeStrFromStringCollection(final Collection<? extends String> collection) {

        if (collection == null || collection.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (Object element : collection) {
            builder.append("\"").append(element).append("\"").append(",");
        }
        builder.delete(builder.length() - 1, builder.length());

        return builder.toString();
    }

    /**
     * 将一个规格字符串变成一个64位long型 1|2|3| ----> 111 = 7
     * 
     * @param input
     * @param delim
     * @return
     */
    public static long transToLong(String input, final String delim) {
        String tmpStr = input;
        long result = 0;
        if (tmpStr == null || tmpStr.length() == 0) {
            return result;
        }

        String[] delimal = tmpStr.split(delim);
        if (delimal.length > 63) {
            return result;
        }

        int charInt = 0;
        for (int i = 0; i < delimal.length; i++) {
            try {
                charInt = Integer.parseInt(delimal[i]);
            } catch (Exception e) {
                charInt = 0;
            }
            if (charInt > 0) {
                result += (1 << i);
            }
        }

        return result;
    }

    public static String concatNumber(Collection<? extends Number> keys) {
        StringBuffer res = new StringBuffer();
        for (Number key : keys) {
            res.append(key);
            res.append(',');
        }
        res.deleteCharAt(res.length() - 1);

        return res.toString();
    }
}
