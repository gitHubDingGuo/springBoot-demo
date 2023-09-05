package top.javahouse.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class HdfsMysql {
    //数据库：ceshi  Host Address：localhost  端口：3306
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/my_project?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF8&allowMultiQueries=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "12580!";

    public static void main(String[] args) throws URISyntaxException, IOException, ClassNotFoundException, SQLException {
        /**
         * 创建filesystem对象，连接hdfs
         */
        URI uri = new URI("hdfs://106.13.58.73:9000");
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(uri, conf);
        /**
         * 连接数据库
         */
        Connection conn = null;
        Statement stmt = null;
        Class.forName(JDBC_DRIVER);//注册JDBC驱动
        conn = DriverManager.getConnection(DB_URL, USER, PASS);//打开数据库
        PreparedStatement ps = conn.prepareStatement("insert into photo(photo_id) values (?)");
        /**
         * 从hdfs中读取数据,切分数据，插入MySQL
         */
        FSDataInputStream in = fs.open(new Path("/in/text1.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",");
            ps.setString(1, split[0]);
            //ps.setString(2, split[1]);
            //ps.setInt(3, Integer.parseInt(split[2]));
            ps.executeUpdate();
        }
        ps.close();
        in.close();
        br.close();
    }
}
