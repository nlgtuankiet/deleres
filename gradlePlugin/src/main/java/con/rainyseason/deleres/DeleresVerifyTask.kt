package con.rainyseason.deleres

import com.rainyseason.deleres.removeDrawable
import com.rainyseason.deleres.removeEpoxy
import com.rainyseason.deleres.removeLayout
import com.rainyseason.deleres.removeMenu
import com.rainyseason.deleres.removePrimitive
import org.gradle.api.tasks.TaskAction

open class DeleresVerifyTask : BaseTask() {
  @TaskAction
  fun verify() {
    val onFound = {
      throw IllegalStateException("Found unused resource, run deleres task to remove all unused resource")
    }
    removePrimitive(
      path = checkPath,
      checkString = checkString,
      checkColor = checkColor,
      checkStringArray = checkStringArray,
      checkBool = checkBool,
      checkDimen = checkDimen,
      checkInteger = checkInteger,
      onFoundUnused = onFound
    )
    removeDrawable(
      path = checkPath,
      extensions = drawableExtensions,
      onFoundUnused = onFound
    )
    if (checkLayout) {
      removeLayout(
        path = checkPath,
        onFoundUnused = onFound
      )
    }
    if (checkMenu) {
      removeMenu(
        path = checkPath,
        onFoundUnused = onFound
      )
    }
    if (checkEpoxyModelView) {
      removeEpoxy(
        path = checkPath,
        onFoundUnused = onFound
      )
    }
  }
}