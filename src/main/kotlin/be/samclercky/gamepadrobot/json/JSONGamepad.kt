package be.samclercky.gamepadrobot.json

import be.samclercky.gamepadrobot.listOptions
import be.samclercky.gamepadrobot.robot.Key
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.parseValue
import java.io.File


/**
 * Handels the json
 */
class JSONGamepad(val file: File) {
    /**
     * Contains all the mapped buttons and there actions
     */
    val buttons: HashMap<String, Key> = hashMapOf()
    /**
     * The buttons that have to be mapped
     */
    val unMappedBtn: ArrayList<UnMappedBtn> = arrayListOf()
    /**
     * The speed of the mouse
     */
    var mouseSensitive = 1

    val exampleJson = """
        {
  "buttons": [
    {"key": "0", "value": "Q"},
    {"key": "0 2", "value": "D"},
    {"key": "1", "value": "Z"},
    {"key": "1 2", "value": "S"},
    {"key": "2", "value": "MOUSEMOVEX"},
    {"key": "2 2", "value": "MOUSEMOVEX"},
    {"key": "3", "value": "MOUSEMOVEY"},
    {"key": "3 2", "value": "MOUSEMOVEY"},
    {"key": "4", "value": "F6"},
    {"key": "4 2", "value": "F7"},
    {"key": "4 3", "value": "F8"},
    {"key": "4 4", "value": "F5"},
    {"key": "5", "value": "MOUSELEFT"},
    {"key": "5 2", "value": "MOUSERIGHT"},
    {"key": "5 3", "value": "SHIFT"},
    {"key": "5 4", "value": "F9"},
    {"key": "6", "value": "E"},
    {"key": "6 2", "value": "ESC"},
    {"key": "6 3", "value": "SPACE"},
    {"key": "6 4", "value": "SPACE"},
    {"key": "7", "value": "F2"},
    {"key": "7 2", "value": "F1"},
    {"key": "7 3", "value": "F4"},
    {"key": "7 4", "value": "F3"}
  ],
  "unmappedBtn" : [
    {"btn": "0", "values": [-1, 1]},
    {"btn": "1", "values": [-1, 1]},
    {"btn": "4", "values": [1, 256, 65536, 16777216]},
    {"btn": "5", "values": [1, 256, 65536, 16777216]},
    {"btn": "6", "values": [1, 256, 65536, 16777216]},
    {"btn": "7", "values": [1, 256, 65536, 16777216]}
  ],
  "mouseSentivity": 50
}"""

    init {
        try {
            if (!file.exists()) {
                println("No file found")
                listOptions()
                System.exit(2)
            }

            var jsonFile = ""

            for(line in file.bufferedReader().readLines()) {
                jsonFile += line
            }

            val json = JSONParser.default.parseValue(jsonFile) as HashMap<String, Any>

            for (section in json) {
                when (section.key) {
                    "buttons" -> fillButtons(section.value)
                    "unmappedBtn" -> fillUnmappedBtn(section.value)
                    "mouseSentivity" -> fillMouseSentivity(section.value)
                }
            }

            println(json)
        } catch (ex: Exception) {
            ex.printStackTrace()
            System.exit(2)
        }
    }

    fun  getCode(btn: String): Key {
        return buttons.get(btn) ?: Key.NOTHING
    }

    private fun fillButtons(values: Any) {
        for (value in (values as ArrayList<HashMap<String, String>>)) {
            val key = value.get("key")
            val v = value.get("value")
            if (key != null && v != null) {
                buttons.put(key, stringToKey(v))
            }
        }
    }

    private fun fillUnmappedBtn(values: Any) {
        for (value in (values as ArrayList<HashMap<String, Any>>)) {
            //println("${value.get("btn")}; ${value.get("values")}")

            val btn = value.get("btn") as String
            val v = value.get("values") as ArrayList<Int>

            if (btn != null && v != null) {
                val map = UnMappedBtn(btn, v.toTypedArray())
                unMappedBtn.add(map)
            }
        }
    }

    private fun fillMouseSentivity(values: Any) {
        mouseSensitive = values as Int
    }

    private fun stringToKey(str: String) = Key.valueOf(str)
}

data class UnMappedBtn(val btn: String, val values: Array<Int>)