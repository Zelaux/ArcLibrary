@file:Suppress("MemberVisibilityCanBePrivate")

package zelaux.arclib.ui.tooltips

import arc.func.*
import arc.math.geom.*
import arc.scene.*
import arc.scene.ui.*
import arc.scene.ui.layout.*
import zelaux.arclib.ui.defaults.DefaultBackground.black6
import zelaux.arclib.ui.utils.*
import kotlin.jvm.internal.Ref.*

typealias Align = Int
typealias Aligns = arc.util.Align

object SideTooltips {
    var defaultTooltipTable: Cons2<String, Table> = Cons2 { text, table ->
        table.background(black6).margin(4f).add(text)
    };

    fun <T : Element> mutableTooltip(table: Table, element: T, tooltipText: ObjectRef<String?>): Cell<T> {
        return table.add(element).tooltip { t: Table -> t.background(black6).margin(4f).label { tooltipText.element }.visible { tooltipText.element != null } }
    }

    fun <T : Element> mutableSideTooltip(table: Table, align: Int, element: T, tooltipText: ObjectRef<String?>): Cell<T> {
        return table.add(element).self { cell: Cell<T> ->
            cell.tooltipSide(align) {
                visible { tooltipText.element != null }
                background(black6).margin(4f).label { tooltipText.element }
            }
        }
    }

    @JvmOverloads
    fun create(align: Align = Aligns.topRight, text: String) = SideTooltip(align) { defaultTooltipTable.get(text, this) };
}


fun <T : Element> Cell<T>.tooltipSide(align: Align, builder: Table.() -> Unit): Cell<T> = self {
    get().addListener(SideTooltip(align, builder))
}


fun <T : Element> Cell<T>.tooltipSide(align: Align, tooltip: String) = tooltipSide(align) { SideTooltips.defaultTooltipTable.get(tooltip, this) }
class SideTooltip(val align: Align, builder: Table.() -> Unit) : Tooltip(builder) {
    val invertedAlign: Align = UIUtils.invertAlign(align)

    companion object {
        private var tmp = Vec2()
    }

    override fun setContainerPosition(element: Element, x: Float, y: Float) {
        setContainerPositionInternal(element)
    }

    fun setContainerPositionInternal(element: Element) {
        targetActor = element
        val stage = element.scene ?: return

        container.pack()
//        val offsetX = manager.offsetX
//        val offsetY = manager.offsetY
        val dist = manager.edgeDistance

        val x = UIUtils.getX(0f, element.width, align);
        val y = UIUtils.getY(0f, element.height, align);
        var point = element.localToStageCoordinates(tmp.set(x, y - container.height))
        if (point.y < dist) point = element.localToStageCoordinates(tmp.set(x, y))
        if (point.x < dist) point.x = dist
        if (point.x + container.width > stage.width - dist) point.x = stage.width - dist - container.width
        if (point.y + container.height > stage.height - dist) point.y = stage.height - dist - container.height
        container.setPosition(point.x, point.y, invertedAlign)

        point = element.localToStageCoordinates(tmp.set(element.width / 2, element.height / 2))
        point.sub(container.x, container.y)
        container.setOrigin(point.x, point.y)
    }
}
