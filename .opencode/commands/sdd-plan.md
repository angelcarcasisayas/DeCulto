
---
description: SDD · Genera el plan técnico de una spec aprobada
agent: plan
---

Lee CONSTITUTION.md, AGENTS.md, PRODUCT_BRIEF.md y specs/$1/spec.md. Usa la skill sdd.

NO escribas código ni modifiques archivos.

Antes de empezar, comprueba que la spec está aprobada y que no tiene dudas abiertas. Si no cumple ambas condiciones, para y avísame.

Genera el contenido de specs/$1/plan.md con:

1. Archivos que se crearán o modificarán y la responsabilidad de cada uno.
2. Funciones puras de lógica cuando aporten valor, indicando sus entradas y salidas.
3. Algoritmos descritos en pseudocódigo.
4. Comportamiento y representación en la interfaz.
5. Decisiones técnicas justificadas y alternativas descartadas.
6. Estrategia de tests con las herramientas existentes en el proyecto.

Todo debe respetar la constitución y las decisiones aprobadas. Cada parte del plan debe indicar qué RF cubre.

No añadas requisitos ni cambies el alcance de la spec. Si detectas un problema que impide planificar, para y consúltamelo.

No avances a las tareas ni a la implementación.
