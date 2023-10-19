package com.porco.javassist.dynamic;

import com.alibaba.excel.annotation.ExcelProperty;
import com.porco.javassist.domain.Lc;
import com.porco.javassist.domain.Template;
import com.porco.javassist.domain.TemplateField;
import com.porco.javassist.mapper.TemplateFieldMapper;
import com.porco.javassist.mapper.TemplateMapper;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <a href="https://blog.csdn.net/m0_61933976/article/details/128519763">...</a>
 */
@Slf4j
@Service
public class ClassCreator implements ApplicationRunner {
    public static final Map<String, Function<Object, Object>> FUNCTION = new HashMap<>();
    // 1. prepare
    private final TemplateMapper templateMapper;
    private final TemplateFieldMapper fieldMapper;

    public ClassCreator(TemplateMapper templateMapper, TemplateFieldMapper fieldMapper) {
        this.templateMapper = templateMapper;
        this.fieldMapper = fieldMapper;
    }

    //    @PostConstruct
    private static void createPerson() throws NotFoundException, CannotCompileException, IOException {

        // 1. prepare
        ClassPool pool = ClassPool.getDefault();
        // 2. make class
        CtClass person = pool.makeClass("com.porco.javassist.domain.Person");

        // 3. insert field
        CtField nameField = new CtField(pool.get("java.lang.String"), "name", person);
        // 设置字段的访问类型为private
        nameField.setModifiers(Modifier.PRIVATE);

        CtField ageField = CtField.make("private Integer age = Integer.valueOf(\"23\");", person);
        // CtField ageField = CtField.make("private int age = 23;", person);
        // CtField ageField = new CtField(pool.get(Integer.class.getTypeName()), "age", person);
        ageField.setModifiers(Modifier.PRIVATE);

        CtField dateField = new CtField(pool.get("java.time.LocalDateTime"), "date", person);
        // 设置字段的访问类型为private
        dateField.setModifiers(Modifier.PRIVATE);

        // default value of field
        person.addField(nameField, CtField.Initializer.constant("Porco"));
        // person.addField(ageField, CtField.Initializer.constant(23));
        person.addField(ageField);
        person.addField(dateField);

        // field annotation
        ConstPool constPool = person.getClassFile().getConstPool();
        FieldInfo nameFieldFieldInfo = nameField.getFieldInfo();
        Annotation annotation = new Annotation(Lc.class.getName(), constPool);
        annotation.addMemberValue("value", new StringMemberValue("老狗", constPool));
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        annotationsAttribute.setAnnotation(annotation);
        nameFieldFieldInfo.addAttribute(annotationsAttribute);

        // 4. getter/setter
        person.addMethod(CtNewMethod.setter("setName", nameField));
        person.addMethod(CtNewMethod.getter("getName", nameField));
        person.addMethod(CtNewMethod.setter("setAge", ageField));
        person.addMethod(CtNewMethod.getter("getAge", ageField));

        // 5. constructor
        CtConstructor constructor = new CtConstructor(new CtClass[0], person);
        constructor.setBody("{name=\"ConstructorName\";age=Integer.valueOf(\"28\");}");
        // constructor.setBody("{name=\"ConstructorName\";age=Integer.parseInt(\"28\");}");
        person.addConstructor(constructor);

        person.writeFile("./demo-javassist/target/classes");

        System.out.println("success...");
    }

    private String bigCamel(String name) {
        char[] chars = name.toCharArray();
        char c = name.charAt(0);
        if (c < 97 || c > 122) {
            return name;
        }
        c -= 32;
        chars[0] = c;
        return new String(chars);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Template> templates = templateMapper.selectAll();
        if (templates.isEmpty()) {
            return;
        }
        ClassPool pool = ClassPool.getDefault();
        // pool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        // String directoryName = "D:\\projects\\demo-springboot\\demo-javassist/target/classes";
        // pool.insertClassPath(directoryName);

        for (Template template : templates) {

            // 2. make class
            CtClass person = pool.makeClass("com.porco.javassist.domain.Template$" + template.getId());

            List<TemplateField> templateFields = fieldMapper.selectAllByTemplateId(template.getId());
            for (TemplateField templateField : templateFields) {
                // 3. insert field
                String fieldName = templateField.getFieldName();
                CtField field = new CtField(pool.get(templateField.getFieldType()), fieldName, person);
                field.setModifiers(Modifier.PRIVATE);
                person.addField(field);
                // field annotation com.alibaba.excel.annotation.ExcelProperty
                ConstPool constPool = person.getClassFile().getConstPool();
                FieldInfo nameFieldFieldInfo = field.getFieldInfo();

                Annotation lcAnnotation = new Annotation(Lc.class.getName(), constPool);
                lcAnnotation.addMemberValue("value", new StringMemberValue(templateField.getBusinessName(), constPool));
                AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                annotationsAttribute.addAnnotation(lcAnnotation);


                Annotation excelAnnotation = new Annotation(ExcelProperty.class.getName(), constPool);
                ArrayMemberValue memberValue = new ArrayMemberValue(new StringMemberValue(templateField.getBusinessName(), constPool), constPool);
                memberValue.setValue(new MemberValue[]{new StringMemberValue(templateField.getBusinessName(), constPool)});
                excelAnnotation.addMemberValue("value", memberValue);
                annotationsAttribute.addAnnotation(excelAnnotation);
                nameFieldFieldInfo.addAttribute(annotationsAttribute);
                // 4. getter/setter
                person.addMethod(CtNewMethod.getter("get" + bigCamel(fieldName), field));
                person.addMethod(CtNewMethod.setter("set" + bigCamel(fieldName), field));

            }
            // 5. constructor
            CtConstructor constructor = new CtConstructor(new CtClass[0], person);
            constructor.setBody("{}");
            person.addConstructor(constructor);

            // 6.
            // 创建calc方法, 带两个参数，参数的类型都为int类型
            // CtMethod ctMethod = new CtMethod(pool.get("java.util.List"), "generateData", new CtClass[]{pool.get("java.util.List")}, person);
            // 设置方法的访问修饰
            // ctMethod.setModifiers(Modifier.PUBLIC);
            // 设置方法体代码
            // ctMethod.setBody("{java.util.List result = new java.util.ArrayList();" +
            //         "            java.util.Iterator iterator = $1.iterator();" +
            //         "            while (iterator.hasNext()) {" +
            //         "                String next = (String) iterator.next();" +
            //         "                result.add(next);" +
            //         "            }" +
            //         "            return result;}");


            // // 添加新建的方法到原有的类中
            // person.addMethod(ctMethod);
            // 6. write class
            // if (log.isDebugEnabled()) {
                person.writeFile("./target");
            // }
            Class<?> aClass = person.toClass();
            for (TemplateField templateField : templateFields) {
                PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(aClass, templateField.getFieldName());
                Method readMethod = propertyDescriptor != null ? propertyDescriptor.getReadMethod() : null;
                FUNCTION.put(template.getId() + "-" + templateField.getFieldName(), i -> {
                    try {
                        if (readMethod != null) {
                            return readMethod.invoke(i, null);
                        } else {
                            return null;
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        System.out.println("success..." + templates);
    }
}
