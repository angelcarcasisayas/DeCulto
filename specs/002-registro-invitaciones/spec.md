# Spec 002 — Registro e invitaciones

Estado: borrador

## Contexto y objetivo

DeCulto es una aplicación privada para un pequeño grupo de cinéfilos (`PRODUCT_BRIEF.md` v0.2, sección 2). Hoy el esqueleto técnico (spec 001) arranca y sirve una página de inicio y las páginas de error, pero no existe ninguna forma de tener una cuenta ni de identificarse.

Esta spec cubre la funcionalidad F1 del MVP: que solo las personas invitadas por el fundador puedan crear una cuenta, que el fundador pueda cerrar la entrada de nuevas personas cuando lo necesite, y que los miembros puedan iniciar y cerrar sesión con protección frente a intentos repetidos de acceso.

El objetivo es que el acceso a DeCulto sea deliberadamente restringido desde el primer día, porque la aplicación contendrá colecciones privadas y el brief define el grupo como cerrado. Sin esta funcionalidad, ninguna de las funcionalidades posteriores (colección, reseñas, watchlist, compartir) puede asociarse a una persona identificada.

**Cambios explícitos sobre la spec 001.** Esta spec modifica las siguientes reglas de la spec 001. La spec 001 no se modifica; estos cambios quedan registrados aquí.

- **Cabecera (RF-23 de esta spec).** La spec 001 establece que la cabecera no contiene enlaces de navegación (RF-3 y CA-2 de la spec 001) y que la página de inicio no incluye navegación a funcionalidades que todavía no existen (sección «Fuera de alcance» de la spec 001). Esta spec sustituye ambas reglas: la cabecera varía según haya sesión iniciada. Los visitantes ven enlaces para iniciar sesión y registrarse; los miembros ven la acción para cerrar sesión; el fundador ve además el enlace a la página de administración de invitaciones.
- **Direcciones de página (RF-24 de esta spec).** La spec 001 solo admite la página de inicio y las de error, y responde 404 a cualquier otra dirección (RNF-8 y CA-13 de la spec 001). Esta spec amplía esa regla a las direcciones de registro, de inicio de sesión, de cierre de sesión y de administración de invitaciones. Cualquier otra dirección sigue respondiendo 404.

## Usuarios

- **Administrador (fundador).** Es la única cuenta con permisos de administración, y solo puede existir una (RF-38). Es la única persona que crea invitaciones y la única que puede desactivarlas. Gestiona todo ello desde la página de administración de invitaciones, a la que solo él tiene acceso. El brief lo define como «Fundador y administrador».
- **Invitado.** Persona que ha recibido un enlace de invitación del administrador y quiere crear su cuenta. No tiene cuenta todavía.
- **Miembro.** Persona con cuenta que inicia sesión y la cierra.
- **Visitante.** Cualquier persona sin sesión iniciada.

## Historias de usuario

- HU-1. Como fundador, quiero crear una invitación desde mi página de administración y obtener su enlace para compartirlo con una persona concreta del grupo.
- HU-2. Como fundador, quiero desactivar todas las invitaciones pendientes de una vez para impedir nuevos registros cuando lo considere oportuno.
- HU-3. Como invitado, quiero registrarme con mi email y una contraseña usando el enlace que me han enviado para entrar al grupo.
- HU-4. Como miembro, quiero iniciar sesión y cerrarla para acceder a mi cuenta de forma segura.
- HU-5. Como miembro, quiero que el sistema bloquee temporalmente los intentos repetidos de acceso fallidos a mi cuenta para que nadie pueda adivinar mi contraseña.
- HU-6. Como fundador, quiero poder crear la primera cuenta de administrador con un código de arranque de un solo uso, porque todavía no existe ninguna invitación que la genere.
- HU-7. Como fundador, quiero volver a consultar el enlace de una invitación pendiente mientras no haya caducado, por si necesito reenviarlo.

## Definiciones

