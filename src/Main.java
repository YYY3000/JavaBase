import java.io.*;

public class Main {

    public static void main(String[] args) {
        readFile("");
    }

    public static void fileRename(String path) {
        path = "E:\\mastercom\\sql\\导入";
        File file = new File(path);
        File[] files = file.listFiles();
        for (File file1 : files) {
            String name = file1.getName();
            if (!name.contains("_copy")) {
                file1.renameTo(new File(path + File.separator + name.replace(".bcp", "") + "_copy.bcp"));
            }
            System.out.println(file1.getParent());
        }
    }

    public static void readFile(String path) {
        path = "E:\\mastercom\\log\\clog.txt";
        String writePath = "E:\\mastercom\\log\\clogfilter.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            BufferedWriter writer = new BufferedWriter(new FileWriter(writePath));
            int i = 0;
            String line = null;
            do {
                line = reader.readLine();
                if (line == null || line.equals("")) {
                    i++;
                } else {
                    if (line.contains("共入库") && !line.contains("共入库 0 条记录.另有 0 条无效记录被忽略.")) {
                        writer.write(line.substring(line.indexOf("共入库"), line.length()));
                        writer.newLine();
                    }
                    i = 0;
                }
            } while (i < 10);
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
