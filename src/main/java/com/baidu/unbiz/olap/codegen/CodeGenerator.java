package com.baidu.unbiz.olap.codegen;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import org.slf4j.Logger;

import com.baidu.unbiz.olap.common.MethodPair;
import com.baidu.unbiz.olap.exception.ReportException;
import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.utils.Fs64Utils;
import com.baidu.unbiz.olap.utils.OlapUtils;

public class CodeGenerator {

    protected final static Logger LOG = AopLogFactory.getLogger(CodeGenerator.class);

    private static final Map<String, String> boxingMap = new HashMap<String, String>();
    private static final Map<String, String> unboxingMap = new HashMap<String, String>();
    private static final Set<String> primitiveTypeSet = new HashSet<String>();

    static {
        boxingMap.put("char", "Char");
        boxingMap.put("byte", "Byte");
        boxingMap.put("short", "Short");
        boxingMap.put("int", "Integer");
        boxingMap.put("long", "Long");
        boxingMap.put("float", "Float");
        boxingMap.put("double", "Double");
        boxingMap.put("boolean", "Boolean");

        unboxingMap.put("Char", "char");
        unboxingMap.put("Byte", "byte");
        unboxingMap.put("Short", "short");
        unboxingMap.put("Integer", "int");
        unboxingMap.put("Long", "long");
        unboxingMap.put("Float", "float");
        unboxingMap.put("Double", "double");
        unboxingMap.put("Boolean", "boolean");

        primitiveTypeSet.addAll(Arrays.asList("char", "byte", "short", "int", "long", 
                "float", "double", "boolean" ));
    }

    /**
     * 是否为原生类型
     * 
     * @param typeName
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static boolean isPrimitiveType(String typeName) {
        return primitiveTypeSet.contains(typeName);
    }

    /**
     * 装箱类型
     * 
     * @param primitive
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static String boxing(String primitive) {
        String pack = boxingMap.get(primitive);
        return pack == null ? primitive : pack;
    }

    /**
     * 拆箱类型
     * 
     * @param pack
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static String unboxing(String pack) {
        String primitive = unboxingMap.get(pack);
        return pack == null ? pack : primitive;
    }

    /**
     * Joiner缓存
     */
    private static AtomicComputeCache<String, OlapJoinerSupport> atomicComputeJoinerCache =
            new AtomicComputeCache<String, OlapJoinerSupport>();

    /**
     * 按指定class的key和value获取OlapJoinerSupport
     * 
     * @param mergeKeys
     * @param mergeVals
     * @param clazz
     * @return OlapJoinerSupport
     * @since 2015-7-28 by wangchongjie
     */
    public static OlapJoinerSupport getOlapJoinerSupportClass(final Set<String> mergeKeys, final Set<String> mergeVals,
            final Class<?> clazz) {

        String keyInCache = clazz.getName() + mergeKeys + mergeVals;
        OlapJoinerSupport result = atomicComputeJoinerCache.preGetAlreadyDoneResult(keyInCache);
        if (result != null) {
            return result;
        }
        result = atomicComputeJoinerCache.getComputeResult(keyInCache, new Callable<Object>() {
            @Override
            public OlapJoinerSupport call() {
                return CodeGenerator.generateJoinerClassCode(mergeKeys, mergeVals, clazz);
            }
        });
        return result;
    }

