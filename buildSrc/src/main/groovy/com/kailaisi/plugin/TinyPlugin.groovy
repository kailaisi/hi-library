package com.kailaisi.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

class TinyPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            //非主工程目录
            throw new ProjectConfigurationException("TinyPlugin must be applied to com.android.application project", null)
        }
       /* //找到android中的配置  和下面的效果一样
        def android = project.extensions.findByType(AppExtension.class)
        android.registerTransform(new TinyTransform())*/
        project.android.registerTransform(new ToastTransform(project))
    }
}
