import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Bin2Hex {

    public static void main(String[] args) {

        File path = getInputName();
        boolean result = false;
        try {
            byte[] bytes = getFileContent(path);
            result = writeFile(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            System.out.println(result ? "文件转换成功" : "文件转换失败");
        }
    }

    private static File getInputName() {
        while (true) {
            System.out.println("请把你要转化成HEX的文件放到当前目录中");
            System.out.println("输入文件名");
            String binName = null;
            Scanner scanner = new Scanner(System.in);
            binName = scanner.nextLine();
            if (binName == null || binName.length() <= 0) {
                System.out.println("输入文件名为空，请重新输入");
            } else {
                File file = getCurrentPath(binName);
                if (file != null) {
                    return file;
                }
            }
        }
    }

    private static File getCurrentPath(String name) {
        File file = new File(System.getProperty("user.dir"), name);
        System.out.println(file.getAbsolutePath());
        if (file.exists()) {
            System.out.println(file.getAbsolutePath());
            return file;
        } else {
            System.out.println(file.getAbsolutePath() + "文件不存在");
            return null;
        }
    }

    private static File getOutPath(String name) {
        File file = new File(System.getProperty("user.dir"), name);
        if (file.exists()) {
            file.delete();
        }
        return file;
    }

    private static byte[] getFileContent(File file) throws IOException {
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

    private static boolean writeFile(byte[] fileBuff) throws IOException {
        File outPath = getOutPath("输入.txt");
        FileWriter fw = new FileWriter(getOutPath("输入.txt"));
        fw.write(toHexNoSpace(fileBuff, 0, fileBuff.length).toUpperCase());
        fw.close();
        return outPath.exists() && outPath.length() > 0;
    }

    public static String toHexNoSpace(byte[] data, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        if (len + offset > data.length) {
            len = data.length - offset;
        }
        for (int i = offset; i < len + offset; i++) {
            sb.append(String.format("%02x", data[i] & 0xff));
        }
        return sb.toString();
    }

}
