# 🎂 CakeByte App

**CakeByte** es una plataforma móvil innovadora diseñada para transformar la interacción entre los amantes de la repostería y los reposteros locales. El objetivo principal del proyecto es facilitar el proceso de compra y venta de productos de repostería, permitiendo a los usuarios descubrir delicias locales mientras ofrece a los vendedores una herramienta robusta para gestionar su negocio.

---

## 🎯 Propósito del Proyecto
Este proyecto fue creado con el fin de digitalizar y optimizar el mercado de repostería artesanal. Proporciona una solución integral donde la seguridad, la facilidad de uso y la eficiencia se unen para ofrecer una experiencia de usuario excepcional, desde la visualización de un producto hasta la entrega final del pedido.

---

## 🚀 Características Principales
*   **Gestión Multi-rol:** Perfiles diferenciados para **Admin**, **Vendedor** y **Comprador**, cada uno con flujos y permisos específicos.
*   **Autenticación Avanzada:**
    *   Ingreso tradicional con Correo y Contraseña.
    *   **Google Sign-In:** Integración con *Credential Manager* para un acceso rápido y seguro.
    *   **Autenticación Biométrica:** Uso de huella dactilar para un re-ingreso instantáneo.
*   **Catálogo Interactivo:** Exploración de productos por categorías (Pasteles, Galletas, Bebidas, etc.).
*   **Gestión de Inventario:** Los vendedores pueden capturar fotos (CameraX) y gestionar stock, precios y descripciones en tiempo real.
*   **Proceso de Compra:** Carrito de compras funcional, resúmenes de pedido y simulación de transacciones exitosas.
*   **Seguridad de Datos:** Encriptación de credenciales locales mediante *Security Crypto*.

---

## 🛠️ Stack Tecnológico
*   **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100%) - Aprovechando las últimas funciones del lenguaje.
*   **Base de Datos y Backend:** [Supabase](https://supabase.com/)
    *   **PostgreSQL:** Almacenamiento relacional de usuarios, productos y órdenes.
    *   **Postgrest:** API RESTful generada automáticamente.
    *   **GoTrue:** Gestión de sesiones y autenticación.
*   **Arquitectura:** MVVM (Model-View-ViewModel) bajo principios de **Clean Architecture**.
*   **Inyección de Dependencias:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) (Dagger).
*   **Asincronía:** Kotlin Coroutines y Flow para un manejo de datos reactivo.
*   **UI/UX:**
    *   View Binding para una vinculación segura de vistas.
    *   Material Design 3 para componentes visuales modernos.
*   **Networking:** Ktor Client para la comunicación con Supabase.
*   **Serialización:** KotlinX Serialization.

---

## 📂 Estructura del Proyecto
El proyecto se organiza en capas para facilitar el mantenimiento y la escalabilidad:
*   **`data`**: Implementación de repositorios, DTOs y entidades de base de datos local.
*   **`domain`**: Definición de modelos de negocio, interfaces de repositorio y Casos de Uso (Use Cases).
*   **`presentation`**: Activities, ViewModels y Adapters organizados por módulos de funcionalidad (auth, buyer, vendor, admin).

---

## ⚙️ Configuración y Ejecución
1.  Clonar el repositorio.
2.  Configurar las variables de entorno de Supabase en `SupabaseClient.kt` (URL y Anon Key).
3.  Para el inicio de sesión con Google:
    *   Configurar el SHA-1 en Google Cloud Console.
    *   Asegurarse de que el Client ID Web esté registrado en el panel de Auth de Supabase.
4.  Sincronizar el proyecto con Gradle y ejecutar en un dispositivo con Android 8.0 (API 26) o superior.

---

## 📄 Licencia
Este proyecto está bajo la **Licencia MIT**. Consulta el archivo [LICENSE](LICENSE) para más detalles.

---
*Desarrollado con ❤️ para los amantes del dulce.*