- **Administrador:** la cuenta del fundador. Es la única cuenta con permisos de administración (RF-38); no puede haber otra.
- **Código de arranque:** código de un solo uso definido en la configuración inicial del sistema. Es válido mientras no exista ningún administrador; una vez usado para crear el fundador deja de ser válido. No afecta a las invitaciones, que siguen funcionando con normalidad.
- **Invitación:** permiso de un solo uso para crear una cuenta, identificado por un enlace de invitación. Caduca a los 7 días desde su creación si no se ha usado.
- **Enlace de invitación:** dirección que da acceso al formulario de registro de una invitación. Es una credencial no adivinable (RNF-7).
- **Invitación pendiente:** invitación que no se ha usado, no ha caducado y no ha sido anulada.
- **Invitación válida:** invitación pendiente cuyo enlace se puede usar para registrarse.
- **Interruptor global de invitaciones:** ajuste único del administrador. Al desactivarlo, anula todas las invitaciones pendientes de forma permanente e impide crear nuevas. Al reactivarlo, solo vuelve a permitir crear nuevas invitaciones; las anuladas no se restauran. Empieza activado cuando se crea el fundador (RF-37).
- **Página de administración de invitaciones:** dirección privada desde la que el fundador crea invitaciones, consulta las pendientes y activa o desactiva el interruptor global. Solo existe para el fundador; para las demás personas responde 404.
- **Email normalizado:** email sin distinguir mayúsculas y minúsculas y sin espacios al principio ni al final (RF-33).
- **Alta:** creación de una cuenta nueva con email y contraseña.
- **Sesión:** estado en el que el sistema reconoce que una persona concreta ha iniciado sesión. Caduca según RF-32.
- **Bloqueo temporal:** período de 15 minutos, aplicado a un email normalizado, tenga o no cuenta asociada, durante el que el sistema no acepta intentos de inicio de sesión con ese email tras 3 intentos fallidos acumulados. Un inicio de sesión correcto reinicia el contador de intentos fallidos de ese email.
- **Bloqueo de códigos de arranque:** período de 15 minutos durante el que el sistema no acepta ningún código de arranque tras 5 códigos incorrectos (RF-35).
- **Hash seguro:** transformación irreversible de la contraseña diseñada para dificultar su recuperación, de modo que la contraseña original no se almacena.
- **Atribución de TMDB:** mención visible en el pie de todas las páginas que indica que los datos de películas proceden de TMDB (definida en spec 001).

## Requisitos funcionales

