package top.javahouse.properties.property;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImport {


}

/*
@Import注解提供了三种用法

1、@Import一个普通类 spring会将该类加载到spring容器中

2、@Import一个类，该类实现了ImportBeanDefinitionRegistrar接口，在重写的registerBeanDefinitions方法里面，能拿到BeanDefinitionRegistry bd的注册器，能手工往beanDefinitionMap中注册 beanDefinition

3、@Import一个类 该类实现了ImportSelector 重写selectImports方法该方法返回了String[]数组的对象，数组里面的类都会注入到spring容器当中*/

//------1---------
class MyClass {
    public void test() {
        System.out.println("test方法");
    }
}

@Import(MyClass.class)
 class ImportConfig {
}

//------2---------
class MyClassRegistry {
    public void test() {
        System.out.println("MyClassRegistry test方法");
    }
}

 class MyImportRegistry implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(MyClassRegistry.class);
        registry.registerBeanDefinition("myClassRegistry", bd);
    }
}

@Import(MyImportRegistry.class)
 class ImportConfigs {

}

//------3---------

 class MyClassImport {
    public void test() {
        System.out.println("MyClassImport test方法");
    }
}

 class MyImportSelector implements ImportSelector {

    //   2. 创建MyImportSelector实现ImportSelector接口 注册我们定义的普通类MyClassImport
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {MyClassImport.class.getName()};
    }

}

@Import(MyImportSelector.class)
 class ImportConfigf {

}
