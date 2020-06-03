package con.rainyseason.deleres

import org.gradle.api.Plugin
import org.gradle.api.Project


@Suppress("unused")
class DeleresPlugin : Plugin<Project> {
  private fun configTask(project: Project, task: BaseTask, extension: ConfigExtension) {
    task.checkPath = extension.path.ifBlank { project.rootProject.projectDir.path }
    task.checkString = extension.checkString
    task.checkColor = extension.checkColor
    task.checkStringArray = extension.checkStringArray
    task.checkBool = extension.checkBool
    task.checkDimen = extension.checkDimen
    task.checkInteger = extension.checkInteger
    task.drawableExtensions = extension.drawableExtensions
    task.checkLayout = extension.checkLayout
    task.checkMenu = extension.checkMenu
    task.checkEpoxyModelView = extension.checkEpoxyModelView
  }

  override fun apply(project: Project) {
    val extension = project.extensions.create("deleres", ConfigExtension::class.java)
    project.tasks.register("deleres", DeleresTask::class.java) { task ->
      configTask(project, task, extension)
    }
    project.tasks.register("deleresVerify", DeleresVerifyTask::class.java) { task ->
      configTask(project, task, extension)
    }
  }
}