- **RF-1.** CUANDO el fundador solicita desde la página de administración de invitaciones crear una invitación y el interruptor global está activado, EL SISTEMA crea una invitación nueva con un enlace de invitación (RNF-7), la muestra al fundador para que pueda compartirla y la registra como pendiente.
- **RF-2.** SI una persona que no es el fundador (sin sesión iniciada o con sesión de otra cuenta) intenta acceder a la página de administración de invitaciones o crear una invitación, ENTONCES EL SISTEMA responde con estado 404 idéntico al de una dirección inexistente y no crea ninguna invitación.
- **RF-3.** CUANDO el fundador desactiva el interruptor global de invitaciones, EL SISTEMA anula todas las invitaciones pendientes de forma permanente, impide cualquier alta mediante invitación y no permite crear nuevas invitaciones mientras el interruptor esté desactivado.
- **RF-4.** CUANDO el fundador activa el interruptor global de invitaciones, EL SISTEMA permite volver a crear invitaciones; las invitaciones anuladas no se restauran.
- **RF-5.** CUANDO un visitante abre un enlace de invitación válido, EL SISTEMA muestra el formulario de registro con los campos de email y contraseña, sin campo de código de arranque.
- **RF-6.** CUANDO un invitado envía el formulario de registro con datos válidos y una invitación válida, EL SISTEMA crea la cuenta, almacena la contraseña únicamente como hash seguro y marca la invitación como usada. SI dos altas simultáneas usan la misma invitación, ENTONCES solo una puede completarse.
- **RF-7.** SI el enlace de invitación no existe, ya ha sido usado, ha caducado o ha sido anulado, ENTONCES EL SISTEMA no muestra el formulario de registro y muestra un mensaje comprensible en español que indica que la invitación no es válida o ya no está disponible.
- **RF-8.** CUANDO transcurren 7 días desde la creación de una invitación que no se ha usado, EL SISTEMA la considera caducada y deja de admitir altas con ella.
- **RF-9.** SI el formulario de registro contiene datos no válidos (campos vacíos o formados solo por espacios, email con un formato no válido o contraseña de menos de 8 caracteres), ENTONCES EL SISTEMA no crea la cuenta, no consume la invitación ni el código de arranque y muestra el error junto al campo afectado.
- **RF-10.** SI el email del formulario de registro, una vez normalizado, ya pertenece a una cuenta existente, ENTONCES EL SISTEMA no crea la cuenta, no consume la invitación ni el código de arranque y muestra un mensaje que indica claramente que ya existe una cuenta con ese email, sin mostrar ningún otro dato de la cuenta.
- **RF-11.** CUANDO se completa un alta válida, EL SISTEMA crea la cuenta sin enviar ni exigir ninguna confirmación por correo electrónico.
- **RF-12.** CUANDO un visitante envía el formulario de inicio de sesión con un email y una contraseña que corresponden a una cuenta, EL SISTEMA inicia su sesión y reinicia a cero el contador de intentos fallidos de ese email.
- **RF-13.** SI el formulario de inicio de sesión contiene credenciales incorrectas, ENTONCES EL SISTEMA rechaza el inicio de sesión con un mensaje genérico que no revela si el email pertenece a una cuenta, registra el intento fallido en el contador de ese email, tenga o no cuenta asociada, y responde en un tiempo similar tanto si el email tiene cuenta como si no.
- **RF-14.** CUANDO se acumulan 3 intentos fallidos de inicio de sesión sobre un mismo email normalizado, tenga o no cuenta asociada, EL SISTEMA aplica a ese email un bloqueo temporal de 15 minutos. El bloqueo se aplica al email y no al origen de la conexión.
- **RF-15.** MIENTRAS el bloqueo temporal de un email esté vigente, EL SISTEMA rechaza cualquier intento de inicio de sesión con ese email, incluidos los que llevan credenciales correctas, e informa al visitante de que el acceso con ese email está temporalmente bloqueado y de cuándo podrá volver a intentarlo, con la misma respuesta tanto si el email tiene cuenta como si no.
- **RF-16.** CUANDO termina el bloqueo temporal, EL SISTEMA vuelve a aceptar intentos de inicio de sesión con ese email y el contador de intentos fallidos de ese email empieza desde cero.
- **RF-17.** CUANDO un miembro cierra sesión, EL SISTEMA finaliza esa sesión en el servidor, de modo que una copia de la sesión anterior deja de ser válida, y deja de reconocer a esa persona como identificada. Cerrar una sesión no afecta a otras sesiones de la misma cuenta.
- **RF-18.** EL SISTEMA almacena las contraseñas únicamente como hash seguro, nunca en texto claro, y no muestra ni devuelve la contraseña en ninguna respuesta, mensaje o registro.
- **RF-19.** EL SISTEMA solicita para crear una cuenta únicamente el email y la contraseña, y además el código de arranque en el caso de RF-21, sin recoger otros datos personales.
- **RF-20.** EL SISTEMA muestra la atribución de TMDB, la cabecera y el pie comunes en todas las páginas que se añadan con esta funcionalidad.
- **RF-21.** CUANDO no existe ningún administrador y un visitante abre la dirección de registro sin enlace de invitación, EL SISTEMA muestra el formulario con los campos de email, contraseña y código de arranque.
- **RF-22.** SI el código de arranque introducido no está configurado, ya se ha usado o no coincide con el configurado, ENTONCES EL SISTEMA no crea la cuenta y muestra un mensaje genérico que no revela el valor del código ni si existe un administrador.
- **RF-23.** MIENTRAS el visitante no tenga sesión iniciada, EL SISTEMA muestra en la cabecera enlaces para iniciar sesión y para registrarse. MIENTRAS haya una sesión de miembro iniciada, EL SISTEMA muestra en la cabecera la acción para cerrar sesión y no muestra esos enlaces. MIENTRAS haya una sesión del fundador iniciada, EL SISTEMA muestra además el enlace a la página de administración de invitaciones, y solo a él.
- **RF-24.** EL SISTEMA atiende como páginas de la aplicación las direcciones de registro (incluidas las que llevan un enlace de invitación), de inicio de sesión y de cierre de sesión. La página de administración de invitaciones se atiende solo al fundador; para cualquier otra persona, tenga o no sesión iniciada, responde con estado 404 idéntico al de una dirección inexistente, sin revelar que la página existe. Cualquier otra dirección de página distinta de la página de inicio de la spec 001, de las páginas de error y de las anteriores responde con estado 404 y la página de error propia de dirección inexistente.
- **RF-25.** CUANDO el fundador accede a la página de administración de invitaciones, EL SISTEMA muestra la lista de invitaciones pendientes, indicando para cada una su fecha de caducidad.
- **RF-26.** CUANDO el fundador usa en la página de administración de invitaciones la acción de activar o desactivar el interruptor global, EL SISTEMA aplica el cambio según RF-3 o RF-4 y muestra el estado actual del interruptor. MIENTRAS el interruptor esté desactivado, EL SISTEMA muestra en esa página un mensaje claro que indica que las invitaciones están desactivadas y que no se pueden crear nuevas, y deshabilita la acción de crear invitación.
- **RF-27.** EL SISTEMA muestra las invitaciones y el estado del interruptor global solo al fundador; ninguna otra cuenta ni visitante puede consultarlos, y son datos privados del fundador.
- **RF-28.** CUANDO no existe ningún administrador y un visitante envía el formulario sin enlace de invitación con el código de arranque correcto y datos válidos, EL SISTEMA crea su cuenta con permiso de administrador, marca el código de arranque como usado y no exige enlace de invitación para esa alta.
- **RF-29.** SI un alta con código de arranque falla por datos no válidos o por email ya registrado, ENTONCES EL SISTEMA no consume el código de arranque. SI dos altas simultáneas usan el mismo código de arranque, ENTONCES solo una puede completarse.
- **RF-30.** CUANDO existe un administrador y un visitante abre la dirección de registro sin enlace de invitación, EL SISTEMA no muestra el formulario y muestra un mensaje claro que indica que necesita un enlace de invitación para crear una cuenta.
- **RF-31.** MIENTRAS una invitación pendiente no haya caducado, EL SISTEMA muestra al fundador en la lista de pendientes su enlace de invitación para volver a consultarlo.
- **RF-32.** MIENTRAS haya una sesión iniciada, EL SISTEMA la cierra automáticamente tras 30 minutos sin actividad o 12 horas desde el inicio de sesión, lo que ocurra antes. SI una persona intenta una acción que requiere sesión con la sesión caducada, ENTONCES EL SISTEMA no la ejecuta, le pide volver a iniciar sesión y no confirma la existencia de ninguna página protegida.
- **RF-33.** EL SISTEMA trata los emails sin distinguir mayúsculas y minúsculas y sin espacios al principio ni al final, en el registro, en el inicio de sesión y en los contadores de intentos fallidos; así, «Ana@x.com» y «ana@x.com» son la misma cuenta.
- **RF-34.** CUANDO termina el bloqueo temporal de un email sin cuenta asociada o transcurren 24 horas desde su último fallo registrado, lo que ocurra primero, EL SISTEMA elimina su contador de intentos fallidos.
- **RF-35.** SI se introducen 5 códigos de arranque incorrectos, ENTONCES EL SISTEMA aplica un bloqueo de códigos de arranque de 15 minutos.
- **RF-36.** MIENTRAS dure el bloqueo de códigos de arranque, EL SISTEMA rechaza cualquier código, incluido el correcto, con un mensaje genérico. CUANDO termina el bloqueo, EL SISTEMA vuelve a aceptar códigos y el contador de códigos incorrectos empieza desde cero.
- **RF-37.** CUANDO se crea la cuenta del fundador según RF-28, EL SISTEMA deja el interruptor global de invitaciones activado.
- **RF-38.** EL SISTEMA mantiene como única cuenta con permisos de administración la del fundador; ni un alta con invitación ni un alta con código de arranque puede crear otra cuenta con esos permisos.

