package zelaux.arclib.ui.components;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.util.*;

public class HorizontalCollapser extends WidgetGroup{
    Table table;
    @Nullable Boolp collapsedFunc;
    private CollapseAction collapseAction = new CollapseAction();
    boolean collapsed, autoAnimate;
    boolean actionRunning;
    float currentWidth;
    float seconds = 0.4f;

    public HorizontalCollapser(Cons<Table> cons, boolean collapsed){
        this(new Table(), collapsed);
        cons.get(table);
    }

    public HorizontalCollapser(Table table, boolean collapsed){
        this.table = table;
        this.collapsed = collapsed;
        setTransform(true);

        updateTouchable();
        addChild(table);
    }

    public HorizontalCollapser setDuration(float seconds){
        this.seconds = seconds;
        return this;
    }

    public HorizontalCollapser setCollapsed(Boolp collapsed){
        this.collapsedFunc = collapsed;
        return this;
    }

    public HorizontalCollapser setCollapsed(boolean autoAnimate, Boolp collapsed){
        this.collapsedFunc = collapsed;
        this.autoAnimate = autoAnimate;
        return this;
    }

    public void toggle(){
        setCollapsed(!isCollapsed());
    }

    public void toggle(boolean animated){
        setCollapsed(!isCollapsed(), animated);
    }

    public void setCollapsed(boolean collapse, boolean withAnimation){
        this.collapsed = collapse;
        updateTouchable();

        if(table == null) return;

        actionRunning = true;

        if(withAnimation){
            addAction(collapseAction);
        }else{
            if(collapse){
                currentWidth = 0;
                collapsed = true;
            }else{
                currentWidth = table.getPrefWidth();
                collapsed = false;
            }

            actionRunning = false;
            invalidateHierarchy();
        }
    }

    public void setCollapsed(boolean collapse){
        setCollapsed(collapse, true);
    }

    public boolean isCollapsed(){
        return collapsed;
    }

    private void updateTouchable(){
        this.touchable = collapsed ? Touchable.disabled : Touchable.enabled;
    }

    @Override
    public void draw(){
        if(currentWidth > 1){
            Draw.flush();
            if(clipBegin(x, y, currentWidth, getHeight())){
                super.draw();
                Draw.flush();
                clipEnd();
            }
        }
    }

    @Override
    public void act(float delta){
        super.act(delta);

        if(collapsedFunc != null){
            boolean col = collapsedFunc.get();
            if(col != collapsed){
                setCollapsed(col, autoAnimate);
            }
        }
    }

    @Override
    public void layout(){
        if(table == null) return;

        table.setBounds(0, 0, getWidth(), getHeight());

        if(!actionRunning){
            if(collapsed)
                currentWidth = 0;
            else
                currentWidth = table.getPrefWidth();
        }
    }

    @Override
    public float getPrefHeight(){
        return table == null ? 0 : table.getPrefHeight();
    }

    @Override
    public float getPrefWidth(){
        if(table == null) return 0;

        if(!actionRunning){
            if(collapsed)
                return 0;
            else
                return table.getPrefWidth();
        }

        return currentWidth;
    }

    public void setTable(Table table){
        this.table = table;
        clearChildren();
        addChild(table);
    }

    @Override
    public float getMinWidth(){
        return 0;
    }

    @Override
    public float getMinHeight(){
        return 0;
    }

    @Override
    protected void childrenChanged(){
        super.childrenChanged();
        if(getChildren().size > 1) throw new ArcRuntimeException("Only one actor can be added to CollapsibleWidget");
    }

    private class CollapseAction extends Action{
        CollapseAction(){
        }

        @Override
        public boolean act(float delta){
            if(collapsed){
                currentWidth -= delta * table.getPrefWidth() / seconds;
                if(currentWidth <= 0){
                    currentWidth = 0;
                    actionRunning = false;
                }
            }else{
                currentWidth += delta * table.getPrefWidth() / seconds;
                if(currentWidth > table.getPrefWidth()){
                    currentWidth = table.getPrefWidth();
                    actionRunning = false;
                }
            }

            invalidateHierarchy();
            return !actionRunning;
        }
    }
}