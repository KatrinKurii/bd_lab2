package database

import java.io.BufferedReader
import java.io.FileReader
import java.sql.Connection
import java.sql.DriverManager

object DatabaseProvider {
    val WARE_PATH = "ware"
    val MAKER_PATH = "maker"
    val SELLER_PATH = "seller"
    val BUYER_PATH = "buyer"
    private lateinit var connection: Connection
    fun init() {
        val url = "jdbc:mysql://localhost:3306/mytest?useSSL=false"
        val user = "root"
        val password = "2222"
        connection = DriverManager.getConnection(url, user, password)
    }
    fun finish() {
        connection.close()
    }
    fun load(table: String, path: String = table+".csv" ) {
        val reader = BufferedReader(FileReader(path))
        var line: String?
        val statement = connection.createStatement()
        statement.execute("DELETE FROM $table;")
        while (true) {
            line = reader.readLine()
            if (line == null || line.isEmpty())
                return
            line = line.split(";").map { "'$it'" }.toString().replace("[\\[\\]]".toRegex(),"")
            statement.execute("""INSERT INTO $table VALUES ($line);""")
        }
    }
    fun addFact(bid: Int, sid: Int, mid: Int, wid: Int, amount: Int) {
        val statement = connection.createStatement()
        statement.execute("INSERT INTO facts (wid, mid, sid, bid, amount, date) VALUES ($wid, $mid, $sid, $bid, $amount, curdate());")
        statement.close()
    }
    fun getWares(): Array<String> {
        val statement = connection.createStatement()
        val result = statement.executeQuery("SELECT wid, wname FROM ware;")
        val list: ArrayList<String> = ArrayList()
        while (result.next()) {
            list.add("${result.getString("wid")}. Ware ${result.getString("wname")}")
        }
        statement.close()
        return list.toTypedArray()
    }
    fun getMakers(): Array<String> {
        val statement = connection.createStatement()
        val result = statement.executeQuery("SELECT mid, mname, country FROM maker;")
        val list: ArrayList<String> = ArrayList()
        while (result.next()) {
            list.add("${result.getString("mid")}. Maker ${result.getString("mname")} from ${result.getString("country")}")
        }
        statement.close()
        return list.toTypedArray()
    }
    fun getSellers(): Array<String> {
        val statement = connection.createStatement()
        val result = statement.executeQuery("SELECT sid, sname FROM seller;")
        val list: ArrayList<String> = ArrayList()
        while (result.next()) {
            list.add("${result.getString("sid")}. Seller ${result.getString("sname")}")
        }
        statement.close()
        return list.toTypedArray()
    }
    fun getBuyers(): Array<String> {
        val statement = connection.createStatement()
        val result = statement.executeQuery("SELECT bid, bname FROM buyer;")
        val list: ArrayList<String> = ArrayList()
        while (result.next()) {
            list.add("${result.getString("bid")}. Buyer ${result.getString("bname")}")
        }
        statement.close()
        return list.toTypedArray()
    }
    fun getFactsNames(): Array<String> {
        val statement = connection.createStatement()
        val result = statement.executeQuery("SELECT fid, wname, mname, sname, bname " +
                "FROM facts f, ware w, maker m, seller s, buyer b " +
                "WHERE w.wid=f.wid AND m.mid=f.mid AND s.sid=f.sid AND b.bid=f.bid;")
        val list: ArrayList<String> = ArrayList()
        while (result.next()) {
            list.add("${result.getString("fid")}. Ware ${result.getString("wname")}, maker ${result.getString("mname")}" +
                    "seller ${result.getString("sname")}, buyer ${result.getString("bname")}")
        }
        statement.close()
        return list.toTypedArray()
    }
    fun getFacts(): Array<Array<String>> {
        val statement = connection.createStatement()
        val result = statement.executeQuery("SELECT wname, mname, country, description, sname, bname, amount, date " +
                "FROM ware w, maker m, seller s, buyer b, facts f " +
                "WHERE w.wid=f.wid AND m.mid=f.mid AND s.sid=f.sid AND b.bid=f.bid;")
        val array: ArrayList<Array<String>> = ArrayList()
        while (result.next()) {
            array.add(arrayOf(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
                    result.getString(5), result.getString(6), result.getString(7), result.getString(8)))
        }
        statement.close()
        return array.toTypedArray()
    }

    fun findFact(wid: Int): Array<Array<String>> {
        val statement = connection.createStatement()
        val result = statement.executeQuery("SELECT wname, mname, country, description, sname, bname, amount, date " +
                "FROM facts f, buyer b, seller s, maker m, ware w " +
                "WHERE b.bid=f.bid AND s.sid=f.sid AND w.wid=f.wid AND m.mid=f.mid AND f.wid=$wid;")
        val array: ArrayList<Array<String>> = ArrayList()
        while (result.next()) {
            array.add(arrayOf(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
                    result.getString(5), result.getString(6), result.getString(7), result.getString(8)))
        }
        statement.close()
        return array.toTypedArray()
    }

    fun delete(fid: Int) {
        val statement = connection.createStatement()
        statement.execute("DELETE FROM facts WHERE fid=$fid;")
        statement.close()
    }

    fun updateFact(fid: Int, bid: Int, sid: Int, mid: Int, wid: Int, amount: Int) {
        val statement = connection.createStatement()
        statement.execute("UPDATE facts SET wid=$bid, mid=$mid, sid=$wid, bid=$sid, amount=$amount WHERE fid=$fid;")
        statement.close()
    }

    fun findText(text: String?): Array<Array<String>> {
        val statement = connection.createStatement()
        val result = statement.executeQuery("SELECT wname, mname, country, description, sname, bname, amount, date " +
                "FROM facts f, ware w, maker m, seller s, buyer b " +
                "WHERE w.wid=f.wid AND m.mid=f.mid AND s.sid=f.sid AND b.bid=f.bid  AND " +
                "MATCH (description) AGAINST ('$text' IN BOOLEAN MODE);")
        val array: ArrayList<Array<String>> = ArrayList()
        while (result.next()) {
            array.add(arrayOf(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
                    result.getString(5), result.getString(6), result.getString(7), result.getString(8)))
        }
        statement.close()
        return array.toTypedArray()
    }
}
