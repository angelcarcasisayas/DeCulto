
description: SDD · Entrevista y genera la spec de una funcionalidad (uso: /sdd-spec 001-nombre idea inicial)

agent: plan

---

NO escribas código en ningún momento. Lee CONSTITUTION.md, AGENTS.md, PRODUCT_BRIEF.md y MEMORY.md, y usa la skill sdd.

Carpeta de la spec: specs/$1/

Idea inicial (el texto que va después del nombre de la carpeta): $ARGUMENTS

Tu trabajo:

1. Hazme preguntas de UNA EN UNA para eliminar ambigüedades (comportamiento esperado, casos límite, errores y qué queda fuera de esta versión). Máximo 5 preguntas.

2. Con mis respuestas, genera specs/$1/spec.md siguiendo la plantilla de la skill sdd, con los requisitos en EARS y "Estado: borrador".

3. Define solo el QUÉ y el POR QUÉ: nada de stack, arquitectura, tecnologías ni nombres de archivos.

4. No inventes requisitos ni tomes decisiones de producto por tu cuenta. Si algo no está definido y no puedes resolverlo con una pregunta, déjalo explícitamente pendiente.

5. No avances al plan ni a las tareas. Al terminar, espera mi revisión y aprobación.
