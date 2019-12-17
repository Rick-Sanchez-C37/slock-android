package vsec.com.slockandroid.generalModels

import kotlinx.serialization.*
import kotlinx.serialization.json.Json

@Serializable
class Lock {
    @Transient private var uuid: String? = null
    private var secret: String? = null
    private var description: String? = null

    @Serializer(forClass = User::class)
    companion object: KSerializer<Lock> {
        override fun serialize(output: Encoder, obj: Lock) {
            val elemOutput = output.beginStructure(descriptor)
            if (obj.uuid != null) elemOutput.encodeStringElement(descriptor, 0, obj.uuid as String)
            if (obj.secret != null) elemOutput.encodeStringElement(descriptor, 1, obj.secret as String)
            if (obj.description != null) elemOutput.encodeStringElement(descriptor, 2, obj.description as String)
            elemOutput.endStructure(descriptor)
        }
    }
    fun setUuid(uuid: String){
        this.uuid = uuid
    }

    fun setSecret(secret: String){
        this.secret = secret
    }

    fun setDiscription(lastName: String){
        this.description = lastName
    }

    fun clear(){
        this.uuid = null
        this.secret = null
        this.description = null
    }

    fun toJSON(): String {
        return Json.stringify(serializer(), this)
    }
}