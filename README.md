# ArcLibrary

[![](https://github.com/Zelaux/ZelauxArcLib/workflows/Java%20CI/badge.svg)](https://github.com/Zelaux/ZelauxArcLib/actions)

Library for [Arc](https://github.com/Anuken/Arc)

Adds useful things that's extends [Arc](https://github.com/Anuken/Arc)

# Gradle dependency

```groovy
repositories{
    maven{ url 'https://raw.githubusercontent.com/Zelaux/Repo/master/repository' }
}
dependencies{
    "com.github.Zelaux.ArcLibrary:$fullModuleName:$arcLibraryVersion"
}
```

# Exported modules ():

- betterCommandHandler `betterCommandHandler`
- graphics
    - dashDraw `graphics-dashDraw`
    - draw3d `graphics-draw3d`
    - drawText `graphics-drawText`
    - extendedDraw `graphics-extendedDraw`
    - g3d `graphics-d3g`
- settings `settings`
- ui
    - components `ui-components`
    - defaults `ui-default`
    - listeners `ui-listeners`
    - tooltips `ui-tooltips`
    - tooltips-kt `ui-tooltips-kt` (kotlin dsl for `ui-tooltips`)
    - utils `ui-utils`
- [utils](utils%2FREADME.md)
    - [entries](utils%2FREADME.md#entries) `utils-entries`
    - [files](utils%2FREADME.md#files) `utils-files`
    - [pools](utils%2FREADME.md#pools) `utils-pools`
    - [refs](utils%2FREADME.md#refs) `utils-refs`