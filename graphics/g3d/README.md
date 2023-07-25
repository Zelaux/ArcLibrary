# 3Д в mindustry
## или азы 3Д через арк
да-да... это просто, доступно и не сильно затратно в плане производительности.

### Помним:
- матрицы `Mat3D`
- камеры `Camera3D`
- буферы `FrameBuffer`
- шейдеры `Shader`
- векторы `Vec3`
- меши `Mesh`
- арк `arc`

## Как начать?
И так... чтобы что-нить нарисовать в 3д нужно следующее: понимание того 
что здесь вообще происходит, умение писать на джаве и glsl (хотя бы азы),
осознание векторов и матриц.

## Начало

### Шейдеры...
Шейдер - программа запускающаяся на gpu.

Делятся на два вида:
- **вершинные** (`.vert`)
- **фрагментные** (`.frag`)

**Фрагментный** - устанавливает цвет пикселя.

**Вершинный** - задаёт координаты вершин (это точки такие... по которым фигура строится), 
а также принимает на вход аттрибуты, которые после может передавать фрагментному шейдеру
через `varing`... хотя кому я это объясняю? Но если всё ещё не в курсе, что такое шейдеры
то, интернет в помощь.

### Написание шейдеров
Тут всё просто.

Вершинный шейдер принимает атрибуты (**обязательно** позицию точки
и текстурную координату... ~~можно ещё нормаль к поверхности, но я сомневаюсь,
что кто-то так упорится, что ему понадобятся нормали~~) и юниформы (в идеале: вектор
смещения, матрицу проецирования и матрицу трансформации). В `gl_Position` этот шейдер
должен передавать позицию точки из атрибутов, сложенную с вектором смещения и умноженную
на матрицу трансформации и проецирования (**обязательно в таком порядке**), ну и ещё,
нужно передать во фрагментный шейдер (через `varying`) текстурную координату.

Фрагментный шейдер принимает через юниформы текстуру, а через `varying` текстурную
координату и пишет значение цвета из текстуры в `gl_FragColor` (в случае с арком
значение пикселя текстуры лучше получать через `texture2D(texture, textureCoord)`).

И оставлю здесь примеры... на всякий ;)

Вершинный:
```glsl
attribute vec4 a_position; // позиция точки
attribute vec2 a_texCoord0; // текстурная координата

varying vec2 v_texCoords; // текстурная координата для фрагентного шейдера

uniform mat4 u_proj; // матрица проекции
uniform mat4 u_transf; // матрица трансформации
uniform vec3 u_transl; // вектор смещения

void main(){
    // здесь я отражаю текстуру по горизонтали, тк arc грузит их перевёрнутыми
    v_texCoords = vec2(a_texCoord0.x, 1.0 - a_texCoord0.y);
    // поним про последовательность операций и про то, что равносильные операции выполняются с права на лево
    gl_Position = u_proj * u_transf * (a_position + vec4(u_transl, 0.0));
}
```
Фрагментный:
```glsl
uniform sampler2D u_texture; // текстура через юниформ

varying vec2 v_texCoords; // тектурные координты из вершинного шейдера

void main(){
    vec4 c = texture2D(u_texture, v_texCoords); // берём цвет из текстуры 
    gl_FragColor = c; // пихаем цвет из тестуры на экран
}
```

### Написание 3Д рендерера
С самой просто частью ма закончили. Начнём написание рендерера (отрисовщика).

Основной его задачей, будет создание всех условий для нормальной отрисовки 3Д графики.

Здесь нам понадобятся 3 важные переменные:
- камера (`cam`)
- кадровый буфер (`buffer`)
- шейдер для рисования буфера (`bufferShader`)

**шейдер для рисования буфера** - всегда одинаковый... рекомендую сохранить его прямо
в коде рендерера... ну как-нить так:
```java
class Renderer {
    // another code
    
    public static Shader createShader(){
        return new Shader(
                "attribute vec4 a_position;\n" +
                        "attribute vec2 a_texCoord0;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "void main(){\n" +
                        "   v_texCoords = a_texCoord0;\n" +
                        "   gl_Position = a_position;\n" +
                        "}",
                "uniform sampler2D u_texture;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "void main(){\n" +
                        "  gl_FragColor = texture2D(u_texture, v_texCoords);\n" +
                        "}"
        );
    }
}
```

**кадровый буфер** - это та штука, в которую мы будем рисовать 3д (она кстати ещё
z-буферизацию обеспечивает)

**камера** - просто камера... будем её использовать для получения матрицы проекции

Хорошо, задачи этих переменные я вам описал... Теперь то, **как** и **где** их создавать.

**Где?**:

Их надо создавать до момента первой отрисовки (лучше в функции инициализации).

**Как?**:

Тут одним предложением не обойтись :(.

**камера** - просто создаём новый экземпляр камеры и устанавливаем ей следующие
переменные: `near` - ближняя отекающая грань (0.1...1), `far` - дальняя отсекающая
грань (>=500), `fov` - угол обзора (60...100).

**шейдер для рисования буфера** - создаём новый экземпляр шейдера (например через выше
описанную функцию)

**кадровый буфер** - создаём новый экземпляр кадрового буфера.

Пример функции инициализации:
```java
class Renderer {
    // another code
    
    public void init() {
        cam = new Camera();
        cam.fov = 100;
        cam.near = 0.1f;
        cam.far = 10000;
        
        bufferShader = createShader();
        
        buffer = new FrameBuffer(2, 2, true);
    }
}
```

## Рендер

**Весь рендер рекомендуется проводить в рендерере** ~~(хотя думаю, это и так ясно)~~

Процедура рендера:

Подгоняем размеры камеры и буфера под размеры окна
```java 
cam.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
cam.update();
```

Настраиваем переменные `Gl`
```java
Gl.clear(Gl.depthBufferBit);
Gl.enable(Gl.depthTest);
Gl.depthMask(true);
Gl.enable(Gl.cullFace);
Gl.cullFace(Gl.back);
```

Запускаем кадровый буфер
```java
buffer.begin(Color.clear);
```

Тут можно рендерить меши
```java
// render there
```

Останавливаем кадровый буфер
```java
buffer.end();
```

Возвращаем переменные `Gl` на исходную
```java
Gl.disable(Gl.cullFace);
Gl.disable(Gl.depthTest);
Gl.depthMask(false);
```

Рисуем содержимое кадрового буфера на весь экран

```java
Draw.blit(buffer, bufferShader);
```

## Полный код минимального рендерера:
```java

public class GenericRenderer3D {
    // нужные нам переменные
    public final Camera3D cam;
    public final FrameBuffer buffer;
    public Shader bufferShader;

    // тут всё это инициализируем
    public void init() {
        cam = new Camera();
        cam.fov = 100;
        cam.near = 0.1f;
        cam.far = 10000;

        bufferShader = createShader();

        buffer = new FrameBuffer(2, 2, true);
    }

    public void render() {
        // настраиваем камеру и кадровый буфер
        cam.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        cam.update(); // обязательно обновляем камеры, после изменения её переменных

        // готовим Gl
        Gl.clear(Gl.depthBufferBit);
        Gl.enable(Gl.depthTest);
        Gl.depthMask(true);
        Gl.enable(Gl.cullFace);
        Gl.cullFace(Gl.back);

        buffer.begin(Color.clear); // чистим буфер и начинаем запись

        // render code there
        
        buffer.end(); // прекращаем писать в буфер

        // Gl в исходную
        Gl.disable(Gl.cullFace);
        Gl.disable(Gl.depthTest);
        Gl.depthMask(false);

        // рисуем рельтаты рендера на экран
        Draw.blit(buffer, bufferShader);
    }

    // шейдер для отрисовки буфера
    public static Shader createShader(){
        return new Shader(
                "attribute vec4 a_position;\n" +
                        "attribute vec2 a_texCoord0;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "void main(){\n" +
                        "   v_texCoords = a_texCoord0;\n" +
                        "   gl_Position = a_position;\n" +
                        "}",
                "uniform sampler2D u_texture;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "void main(){\n" +
                        "  gl_FragColor = texture2D(u_texture, v_texCoords);\n" +
                        "}"
        );
    }
}
```

### Меши

Вообще это самая сложная часть, тк если вы хотите загружать свои меши,
то вам надо писать парсер, что сложно и объёмно. Поэтому покажу закоментированные
кусоки кода... 

**Создание и заполнение**:
```java
// vertices - содержимое нашего меша, то что передаётся шейдерам через атрибуты (всё сложно
// так что, либо в отдельном гайде, либо юзайте либу (ссыль потом кину))

// создание меша
mesh = new Mesh(true, vertices.length, 0, VertexAttribute.position3, VertexAttribute.texCoords);

// пихаем данные в наш меш
mesh.getVerticesBuffer().limit(vertices.length);
mesh.getVerticesBuffer().put(vertices, 0, vertices.length);
```

**Отрисовка**:
```java
// биндим (указываем программе, что будем использовать)
texture.bind(); // текстуру (её грузить надо... думаю тут сами разберётесь))
shader.bind(); // и шейдер (это тот шейдер, что мы писали в начале) (опять таки, грузим сами)
        
shader.apply(); // вызываем метод apply
// устанавливаем юниформы шейдеру
shader.setUniformMatrix4("u_proj", projection.val); // матрицу проекции (берём её из камеры рендерера)
shader.setUniformMatrix4("u_transf", transformation.val); // матрицу трансформации (лучше иметь по одной на каждый меш)
shader.setUniformf("u_transl", translation); // вектор смещения (также один на меш)

mesh.render(shader, Gl.triangles); // рендерим нам меш треугольниками
```

Вот в принципе и азы 3Д рендеринга через `arc`.
В гайде не упомянуты работы с матрицами... ну тут можно самим посмотреть методы
`setToRotation` и `setToScaling`.
**Возможно**, скоро будет ещё один гайд на 3Д, но уже по библиотеке, упрощающей этот процесс.