    /**
     * 生成SetValuesMethod
     * 
     * @param mergeVals
     * @param clazz
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private static String generateSetValuesMethod4JoinerClassCode(Set<String> mergeVals, Class<?> clazz) {
        String clazzName = clazz.getName();
        String setValuesMethod =
                "public void setValues(Object source, Object target){" 
                        + clazzName + " s = (" + clazzName + ") source;"
                        + clazzName + " t = (" + clazzName + ") target;";

        Map<String, MethodPair> methodMap = OlapUtils.generateMethodMapper(clazz);
        for (String val : mergeVals) {
            MethodPair methods = methodMap.get(val);
            methods = (methods == null) ? methodMap.get((val = val.toLowerCase())) : methods;
            Method getter = methods.getter;
            String getterName = getter.getName();
            Method setter = methods.setter;
            String setterName = setter.getName();
            setValuesMethod += "t." + setterName + "(" + "s." + getterName + "()" + ");";
        }
        setValuesMethod += "}";
        return setValuesMethod;
    }

    /**
     * 生成GetKeysMethod
     * 
     * @param mergeKeys
     * @param clazz
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private static String generateGetKeysMethod4JoinerClassCode(Set<String> mergeKeys, Class<?> clazz) {
        String clazzName = clazz.getName();
        String getKeysMethod =
                "public String getKeys(Object t){" + clazzName + " item = (" + clazzName + ") t;"
                        + "StringBuilder multiKey = new StringBuilder(\"\");";

        Map<String, MethodPair> methodMap = OlapUtils.generateMethodMapper(clazz);
        for (String key : mergeKeys) {
            MethodPair methods = methodMap.get(key);
            Method getter = methods.getter;
            String getterName = getter.getName();
            getKeysMethod += "multiKey.append(\"-\");multiKey.append(item." + getterName + "());";
        }
        getKeysMethod += "return multiKey.toString();}";
        return getKeysMethod;
    }

    private static AtomicBoolean alreadyInitClassPath = new AtomicBoolean(false);

    /**
     * 生成OlapJoinerSupport
     * 
     * @param mergeKeys
     * @param mergeVals
     * @param clazz
     * @return OlapJoinerSupport
     * @since 2015-7-28 by wangchongjie
     */
    private static OlapJoinerSupport generateJoinerClassCode(Set<String> mergeKeys, Set<String> mergeVals,
            Class<?> clazz) {

        String helperName = generateJoinerHelperClassName(mergeKeys, mergeVals, clazz);
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(helperName);
        if (!alreadyInitClassPath.get()) {
            pool.appendClassPath(new ClassClassPath(clazz));
            alreadyInitClassPath.set(true);
        }

        String getKeysMethod = generateGetKeysMethod4JoinerClassCode(mergeKeys, clazz);
        // System.out.println("-----" + getKeysMethod);
        LOG.info("CodeGenerator--getKeysMethod body:" + getKeysMethod);

        String setValuesMethod = generateSetValuesMethod4JoinerClassCode(mergeVals, clazz);
        // System.out.println("-----"+setValuesMethod);
        LOG.info("CodeGenerator--setValuesMethod body:" + setValuesMethod);

        OlapJoinerSupport joiner = null;
        String errMsg = "generateJoinerClassCode fail: ";
        try {
            ctClass.addInterface(pool.get(OlapJoinerSupport.class.getName()));
            CtMethod newMethod = CtNewMethod.make(getKeysMethod, ctClass);
            ctClass.addMethod(newMethod);

            newMethod = CtNewMethod.make(setValuesMethod, ctClass);
            ctClass.addMethod(newMethod);

            CtConstructor cons = new CtConstructor(new CtClass[] {}, ctClass);
            cons.setBody("{System.out.println(\"class code init---" + helperName + "\");}");
            ctClass.addConstructor(cons);
            // ctClass.writeFile();
            // ctClass.defrost();
            Class<?> joinerClazz = ctClass.toClass();
            joiner = (OlapJoinerSupport) joinerClazz.newInstance();
            ctClass.detach();

        } catch (CannotCompileException e) {
            LOG.error(errMsg, e);
            throw new ReportException(e);
        } catch (InstantiationException e) {
            LOG.error(errMsg, e);
            throw new ReportException(e);
        } catch (IllegalAccessException e) {
            LOG.error(errMsg, e);
            throw new ReportException(e);
        } catch (NotFoundException e) {
            LOG.error(errMsg, e);
            throw new ReportException(e);
        }

        return joiner;
    }

    /**
     * 生成JoinerHelper的类名称
     * 
     * @param mergeKeys
     * @param mergeVals
     * @param clazz
     * @return JoinerHelper的类名称
     * @since 2015-7-28 by wangchongjie
     */
    private static String generateJoinerHelperClassName(Set<String> mergeKeys, Set<String> mergeVals, Class<?> clazz) {
        String clazzName = clazz.getName();
        String paramSign = Fs64Utils.signFs64("" + mergeKeys + mergeVals).toString();
        String helperSuffix = "JoinerSupport_";
        String helperName = clazzName + helperSuffix + paramSign;
        return helperName;
    }

}
