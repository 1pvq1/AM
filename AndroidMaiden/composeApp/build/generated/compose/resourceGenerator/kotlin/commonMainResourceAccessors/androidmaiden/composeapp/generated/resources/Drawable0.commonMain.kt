@file:OptIn(InternalResourceApi::class)

package androidmaiden.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/androidmaiden.composeapp.generated.resources/"

internal val Res.drawable.char_androidMaiden_full: DrawableResource by lazy {
      DrawableResource("drawable:char_androidMaiden_full", setOf(
        ResourceItem(setOf(), "${MD}drawable/char_androidMaiden_full.png", -1, -1),
      ))
    }

internal val Res.drawable.char_androidMaiden_half: DrawableResource by lazy {
      DrawableResource("drawable:char_androidMaiden_half", setOf(
        ResourceItem(setOf(), "${MD}drawable/char_androidMaiden_half.png", -1, -1),
      ))
    }

internal val Res.drawable.compose_multiplatform: DrawableResource by lazy {
      DrawableResource("drawable:compose_multiplatform", setOf(
        ResourceItem(setOf(), "${MD}drawable/compose-multiplatform.xml", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("char_androidMaiden_full", Res.drawable.char_androidMaiden_full)
  map.put("char_androidMaiden_half", Res.drawable.char_androidMaiden_half)
  map.put("compose_multiplatform", Res.drawable.compose_multiplatform)
}
