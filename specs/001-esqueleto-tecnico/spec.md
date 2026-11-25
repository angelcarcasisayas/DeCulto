# Spec 001 — Esqueleto técnico

Estado: implementada

## Contexto y objetivo

DeCulto está definido en `PRODUCT_BRIEF.md` v0.2 (aprobado), pero todavía no existe ninguna base ejecutable: no hay proyecto, ni base de datos preparada, ni páginas. Antes de especificar e implementar las funcionalidades del MVP (F1–F10) hace falta un punto de partida fiable sobre el que construir.

El objetivo de esta spec es dejar la aplicación **en marcha y verificable**, sin ninguna funcionalidad de producto: que arranque, se conecte a su base de datos y deje constancia automática de su estado inicial, presente una cabecera y un pie comunes con la atribución de TMDB, muestre una página de inicio y una página de error comprensibles, y que otra persona pueda reproducir el entorno siguiendo un README con las variables necesarias.

El esqueleto existe para reducir riesgo, no para adelantar trabajo: sin él, cada funcionalidad posterior arrastraría decisiones de entorno sin resolver. Por eso esta spec se limita a lo imprescindible para empezar a implementar y deja explícitamente fuera el resto.

## Usuarios

- **Fundador y desarrollador del proyecto.** Es el usuario directo: arranca la aplicación, configura el entorno, ejecuta las comprobaciones y necesita que todo funcione sin pasos manuales ocultos.
- **Cualquier persona que retome el proyecto en otra máquina** (el propio fundador más adelante, o un colaborador). Necesita reproducir el entorno solo con la documentación.
- **Visitante de la aplicación.** Hoy solo puede ver una página de inicio y una página de error; no tiene ninguna funcionalidad de producto disponible.

## Historias de usuario

- HU-1. Como desarrollador, quiero arrancar la aplicación en local siguiendo pasos documentados para comprobar que el entorno está completo antes de escribir funcionalidades.
- HU-2. Como desarrollador, quiero que la aplicación compruebe y registre sola el estado de la base de datos al arrancar para no prepararla a mano ni depender de mi memoria.
- HU-3. Como desarrollador, quiero una cabecera y un pie comunes en todas las páginas para no repetir el nombre de la aplicación ni la atribución de TMDB en cada vista futura.
- HU-4. Como desarrollador que retoma el proyecto en otra máquina, quiero un README con requisitos, arranque, variables y comprobaciones para reproducir el entorno sin ayuda externa.
- HU-5. Como visitante, quiero una página de inicio y, si algo falla, una página de error en español que me explique qué ha pasado y me permita volver, para no encontrarme una pantalla técnica incomprensible.
- HU-6. Como responsable del proyecto, quiero que la aplicación arranque aunque la clave de TMDB no esté configurada, para no bloquear el desarrollo por una integración que todavía no existe.

## Definiciones

- **Esqueleto técnico:** base ejecutable de la aplicación sin ninguna funcionalidad de producto del MVP.
- **Tabla de la aplicación:** cualquier tabla creada por DeCulto para almacenar datos del producto (usuarios, películas, colecciones, reseñas, watchlist, recomendaciones o invitaciones). El historial de migraciones que mantiene la herramienta de migraciones **no** es una tabla de la aplicación: es la evidencia de que la migración se aplicó.
- **Migración inicial:** registro automático, al arrancar contra una base de datos sin migraciones aplicadas, de que la base queda en el estado inicial conocido por la aplicación. No crea estructura de dominio ni ninguna tabla de la aplicación.
- **Base de datos vacía:** base de datos recién creada, sin ningún objeto de la aplicación y sin historial de migraciones.
- **Entorno reproducible:** conjunto documentado de requisitos previos, pasos de arranque, variables necesarias y comprobaciones que permite poner la aplicación en marcha en otra máquina.
- **Atribución de TMDB:** mención visible que indica que los datos de películas proceden de TMDB, presente en el pie de todas las páginas.
- **Página de error propia:** respuesta que la aplicación devuelve cuando una petición falla o cuando la dirección solicitada no existe. No tiene dirección ni estado propios, y no se solicita directamente. Tiene dos variantes, en español de España, con cabecera y pie comunes, mensajes distintos y un enlace para volver al inicio: la de **dirección inexistente** (estado 404, indica que la página no existe) y la de **error interno** (estado 500, mensaje genérico).
- **Dirección de página:** dirección que un visitante solicita esperando una página de la aplicación. Quedan fuera de esta noción los recursos estáticos que la aplicación sirve para su propio funcionamiento (hojas de estilo, scripts e imágenes).

