<h1 align="center">
  <img src="./la-flamita-android.svg" alt="la-flamita-android" width="200">
  <br>
  La Flamita Android
  <br>
  <br>
</h1>

<p align="center">
  <a href="https://developer.android.com/kotlin"><img src="https://img.shields.io/badge/Built_using-Kotlin-orangered.svg?logo=kotlin" alt="kotlin-android"></a>
  <a href="https://www.espressif.com/en/products/socs/esp32"><img src="https://img.shields.io/badge/Made_for-Android-green.svg?logo=android" alt="android"></a>
</p>



> [!NOTE]
> Este es un `submodule` que forma parte del proyecto [`la-flamita`](https://github.com/InterdataUTJ/la-flamita/).

> [!IMPORTANT]
> Este proyecto solo representa el área del cliente en forma de app Android. Puedes consultar el área administrativa desde el repositorio [`la-flamita-admin`](https://github.com/InterdataUTJ/la-flamita-admin/).

> [!IMPORTANT]
> Este proyecto solo es un cliente y no interactua con base de datos. Este proyecto necesita de un servidor desplegado usando [`la-flamita-cliente`](https://github.com/InterdataUTJ/la-flamita-cliente/).

Desarrollo para app área cliente de [`Android`](https://developer.android.com/kotlin) para taquería la flamita. La solución se enfoca en desarrollar toda la interfaz del cliente para emplear las operaciones de `la-flamita-cliente` enfocada a los clientes (el área administrativa se maneja en el proyecto [`la-flamita-admin`](https://github.com/InterdataUTJ/la-flamita-admin/)).


# Configuración

Este proyecto implementa un login usando la API de google, para funcionar es necesario que crees un archivo `la_flamita.xml` en la ruta `app/src/main/res/values`, mira el siguiente ejemplo.

Además, tienes que agregar el url de la API de la-flamita-cliente

```xml
<!-- Remplaza los ... con el Web Client ID de la consola de Google Cloud y el URL de tu api de la-flamita-cliente -->
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="web_client_id">...</string>
    <string name="server_api_url">...</string>
</resources>
```