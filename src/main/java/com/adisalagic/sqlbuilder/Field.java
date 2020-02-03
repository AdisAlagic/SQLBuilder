package com.adisalagic.sqlbuilder;

public class Field {
	private String  name;
	private String  type;
	private boolean isPrimaryKey;
	private int     length  = -1;
	private boolean notNull = false;
	private boolean virtual = false;
	private String  comment;

	public Field(String name, String type) {
		this.name = name;
		this.type = type;
		isPrimaryKey = false;
		comment = "";
	}

	public Field(String name, String type, boolean isPrimaryKey) {
		this.name = name;
		this.type = type;
		this.isPrimaryKey = isPrimaryKey;
		comment = "";
	}

	public Field(String name, String type, boolean isPrimaryKey, int length) {
		this.name = name;
		this.type = type;
		this.isPrimaryKey = isPrimaryKey;
		this.length = length;
	}

	public Field(String name, String type, boolean isPrimaryKey, int length, boolean notNull) {
		this.name = name;
		this.type = type;
		this.isPrimaryKey = isPrimaryKey;
		this.length = length;
		this.notNull = notNull;
	}

	public Field(String name, String type, boolean isPrimaryKey, int length, boolean notNull, boolean virtual) {
		this.name = name;
		this.type = type;
		this.isPrimaryKey = isPrimaryKey;
		this.length = length;
		this.notNull = notNull;
		this.virtual = virtual;
	}

	public Field(String name, String type, boolean isPrimaryKey, int length, boolean notNull, boolean virtual, String comment) {
		this.name = name;
		this.type = type;
		this.isPrimaryKey = isPrimaryKey;
		this.length = length;
		this.notNull = notNull;
		this.virtual = virtual;
		this.comment = comment;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		isPrimaryKey = primaryKey;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String toString() {
		if (comment == null) {
			comment = "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("`").append(name).append("`").append(" ").append(type).append(" ");
		if (length != -1) {
			builder.append("(").append(length).append(") ");
		}
		if (virtual) {
			builder.append("AS () ");
		}
		if (comment.length() > 0) {
			builder.append("COMMENT '").append(comment).append("' ");
		}
		if (notNull) {
			builder.append("NOT NULL");
		}
		if (isPrimaryKey) {
			builder.append(", PRIMARY KEY (`").append(name).append("`)");
		}
		return builder.toString();
	}
}
