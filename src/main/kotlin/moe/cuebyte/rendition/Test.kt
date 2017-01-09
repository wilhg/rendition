//package moe.cuebyte.rendition
//
//import moe.cuebyte.rendition.Model
//import moe.cuebyte.rendition.util.Connection
//import redis.clients.jedis.JedisPool
//import redis.clients.jedis.JedisPoolConfig
//
//
//object Red : Model("Red") {
//  init {
//    // If id is number, then you can use it as a index. However it will not be auto-generated.
//    schema["id"] = string().primaryKey().auto()
//    schema["age"] = int(18).index()
//    schema["name"] = string("nobody").index()
//    schema["gender"] = bool(false).index()
//    schema["content"] = string()
//    schema["bio"] = string("这家伙很懒，什么也没留下。")
//  }
//}
//
//fun main(args: Array<String>) {
//
//  Connection.set(JedisPool(JedisPoolConfig(), "localhost"))
//
////  Red.where.or {
////    it["name"] eq "Bill"
////    it["age"] range 1..2
////  }
//
////  Red.findBy("name", "Bill")
////  Red.findBy("age", 12)
////  Red.find(12)
//
//  Red.insert {
//    it["name"] = "a"
//    it["gender"] = true
//    it["age"] = 16
//    it["content"] = "nope"
//  }
//}