## Requisitos funcionales

- **RF-1.** CUANDO se arranca la aplicación con la configuración local documentada, EL SISTEMA queda disponible y responde a peticiones web en la dirección y el puerto indicados en el README.
- **RF-2.** CUANDO un visitante solicita la página de inicio, EL SISTEMA devuelve una página en español de España que muestra el nombre de la aplicación, una frase sobre su propósito (colección privada de películas) y la atribución de TMDB en el pie, sin listas de funcionalidades futuras y sin enlaces a direcciones que todavía no existen.
- **RF-3.** EL SISTEMA presenta en todas sus páginas la misma cabecera y el mismo pie; la cabecera muestra el nombre de la aplicación y no contiene enlaces de navegación mientras no existan funcionalidades, y la atribución de TMDB aparece en el pie de todas ellas.
- **RF-4.** CUANDO la aplicación arranca contra una base de datos vacía, EL SISTEMA aplica automáticamente la migración inicial sin intervención manual; esa migración no crea ninguna tabla de la aplicación y su aplicación queda evidenciada únicamente por el historial de migraciones que la herramienta de migraciones mantiene en la base de datos.
- **RF-5.** SI la aplicación arranca contra una base de datos que ya tiene registrada la migración inicial, ENTONCES EL SISTEMA no la vuelve a aplicar, conserva los objetos existentes y queda operativa con normalidad.
- **RF-6.** SI la aplicación no consigue conectar con la base de datos configurada, ENTONCES EL SISTEMA falla el arranque, el proceso termina sin quedar escuchando peticiones y emite en la salida del proceso un mensaje comprensible que no revela credenciales ni la cadena de conexión completa.
- **RF-7.** CUANDO una petición falla por un error interno, EL SISTEMA responde con estado 500 y la variante de error interno de la página de error propia, con un mensaje genérico en español de España («algo ha fallado» o equivalente), cabecera y pie comunes y un enlace para volver al inicio; este estado prevalece sobre el 404 cuando la petición ha fallado internamente.
- **RF-8.** SI el visitante solicita una dirección de página que no existe y la petición no ha fallado internamente, ENTONCES EL SISTEMA responde con estado 404 y la variante de dirección inexistente de la página de error propia indicando que la página no existe, con la misma cabecera y el mismo pie que el resto de páginas y un enlace para volver al inicio.
- **RF-9.** EL SISTEMA arranca y sirve la página de inicio tanto si la variable `TMDB_API_KEY` no está definida como si está definida vacía, porque es opcional en esta fase.
- **RF-10.** EL SISTEMA no muestra en las páginas de error visibles al visitante detalles técnicos internos, como trazas de error, nombres de tablas, consultas o rutas del servidor, ni en las respuestas 404 ni en las 500.
- **RF-11.** EL SISTEMA incluye un README que documenta los requisitos locales, los pasos de arranque, cómo ejecutar las comprobaciones y las variables de entorno necesarias, de forma que el entorno pueda reproducirse en otra máquina sin información externa.
- **RF-12.** EL SISTEMA incluye un fichero de ejemplo de variables de entorno bajo control de versiones que contiene todas las variables documentadas y distingue cuáles son obligatorias (URL, usuario y contraseña de la base de datos y perfil activo) y cuál es opcional (`TMDB_API_KEY`, que aparece vacía); mantiene fuera del control de versiones cualquier fichero con valores reales.

## Requisitos no funcionales