## Requisitos no funcionales

- **RNF-1.** Todos los textos visibles de las páginas de registro, inicio de sesión, cierre de sesión, administración de invitaciones y mensajes de error están en español de España, con tratamiento de tú.
- **RNF-2.** Las páginas de registro, inicio de sesión, cierre de sesión y administración de invitaciones son responsive, con enfoque mobile-first, y se leen sin desplazamiento horizontal ni solapamientos a 375 px y a 1440 px de ancho.
- **RNF-3.** Los formularios y las páginas de registro, inicio de sesión, cierre de sesión y administración de invitaciones cumplen accesibilidad básica: cada campo tiene una etiqueta visible, los errores se asocian al campo afectado, todo es operable con teclado, el foco es visible y el texto normal alcanza un contraste mínimo de 4,5:1 (WCAG 2.1 AA).
- **RNF-4.** En producción, todas las páginas nuevas de esta funcionalidad (registro, inicio de sesión, cierre de sesión y administración de invitaciones) se atienden únicamente mediante HTTPS, según el brief.
- **RNF-5.** Los datos de cuenta y de sesión de cada usuario son privados: ninguna consulta de datos de un usuario puede devolver datos de otro usuario al miembro autenticado.
- **RNF-6.** El email, la contraseña, el código de arranque y el enlace de invitación no aparecen en registros del sistema. En pantalla, solo pueden mostrarse a quien tiene derecho a verlos: el email a quien lo acaba de escribir en su formulario, y el enlace de invitación al fundador (RF-31). Ningún otro mensaje contiene estos datos.
- **RNF-7.** Los enlaces de invitación son no adivinables: cada uno se genera de forma aleatoria y no puede predecirse a partir de otros enlaces ni de los datos de la invitación.

## Casos límite

