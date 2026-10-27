# Entrega · Prototipo El Azar

Prototipo navegable de **El Azar** para la práctica de Interfaces de Usuario,
Layouts, Estilos, Temas y Menús.

## Qué se entrega

| Recurso | Ruta |
|---|---|
| APK instalable (Android 7.0+) | `ElAzar/apk/ElAzar-v1.1-prototipo-debug.apk` |
| Proyecto Android Studio | `ElAzar/` (abrir esta carpeta, no la raíz) |
| Proyecto comprimido | `ElAzar-prototipo-android.zip` |
| Prototipo web adicional | `Prototipo/index.html` |
| Prototipo web comprimido | `Prototipo-web.zip` |

## Instalar el APK

```bash
adb install -r ElAzar/apk/ElAzar-v1.1-prototipo-debug.apk
```

También se puede copiar el APK al teléfono y abrirlo desde el explorador de
archivos, permitiendo la instalación desde orígenes desconocidos.

## Abrir el proyecto

1. En Android Studio: **File → Open…** → seleccionar `ElAzar/`.
2. Esperar la sincronización de Gradle (AGP 8.5.2, JDK 17, SDK 34).
3. Ejecutar con **Run ▶** en un emulador o dispositivo con API 24 o superior.

Si hace falta indicar el SDK, crear `ElAzar/local.properties` con:

```properties
sdk.dir=/ruta/a/tu/Android/Sdk
```

Desde terminal:

```bash
cd ElAzar
./gradlew assembleDebug
```

## Pantallas incluidas

1. Inicio de sesión
2. Crear cuenta
3. Recuperar contraseña
4. Dashboard con saldo, filtros, juegos y jugadores
5. Alta de medio de pago
6. Tienda
7. Dados M
8. Serpientes y Escaleras
9. Piedra, papel o tijera
10. Disparos
11. Mano de Póker *(vista adicional)*
12. Ruleta *(vista adicional)*
13. Mi perfil editable *(vista adicional)*
14. Historial de partidas *(vista adicional)*
15. Ayuda y avisos legales *(vista adicional)*

Todas las pantallas cuentan con flecha de regreso y menú de opciones. El perfil
permite editar nombre, correo, usuario y teléfono; los cambios se guardan en el
dispositivo con `SharedPreferences`. El resto de acciones (juegos, compras,
tarjetas, redes sociales) son simulaciones locales sin backend.

## Limitaciones

- No hay servidor, autenticación real, pagos ni partidas multijugador.
- El APK está firmado con la clave de depuración de Android, suficiente para
  instalar y evaluar el prototipo, no para publicarlo en una tienda.
- El objetivo es validar la interfaz y la navegación antes de implementar la
  lógica definitiva.

> El Azar es un juego responsable: solo para mayores de 18 años.
