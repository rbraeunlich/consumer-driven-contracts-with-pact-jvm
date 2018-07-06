package de.codecentric.pactconsumer;

import java.util.Objects;
import java.util.UUID;

public class SimpleClass {

    private UUID id = null;

    private String name;

    public SimpleClass(String name){
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleClass that = (SimpleClass) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "SimpleClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
