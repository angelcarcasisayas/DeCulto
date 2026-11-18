---
name: sdd
description: Úsala siempre que trabajes con Spec-Driven Development en DeCulto: redactar, revisar o modificar specs, planes y tareas, o implementar y validar tareas de una spec.
---

# Spec-Driven Development (SDD)

## Cuándo utilizar SDD

- Utiliza SDD completo para funcionalidades nuevas, cambios que afecten a varias partes del dominio, modificaciones de seguridad o privacidad y cambios importantes de arquitectura.
- Para cambios pequeños y localizados, utiliza `/feature`, sin crear innecesariamente una spec, un plan y una lista de tareas.
- Si durante un `/feature` descubres que el alcance o la complejidad son mayores de lo previsto, detente y propone pasar a SDD.
- Si existen dudas sobre qué flujo utilizar, explica las alternativas y espera la decisión del usuario.

## Principios de trabajo

- El usuario es quien decide el alcance, las prioridades y las decisiones de producto.
- Nunca pases a la siguiente fase sin la aprobación explícita del usuario.
- La spec manda: si algo no está en la spec aprobada, no se implementa. Si falta una decisión relevante, detente y pregunta.
- Un cambio de requisitos se refleja primero en la spec, después en el plan y las tareas, y finalmente en el código.
- No inventes requisitos ni resuelvas unilateralmente decisiones pendientes.
- Respeta siempre `CONSTITUTION.md` y las instrucciones de `AGENTS.md`.

## Flujo

Constitución → Spec → Clarificación → Plan → Tareas → Implementación → Validación → Revisión y cierre.

Cada fase requiere aprobación explícita antes de avanzar a la siguiente.

### 1. Spec

Cada funcionalidad compleja tiene su propia carpeta:

`specs/NNN-nombre/`

Contiene:
- `spec.md`
- `plan.md`
- `tasks.md`

#### Plantilla de spec (`spec.md`)

```markdown
# Spec NNN — <Nombre>

Estado: borrador | aprobada | implementada

## Contexto y objetivo

## Usuarios

## Historias de usuario
- HU-1. Como <rol>, quiero <acción> para <beneficio>.

## Definiciones
(Solo si existen términos ambiguos)

## Requisitos funcionales

## Requisitos no funcionales

## Casos límite

## Fuera de alcance

## Criterios de finalización

## Dudas abiertas
- [NECESITA ACLARACIÓN] <duda>
```

La spec describe el QUÉ y el POR QUÉ.
No debe incluir stack, arquitectura, nombres de clases ni nombres de archivos.

### 2. Clarificación

- Revisa la spec para detectar ambigüedades, contradicciones y decisiones pendientes.
- Formula preguntas concretas al usuario.
- No propongas una implementación mientras existan dudas que puedan cambiar el comportamiento esperado.
- Registra las decisiones acordadas en la spec.
- No marques la spec como aprobada hasta recibir aprobación explícita.

### 3. Requisitos en EARS (en español)

Utiliza estas estructuras cuando sean adecuadas:

- RF-x: CUANDO <evento>, EL SISTEMA <respuesta>.
- RF-x: SI <condición no deseada>, ENTONCES EL SISTEMA <respuesta>.
- RF-x: MIENTRAS <estado>, EL SISTEMA <respuesta>.
- RF-x: EL SISTEMA <comportamiento permanente>.

Cada requisito debe ser verificable.
Evita expresiones subjetivas como "rápido", "intuitivo" o "bonito" sin criterios medibles.

### 4. Plan (`plan.md`)

Incluye:

- Archivos afectados y responsabilidad de cada uno.
- Organización de la lógica de dominio y de la interfaz.
- Funciones puras cuando aporten valor, especialmente para reglas de dominio y cálculos dependientes del tiempo.
- Algoritmos en pseudocódigo cuando ayuden a entender la solución.
- Cambios de interfaz, si corresponden.
- Decisiones técnicas justificadas y alternativas descartadas.
- Estrategia de pruebas utilizando las herramientas ya disponibles en el proyecto.
- Relación entre cada parte del plan y los requisitos RF que cubre.

No añadas dependencias ni tecnologías sin aprobación del usuario.

### 5. Tareas (`tasks.md`)

```markdown
- [ ] **Tn. <Descripción>.** RF-x, RF-y
  - Hecho cuando: <comprobación verificable>.
```

- Divide el trabajo en tareas pequeñas, preferiblemente de 20–30 minutos.
- Ordénalas según sus dependencias.
- Si se necesitan más de 10 tareas, propone dividir la funcionalidad en partes y explica las ventajas e inconvenientes. El usuario decide.
- Cada tarea debe tener un resultado verificable.

### 6. Implementación

- Implementa una sola tarea cada vez.
- Cuando sea viable, escribe primero las pruebas y comprueba que fallan antes de implementar.
- Implementa únicamente lo necesario para cumplir la tarea y los requisitos aprobados.
- Ejecuta las pruebas correspondientes después de cada tarea.
- Marca una tarea como completada solo cuando se haya verificado.
- Detente al terminar cada tarea y espera aprobación antes de continuar.
- No aproveches para introducir refactors o funcionalidades ajenas al alcance.

### 7. Validación

- Comprueba que todos los requisitos de la spec están implementados.
- Ejecuta las pruebas pertinentes y `mvnw.cmd verify`.
- Comprueba que no se han introducido cambios fuera del alcance aprobado.
- Revisa especialmente privacidad, seguridad y posibles regresiones.
- Si algo falla, informa del problema y propón cómo resolverlo.
- No declares una funcionalidad terminada si quedan criterios de aceptación pendientes.

### 8. Revisión y cierre

Presenta un resumen que incluya:

- Requisitos completados y pendientes.
- Tareas realizadas.
- Pruebas ejecutadas y resultados.
- Archivos principales modificados.
- Problemas o limitaciones conocidos.
- Diferencias respecto a la spec, si las hubiera.

Actualiza `MEMORY.md` únicamente cuando haya cambios relevantes en decisiones, estado del proyecto, acuerdos o próximos pasos.

Si los requisitos cambian durante el desarrollo, vuelve a la spec y solicita aprobación antes de modificar el plan, las tareas o el código.

## Restricciones

- No instales dependencias nuevas de producción o test sin aprobación.
- No sustituyas las herramientas de pruebas existentes por otras.
- No utilices herramientas de Node.js como `node --test` para validar este proyecto Java.
- No modifiques `CONSTITUTION.md`, `AGENTS.md` ni esta skill sin autorización.
- No avances de fase ni amplíes el alcance por iniciativa propia.