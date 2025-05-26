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

## How to Use
### Endpoints

#### `/api/number?n=42`
Returns an integer.
```bash
curl "http://localhost:8080/api/number?n=42"
```
Example response:
```json
42
```

---

#### `/api/string?text=hello`
Returns a string.
```bash
curl "http://localhost:8080/api/string?text=hello"
```
Example response:
```json
"hello"
```

---

#### `/api/array?list=1&list=2&list=three`
Returns a list of values.
```bash
curl "http://localhost:8080/api/array?list=1&list=2&list=three"
```
Example response:
```json
[1, 2, "three"]
```

---

#### `/api/object?mapString=age=25&mapString=name=John&mapString=tags=one,two`
Returns a map from key=value strings.
```bash
curl "http://localhost:8080/api/object?mapString=age=25&mapString=name=John&mapString=tags=one,two"
```
Example response:
```json
{
  "age": 25,
  "name": "John",
  "tags": ["one", "two"]
}
```

---

#### `/api/null`
Returns null.
```bash
curl "http://localhost:8080/api/null"
```
Example response:
```json
null
```

---

#### `/api/boolean?bool=true`
Returns a boolean.
```bash
curl "http://localhost:8080/api/boolean?bool=true"
```
Example response:
```json
true
```

---

#### `/api/ints`
Returns a static list of integers.
```bash
curl "http://localhost:8080/api/ints"
```
Example response:
```json
[1, 2, 3]
```

---

#### `/api/pair`
Returns a pair of strings.
```bash
curl "http://localhost:8080/api/pair"
```
Example response:
```json
{"first":"um", "second":"dois"}
```

---

#### `/api/path/{pathvar}`
Pass a value as a path variable.
```bash
curl "http://localhost:8080/api/path/example"
```
Example response:
```text
"example!"
```

---

#### `/api/args?n=3&text=hi`
Combines parameters into a map.
```bash
curl "http://localhost:8080/api/args?n=3&text=hi"
```
Example response:
```json
{"hi": "hihihi"}
```

---

### Notes
- `@Param` fields are passed as query parameters.
- `@Path` fields are passed as URL path variables.


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