- **RNF-1.** Toda la interfaz visible está en español de España, con tratamiento de tú.
- **RNF-2.** Las páginas del esqueleto son responsive y están diseñadas con enfoque mobile-first, y se leen sin desplazamiento horizontal ni solapamientos a 375 px y a 1440 px de ancho.
- **RNF-3.** Las páginas cumplen accesibilidad básica: navegación completa por teclado, foco visible en todos los elementos interactivos, contraste mínimo de 4,5:1 para el texto normal (WCAG 2.1 AA) y textos alternativos en las imágenes.
- **RNF-4.** La atribución de TMDB es visible sin interacción adicional y aparece en el pie de todas las páginas, incluida la de error.
- **RNF-5.** Existe un conjunto de comprobaciones automáticas que, como mínimo, comprueba que la aplicación arranca contra la base de datos de pruebas, que la migración inicial queda registrada como aplicada y que la página de inicio y las dos variantes de la página de error devuelven el contenido y el estado esperados. Un conjunto de comprobaciones vacío que solo termine con éxito no cumple este requisito. No se añaden dependencias de prueba nuevas.
- **RNF-6.** La base de datos de pruebas es distinta de la de desarrollo. La de desarrollo se selecciona mediante variables de entorno; la de pruebas se fija en la configuración de las pruebas y no depende de variables de entorno ni del fichero de configuración local del usuario. Sus credenciales son de una base local de prueba, no son secretos reales y se documentan en el README; ningún valor real de configuración se versiona (RNF-7).
- **RNF-7.** No se versiona ningún secreto: los ficheros con valores reales de configuración quedan fuera del control de versiones.
- **RNF-8.** El esqueleto expone únicamente la página de inicio y la página de error propia; cualquier otra dirección de página (conforme a la definición de dirección de página, sin incluir recursos estáticos) responde con estado 404 y la variante de dirección inexistente de la página de error, sin ningún contenido funcional. Los recursos estáticos propios de la aplicación (hojas de estilo, scripts e imágenes) quedan fuera de esta regla y responden con su contenido.
- **RNF-9.** El arranque local se consigue con los pasos documentados en el README y no requiere preparar el esquema de la base de datos a mano.

## Criterios de aceptación