- **CL-1.** Dos personas abren el mismo enlace de invitación y envían el formulario a la vez: solo una de las altas puede completarse y la invitación queda usada una única vez; la otra recibe el mensaje de invitación no válida.
- **CL-2.** Un invitado envía el formulario con un email ya registrado: no se crea cuenta, la invitación sigue sin usar y el visitante recibe el mensaje que indica que ya existe una cuenta con ese email.
- **CL-3.** Un invitado envía datos no válidos y corrige después: la invitación sigue pendiente mientras no haya caducado ni sido anulada.
- **CL-4.** Una invitación caduca mientras el invitado tiene el formulario abierto: al enviarlo, el alta se rechaza con el mensaje de invitación no válida y no se crea cuenta.
- **CL-5.** El fundador desactiva el interruptor global y después lo vuelve a activar: las invitaciones pendientes que existían antes de la desactivación siguen anuladas; solo las invitaciones creadas después de la reactivación pueden usarse.
- **CL-6.** Un visitante que no es el fundador intenta acceder a la página de administración de invitaciones o crear una invitación introduciendo la dirección correspondiente: responde 404 idéntico al de una dirección inexistente y no se crea ninguna invitación.
- **CL-7.** Un visitante acumula 2 intentos fallidos sobre un email desde un origen, y un tercer intento fallido sobre el mismo email desde otro origen: el contador es por email, por lo que el tercer fallo aplica el bloqueo temporal.
- **CL-8.** Durante el bloqueo temporal se introducen credenciales correctas de la cuenta bloqueada: el inicio de sesión se rechaza hasta que termine el bloqueo.
- **CL-9.** Un miembro cierra sesión y después vuelve atrás en el historial del navegador: la cabecera de la página mostrada vuelve a presentar los enlaces de visitante (iniciar sesión y registrarse) y no la acción de cerrar sesión.
- **CL-10.** Un miembro cierra sesión en un dispositivo mientras tiene otra sesión abierta en otro: el cierre solo afecta a la sesión en la que se ha pulsado.
- **CL-11.** Un formulario se envía con el campo de contraseña vacío o formado solo por espacios: se trata como dato no válido.
- **CL-12.** Visita desde un móvil de 375 px y desde escritorio de 1440 px: los formularios y los mensajes de error se leen sin desplazamiento horizontal ni solapamientos.
- **CL-13.** El código de arranque ya se ha usado, no está configurado o se introduce incorrecto: no se crea ninguna cuenta y el mensaje es genérico, sin revelar el código ni si existe un administrador.
- **CL-14.** Se producen 3 intentos fallidos con un email que no tiene cuenta: se aplica el mismo bloqueo temporal, las respuestas durante el bloqueo son idénticas a las de un email con cuenta y los tiempos de respuesta son similares, de modo que no se puede deducir qué cuentas existen.
- **CL-15.** Un visitante acumula 2 intentos fallidos sobre un email, inicia sesión correctamente y, después, acumula 2 fallos más: el contador se ha reiniciado con el inicio correcto, así que no se aplica bloqueo hasta un tercer fallo posterior a ese inicio.
- **CL-16.** Un visitante solicita una dirección de página que no es de registro, inicio de sesión, cierre de sesión, inicio, error ni administración de invitaciones (por ejemplo, una dirección de una funcionalidad posterior): responde con 404 y la página de error propia.
- **CL-17.** Un miembro que no es el fundador, o un visitante sin sesión, solicita la página de administración de invitaciones: recibe el mismo 404 que una dirección inexistente, sin ningún dato de invitaciones ni indicación de que la página existe.
- **CL-18.** El fundador abre la página de administración con invitaciones pendientes que caducan en breve y otras que ya han caducado o usado: la lista muestra solo las pendientes, con su fecha de caducidad.
- **CL-19.** Dos personas envían a la vez un alta con el mismo código de arranque mientras no existe administrador: solo una cuenta se crea y el código queda usado una única vez.
- **CL-20.** Se introducen 5 códigos de arranque incorrectos: se aplica el bloqueo de códigos de 15 minutos y, durante él, el código correcto también se rechaza.
- **CL-21.** La sesión de un miembro lleva 30 minutos sin actividad, o 12 horas desde su inicio: la sesión caduca y la siguiente acción que requiere sesión pide volver a iniciar sesión.
- **CL-22.** Un visitante intenta registrarse con «  Ana@x.com  » cuando ya existe la cuenta «ana@x.com»: el sistema lo trata como email ya registrado.
- **CL-23.** Existe un administrador y un visitante abre la dirección de registro sin enlace de invitación: ve el mensaje que indica que necesita un enlace de invitación y no ve el formulario.
- **CL-24.** El fundador tiene el interruptor desactivado y abre la página de administración: ve el mensaje de invitaciones desactivadas y la acción de crear invitación aparece deshabilitada.
- **CL-25.** Un email sin cuenta acumula fallos y no vuelve a fallar durante 24 horas: su contador se elimina y el siguiente fallo empieza de nuevo desde uno.
- **CL-26.** El fundador tiene la sesión caducada y pulsa una acción de la página de administración: no se ejecuta la acción y se le pide volver a iniciar sesión.

