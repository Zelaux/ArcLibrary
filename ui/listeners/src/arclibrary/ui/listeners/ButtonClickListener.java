package arclibrary.ui.listeners;

import arc.scene.event.*;
import arc.scene.ui.*;

public class ButtonClickListener extends ClickListener{
    public Button buttonObject;

    public ButtonClickListener(Button buttonObject){
        this.buttonObject = buttonObject;
    }

    @Override
    public void clicked(InputEvent event, float x, float y){
        if(buttonObject.isDisabled()) return;
        buttonObject.setProgrammaticChangeEvents(true);
        buttonObject.setChecked(!buttonObject.isChecked());
        buttonObject.setProgrammaticChangeEvents(false);
    }
}
