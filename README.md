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

# Exported modules:

- betterCommandHandler `betterCommandHandler`
- [graphics](graphics%2FREADME.md)
    - [dashDraw](graphics%2FREADME.md#dashDraw) `graphics-dashDraw`
    - [draw3d](graphics%2FREADME.md#draw3d) `graphics-draw3d`
    - [drawText](graphics%2FREADME.md#drawText) `graphics-drawText`
    - [extendedDraw](graphics%2FREADME.md#extendedDraw) `graphics-extendedDraw`
    - [g3d](graphics%2Fg3d%2FREADME.md) `graphics-d3g`
- [settings](settings%2FREADME.md) `settings`
- [ui](ui%2FREADME.md)
    - [components](ui%2FREADME.md#components) `ui-components`
    - [defaults](ui%2FREADME.md#defaults) `ui-default`
    - [listeners](ui%2FREADME.md#listeners) `ui-listeners`
    - [tooltips](ui%2FREADME.md#tooltips) `ui-tooltips`
    - [tooltips-kt](ui%2FREADME.md#tooltips) `ui-tooltips-kt` (kotlin dsl for `ui-tooltips`)
    - [utils](ui%2FREADME.md#utils) `ui-utils`
- [utils](utils%2FREADME.md)
    - [entries](utils%2FREADME.md#entries) `utils-entries`
    - [files](utils%2FREADME.md#files) `utils-files`
    - [pools](utils%2FREADME.md#pools) `utils-pools`
    - [refs](utils%2FREADME.md#refs) `utils-refs`