## Fuera de alcance

- **Borrado de cuenta y de datos personales.** Requisito no funcional del brief que queda para una spec posterior, según decisión del fundador.
- **Verificación del email mediante enlace de confirmación.** No forma parte del alcance aprobado.
- **Recuperación o cambio de contraseña.** No está incluido en el alcance aprobado de F1.
- **Gestión individual de invitaciones:** revocar una invitación concreta o reenviarla, y listar las invitaciones ya usadas o caducadas. Solo existe el interruptor global.
- **Límite de número de invitaciones** por administrador o por periodo.
- **Nombre de usuario y perfil mínimo.** Pertenecen a F10.
- **Invitar a una persona a una dirección de email concreta.** El enlace se comparte manualmente por el fundador.
- **Gestión del código de arranque** (generación, rotación o consulta desde la aplicación). Se define únicamente en la configuración inicial.
- **Gestión de usuarios desde la página de administración** y consulta de colecciones o datos de otros usuarios desde esa página.
- **Notificaciones por email**, series, listas temáticas, feeds, seguidores, importaciones de Letterboxd o IMDb y navegación de colecciones ajenas: fuera del MVP según el brief.
- **Colección, puntuaciones, reseñas, watchlist, compartir y búsqueda de películas**: se especifican en sus propias specs.

## Criterios de aceptación

