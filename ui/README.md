# UI

- [Components](#components)
- [Defaults](#defaults)
- [Listeners](#listeners)
- [Tooltips & tooltips-kt](#tooltips)
- [Utils](#utils)

----

## <a name="components"></a> Components

###### Full name: `ui-components`

### Adds
`ComboBox` and `HorizontalCollapser`

### Dependencies:
`ui-utils`, `ui-defaults`, `ui-tooltips`, `ui-listeners`, `utils-refs`

----
## <a name="defaults"></a> Defaults

###### Full name: `ui-defaults`

Default `Drawables` for other UI modules.
You can set your version besides default

----
## <a name="listeners"></a> Listeners

###### Full name: `ui-listeners`

### Adds 
- [`ButtonClickListener`](listeners%2Fsrc%2Farclibrary%2Fui%2Flisteners%2FButtonClickListener.java) - default implementation for Button(the button uses an abstract class)
- [`Listeners`](listeners%2Fsrc%2Farclibrary%2Fui%2Flisteners%2FListeners.java)
  - `onScreenClick` - checks the selected element and, if necessary, performs the specified action 
----

## <a name="tooltips"></a> Tooltips & tooltips-kt

###### Full name: `ui-tooltips` & `ui-tooltips-kt`

`ui-tooltips-kt` - It is `ui-tooltips` with kotlin extensions
### Adds [`AdvancedTooltips`](tooltips%2Fsrc%2Farclibrary%2Fui%2Ftooltips%2FAdvancedTooltips.java)
- Possibility to add tooltip with dynamic text, by using `arclibrary.utils.refs.Ref.ObjectRef<String>`
- New `SideTooltip` - Shows nearby element in selected direction


### Dependencies:
`ui-utils`, `ui-defaults`, `utils-refs`

----

## <a name="utils"></a> Utils

###### Full name: `ui-utils`

### Adds
- [`Fields`](utils%2Fsrc%2Farclibrary%2Fui%2Futils%2FFields.java)
  - `uniqueField`
  - `rangedNumberField`
  - `rangedFloatNumberField`
- [`Separators`](utils%2Fsrc%2Farclibrary%2Fui%2Futils%2FSeparators.java)
  - `verticalSeparator`
  - `horizontalSeparator`
- [`UIUtils`](utils%2Fsrc%2Farclibrary%2Fui%2Futils%2FUIUtils.java)
  - `wrapText`
  - `hovered`
  - `hitChild`
  - `invertAlign(int)`
  - `getX(x,widht,align)` & `getY(y,height,align)`
  - `replaceClickListener(Button,ClickListener)`
### Dependencies
`ui-defaults`, `utils-refs`

----
