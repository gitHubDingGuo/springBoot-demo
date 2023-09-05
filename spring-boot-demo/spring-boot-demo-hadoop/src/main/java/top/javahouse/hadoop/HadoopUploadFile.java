package top.javahouse.hadoop;


// https://gitee.com/nkuhyx/winutils/tree/master/hadoop-3.3.0-YARN-8246/bin
// https://blog.csdn.net/whs0329/article/details/121878162
import java.io.BufferedInputStream;

        import java.io.FileInputStream;

        import java.io.FileNotFoundException;

        import java.io.InputStream;

        import java.io.OutputStream;

        import java.net.URI;

        import org.apache.hadoop.conf.Configuration;

        import org.apache.hadoop.fs.FileStatus;

        import org.apache.hadoop.fs.FileSystem;

        import org.apache.hadoop.fs.Path;

        import org.apache.hadoop.io.IOUtils;

        import org.apache.hadoop.util.Progressable;

public class HadoopUploadFile {
    public static void main(String[] args) {

        try {

            String localSrc = "D://log.txt";

            String dst = "hdfs://123.com:9000/user/root/log.txt";

            InputStream in = new BufferedInputStream(new FileInputStream(localSrc));

            Configuration conf = new Configuration();

            FileSystem fs = FileSystem.get(URI.create(dst), conf);

            OutputStream out = fs.create(new Path(dst), new Progressable() {

                public void progress() {
                    System.out.print(".");
                }

            });

            IOUtils.copyBytes(in, out, 4096, true);

            System.out.println("success");

        } catch (Exception e) {

// TODO Auto-generated catch block

            e.printStackTrace();

        }

    }

}
