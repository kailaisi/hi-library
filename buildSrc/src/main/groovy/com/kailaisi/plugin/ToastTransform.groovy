package com.kailaisi.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.io.ByteStreams
import javassist.*
import javassist.bytecode.ClassFile
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

/**
 * 通过字节码调整，修改ImageView的setImageDrawable方法，里面增加上对应的图片检测的功能
 * 1.生成一个RunnableImpl类，
 * 2.在setImageDrawable中调用中的方法，检测图片大小和对应的View的大小是否满足一定的条件。
 */
class ToastTransform extends Transform {

    private static final String RunnableImplName = "com.kailaisi.hi_debugtool.RunnableImpl"

    private ClassPool classPool = ClassPool.getDefault()

    ToastTransform(Project project) {
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
                if (jarInputs.file.size() != 0) {
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
                if (shouldModifyClass2(filePath)) {
                    def inputStream = new FileInputStream(file)
                    def ctClass = modifyClass2(inputStream)
                    //写入磁盘
                    ctClass.writeFile(dir.name)
                    ctClass.detach()
                }
            }
        }
    }

    File handleJar(File jarFile) {
        if (jarFile.size() == 0) {
        }
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
            if (!shouldModifyClass2(inputJarEntryName)) {
                ByteStreams.copy(inputStream, jarOutputStream)
                inputStream.close()
                continue
            }
            def ctClass = modifyClass2(inputStream)
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

    //这个方法是 往appcomimageview -setimagedrawable --插入不合理大图检测的代码段
    CtClass modifyClass2(InputStream is) {
        def classFile = new ClassFile(new DataInputStream(new BufferedInputStream(is)))
        //org.devio.as.proj.main.degrade.DegradeGlobalActivity
        println("modifyClass name：" + classFile.name)//全类名
        def ctClass = classPool.get(classFile.name)
        if (ctClass.isFrozen()) {
            ctClass.defrost()
        }
        def drawable = classPool.get("android.graphics.drawable.Drawable")
        CtClass[] params = Arrays.asList(drawable).toArray()
        def setImageDrawableMethod = ctClass.getDeclaredMethod("setImageDrawable", params)
        def runnableImpl
        try {
            runnableImpl = classPool.makeClass(RunnableImplName)
            if (runnableImpl.isFrozen()) {
                runnableImpl.defrost()
            }
            CtField viewField = new CtField(classPool.get("androidx.appcompat.widget.AppCompatImageView"), "view", runnableImpl)
            viewField.setModifiers(Modifier.PUBLIC)
            runnableImpl.addField(viewField)


            CtField drawableField = new CtField(classPool.get("android.graphics.drawable.Drawable"), "drawable", runnableImpl)
            drawableField.setModifiers(Modifier.PUBLIC)
            runnableImpl.addField(drawableField)

            runnableImpl.addConstructor(CtNewConstructor.make("public RunnableImpl(android.view.View view, android.graphics.drawable.Drawable drawable) {\n" +
                    "            this.view = view;\n" +
                    "            this.drawable = drawable;\n" +
                    "        }", runnableImpl))

            runnableImpl.addInterface(classPool.get("java.lang.Runnable"))

            CtMethod runMethod = new CtMethod(CtClass.voidType, "run", null, runnableImpl)
            runMethod.setModifiers(Modifier.PUBLIC)
            runMethod.setBody("{int width = view.getWidth();\n" +
                    "            int height = view.getHeight();\n" +
                    "            int drawableWidth = drawable.getIntrinsicWidth();\n" +
                    "            int drawableHeight = drawable.getIntrinsicHeight();\n" +
                    "            if (width > 0 && height > 0) {\n" +
                    "                if (drawableWidth >= 2 * width && drawableHeight >= 2 * height) {\n" +
                    "                    android.util.Log.e(\"LargeBitmapChecker\", \"bitmap:[\" + drawableWidth + \",\" + drawableHeight + \"],view:[\" + width + \",\" + height + \"],className:\" + view.getContext().getClass().getSimpleName());\n" +
                    "                }\n" +
                    "            }\n" +
                    "            android.util.Log.e(\"LargeBitmapChecker\", \"bitmap:[\" + drawableWidth + \",\" + drawableHeight + \"],view:[\" + width + \",\" + height + \"],className:\" + view.getContext().getClass().getSimpleName());}")
            runnableImpl.addMethod(runMethod)
            runnableImpl.writeFile("hi_debugtool/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes")
            runnableImpl.toClass()

            classPool.insertClassPath(RunnableImplName)
            setImageDrawableMethod.insertBefore("if(drawable!=null){ post(new RunnableImpl(this, drawable));}")
        }

        catch (Exception e) {}
        return ctClass
    }


    boolean shouldModifyClass2(String filePath) {
        return (filePath.contains("androidx/appcompat/widget/AppCompatImageView")
                && filePath.endsWith("AppCompatImageView")
                && !filePath.contains("R.class")
                && !filePath.contains('$')
                && !filePath.contains('R$')
                && !filePath.contains("BuildConfig.class"))
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