package con.rainyseason.deleres

import com.rainyseason.deleres.removeDrawable
import com.rainyseason.deleres.removeEpoxy
import com.rainyseason.deleres.removeLayout
import com.rainyseason.deleres.removeMenu
import com.rainyseason.deleres.removePrimitive
import org.gradle.api.tasks.TaskAction

open class DeleresTask : BaseTask() {
  @TaskAction
  fun remove() {
    removePrimitive(
      path = checkPath,
      checkString = checkString,
      checkColor = checkColor,
      checkStringArray = checkStringArray,
      checkBool = checkBool,
      checkDimen = checkDimen,
      checkInteger = checkInteger
    )
    removeDrawable(
      path = checkPath,
      extensions = drawableExtensions
    )
    if (checkLayout) {
      removeLayout(path = checkPath)
    }
    if (checkMenu) {
      removeMenu(path = checkPath)
    }
    if (checkEpoxyModelView) {
      removeEpoxy(path = checkPath)
    }
  }
}
