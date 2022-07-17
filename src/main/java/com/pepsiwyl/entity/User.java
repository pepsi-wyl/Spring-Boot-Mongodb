package com.pepsiwyl.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author by pepsi-wyl
 * @date 2022-07-16 10:01
 */



// lombok注解
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
// lombok注解  建造者模式
@Builder

// 类的实例代表一条文档 并且插入 wyl 集合中
@Document(collection  = "user")
public class User {

    // 将 id 映射为 文档_id
    @Id
    private Integer id;

    // 将 成员变量及值 映射为 文档中一个key、value对 并且文档中名字为 username
    @Field(name = "username")
    private String name;

    // 将 成员变量及值 映射为 文档中一个key、value对
    @Field
    private Double salary;

    // 将 成员变量及值 映射为 文档中一个key、value对
    @Field
    private Date birthDate;

    // 成员变量，不参与文档的序列化
    @Transient
    private Integer age;

}