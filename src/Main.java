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
        path = "E:\\mastercom\\log\\firestrom.txt";
        String writePath = "E:\\mastercom\\log\\firestormfilter.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            BufferedWriter writer = new BufferedWriter(new FileWriter(writePath));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("共入库")) {
                    writer.write(line.substring(line.indexOf("共入库"), line.length()));
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
