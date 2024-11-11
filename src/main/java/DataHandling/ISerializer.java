package DataHandling;
/**
 * This interface is responsible for serializing and deserializing objects to String.
 * @param <T> The type of object to serialize and deserialize.
 */
public interface ISerializer<T> {
    final String SEPARATOR = ",";

    String serialize(T object);

    T deserialize(String data);
}
