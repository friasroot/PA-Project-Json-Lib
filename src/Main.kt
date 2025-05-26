import jsonapi.Controller
import jsonapi.JsonAPI
import jsonmodel.*
import jsonmodel.validations.ArrayTypeCheckerVisitor
import jsonmodel.validations.ObjectValidatorVisitor

fun main() {

    println("\t\t----------\t\nSample:")

    //Creates a json object
    val json = JsonObject(mutableMapOf(
            "ViagemA" to JsonArray(mutableListOf(
                JsonString("a1"),
                JsonString("a2"),
                JsonBoolean(true),
                JsonNull()
            )),
            "t1" to JsonString("a"),
            "t2" to JsonNumber(1)
        )
    )

    //prints structured json object
    println("\t" + json.serialize())

    // Validations
    val objValidator = ObjectValidatorVisitor()
    json.accept(objValidator)
    println("\tValid objects: ${objValidator.valid}")

    val typeChecker = ArrayTypeCheckerVisitor()
    json.accept(typeChecker)
    println("\tArrays have only homogeneous json types: ${typeChecker.allSameType}")
    println("\t\t----------\t\n")

    // Starts server
    val app = JsonAPI(Controller::class)
    app.start(8080)

    //Gives the option to stop the server via command line
    // Reads a single line of input
    println("## Type stop to stop the server: ")

    if (readln().lowercase() == "stop")
        app.stop()
}