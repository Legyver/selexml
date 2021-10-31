# selexml
Desktop client for querying XML

## Mental model
### Introduction
Both XML and relational databases are structured data.  Therefore, it should be possible to map XML onto a database structure.

Consider the following XML from [Reed College](http://aiweb.cs.washington.edu/research/projects/xmltk/xmldata/www/repository.html).
```xml
<root>
    <course>
        <reg_num>10577</reg_num>
        <subj>ANTH</subj>
        <crs>211</crs>
        <sect>F01</sect>
        <title>Introduction to Anthropology</title>
        <credit>1.0</credit>
    </course>
    <course>
        <reg_num>20573</reg_num>
        <subj>ANTH</subj>
        <crs>344</crs>
        <sect>S01</sect>
        <title>Sex and Gender</title>
        <credit>1.0</credit>
    </course>
</root>
```
It should be possible to see this is similar to the following table

<table>
<tr><td>reg_num</td><td>subj</td><td>crs</td><td>sect</td><td>title</td><td>credit</td></tr>
<tr><td>10577</td><td>ANTH</td><td>211</td><td>F01</td><td>Introduction to Anthropology</td><td>1.0</td></tr>
<tr><td>20573</td><td>ANTH</td><td>344</td><td>S01</td><td>Sex and Gender</td><td>1.0</td></tr>
</table>
Thus we should be able to query it like a normal table.

```roomsql
select * from course where subj = ANTH
```
### Mapping rules
Thus consider the following
1. Mapping XML to tables
   1. Any element tag like &lt;course&gt; can be mapped to a table named 'course'
   2. Any attribute like &lt;attr course="20573"&gt; can be mapped to an element like &lt;course&gt;20573&lt;/course>
   3. Therefore, named attributes can also be mapped to tables.
2. Mapping XML to columns
   1. Any element like &lt;reg_num&gt;10577&lt;/reg_num&gt;can be mapped to a column named 'reg_num'
   2. Since we can represent named attributes as elements (1.2 above), attributes can be similarly mapped

### Extensions to the SQL concept
#### Pseudo-reference prefixes
So you might be thinking, if both elements and attributes map to tables and columns won't they interfere?

Loosely analogous to schemas, pseudo-references allow for filtering to alleviate this.  However, unlike schemas, these can be applied individually to any column or table reference
* $e is a shortcut for elements
```roomsql
  select $e:column1, $e:column2 from $e:table where $e:column1 = a value
```
* $a is a shortcut for attributes
```roomsql
  select $a:column1, $a:column2 from $a:table where $a:column1 = a value
```

These can be mixed-and-matched.  If there is no pseudo-reference prefix, both are searched

#### Case-sensitivity
All comparisons are case-sensitive by default.  Appending an underscore to the command turns off case sensitivity

##### Supported comparisons
<table>
<tr><td>Case sensitive</td><td>example</td><td>Case insensitive</td><td>example</td></tr>
<tr><td>is</td><td>column is value</td><td>is_</td><td>column is_ value</td></tr>
<tr><td>is</td><td>column is null</td><td></td><td></td></tr>
<tr><td>=</td><td>column = value</td><td>=_</td><td>column =_ value</td></tr>

<tr><td>not</td><td>column not value</td><td>not_</td><td>column not_ value</td></tr>
<tr><td>not</td><td>column not null</td><td></td><td></td></tr>
<tr><td>!=</td><td>column != value</td><td>!=_</td><td>column !=_ value</td></tr>
<tr><td><></td><td>column <> value</td><td><>_</td><td>column <>_ value</td></tr>

<tr><td>like</td><td>column like p_tt%rn</td><td>like_</td><td>column like_ P_TT%RN</td></tr>

</table>


## API
SQL-like
* Select all values from all elements
    * SQL analogy: select * from UNION(select * from table1, select * from table2, ...)
```roomsql
select * from *
```

* Select all values from one-or-more elements or attributes
  * SQL analogy: select * from table
```roomsql
select * from course
select * from course, lab, lecture
```

* Select all values from specific elements or attributes
```roomsql
select * from $e:course
select * from $a:course
```

* Specific the XML element elements/attributes to include
    * SQL analogy: selecting columns
```roomsql
select reg_num, subj, crs from course
```

* Select all values meeting one-or-more conditions
  * SQL analogy: where clause in select statements
```roomsql
select * from course where instructor = Kaplan
select * from course where instructor = Kaplan, days = T,W and is_full = false
select * from course where instructor = Kaplan and days = T,W and is_full = false
```

## Roadmap
### Bugs
* Handle semi-colons at line endings
### New functionality
* GUI for command execution
