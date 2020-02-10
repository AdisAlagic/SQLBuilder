# SQLBuilder

This library allows you to build SQL Statements. To start, you need to create `SQLBuilder` object.

```java
SQLBuilder sqlBuilder = new SQLBuilder();
```

To create **arguments** for statement, you need to create `ArgsBuilder`

```java
ArgsBuilder argsBuilder = new ArgsBuilder();
```

Add this in condition

```java
sqlBuilder.Select("table", argsBuilder);
```
