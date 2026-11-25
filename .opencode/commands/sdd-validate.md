
---
description: SDD · Valida la spec RF por RF (tests + Chrome DevTools)
agent: build
---

Lee specs/$1/spec.md, specs/$1/plan.md, specs/$1/tasks.md, CONSTITUTION.md, AGENTS.md y PRODUCT_BRIEF.md. Usa la skill sdd.

Revisa los cambios realizados mediante `git diff`.

Recorre la spec requisito funcional (RF) por requisito funcional.

Para cada RF:

1. Identifica sus criterios de aceptación (CA).
2. Comprueba que cada CA tiene al menos una prueba asociada.
3. Ejecuta las pruebas y registra sus resultados.
4. Si algún CA corresponde a la interfaz y no puede verificarse mediante tests automatizados, compruébalo con el MCP de Chrome DevTools.
5. Comprueba también la vista móvil a 375 px y la de escritorio a 1440 px cuando corresponda.
6. Indica los CA cubiertos, fallidos y sin verificar.

Después:

- Ejecuta `mvnw.cmd verify` y muestra el resultado.
- Comprueba los criterios de aceptación y finalización de la spec.
- Detecta requisitos sin implementar, sin tests o con resultados fallidos.

Presenta una tabla con cada RF, sus CA, las pruebas asociadas y el resultado de validación.

Dame un veredicto final:

- CUMPLIDA: todos los RF y CA están satisfechos y verificados.
- NO CUMPLIDA: hay requisitos pendientes, fallidos o sin verificar.

Si algún requisito o criterio no puede verificarse, indícalo expresamente. No lo des por cumplido.

NO corrijas errores, modifiques código ni marques tareas como completadas. Solo valida e informa.
