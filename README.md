# 🛰️ App de Reportes por Zona

Aplicación que permite a los usuarios crear reportes geolocalizados sobre acontecimientos en sus zonas. Funciona como una red social donde se pueden agregar imágenes, comentar publicaciones y mantenerse informado sobre lo que sucede en tiempo real.

## 🚀 Características

- 📍 Reportes con ubicación gracias a **Mapbox**.
- 🖼️ Subida de imágenes utilizando **Cloudinary**.
- 📢 Publicaciones con título, descripción y fotos.
- 💬 Comentarios en los reportes estilo red social.
- 📧 Validación de cuenta por correo electrónico.
- 🔐 Recuperación de contraseña y códigos de verificación.
- 🧑‍💼 Gestión de usuarios y autenticación.
- 🌐 Backend basado en **MongoDB** para almacenamiento flexible y eficiente.

## 🛠️ Tecnologías

- **Backend**: Java y SpringBoot 
- **Base de datos**: MongoDB
- **Mapas**: Mapbox
- **Imágenes**: Cloudinary
- **Email**: JavaMail
- **Autenticación**: JWT (JSON Web Tokens)

## ⚙️ Configuración

Para que la aplicación funcione correctamente, es necesario crear un archivo `application.properties` en el directorio `src/main/resources/` con las siguientes configuraciones:

```properties
# Configuración de MongoDB
spring.data.mongodb.database=alert360BD
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017

# Configuración de JWT
jwt.secret=TU_SECRETO_AQUI
jwt.expiration=86400000

# Configuración de correo electrónico
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=TU_CORREO_AQUI
spring.mail.password=TU_CONTRASEÑA_AQUI
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Configuración de Cloudinary
cloudinary.cloud_name=TU_CLOUD_NAME
cloudinary.api_key=TU_API_KEY
cloudinary.api_secret=TU_API_SECRET

# Configuración de Mapbox
mapbox.access_token=TU_ACCESS_TOKEN
