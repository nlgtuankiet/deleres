package con.rainyseason.deleres

import com.rainyseason.deleres.removeDrawableDefaultExtensions
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input

open class BaseTask : DefaultTask() {
  @get:Input
  var checkPath: String = ""

  // primitive
  @get:Input
  var checkString: Boolean = true

  @get:Input
  var checkColor: Boolean = true

  @get:Input
  var checkStringArray: Boolean = true

  @get:Input
  var checkBool: Boolean = true

  @get:Input
  var checkDimen: Boolean = true

  @get:Input
  var checkInteger: Boolean = true

  // drawable
  @get:Input
  var drawableExtensions: String = removeDrawableDefaultExtensions

  // layout
  @get:Input
  var checkLayout: Boolean = true

  @get:Input
  var checkMenu: Boolean = true

  @get:Input
  var checkEpoxyModelView: Boolean = false
}