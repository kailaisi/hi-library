package com.kailaisi.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.io.ByteStreams
import javassist.ClassPool
import javassist.CtClass
import javassist.NotFoundException
import javassist.bytecode.ClassFile
import javassist.bytecode.stackmap.BasicBlock
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class TinyTransform extends Transform {

    private ClassPool classPool = ClassPool.getDefault()

    TinyTransform(Project project) {
        //为了能够查找到android相关的类，需要把android.jar包的路径添加到classPool类搜索路径中
        classPool.appendClassPath(project.android.bootClasspath[0].toString())
        classPool.importPackage("android.os.Bundle")
        classPool.importPackage("android.widget.Toast")
        classPool.importPackage("android.app.Activity")
    }

    @Override
    String getName() {
        return "TinyPngTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        //接收的输入类型
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        //1。对input   directory  class 文件进行遍历
        //2。对input   jarInputs  class 文件进行遍历
        //3。对符合项目包名的activity.class文件进行处理
        def outputProvider = transformInvocation.outputProvider
        transformInvocation.inputs.each { input ->
            input.directoryInputs.each { dir ->
                println("dirInput abs file path:" + dir.file.absolutePath)
                handleDirectory(dir.file)

                //将input.dir.class->dest目标目录中去
                def dest = outputProvider.getContentLocation(dir.name, dir.contentTypes, dir.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(dir.file, dest)
            }
            input.jarInputs.each { jarInputs ->
                //  println("jarInput abs file path:" + jarInputs.file.absolutePath)
                //对jar包修改完成之后，还会返回一个新的jar文件
                def srcFile = handleJar(jarInputs.file)
                def jarName = jarInputs.name
                def md5 = DigestUtils.md5Hex(jarInputs.file.absolutePath)
                if (jarName.endsWith(".jarInputs")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                //将input.dir.class->dest目标目录中去
                def dest = outputProvider.getContentLocation(md5 + jarName, jarInputs.contentTypes, jarInputs.scopes, Format.JAR)
                FileUtils.copyFile(srcFile, dest)
            }
        }
        classPool.clearImportedPackages()
    }

    /**
     * 处理当前目录下所有的class文件
     * @param file
     */
    void handleDirectory(File dir) {
        classPool.appendClassPath(dir.absolutePath)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { file ->
                def filePath = file.absolutePath
                println("handleDirectory file path:" + filePath)
                if (shouldModifyClass(filePath)) {
                    def inputStream = new FileInputStream(file)
                    def ctClass = modifyClass(inputStream)
                    //写入磁盘
                    ctClass.writeFile(dir.name)
                    ctClass.detach()
                }
            }
        }
    }

    File handleJar(File jarFile) {
        classPool.appendClassPath(jarFile.absolutePath)
        //ssesWithTinyPngPTransformForDebug
        //jarInputs abs file path :/Users/timian/Desktop/AndroidArchitect/AndroidArchitect/ASProj/app/build/intermediates/transforms/com.alibaba.arouter/debug/0.jar
        def inputJarFile = new JarFile(jarFile)
        def enumeration = inputJarFile.entries()
        def outputJarFile = new File(jarFile.parentFile, "temp_" + jarFile.name)
        if (outputJarFile.exists()) outputJarFile.delete()
        def jarOutputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(outputJarFile)))
        while (enumeration.hasMoreElements()) {
            def inputJarEntry = enumeration.nextElement()
            def inputJarEntryName = inputJarEntry.name

            def outputJarEntry = new JarEntry(inputJarEntryName)
            jarOutputStream.putNextEntry(outputJarEntry)
            //com/leon/channel/helper/BuildConfig.class
            //    println("inputJarEntryName: " + inputJarEntryName)

            def inputStream = inputJarFile.getInputStream(inputJarEntry)
            if (!shouldModifyClass(inputJarEntryName)) {
                ByteStreams.copy(inputStream, jarOutputStream)
                inputStream.close()
                continue
            }
            def ctClass = modifyClass(inputStream)
            def byteCode = ctClass.toBytecode()
            ctClass.detach()
            inputStream.close()

            jarOutputStream.write(byteCode)
            jarOutputStream.flush()
        }
        inputJarFile.close()
        jarOutputStream.closeEntry()
        jarOutputStream.flush()
        jarOutputStream.close()
        return outputJarFile
    }

    CtClass modifyClass(InputStream is) {
        def classFile = new ClassFile(new DataInputStream(new BufferedInputStream(is)))
        println("modifyClass name:" + classFile.name)
        def ctClass = classPool.get(classFile.name)
        if (ctClass.isFrozen()) {
            ctClass.defrost()
        }
        def bundle = classPool.get("android.os.Bundle")
        try {
            CtClass[] params = Arrays.asList(bundle).toArray()
            def method = ctClass.getDeclaredMethod('onCreate', params)
            def message = classFile.name
            method.insertAfter("Toast.makeText(this," + "\"" + message + "\"" + ",Toast.LENGTH_SHORT).show();")
        } catch (NotFoundException e) {
            println(e.getMessage())
        }
        return ctClass
    }


    boolean shouldModifyClass(String filePath) {
        filePath = filePath.replace("/", ".")
                .replace("\"", ".")
                .replace(File.separator, ".")
        return (filePath.contains("com.kailaisi")
                && filePath.endsWith("Activity.class")
                && !filePath.contains("R.class")
                && !filePath.contains('$')
                && !filePath.contains('R$')
                && !filePath.contains("BuildConfig.class"))

    }
}