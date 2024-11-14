package DataHandling;

/**
 * This interface is responsible for serializing and deserializing objects to String.
 *
 * @param <T> The type of object to serialize and deserialize.
 */
public interface ISerializer<T> {
    final String SEPARATOR = ",";

    /**
     * Serializes the object to a String.
     *
     * @param object The object to serialize.
     * @return The serialized object.
     */
    String serialize(T object);

    /**
     * Deserializes the String to an object.
     *
     * @param data The serialized object.
     * @return The deserialized object.
     */
    T deserialize(String data);
}
