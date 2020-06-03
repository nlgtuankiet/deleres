package con.rainyseason.deleres

import com.rainyseason.deleres.removeDrawableDefaultExtensions
import org.gradle.api.Project

open class ConfigExtension {
  var path = ""
  var checkString: Boolean = true
  var checkColor: Boolean = true
  var checkStringArray: Boolean = true
  var checkBool: Boolean = true
  var checkDimen: Boolean = true
  var checkInteger: Boolean = true
  var drawableExtensions: String = removeDrawableDefaultExtensions
  var checkLayout: Boolean = true
  var checkMenu: Boolean = true
  var checkEpoxyModelView: Boolean = false
}

@Suppress("unused")
fun Project.deleres(configure: ConfigExtension.() -> Unit): Unit =
  (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("deleres", configure)