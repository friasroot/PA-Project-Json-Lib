# JSON Manipulation Library
## PA-Project-Json-Lib
Advanced Programming - Project 2024/2025

Authors: Henrique Niza (131898) e Rute Roque (128919)

## Features

- Define controllers using annotations (`@RestController`, `@GetMapping`)
- Automatic conversion of Kotlin values to a custom JSON model
- Serve HTTP/GET endpoints via the built-in Java HTTP server
- Minimal and testable — uses only JUnit and OkHttp for testing
- No external frameworks or dependencies

---

## Project Structure
```
src/
  jsonmodel/
    JsonLib.kt
    JsonMapper
  jsonapi/
    Controller.kt
    JsonAPI.kt
    jsonmodel.tests/
      JsonAPITest.kt
  jsonmodel.tests/
    JsonLibTest.kt
  jsonmodel.validations/
    Validations.kt
  jsonmodel.visitor/
    JsonVisitor.kt
  Main.kt
```

---

## Example Usage

### 1. Define Your Data

``` kotlin
data class Course(
    val name: String,
    val credits: Int,
    val evaluation: List<EvalItem>
)

data class EvalItem(
    val name: String,
    val percentage: Double,
    val mandatory: Boolean,
    val type: EvalType?
)

enum class EvalType {
    TEST, PROJECT, EXAM
}
```

---

## Limitations
Supports only GET endpoints for now

No support for path/query parameters (yet)

Only Kotlin-native types (Int, String, Boolean, List, Map, Enums, Data Classes, Null) are supported in JSON inference

## License
This project is educational and lightweight. Feel free to use, adapt, and extend it.

## Acknowledgements
This microframework was built from scratch as an academic alternative to Spring Boot, demonstrating:

- Manual JSON modeling 
- Kotlin reflection for type-safe inference 
- Simple annotation-based HTTP routing