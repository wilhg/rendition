# Rendition

Rendition is an ORM for Redis, support conditional query.

>  Rendition = Redis + condition

Cause the Redis doesn't support any index, it is hard to make conditional query. However it supports data structures such as *Hash tables* and *Sorted set*. Redition make use of these two data structures to implement index.

By using **Redition**, it will be possible to query conditionally in Redis.

## How to Install

## Usage

### Model

You can use DSL way to define the model. Like this:

```kotlin
object Book : Model("book", {
  it["id"] = int().primaryKey()
  it["author"] = string().index()
  it["publish"] = string().index()
  it["words"] = long().index()
  it["sales"] = long().index()
  it["introduce"] = string()
  it["date"] = string()
})
```

You can also define the model with`Map`. Like this:

```kotlin
val schema = mapOf(
  "id" to int().primaryKey(),
  "author" to string().index(),
  "publish" to string().index(),
  "words" to long().index(),
  "sales" to long().index(),
  "introduce" to string(),
  "date" to string()
)
object Book : Model("book", schema)
```



### Find

```kotlin
val book: Map<String, Any> = Book.find(0)
```

```kotlin
val books1: Map<String, Any> = Book.findBy("author", "Me")
val books2: Map<String, Any> = Book.findBy("id", 0)
val books3: Map<String, Any> = Book.range("sales", 0, 1000)
```



### Insert

```kotlin
Book.insert {
  it["id"] = 0
  it["author"] = "Me"
  it["publish"] = "R.D"
  it["words"] = 100_000
  it["sales"] = 100_100_000
}
```

```kotlin
val data = mapOf(
  "id" to 0,
  "author" to "Me",
  "publish" to "R.D",
  "words" to 100_100,
  "sales" to 100_100_000,
)
Book.insert(data)
```

```kotlin
val data0 = mapOf(
  "id" to 0,
  "author" to "Me",
  "publish" to "R.D",
  "words" to 100_100,
  "sales" to 100_100_000,
)
val data1 = mapOf(
  "id" to 1,
  "author" to "Me",
  "publish" to "R.D",
  "words" to 100_100,
  "sales" to 100_100_000,
)
Book.batchInsert(listOf(data0, data1))
```

### Update

```kotlin
Book.find(0).update {
  it["author"] = "Nobody"
  it["words"] = 200_000
}

Book.findBy("author", "Me").update {
  it["author"] = "Nobody"
  it["words"] = 200_000
}
```

### Delete

```kotlin
Book.find(0).delete()
Book.findBy("author", "Me").delete()
```



## License

LGPL-3.0