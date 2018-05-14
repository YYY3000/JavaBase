package util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件操作工具类
 *
 * @author yinyiyun
 * @date 2018/5/2 10:32
 */
public class FileUtil {

    /**
     * 文件路径连接
     *
     * @param path 路径
     * @param file 文件名
     * @return
     */
    public static String pathCombine(String path, String file) {
        return path + File.separator + file;
    }

    /**
     * 获取文件名称(不存在则新建)
     *
     * @param path 路径
     * @return
     * @throws IOException
     */
    public static String getDirName(String path) {
        return FileUtil.createDir(path);
    }

    /**
     * 获取文件最后修改时间
     *
     * @param path 路径
     * @return
     */
    public static long getFileModifyTime(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.lastModified();
        } else {
            throw new RuntimeException("File Not Exists, path:" + path);
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path 路径
     * @return
     */
    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 判断文件是否是文件夹
     *
     * @param path 路径
     * @return
     */
    public static boolean isDirectory(String path) {
        File file = new File(path);
        return file.isDirectory();
    }

    /**
     * 创建文件夹
     *
     * @param path 路径
     * @return
     */
    public static String createDir(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            flag = file.mkdir();
        } else if (!file.isDirectory()) {
            if (file.delete()) {
                flag = file.mkdir();
            }
        } else {
            flag = true;
        }
        if (!flag) {
            throw new RuntimeException("Create Directory Failure, path:" + path);
        }
        return file.getName();
    }

    /**
     * 获取文件
     *
     * @param path 路径
     * @return
     */
    public static File getFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("File Not Exists, path:" + path);
        }
        return file;
    }

    /**
     * 创建文件
     *
     * @param path 路径
     * @return
     */
    private static File createFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Create File Failure, path:" + path);
            }
        }
        return file;
    }

    /**
     * 删除文件
     *
     * @param path 路径
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        } else {
            return true;
        }
    }

    /**
     * 获取子文件数组
     *
     * @param path 路径
     * @return
     */
    public static String[] getFiles(String path) {
        return getChildDirs(path, null, true);
    }

    /**
     * 获取子文件夹数组
     *
     * @param path       路径
     * @param filterPath 过滤参数
     * @return
     */
    public static String[] getChildDirs(String path, String filterPath) {
        return getChildDirs(path, filterPath, false);
    }

    /**
     * 获取子文件夹或文件数组
     *
     * @param path       路径
     * @param filterPath 过滤参数
     * @param isFile     true 文件 false 文件夹
     * @return
     */
    private static String[] getChildDirs(String path, String filterPath, boolean isFile) {
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("File Not Exists, path:" + path);
        }
        if (!file.isDirectory()) {
            throw new RuntimeException("Is Not A Directory, path:" + path);
        }
        FilenameFilter filenameFilter = null;
        if (null != filterPath) {
            filenameFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String filter = "*";
                    if (filter.equals(filterPath)) {
                        return true;
                    } else if (filterPath.startsWith(filter) || filterPath.endsWith(filter)) {
                        String regex = "(" + filterPath.replace(filter, ".*") + ")";
                        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(name);
                        return matcher.matches();
                    }
                    return false;
                }
            };
        }
        File[] fileList = file.listFiles(filenameFilter);
        if (null != fileList && fileList.length > 0) {
            List<String> dirs = new ArrayList<>();
            for (File s : fileList) {
                // 若当前为文件且需要获取的是文件集合 或 当前是文件夹且需要获取的是文件夹时
                boolean flag = (s.isFile() && isFile) || (s.isDirectory() && !isFile);
                if (flag) {
                    dirs.add(s.getPath());
                }
            }
            return dirs.toArray(new String[0]);
        }
        return new String[0];
    }

}
