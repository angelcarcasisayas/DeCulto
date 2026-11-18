
---
description: SDD · Nuevo requisito: primero la spec, luego el código
agent: plan
---

Lee CONSTITUTION.md, AGENTS.md, PRODUCT_BRIEF.md, MEMORY.md y los documentos de specs/$1/. Usa la skill sdd.

Nuevo requisito para specs/$1/ (el texto que va después del nombre de la carpeta): $ARGUMENTS

NO toques código ni modifiques plan.md o tasks.md.

1. Analiza el cambio solicitado y comprueba si añade un RF o modifica uno existente.
2. Actualiza la propuesta de specs/$1/spec.md:
   - Requisito en formato EARS.
   - Criterios de aceptación verificables.
   - Casos límite y escenarios de error.
   - Qué queda fuera de alcance.
3. Identifica qué partes de plan.md y tasks.md deberán revisarse como consecuencia del cambio.
4. Muéstrame el diff completo de spec.md y resume el impacto sobre plan.md y tasks.md.

No inventes requisitos ni amplíes el alcance solicitado.

Si el cambio contradice decisiones aprobadas o afecta a otras funcionalidades, indícalo antes de continuar.

Espera mi aprobación explícita antes de guardar los cambios en spec.md.
