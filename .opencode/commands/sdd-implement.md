
---
description: SDD · Implementa UNA tarea, tests primero (uso: /sdd-implement 001-nombre T3)
agent: build
---

Implementa SOLO la tarea $2 de specs/$1/tasks.md, siguiendo specs/$1/spec.md, specs/$1/plan.md, CONSTITUTION.md, AGENTS.md y la skill sdd.

Antes de empezar:
- Comprueba que la spec, el plan y las tareas están aprobados.
- Comprueba las dependencias y que las tareas anteriores necesarias están completadas.
- Si hay dudas, bloqueos o decisiones pendientes, para y consúltame.

Proceso:

1. Escribe primero los tests de la tarea y comprueba que fallan por el motivo esperado.
2. Implementa el código necesario hasta que pasen.
3. Ejecuta `mvnw.cmd verify` y muestra el resultado.
4. Marca $2 como completada en specs/$1/tasks.md e indica los RF que cubre.
5. Informa de los archivos modificados, tests ejecutados y decisiones relevantes.

Respeta el alcance de la tarea. No añadas dependencias ni modifiques requisitos o arquitectura aprobados sin mi permiso.

Después PÁRATE. No empieces la siguiente tarea.
