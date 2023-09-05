package top.javahouse.hadoop;

import java.sql.*;

public class example1 {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/my_project?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF8&allowMultiQueries=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "12580";

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Connection conn = null;

        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // 执行查询
            System.out.println("实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM sys_user ";//搜索login表，实际须填入数据库的表名
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("user_id");//得到“id”列的值，类型需要与数据库的数值类型相同
                String password = rs.getString("user_name");//得到“password”列的值，类型需要与数据库的数值类型相同
                //如有其他列可以选择添加
                // 输出数据
                System.out.print("ID: " + id);
                System.out.print(", 密码: " + password);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
}
