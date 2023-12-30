# ArcLibrary

[![](https://github.com/Zelaux/ZelauxArcLib/workflows/Java%20CI/badge.svg)](https://github.com/Zelaux/ZelauxArcLib/actions)

Library for [Arc](https://github.com/Anuken/Arc)

Adds useful things that's extends [Arc](https://github.com/Anuken/Arc)

# Gradle dependency

```groovy
repositories{
    maven{ url  'https://raw.githubusercontent.com/Zelaux/MindustryRepo/master/repository' }//for Arc
    maven{ url 'https://raw.githubusercontent.com/Zelaux/Repo/master/repository' }//for ArcLibrary
}
static String arcLibraryModule(String name){
    //module path to full module name
    if(name.contains(':')) name = name.split(':').join("-")
    return "com.github.Zelaux.ArcLibrary:$name:$arcLibraryVersion"
}
//use this if you do not need Arc in result jar
dependencies{
    compileOnly "com.github.Anuken.Arc:arc-core:$arcVersion"
    implementation arcLibraryModule("$fullModuleName")
}
//use this if you do need Arc in result jar
dependencies{
    implementation "com.github.Anuken.Arc:arc-core:$arcVersion"
    implementation arcLibraryModule("$fullModuleName")
}
```

# Exported modules:

- betterCommandHandler `betterCommandHandler`
- [graphics](graphics%2FREADME.md)
    - [dashDraw](graphics%2FREADME.md#dashDraw) `graphics-dashDraw`
    - [draw3d](graphics%2FREADME.md#draw3d) `graphics-draw3d`
    - [drawText](graphics%2FREADME.md#drawText) `graphics-drawText`
    - [extendedDraw](graphics%2FREADME.md#extendedDraw) `graphics-extendedDraw`
    - [g3d](graphics%2Fg3d%2FREADME.md) `graphics-g3d`
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
    - [events](utils%2FREADME.md#events) `utils-events`
    - [files](utils%2FREADME.md#files) `utils-files`
    - [io](utils%2FREADME.md#io) `utils-io`
    - [pools](utils%2FREADME.md#pools) `utils-pools`
    - [refs](utils%2FREADME.md#refs) `utils-refs`