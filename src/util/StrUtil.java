package util;

/**
 * @author yinyiyun
 * @date 2018/5/2 10:32
 */
public class StrUtil {

    /**
     * 将数字转换为特定格式的字符串
     *
     * @param num        处理的数字
     * @param minLength  表示最小长度 不够在前面补0
     * @return
     */
    public static String tranToString(Integer num, int minLength) {
        return String.format("%0" + minLength + "d", num);
    }

    /**
     * 将所有字符串以 '',  方式拼接
     *
     * @param params
     * @return
     */
    public static String format(String... params) {
        StringBuffer sb = new StringBuffer();
        if (params != null && params.length > 0) {
            for (String param : params) {
                if (param == null) {
                    sb.append("'',");
                } else {
                    sb.append("'" + param + "',");
                }
            }
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    /**
     * 将所有参数按,拼接 字符串则添加''
     *
     * @param params
     * @return
     */
    public static String format(Object... params) {
        StringBuffer sb = new StringBuffer();
        if (params != null && params.length > 0) {
            for (Object param : params) {
                if (param == null) {
                    sb.append("'',");
                } else {
                    if (param instanceof String) {
                        sb.append("'" + param + "',");
                    } else if (StrUtil.isNum(param)) {
                        sb.append(param + ",");
                    }
                }
            }
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    public static boolean isNum(Object param) {
        boolean isNum = false;
        isNum = (param instanceof Integer || param instanceof Double);
        return isNum;
    }

    /**
     * 判断一个参数是否能与其它参数集合的某一个匹配
     *
     * @param object
     * @param params
     * @return
     */
    public static boolean equals(Object object, Object... params) {
        if (object == null) {
            return false;
        }
        if (params != null && params.length > 0) {
            for (Object param : params) {
                if (param == null) {
                    return false;
                } else {
                    if (param.equals(object)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param b
     * @return startIndex
     */
    public static int byteArrayToInt(byte[] b, int startIndex) {
        byte[] a = new byte[4];
        int i = a.length - 1, j = startIndex + 4 - 1;
        for (; i >= 0; i--, j--) {
            // 从b的尾部(即int值的低位)开始copy数据
            if (j >= 0 && b.length > j) {
                a[i] = b[j];
            } else {
                // 如果b.length不足j,则将高位补0
                a[i] = 0;
            }
        }
        //&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v0 = (a[0] & 0xff);
        int v1 = (a[1] & 0xff) << 8;
        int v2 = (a[2] & 0xff) << 16;
        int v3 = (a[3] & 0xff) << 24;
        return v0 + v1 + v2 + v3;
    }

    public static void main(String[] args) {
        int a = 9;
        System.out.println(StrUtil.tranToString(a, 5));
    }

}
