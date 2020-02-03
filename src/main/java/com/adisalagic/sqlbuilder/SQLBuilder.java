package com.adisalagic.sqlbuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLBuilder {
	private static String SELECT_ALL      = "SELECT * FROM %tables%";
	private static String SELECT          = "SELECT %fields% FROM %tables%";
	private static String CREATE_DATABASE = "CREATE DATABASE %name_of_db%";
	private static String CREATE_TABLE    = "CREATE TABLE %name_of_table% ( %hash_map% )";
	private static String DELETE          = "DELETE FROM %table% WHERE %condition%";
	private static String UPDATE          = "UPDATE %tables% SET %update_fields% WHERE %condition%";
	private static String INSERT = "INSERT INTO %table_name%";

	private String query = "";

	/**
	 * Makes <code>String</code> from array, where every element goes comma separated<br>
	 * @param args Array of strings
	 * @return Line of strings
	 */
	private String inlineArrayOfString(String[] args) {
		StringBuilder result = new StringBuilder();
		String        fi;
		for (int i = 0; i < args.length; i++) {
			if (i == args.length - 1) {
				result.append(args[i]);
			} else {
				result.append(args[i]).append(", ");
			}
		}
		fi = result.toString();
		return fi;
	}

	/**
	 * Standard 'Select' statement.<br>
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param fields The fields as <code>String[]</code> array
	 * @param tables The tables as <code>String[]</code> array
	 * @return com.adisalagic.sqlbuilder.SQLBuilder class with Select statement
	 */
	public SQLBuilder Select(String[] fields, String[] tables) {
		query += SELECT;

		query = query.replaceAll("%fields%", inlineArrayOfString(fields));

		if (tables.length == 0) {
			return null;
		}

		query = query.replaceAll("%tables%", inlineArrayOfString(tables));
		return this;
	}

	/**
	 * Super simple 'Select' statement, which uses <code>*</code> to select everything from tables<br>
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param tables The tables as <code>String[]</code> array
	 * @return com.adisalagic.sqlbuilder.SQLBuilder class with this statement
	 */
	public SQLBuilder Select(String[] tables) {
		query += SELECT_ALL;
		if (tables.length == 0) {
			return null;
		}
		query = query.replaceAll("%tables%", inlineArrayOfString(tables));
		return this;
	}

	/**
	 * Ultra simple 'Select' statement. Selects everything from table<br>
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param table The table as <code>String</code>
	 * @return com.adisalagic.sqlbuilder.SQLBuilder class with this statement
	 */
	public SQLBuilder Select(String table) {
		Select(new String[]{table});
		return this;
	}

	/**
	 * 'Select' statement with one table<br>
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param fields The fields as <code>String[]</code> array
	 * @param table The table with data
	 * @return com.adisalagic.sqlbuilder.SQLBuilder class with this statement
	 */
	public SQLBuilder Select(String[] fields, String table) {
		Select(fields, new String[]{table});
		return this;
	}

	/**
	 * 'Select' statement. Uses {@link ArgsBuilder} to create <code>WHERE</code> conditions.<br>
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param fields The fields as <code>String[]</code> array
	 * @param table The table as <code>String</code>
	 * @param argsBuilder {@link ArgsBuilder} as condition
	 * @return {@link SQLBuilder} class with this statement
	 */
	public SQLBuilder Select(String[] fields, String table, ArgsBuilder argsBuilder) {
		Select(fields, new String[]{table}, argsBuilder);
		return this;
	}

	/**
	 * 'Select' statement with one table and {@link ArgsBuilder} as condition
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param table The table as <code>String</code>
	 * @param argsBuilder Condition as {@link ArgsBuilder}
	 * @return {@link SQLBuilder} class with this statement
	 */
	public SQLBuilder Select(String table, ArgsBuilder argsBuilder) {
		Select(new String[]{table}, argsBuilder);
		return this;
	}

	/**
	 * 'Select' statement with {@link ArgsBuilder} as condition
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param fields The fields as <code>String[]</code> array
	 * @param tables The tables as <code>String[]</code> array
	 * @param argsBuilder {@link ArgsBuilder} as condition
	 * @return {@link SQLBuilder} class with this statement
	 */
	public SQLBuilder Select(String[] fields, String[] tables, ArgsBuilder argsBuilder) {
		SQLBuilder builder = Select(fields, tables);
		return builder.addArgs(argsBuilder);
	}

	/**
	 * The same as {@link SQLBuilder#Select(String[], String[], ArgsBuilder)} but without fields
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param tables The tables as <code>String[]</code> array
	 * @param argsBuilder {@link ArgsBuilder} as condition
	 * @return {@link SQLBuilder} with this statement
	 */
	public SQLBuilder Select(String[] tables, ArgsBuilder argsBuilder) {
		SQLBuilder builder = Select(tables);
		return builder.addArgs(argsBuilder);
	}

	/**
	 * Adds args from {@link ArgsBuilder} and adds it to query
	 * @param builder {@link ArgsBuilder} as condition
	 * @return {@link SQLBuilder} with condition
	 */
	private SQLBuilder addArgs(ArgsBuilder builder) {
		query += builder.toString();
		return this;
	}

	/**
	 * Cleans query
	 */
	public void clean() {
		query = "";
	}

	public String toString() {
		return query + ";";
	}

	/**
	 * 'Create database' statement
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param nameOfDatabase Name of database as <code>String</code>
	 * @return {@link SQLBuilder} with this statement
	 */
	public SQLBuilder createDatabase(String nameOfDatabase) {
		query = CREATE_DATABASE;
		query = query.replaceAll("%name_of_db%", nameOfDatabase);
		return this;
	}

	/**
	 * Closes current query with <code>;</code> and goes to a new line
	 * @return {@link SQLBuilder} class with statements
	 */
	public SQLBuilder endStatement() {
		query += ";\n";
		return this;
	}

	/**
	 * 'Create table' statement
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param name Name of table
	 * @param fields List with {@link Field}
	 * @return {@link SQLBuilder} with this statement
	 */
	public SQLBuilder CreateTable(String name, List<Field> fields) {
		query += CREATE_TABLE;
		query = query.replaceAll("%name_of_table%", name);
		query = query.replaceAll("%hash_map%", inlineFields(fields));
		return this;
	}

	/**
	 * Inlines fields to String.
	 * @param fields List of {@link Field}
	 * @return Inline fields as <code>String</code>
	 */
	private String inlineFields(List<Field> fields) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {
			if (i == 0) {
				builder.append(fields.get(i).toString());
			} else {
				builder.append(", ").append(fields.get(i).toString());
			}
		}
		return builder.toString();
	}

	/**
	 * 'Delete' statement with a single table<br>
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param table Name of the table
	 * @param argsBuilder {@link ArgsBuilder} as condition
	 * @return {@link SQLBuilder} with this statement
	 */
	public SQLBuilder Delete(String table, ArgsBuilder argsBuilder) {
		this.Delete(new String[]{table}, argsBuilder);
		return this;
	}

	/**
	 * 'Delete' statement.<br>
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param tables The tables as <code>String[]</code> array
	 * @param argsBuilder {@link ArgsBuilder} as condition
	 * @return {@link SQLBuilder} with this statement
	 */
	public SQLBuilder Delete(String[] tables, ArgsBuilder argsBuilder) {
		query += DELETE;
		query = query.replaceAll("%table%", inlineArrayOfString(tables));
		if (argsBuilder.hasWhere()) {
			query = query.replaceAll(" WHERE %condition%", "%condition%");
		}
		query = query.replaceAll("%condition%", argsBuilder.toString());
		return this;
	}

	/**
	 * Inlines <code>HashMap(String, Object)</code> to <code>String</code>
	 * @param map Map that will be used
	 * @return <code>String</code> of inlined map
	 */
	private String inlineHashMap(HashMap<String, Object> map) {
		StringBuilder builder = new StringBuilder();
		int           count   = 1;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key   = entry.getKey().replaceAll("'", "\\\\\\\\\'");
			String value = entry.getValue().toString().replaceAll("'", "");
			if (count == map.size()) {
				builder.append(key).append(" = ").append("'").append(value).append("'");
			} else {
				builder.append(key).append(" = ").append("'").append(value).append("'").append(", ");
			}
		}
		return builder.toString();
	}

	private String insertInlineHashMap(HashMap<String, Object> map){
		String srt = " ( %next_key% ) VALUES ( %next_value% )";
		int count = 1;
		for (Map.Entry<String, Object> entry : map.entrySet()){
			String key   = entry.getKey().replaceAll("'", "\\\\\\\\\'");
			String value = entry.getValue().toString().replaceAll("'", "");
			if (count == map.size()){
				srt = srt.replaceAll("%next_key%", key);
				srt = srt.replaceAll("%next_value%", value);
			}else {
				srt = srt.replaceAll("%next_key%", key + ", %next_key%");
				srt = srt.replaceAll("%next_value%", value + " %next_value%");
			}
		}
		return srt;
	}

	/**
	 * 'Update' statement<br>
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param tables The tables as <code>String[]</code> array
	 * @param fields <code>HashMap(String, Object)</code> as fields
	 * @param argsBuilder {@link ArgsBuilder} as condition
	 * @return {@link SQLBuilder} with this statement
	 */
	public SQLBuilder Update(String[] tables, HashMap<String, Object> fields, ArgsBuilder argsBuilder) {
		query += UPDATE;
		query = query.replaceAll("%tables%", inlineArrayOfString(tables));
		query = query.replaceAll("%update_fields%", inlineHashMap(fields));
		if (argsBuilder.hasWhere()) {
			query = query.replaceAll(" WHERE %condition%", "%condition%");
		}
		query = query.replaceAll("%condition%", argsBuilder.toString());
		return this;
	}

	/**
	 * The same as {@link SQLBuilder#Update(String[], HashMap, ArgsBuilder)}, but with single table<br>
	 * DOES NOT OVERWRITE THE LAST STATEMENT! See {@link SQLBuilder#endStatement}
	 * @param table The tables as <code>String</code>
	 * @param fields <code>HashMap(String, Object)</code> as fields
	 * @param builder {@link ArgsBuilder} as condition
	 * @return {@link SQLBuilder} with this statement
	 */
	public SQLBuilder Update(String table, HashMap<String, Object> fields, ArgsBuilder builder) {
		this.Update(new String[]{table}, fields, builder);
		return this;
	}

	public SQLBuilder Insert(String table, HashMap<String, Object> fields){
		query += INSERT;
		query = query.replaceAll("%table_name%", table);
		query += insertInlineHashMap(fields);
		return this;
	}
	

	//Python - го(а)вно
}