- **CA-1.** Partiendo de una base de datos vacía y de un fichero de configuración local creado a partir del fichero de ejemplo, con la clave de TMDB vacía, el arranque documentado deja la aplicación respondiendo a peticiones, sin haber preparado el esquema a mano, y la página de inicio se sirve con el nombre de la aplicación y la frase sobre su propósito. (RF-1, RF-2, RF-12, RNF-9)
- **CA-2.** La página de inicio y la variante de dirección inexistente (404) de la página de error propia muestran la misma cabecera y el mismo pie; la cabecera no contiene enlaces de navegación; la atribución de TMDB es legible en ambos pies; el contenido de la página de inicio no incluye listas de funcionalidades futuras ni enlaces a direcciones inexistentes. (RF-2, RF-3, RF-8, RNF-4)
- **CA-3.** Tras el primer arranque contra una base vacía no existe ninguna tabla de la aplicación y el historial de migraciones registra la migración inicial como aplicada; tras un segundo arranque sobre la misma base, no hay errores de migración y la aplicación sigue operativa. (RF-4, RF-5)
- **CA-4.** Arrancando sin `TMDB_API_KEY` definida y, por separado, con la variable definida vacía, la aplicación queda operativa en ambos casos y sirve la página de inicio sin mensajes de error visibles. (RF-9)
- **CA-5.** Con la base de datos parada o con credenciales incorrectas, el arranque falla, el proceso no queda escuchando peticiones y el mensaje emitido no contiene la contraseña ni la cadena de conexión completa. (RF-6)
- **CA-6.** Al solicitar una dirección inexistente se recibe el estado 404 con la página de error propia, en español, indicando que la página no existe, con enlace al inicio y sin trazas ni rutas internas visibles. (RF-8, RF-10)
- **CA-7.** Provocando un error interno se recibe el estado 500 con la página de error propia, un mensaje genérico distinto de «la página no existe», enlace al inicio y sin trazas ni rutas internas visibles. (RF-7, RF-10)
- **CA-8.** El README permite reproducir el entorno desde cero y enumera las variables necesarias: obligatorias (URL, usuario y contraseña de la base de datos, perfil activo) y opcional (`TMDB_API_KEY`); el fichero de ejemplo está versionado con todas ellas, con la clave de TMDB vacía, y ningún fichero con valores reales aparece en el control de versiones. (RF-11, RF-12, RNF-7)
- **CA-9.** La comprobación automática verifica el arranque de la aplicación contra la base de datos de pruebas, acredita la migración inicial y comprueba el contenido y el estado de la página de inicio y de ambas variantes de la página de error propia (dirección inexistente, 404, y error interno, 500); un conjunto de comprobaciones automáticas vacío que solo termine con éxito no se acepta como cumplimiento. (RNF-5, RF-4)
- **CA-10.** Durante la ejecución automática se usa la base de datos de pruebas fijada en la configuración de las pruebas y no la de desarrollo, aunque existan variables de entorno o un fichero de configuración local con los valores de desarrollo; al terminar, la base de desarrollo no presenta cambios derivados de esa ejecución. (RNF-6)
- **CA-11.** Las páginas se revisan a 375 px y a 1440 px sin desplazamiento horizontal ni solapamientos, se recorren por completo con teclado, todos los elementos interactivos muestran foco visible y el texto normal alcanza un contraste mínimo de 4,5:1. (RNF-2, RNF-3)
- **CA-12.** Todos los textos visibles de la página de inicio y de la página de error están en español de España y usan el tratamiento de tú. (RNF-1)
- **CA-13.** Cualquier otra dirección de página que no sea la de la página de inicio responde con estado 404 y la variante de dirección inexistente de la página de error propia, sin contenido funcional de ninguna clase; si la petición falla internamente, prevalece el estado 500 y la variante de error interno, y 404 y 500 nunca se aplican a la misma petición. Los recursos estáticos propios de la aplicación (hojas de estilo, scripts e imágenes) quedan fuera de esta regla y responden con su contenido. (RNF-8)

## Casos límite

- **CL-1.** Base de datos vacía por primera vez: la migración inicial queda registrada sin pasos manuales, no se crea ninguna tabla de la aplicación, el historial de migraciones es la única evidencia y el arranque continúa.
- **CL-2.** Base de datos ya migrada: no se reaplica la migración, no se destruyen objetos existentes y el arranque continúa.
- **CL-3.** Base de datos parada o inaccesible: el arranque falla, el proceso no queda escuchando peticiones y el mensaje no revela credenciales ni la cadena de conexión completa.
- **CL-4.** Variable `TMDB_API_KEY` ausente o definida vacía: el arranque y la página de inicio funcionan; solo la futura búsqueda se verá afectada.
- **CL-5.** Dirección inexistente o de una funcionalidad todavía no implementada: estado 404 con la página de error propia indicando que la página no existe, nunca una pantalla técnica del servidor.
- **CL-6.** Error interno no controlado: estado 500 con la página de error propia y mensaje genérico, sin trazas ni rutas internas y sin afirmar que la página no existe.
- **CL-7.** Repetición de la comprobación automática sobre una base de pruebas que conserva datos de una ejecución anterior: la comprobación sigue terminando correctamente. (RNF-5, CA-9)
- **CL-8.** Ejecución en una máquina sin fichero de configuración con valores reales: el README explica qué crear y el fichero de ejemplo contiene todas las variables necesarias, con la clave de TMDB vacía.
- **CL-9.** Visita desde un dispositivo móvil de 375 px y desde escritorio de 1440 px: cabecera, contenido y pie se leen sin solapamientos ni desplazamiento horizontal.
- **CL-10.** Fichero de variables con valores reales presente en la máquina: no aparece en el control de versiones.

