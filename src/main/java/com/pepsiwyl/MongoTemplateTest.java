package com.pepsiwyl;

import com.mongodb.client.result.UpdateResult;
import com.pepsiwyl.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author by pepsi-wyl
 * @date 2022-07-16 9:12
 */

@Slf4j
@Component("MongoTemplateTest")
public class MongoTemplateTest {
    public static MongoTemplate mongoTemplate;

    @Autowired
    public MongoTemplateTest(MongoTemplate mongoTemplate) {
        MongoTemplateTest.mongoTemplate = mongoTemplate;
    }

    // 创建集合
    public static void createCollection() {
        // 判断集合是否存在
        boolean exists = mongoTemplate.collectionExists("user");
        if (!exists)
            // 创建集合
            mongoTemplate.createCollection("user");
        else log.info("集合已经存在!");
    }

    // 删除集合
    public static void dropCollection() {
        // 判断集合是否存在
        boolean exists = mongoTemplate.collectionExists("user");
        if (exists)
            // 删除集合
            mongoTemplate.dropCollection("user");
        else log.info("集合不存在!");
    }

    // 插入文档
    public static void insertDocument() {

        /*
        单条插入
        insert ID重复的时候 抛异常DuplicateKeyException
        save   ID重复的时候 对该数据进行更新
        */
        // 构建User对象
        User user = User.builder().id(1).name("pepsi-wyl").salary(9000.99).age(21).birthDate(new Date()).build();
        mongoTemplate.insert(user);
        mongoTemplate.save(user);

        /*
        批处理
        insert可以一次性插入整个数据，效率较高
        save需遍历整个数据，一次插入或更新，效率较低
        */
        // 构建User集合对象
        List<User> us = Arrays.asList(
                User.builder().id(2).name("pepsi-wyl_2").salary(9000.99).age(21).birthDate(new Date()).build(),
                User.builder().id(3).name("pepsi-wyl_3").salary(9000.99).age(21).birthDate(new Date()).build()
        );
        // 直接批量添加 <插入的数据集合,集合类型>
        mongoTemplate.insert(us, User.class);
        // 遍历批量添加
//        us.forEach(u -> mongoTemplate.insert(u));
//        us.forEach(u -> mongoTemplate.save(u));
    }

    // 查询文档
    public static void queryDocument() {

        // 根据ID查询
        mongoTemplate.findById(1, User.class);

        // 查询所有
        mongoTemplate.findAll(User.class);
        mongoTemplate.find(new Query(), User.class);

        // 等值查询
        mongoTemplate.find(
                Query.query(Criteria.where("username").is("pepsi-wyl_1")),
                User.class
        );

        // < lt <= lte> gt >= gte
        mongoTemplate.find(
                Query.query(Criteria.where("salary").lt(10000)),
                User.class
        );
        mongoTemplate.find(
                Query.query(Criteria.where("salary").lte(10000)),
                User.class
        );
        mongoTemplate.find(
                Query.query(Criteria.where("salary").gt(10000)),
                User.class
        );
        mongoTemplate.find(
                Query.query(Criteria.where("salary").gte(10000)),
                User.class
        );

        // and
        mongoTemplate.find(
                Query.query(
                        Criteria.where("username").is("pepsi-wyl_1")
                                .and("salary").is(9000.99)
                ),
                User.class
        );

        // or
        mongoTemplate.find(
                Query.query(new Criteria().orOperator(
                        Criteria.where("username").is("pepsi-wyl_1"),
                        Criteria.where("username").is("pepsi-wyl_2")
                )),
                User.class
        );

        // and or
        mongoTemplate.find(
                Query.query(
                        Criteria.where("salary").is(9000.99).
                                orOperator(
                                        Criteria.where("username").is("pepsi-wyl_1"),
                                        Criteria.where("username").is("pepsi-wyl_2")
                                )
                ),
                User.class
        );

        // 排序
        mongoTemplate.find(
                new Query().with(
                        Sort.by(Sort.Order.desc("salary"))
                ),
                User.class
        );

        // 分页
        mongoTemplate.find(
                new Query()
                        .with(Sort.by(Sort.Order.desc("salary")))  //desc 降序  asc 升序
                        .skip(0).limit(2),
                User.class
        );

        // 模糊查询
        mongoTemplate.find(
                Query.query(Criteria.where("username").regex("pepsi-wyl")),
                User.class
        );

        // 总条数
        mongoTemplate.count(new Query(), User.class);
        mongoTemplate.count(Query.query(Criteria.where("salary").gt(1000)), User.class);

        // 去重 1:查询条件 参数 2: 去重字段  参数 3: 操作集合  参数 4: 返回类型
        mongoTemplate.findDistinct(new Query(), "name", User.class, String.class);

        // Json字符串查询
        List<User> users = mongoTemplate.find(
                new BasicQuery(
                        "{$or:[{name: 'pepsi-wyl'},{salary:900.99 }]}",
                        "{name: 1,salary: 1}"
                ),
                User.class
        );
    }

    // 更新文档
    public static void updateDocument() {

        // 单条更新 更新第一条
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("username").is("pepsi-wyl_1")),
                Update.update("salary", 10000),
                User.class
        );

        // 多条更新 全部更新
        mongoTemplate.updateMulti(
                Query.query(Criteria.where("username").regex("pepsi-wyl")),
                Update.update("salary", 10000),
                User.class
        );

        // 插入更新  不存在则插入
        UpdateResult upsert = mongoTemplate.upsert(
                Query.query(Criteria.where("username").is("pepsi-wyl")),
                new Update().set("name", "pepsi-wyl").set("salary", 10000).set("birthDate", new Date()).set("_class", "com.pepsiwyl.entity.User"),
                User.class
        );

        // 返回值均为 updateResult
        log.info("匹配条数:" + String.valueOf(upsert.getMatchedCount()));
        log.info("修改条数:" + String.valueOf(upsert.getModifiedCount()));
        log.info("插入id_:" + String.valueOf(upsert.getUpsertedId()));

    }

    // 删除文档
    public static void deleteDocument() {
        // 删除所有
//        mongoTemplate.remove(new Query(), User.class);
        // 条件删除
        mongoTemplate.remove(Query.query(
                Criteria.where("name").is("pepsi-wyl")
        ), User.class);
    }


}
