@file:Suppress("unused")

package arclibrary.ui.tooltips

import arc.scene.Element
import arc.scene.ui.layout.Cell
import arc.scene.ui.layout.Table
import arclibrary.ui.utils.UIUtils.AlignConstant


fun <T : Element> Cell<T>.tooltipSide(@AlignConstant align: Int, builder: Table.() -> Unit): Cell<T> = AdvancedTooltips.tooltipSide(this, align, builder)


fun <T : Element> Cell<T>.tooltipSide(@AlignConstant align: Int, tooltip: String) = AdvancedTooltips.tooltipSide(this, align, tooltip)
