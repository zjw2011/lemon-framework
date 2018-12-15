package org.lemonframework.cache;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.Test;

/**
 * 测试ObjectMapper.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class TestObjectMapper {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "class")
    @JsonFilter("classFilter")
    public static class ClassMixIn {

    }

    public static class Person {
        private String name;

        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    @Test
    public void testFilter() throws JsonProcessingException {
        Person person = new Person();
        person.setName("zhangsan");
        person.setAge(30);
        ObjectMapper mapper = new ObjectMapper(null);

        mapper.addMixIn(Person.class, ClassMixIn.class);

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("classFilter", SimpleBeanPropertyFilter.filterOutAllExcept("name"));
        mapper.setFilterProvider(filterProvider);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        final String value = mapper.writeValueAsString(person);
        System.out.println(value);
    }

}