- **CA-1.** Con la cuenta del fundador y el interruptor activado, al pedir desde la página de administración una invitación se muestra un enlace nuevo que, al abrirse en un navegador sin sesión, lleva al formulario de registro. (RF-1, RF-5)
- **CA-2.** Una persona que no es el fundador, tenga o no sesión iniciada, que pide la página de administración o intenta crear una invitación recibe el 404 idéntico al de una dirección inexistente, y no aparece ninguna invitación nueva en el sistema. (RF-2)
- **CA-3.** Tras desactivar el interruptor, un enlace de invitación pendiente que antes era válido muestra el mensaje de invitación no válida y no muestra el formulario; no se pueden crear invitaciones nuevas. Tras reactivar el interruptor, ese mismo enlace sigue mostrando el mensaje de invitación no válida, y una invitación creada después de la reactivación sí muestra el formulario. (RF-3, RF-4, RF-7)
- **CA-4.** Un enlace de invitación creado hace 7 días o más y nunca usado muestra el mensaje de invitación no válida. Uno creado hace menos de 7 días y no usado muestra el formulario. (RF-7, RF-8)
- **CA-5.** Tras un alta válida, la misma invitación no puede volver a usarse para crear otra cuenta. (RF-6, RF-7)
- **CA-6.** Tras un alta válida, la cuenta queda creada sin que se envíe ningún correo de confirmación ni se exija confirmarlo. (RF-11)
- **CA-7.** Una contraseña de 7 caracteres, un email con formato no válido o campos vacíos impiden el alta, muestran el error junto al campo y no marcan la invitación como usada. Una contraseña de 8 caracteres o más es aceptada. (RF-9)
- **CA-8.** Un alta con un email ya registrado no crea cuenta, no consume la invitación ni el código de arranque, y muestra un mensaje que indica que ya existe una cuenta con ese email, sin mostrar otros datos de la cuenta. (RF-10, RF-29)
- **CA-9.** Tras un alta válida, la base de datos no contiene la contraseña en texto claro. Tras el registro, el inicio de sesión y la creación de una invitación, ni el email, ni la contraseña, ni el código de arranque, ni el enlace de invitación aparecen en los registros del sistema. (RF-18, RNF-6)
- **CA-10.** Con administrador existente, el formulario de registro por enlace de invitación solicita solo email y contraseña. Sin administrador, el formulario sin enlace solicita además el código de arranque. (RF-5, RF-19, RF-21)
- **CA-11.** Con credenciales correctas, el inicio de sesión funciona y el visitante queda identificado. Con credenciales incorrectas, el mensaje es el mismo tanto si el email existe como si no. (RF-12, RF-13)
- **CA-12.** Tres intentos fallidos seguidos sobre un email bloquean el inicio de sesión con ese email durante 15 minutos; durante ese tiempo, incluso con la contraseña correcta, el inicio se rechaza e informa de cuándo podrá volver a intentarlo. (RF-14, RF-15)
- **CA-13.** Pasados 15 minutos desde el bloqueo, un inicio de sesión correcto vuelve a funcionar y, tras él, son necesarios de nuevo 3 fallos para volver a bloquear. (RF-16)
- **CA-14.** Tras cerrar sesión, la sesión deja de estar identificada, no se conservan datos de sesión en el navegador que permitan reconocer al miembro, y una copia de la sesión anterior deja de servir en el servidor: al reutilizarla, la persona aparece como no identificada. (RF-17)
- **CA-15.** Las páginas nuevas muestran la atribución de TMDB, la cabecera y el pie comunes, y todos sus textos están en español de España con tratamiento de tú. (RF-20, RNF-1)
- **CA-16.** Las páginas de registro, inicio de sesión, cierre de sesión y administración de invitaciones se revisan a 375 px y 1440 px sin desplazamiento horizontal ni solapamientos, se recorren completas con teclado, muestran foco visible y alcanzan contraste de 4,5:1. (RNF-2, RNF-3)
- **CA-17.** En producción, las direcciones de registro, de inicio de sesión, de cierre de sesión y de administración de invitaciones solo responden por HTTPS. (RNF-4)
- **CA-18.** Ninguna consulta de datos de cuenta devuelve datos de otro usuario al miembro autenticado. (RNF-5)
- **CA-19.** Sin ningún administrador, el registro sin enlace con el código de arranque correcto y datos válidos crea una cuenta con permiso de administrador sin exigir enlace de invitación; después, el mismo código ya no crea ninguna cuenta. (RF-21, RF-28)
- **CA-20.** Con el código de arranque no configurado, ya usado o incorrecto, el registro no crea cuenta y el mensaje es genérico: no muestra el código ni indica si existe un administrador. (RF-22, RNF-6)
- **CA-21.** Con la sesión cerrada, la cabecera de cualquier página muestra los enlaces para iniciar sesión y registrarse y no muestra la acción de cerrar sesión. Con sesión de miembro, muestra la acción de cerrar sesión y no esos enlaces. Solo con sesión del fundador aparece además el enlace a la página de administración de invitaciones. (RF-23)
- **CA-22.** Tres fallos sobre el email de la cuenta A no impiden iniciar sesión en la cuenta B desde el mismo origen. (RF-14)
- **CA-23.** Tres fallos seguidos con un email sin cuenta aplican el mismo bloqueo de 15 minutos. Durante el bloqueo las respuestas son idénticas a las de un email con cuenta, y los tiempos de respuesta de ambos casos son similares; no es posible distinguir ambos casos por el mensaje ni por el tiempo de respuesta. (RF-13, RF-14, RF-15)
- **CA-24.** Un inicio de sesión correcto tras 2 fallos reinicia el contador: 2 fallos posteriores no bloquean el email, y el tercero sí. (RF-12, RF-14)
- **CA-25.** Las direcciones de registro, de inicio de sesión y de cierre de sesión responden con su página; la de administración de invitaciones responde con su página solo al fundador y con 404 idéntico al de una dirección inexistente a cualquier otra persona; cualquier otra dirección de página distinta de la página de inicio y de las de error responde 404 con la página de error propia. (RF-24)
- **CA-26.** Desde la página de administración, el fundador crea una invitación y ve en la lista de pendientes esa invitación con su fecha de caducidad; las invitaciones usadas o caducadas no aparecen en la lista. (RF-1, RF-25)
- **CA-27.** Desde la página de administración, el fundador desactiva el interruptor: la página muestra el estado desactivado, un mensaje claro de que no se pueden crear invitaciones y la acción de crear deshabilitada; al reactivarlo, muestra el estado activado y permite crear invitaciones de nuevo. (RF-26, RF-3, RF-4)
- **CA-28.** Un miembro o un visitante distinto del fundador no puede consultar la lista de invitaciones ni el estado del interruptor: la página de administración responde 404 y ningún dato de invitaciones aparece en ninguna otra respuesta. (RF-27, RF-2, RNF-5)
- **CA-29.** El fundador vuelve a consultar en la lista de pendientes el enlace de una invitación no caducada, y ese enlace funciona para registrarse. Tras la caducidad, la invitación deja de aparecer en la lista. Dos invitaciones distintas tienen enlaces distintos, que no pueden predecirse a partir de otros. (RF-31, RNF-7)
- **CA-30.** Una sesión sin actividad durante 30 minutos, o con 12 horas desde su inicio, caduca; la siguiente acción que requiere sesión no se ejecuta y se pide volver a iniciar sesión, sin confirmar la existencia de ninguna página protegida. (RF-32)
- **CA-31.** Un registro con «Ana@x.com» cuando existe «ana@x.com», y un inicio de sesión o un contador con mayúsculas o espacios sobrantes, se tratan como el mismo email. (RF-33)
- **CA-32.** Un email sin cuenta que termina su bloqueo, o que pasa 24 horas desde su último fallo, pierde su contador: tras ello, el siguiente fallo cuenta como primero. (RF-34)
- **CA-33.** Cinco códigos de arranque incorrectos aplican un bloqueo de códigos de 15 minutos; durante el bloqueo se rechaza también el código correcto con un mensaje genérico; al terminar, se aceptan códigos de nuevo con el contador a cero. (RF-35, RF-36)
- **CA-34.** Tras crear la cuenta del fundador, el interruptor global de invitaciones aparece activado. (RF-37)
- **CA-35.** Ninguna vía de alta, con invitación o con código de arranque, deja crear una segunda cuenta con permisos de administración. (RF-38)
- **CA-36.** Un alta con código de arranque y datos no válidos, o con un email ya registrado, no consume el código: después, un alta válida con ese código sí se completa. Dos altas simultáneas con el mismo código dan lugar a una sola cuenta. (RF-29)
- **CA-37.** Dos altas simultáneas con el mismo enlace de invitación dan lugar a una sola cuenta; la otra recibe el mensaje de invitación no válida. (RF-6, RF-7)
- **CA-38.** Con administrador existente, abrir la dirección de registro sin enlace muestra el mensaje que indica que se necesita un enlace de invitación, y no muestra el formulario. (RF-30)
- **CA-39.** Tras un intento con datos no válidos, la misma invitación sigue admitiendo el alta cuando el visitante corrige los datos y la invitación no ha caducado ni sido anulada. (RF-9, RF-6)
- **CA-40.** Una invitación que caduca mientras el formulario está abierto rechaza el envío con el mensaje de invitación no válida y no crea cuenta. (RF-8, RF-7)
- **CA-41.** Dos fallos desde un origen y un tercer fallo sobre el mismo email desde otro origen aplican el bloqueo temporal de 15 minutos. (RF-14)
- **CA-42.** Tras cerrar sesión, al volver atrás en el historial del navegador, la cabecera mostrada presenta los enlaces de visitante y no la acción de cerrar sesión. (RF-17, RF-23)
- **CA-43.** Al cerrar sesión en un dispositivo, otra sesión de la misma cuenta abierta en otro dispositivo sigue activa. (RF-17)
- **CA-44.** Un formulario con la contraseña vacía o formada solo por espacios no crea la cuenta, muestra el error junto al campo y no consume la invitación. (RF-9)

