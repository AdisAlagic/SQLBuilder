import com.adisalagic.sqlbuilder.ArgsBuilder;
import com.adisalagic.sqlbuilder.Field;
import com.adisalagic.sqlbuilder.SQLBuilder;
import com.adisalagic.sqlbuilder.VariableType;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLBuilderTest extends TestCase {
	public void testCreateDataBase() {
		SQLBuilder builder = new SQLBuilder();
		assertEquals("CREATE DATABASE Fake;", builder.createDatabase("Fake").toString());
	}

	public void testSelectFields() {
		SQLBuilder builder = new SQLBuilder();
		String[]   fields  = {"f1", "f2"};
		String[]   tables  = {"t1", "t2"};
		assertEquals("SELECT f1, f2 FROM t1, t2;", builder.Select(fields, tables).toString());
	}

	public void testSelectAll() {
		SQLBuilder builder = new SQLBuilder();
		String[]   tables  = {"t1", "t2"};
		builder.Select(tables);
		assertEquals("SELECT * FROM t1, t2;", builder.toString());
	}

	public void testSelectWithAgs() {
		SQLBuilder  builder     = new SQLBuilder();
		ArgsBuilder argsBuilder = new ArgsBuilder();
		argsBuilder.addCondition("A < ?", true, "Arigato");
		builder.Select(new String[]{"t1", "t2"}, argsBuilder);
		assertEquals("SELECT * FROM t1, t2 WHERE A < 'Arigato';", builder.toString());
	}

	public void testSelectWithArgsAnd() {
		SQLBuilder  builder     = new SQLBuilder();
		ArgsBuilder argsBuilder = new ArgsBuilder();
		argsBuilder.addCondition("A < ?", true, "Arigato")
				.And()
				.addCondition("? > B", false, "Go");
		builder.Select(new String[]{"a"}, argsBuilder);
		assertEquals("SELECT * FROM a WHERE A < 'Arigato' AND Go > B;", builder.toString());
	}

//	public void testSelectBigString(){
//		com.adisalagic.sqlbuilder.SQLBuilder builder = new com.adisalagic.sqlbuilder.SQLBuilder();
//		com.adisalagic.sqlbuilder.ArgsBuilder builder1 = new com.adisalagic.sqlbuilder.ArgsBuilder();
//		builder1.addCondition("? > A AND B < ? OR ? < 4 AND B < ?", true ,"a", "b", "c", "d");
//		builder.Select(new String[]{"t1"}, builder1);
//		assertEquals("SELECT * FROM t1 WHERE a > A AND B < 'b' OR c < 4 AND B < 'd';", builder.toString());
//	}

	public void testSimpleSelect() {
		SQLBuilder builder = new SQLBuilder();
		builder.Select("t1");
		assertEquals("SELECT * FROM t1;", builder.toString());
	}

	public void testSimpleSelectFields() {
		SQLBuilder builder = new SQLBuilder();
		builder.Select(new String[]{"f1"}, "t1");
		assertEquals("SELECT f1 FROM t1;", builder.toString());
	}

	public void testSimpleSelectFieldsWithArgs() {
		SQLBuilder  builder     = new SQLBuilder();
		ArgsBuilder argsBuilder = new ArgsBuilder();
		argsBuilder.addCondition("f1 < ?", true, "1");
		builder.Select(new String[]{"f1"}, "t1", argsBuilder);
		assertEquals("SELECT f1 FROM t1 WHERE f1 < '1';", builder.toString());
	}

	public void testSQLInjection() {
		SQLBuilder  builder     = new SQLBuilder();
		ArgsBuilder argsBuilder = new ArgsBuilder();
		argsBuilder.addCondition("f1 = ?", true, "a'");
		builder.Select("t1", argsBuilder);
		assertEquals("SELECT * FROM t1 WHERE f1 = 'a\\'';", builder.toString());
	}

	public void testCreateDatabase() {
		SQLBuilder builder = new SQLBuilder();
		builder.createDatabase("F");
		assertEquals("CREATE DATABASE F;", builder.toString());
	}

	public void testCreateTable() {
		SQLBuilder  builder = new SQLBuilder();
		List<Field> fields  = new ArrayList<>();
		fields.add(new Field("a", VariableType.TEXT, true, -1, false, false, null));
		fields.add(new Field("b", VariableType.TEXT));
		builder.CreateTable("A", fields);
		assertEquals("CREATE TABLE A ( `a` text , PRIMARY KEY (`a`), `b` text  );", builder.toString());
	}

	public void testDelete(){
		SQLBuilder builder = new SQLBuilder();
		ArgsBuilder argsBuilder = new ArgsBuilder();
		argsBuilder.addCondition("A IS ?", true, 4);
		builder.Delete("t1", argsBuilder);
		assertEquals("DELETE FROM t1 WHERE A = '4';", builder.toString());
	}

	public void testDeleteTables(){
		SQLBuilder builder = new SQLBuilder();
		ArgsBuilder argsBuilder = new ArgsBuilder();
		argsBuilder.addCondition("A IS ?", false, 1);
		builder.Delete(new String[]{"t1, t2"}, argsBuilder);
		assertEquals("DELETE FROM t1, t2 WHERE A = '1';", builder.toString());
	}

	public void testUpdate(){
		SQLBuilder builder = new SQLBuilder();
		ArgsBuilder argsBuilder = new ArgsBuilder();
		argsBuilder.addCondition("A IS ?", true, "firefly");
		HashMap<String, Object> fields = new HashMap<>();
		fields.put("A'", "1");
		builder.Update("t1", fields, argsBuilder);
		assertEquals("UPDATE t1 SET A\\' = '1' WHERE A = 'firefly';", builder.toString());
	}

	public void testInnerJoin(){
		SQLBuilder builder = new SQLBuilder();
		ArgsBuilder argsBuilder = new ArgsBuilder();
		argsBuilder
				.innerJoin("t1", "f1", "f2")
				.innerJoin("t2", "f3", "f4");
		builder.Select("t1", argsBuilder);
		assertEquals("SELECT * FROM t1 INNER JOIN t1 ON f1=f2 INNER JOIN t2 ON f3=f4;", builder.toString());
	}

	public void testInsert(){
		SQLBuilder sqlBuilder = new SQLBuilder();
		HashMap<String, Object> fields = new HashMap<>();
		fields.put("a1", "b1");
		sqlBuilder.Insert("t1", fields);
		assertEquals("INSERT INTO t1 ( a1 ) VALUES ( b1 );", sqlBuilder.toString());
	}
}
