import json.*
import validations.ArrayTypeCheckerVisitor
import validations.ObjectValidatorVisitor

fun main() {
    val json = JsonObject(mapOf(
        "ViagemA" to JsonArray(mutableListOf(
            JsonString("1"),
                                                    JsonString("2"),
                                                    JsonBoolean(true),
                                                    JsonNull("d")
        )),
        "t1" to JsonString("a"),
        "t2" to JsonNumber(1)
        /*"anotherObject" to JsonObject(mapOf("ob1" to JsonString("dois"), "ob2" to JsonBoolean(true)))*/)
    )

    json.print()

    // Validations
    val objValidator = ObjectValidatorVisitor()
    json.accept(objValidator)
    println("Valid objects: ${objValidator.valid}")

    val typeChecker = ArrayTypeCheckerVisitor()
    json.accept(typeChecker)
    println("Arrays with only same json types: ${typeChecker.allSameType}")
}