## Fuera de alcance

- **Todas las funcionalidades del MVP, F1–F10** del `PRODUCT_BRIEF.md`: registro e invitaciones, búsqueda de películas, ficha, puntuación, colección personal, reseñas, watchlist, compartir películas, búsqueda en la colección y perfil. Cada una se especifica en su propia spec.
- **Autenticación y seguridad de sesión**: formulario de inicio de sesión, registro, cierre de sesión, autorización, invitaciones y protección frente a intentos repetidos. Quedan en la spec de registro e invitaciones.
- **Consumo real de TMDB**: llamadas a la API, búsqueda, caché de películas y metadatos. Esta spec solo reserva la atribución visible y la ausencia de bloqueo por clave faltante.
- **Modelo de datos del dominio**: la migración inicial no crea ninguna tabla de la aplicación, solo el historial propio de la herramienta de migraciones. El esquema de usuarios, películas, colecciones, reseñas, watchlist, recomendaciones e invitaciones nace con la spec de registro e invitaciones y las posteriores; esta spec no se adelanta a ese modelo.
- **Datos de ejemplo o contenido inicial de películas.**
- **Empaquetado Docker y despliegue** en cualquier proveedor, incluida la verificación de planes gratuitos. Se especifican en una spec propia de despliegue.
- **Integración continua, herramientas de lint, analítica y monitorización.**
- **Multiidioma**: la interfaz solo se ofrece en español de España.
- **Enlaces de navegación y contenido editorial de la página de inicio** más allá de la presentación de la aplicación y la atribución: sin textos de marketing, sin secciones informativas y sin navegación a funcionalidades que todavía no existen.

## Criterios de finalización

- [ ] Todos los requisitos funcionales (RF-1 a RF-12) y no funcionales (RNF-1 a RNF-9) están cubiertos y verificados por al menos un criterio de aceptación.
- [ ] La aplicación arranca desde cero siguiendo únicamente el README, con base de datos vacía y fichero de configuración creado a partir del ejemplo.
- [ ] El segundo arranque sobre la misma base de datos no produce errores de migración.
- [ ] El arranque sin `TMDB_API_KEY` deja la aplicación operativa.
- [ ] La comprobación automática verifica el arranque de la aplicación contra la base de datos de pruebas, acredita la migración inicial y comprueba las páginas de inicio y de error con su contenido y su estado; no se acepta un conjunto de comprobaciones automáticas vacío.
- [ ] Las respuestas 404 y 500 se han comprobado con la página de error propia, con enlace al inicio y sin detalles técnicos.
- [ ] La página de inicio y la página de error se han revisado a 375 px y 1440 px, con teclado, foco visible y contraste de 4,5:1, y con la atribución de TMDB visible.
- [ ] No se ha implementado ninguna funcionalidad de F1–F10 ni se ha creado ninguna tabla de la aplicación.
- [ ] El control de versiones no contiene secretos y sí contiene el fichero de ejemplo de variables.
- [ ] `MEMORY.md` refleja el estado alcanzado y las decisiones tomadas para esta spec.

## Riesgos aceptados

- **La verificación automática depende de una base de datos PostgreSQL arrancada.** Es una consecuencia aceptada por el usuario al descartar dependencias de prueba adicionales. Tensa el punto 4 de `docs/constitution.md` («`mvnw.cmd verify` pasa antes de cerrar»), porque la comprobación ya no es autocontenida: si PostgreSQL no está disponible, la verificación falla por el entorno y no por el código. Queda registrado para que esa expectativa conste en el README y en los pasos de la comprobación automática definida en RNF-5.
- **Sin empaquetado ni integración continua en esta spec**, la reproducibilidad depende por completo de la exactitud del README.

## Dudas abiertas

Sin entradas pendientes. Las dos dudas registradas durante la clarificación (qué crea la migración inicial y qué contiene la página de inicio) se resolvieron antes de esta versión: la migración no crea tablas de la aplicación y la página de inicio muestra el nombre y el propósito, sin funciones futuras.
