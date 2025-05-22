import jsonmodel.*
import jsonmodel.validations.ArrayTypeCheckerVisitor
import jsonmodel.validations.ObjectValidatorVisitor

fun main() {
    val json = JsonObject(mutableMapOf(
            "ViagemA" to JsonArray(mutableListOf(
                JsonString("1"),
                JsonString("2"),
                JsonBoolean(true),
                JsonNull()
            )),
            "t1" to JsonString("a"),
            "t2" to JsonNumber(1)
        )
    )

    println(json.serialize())

    // Validations
    val objValidator = ObjectValidatorVisitor()
    json.accept(objValidator)
    println("Valid objects: ${objValidator.valid}")

    val typeChecker = ArrayTypeCheckerVisitor()
    json.accept(typeChecker)
    println("Arrays with only same json types: ${typeChecker.allSameType}")
}