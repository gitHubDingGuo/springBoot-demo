package top.javahouse.xxljobClient.executor.core.config;


import java.lang.reflect.Field;

public class Person {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

         Person person=new Person();
         person.setName("1111");
         Class classes=person.getClass();
         Field field=classes.getDeclaredField("name");
         field.setAccessible(true);
         Object object=field.get(person);
         System.out.println(object.toString());
    }
}
