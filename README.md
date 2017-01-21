# Rendition

Rendition is an ORM for Redis, support conditional query.

>  Rendition = Redis + condition

Cause the Redis doesn't support any index, it is hard to make conditional query. However it supports data structures such as *Hash tables* and *Sorted set*. Redition make use of these two data structures to implement index.

By using **Redition**, it will be possible to query conditionally in Redis.