## Criterios de finalización

- [ ] Todos los RF-1 a RF-38 y RNF-1 a RNF-7 están cubiertos por al menos un criterio de aceptación.
- [ ] La spec ha sido aprobada explícitamente por el fundador.
- [ ] Recorrido completo con dos cuentas: el fundador crea una invitación, el invitado se registra, inicia y cierra sesión, y el bloqueo tras 3 fallos se comprueba.
- [ ] La segunda cuenta no puede acceder a datos privados de la primera ni a la página de administración.
- [ ] Las páginas nuevas se han revisado a 375 px y 1440 px, con teclado, foco visible y contraste de 4,5:1, con la atribución de TMDB visible.
- [ ] `MEMORY.md` refleja el estado y las decisiones de esta spec una vez aprobada.

## Riesgos aceptados

- **Un único administrador concentra la capacidad de invitar.** Es coherente con el grupo cerrado del brief, pero si el fundador no está disponible, nadie puede incorporar nuevos miembros.
- **Sin verificación de email,** cualquier persona con una invitación puede registrar cualquier dirección que controle o no; la confianza descansa en la invitación compartida manualmente.
- **Anulación permanente de invitaciones pendientes.** Al desactivar el interruptor, las invitaciones pendientes dejan de servir aunque se reactive después; el fundador tendrá que crear invitaciones nuevas.
- **Un tercero puede bloquear el inicio de sesión de una cuenta concreta** repitiendo 3 fallos sobre su email cada vez que el bloqueo termina. Es la consecuencia aceptada de bloquear por email y no por origen.
- **El registro revela si un email tiene cuenta.** Es una diferencia deliberada con el inicio de sesión, aceptada por el fundador para que el mensaje sea claro al registrarse.
- **Un tercero puede retrasar la creación del fundador** bloqueando los códigos de arranque durante 15 minutos, porque ese bloqueo es global. El riesgo se limita a ese periodo.

## Dudas abiertas

Sin entradas pendientes.
