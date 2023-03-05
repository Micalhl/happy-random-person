import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type

/**
 * happy-random-person
 * .Test
 *
 * @author mical
 * @since 2023/3/5 5:25 PM
 */
fun main() {
    Configuration.empty(type = Type.JSON).saveToString().also { println(it) }
}