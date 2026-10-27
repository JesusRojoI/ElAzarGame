# El Azar · Prototipo navegable

Prototipo Android del proyecto **El Azar**, construido a partir del prototipado en papel
(`Vistas.pdf`) y de la identidad visual (`ElAzarDesigns.pdf`). Implementa las pantallas
de los mockups y algunas vistas adicionales para que la navegación sea completa.

## Vistas incluidas

| # | Pantalla | Descripción |
|---|----------|-------------|
| 1 | Inicio de sesión | Acceso simulado, Google/Facebook, recuperación de contraseña |
| 2 | Crear cuenta | Validación de campos, correo y contraseña |
| 3 | Dashboard | Saldo, filtros, rejilla de 6 juegos y jugadores en línea |
| 4 | Alta de medio de pago | Vista previa de tarjeta y validación |
| 5 | Tienda | Artículos, compras simuladas y promociones |
| 6 | Dados M | Lanzamiento de 1 a 3 dados y apuesta |
| 7 | Serpientes y Escaleras | Tablero de 36 casillas, serpientes y escaleras |
| 8 | Piedra, papel o tijera | Rondas, apuesta y “todo o nada” |
| 9 | Disparos | Blanco interactivo, puntaje y municiones |
| 10 | Mano de Póker | Vista adicional con reparto y evaluación de manos |
| 11 | Ruleta | Vista adicional con giro animado y apuesta a color |
| 12 | Mi perfil | Vista adicional con edición real de datos locales |
| 13 | Historial de partidas | Vista adicional con resultados de ejemplo |
| 14 | Ayuda | Preguntas frecuentes, contacto y avisos legales |
| 15 | Recuperar contraseña | Vista adicional con envío simulado |

También incluye menú lateral (NavigationView), menú de opciones, diálogos de privacidad
y juego responsable, y navegación con flecha de regreso en todas las pantallas.

## Requisitos

- Android Studio **Koala 2024.1.1** o superior (AGP 8.5.2).
- JDK 17 (Android Studio incluye uno compatible).
- Android SDK Platform 34 y Build-Tools 34.0.0.
- Un dispositivo o emulador con Android 7.0 (API 24) o superior.

## Abrir y ejecutar en Android Studio

1. Descomprime la carpeta del proyecto.
2. En Android Studio elige **File → Open…** y selecciona la carpeta **`ElAzar`**
   (la que contiene `settings.gradle`, no la carpeta padre).
3. Espera a que termine la sincronización de Gradle. Si Android Studio pregunta por el
   SDK, selecciona tu Android SDK local (API 34).
4. Conecta un dispositivo por USB (con depuración USB activada) o crea un emulador
   con API 34.
5. Pulsa **Run ▶** o usa **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
6. El APK de depuración queda en:
   `app/build/outputs/apk/debug/app-debug.apk`.

### Compilar desde terminal

```bash
cd ElAzar
./gradlew assembleDebug
```

En Windows:

```bat
cd ElAzar
gradlew.bat assembleDebug
```

Si la terminal no encuentra el SDK, crea un archivo `local.properties` en `ElAzar/` con:

```properties
sdk.dir=/ruta/a/tu/Android/Sdk
```

## Instalar el APK incluido

El APK compilado se entrega en:

```text
ElAzar/apk/ElAzar-v1.1-prototipo-debug.apk
```

Instalación por USB:

```bash
adb install -r ElAzar/apk/ElAzar-v1.1-prototipo-debug.apk
```

También puedes copiar el APK al teléfono y abrirlo desde el explorador de archivos
(permitiendo la instalación de apps de origen desconocido).

## Cómo probar el prototipo

- En el inicio de sesión escribe cualquier usuario y una contraseña de 4 caracteres o más,
  o usa los botones de Google/Facebook.
- Desde el dashboard toca el avatar, el saludo o **Mi perfil** en el menú lateral para
  editar tus datos. Los cambios se guardan en el dispositivo con `SharedPreferences`.
- Todas las pantallas tienen flecha de regreso en la barra superior.
- Los juegos y compras son simulados: actualizan el saldo local, pero no hay backend ni
  pagos reales.

## Limitaciones intencionales del prototipo

- No hay servidor, autenticación real ni partidas multijugador.
- Los datos son locales y de ejemplo.
- Los botones de Google/Facebook, recuperación de contraseña y medios de pago simulan
  el flujo; no se conectan a servicios externos.
- El objetivo es validar la interfaz y la navegación antes de implementar la lógica
  definitiva del proyecto.

## Estructura relevante

```text
ElAzar/
├── app/src/main/java/com/elazar/app/   # Actividades Kotlin
├── app/src/main/res/layout/            # Vistas XML
├── app/src/main/res/drawable*/         # Iconos e imágenes
├── app/src/main/res/values/            # Colores, estilos, temas y textos
├── app/src/main/AndroidManifest.xml
├── apk/                                # APK listo para instalar
└── gradlew / gradlew.bat               # Wrapper de Gradle
```

> Proyecto académico de la práctica de Interfaces de Usuario, Layouts, Estilos, Temas y
> Menús. El Azar es un juego responsable: solo para mayores de 18 años.
