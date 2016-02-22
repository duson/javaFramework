package duson.java.persistence.mybatis.enumeration;

public interface IntEnum<E extends Enum<E>> {
    int getIntValue();
}