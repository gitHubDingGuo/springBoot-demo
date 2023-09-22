
import java.io.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
public class AllFileNames {

    private static String filePath="D://公司项目//springBoot//spring-boot-demo//spring-boot-demo-logback//README.md";



    public static void main(String[] args) throws IOException {

        File md=new File("D://公司项目//springBoot//spring-boot-demo//spring-boot-demo-logback//README.md");
        md.createNewFile();

        BufferedWriter out = null;
        //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(md, true)));
        out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(md, true)));
       // out.write("# spring-boot-demo-logbakc"+"\n");
      //  out.write("> 此 demo 主要演示了 Spring Boot 如何使用logback"+"\n"+"\n"+"\n");

        Path path = Paths.get(filePath);
        String content="# spring-boot-demo-logback"+"\n"+"\n";
        String content1="> 此 demo 主要演示了 Spring Boot 如何使用logback"+"\n"+"\n";

        Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
        Files.write(path, content1.getBytes(), StandardOpenOption.APPEND);


        /*FileWriter fileWriter=new FileWriter(md);
        fileWriter.write("# spring-boot-demo-logbakc"+"\n");
        fileWriter.write("> 此 demo 主要演示了 Spring Boot 如何使用logback"+"\n"+"\n"+"\n");

         */
        //1、根据路径创建一个File对象
        File file = new File("D:\\公司项目\\springBoot\\spring-boot-demo\\spring-boot-demo-logback\\src\\main");
        //2、调用fileTest()方法
        fileTest(file, "",path);
    }
    /**
     * 判断是文件还是目录
     * @param file
     * @param temp
     */

    public static void fileTest(File file, String temp,Path path) throws IOException {
        //如果是文件的情况
        if (file.isFile()){
            System.out.print("文件 :" + file.getName() + "\t");
            System.out.print("文件path :" + file.getAbsolutePath() + "\t");

            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }else{
            //如果是目录的情况
            //创建一个File数组来存储当前目录下所有文件和目录的绝对路径
            File[] files = file.listFiles();
            //循环遍历files
            for (File fileTemp : files){
                //子级是目录
                if (fileTemp.isDirectory()){
                    //System.out.println(temp + "目录 :" + fileTemp.getName() + "\t");
                    //递归再次进行判断
                    fileTest(fileTemp, temp + "\t",path);
                }else{
                    //子级是文件
                    /*System.out.println("## " + fileTemp.getName() + "\t");
                    System.out.println("```"+TypeEnum.getName(fileTemp.getName())+"\n");
                    FileReader reader = new FileReader(fileTemp);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println(line);
                    }
                    System.out.println("```"+"\n");
                    */
                    String one="## " + fileTemp.getName() + "\n";
                    Files.write(path, one.getBytes(), StandardOpenOption.APPEND);
                    String two="```"+TypeEnum.getName(fileTemp.getName())+"\n";
                    Files.write(path, two.getBytes(), StandardOpenOption.APPEND);
                    FileReader reader = new FileReader(fileTemp);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        line=line+"\n";
                        Files.write(path, line.getBytes(), StandardOpenOption.APPEND);
                    }
                    String three="```"+"\n";
                    Files.write(path, three.getBytes(), StandardOpenOption.APPEND);
                }
            }
        }
    }
}
