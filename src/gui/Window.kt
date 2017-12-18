package gui

import java.awt.Dimension
import database.DatabaseProvider as db
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.*
import javax.swing.BoxLayout.X_AXIS
import javax.swing.BoxLayout.Y_AXIS

class Window: JFrame("Lab DB"), WindowListener {
    override fun windowDeiconified(p0: WindowEvent?) = Unit

    override fun windowClosing(p0: WindowEvent?) = Unit

    override fun windowActivated(p0: WindowEvent?) = Unit

    override fun windowDeactivated(p0: WindowEvent?) = Unit

    override fun windowOpened(p0: WindowEvent?) = Unit

    override fun windowIconified(p0: WindowEvent?) = Unit

    override fun windowClosed(p0: WindowEvent?) {
        db.finish()
        dispose()
    }

    var table: JTable
    val scroll: JScrollPane
    init {
        db.init()
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.layout = BoxLayout(contentPane,Y_AXIS)
        contentPane.add(createToolBar())
        table = showTable()
        scroll = JScrollPane(table)
        scroll.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scroll.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scroll.preferredSize = Dimension(1000,400)
        contentPane.add(scroll)
        addWindowListener (this)
        pack()
    }

    private fun showTable(provider: Array<Array<String>> = db.getFacts()): JTable = JTable(provider, arrayOf("Ware",  "Maker", "Country", "Description","Seller", "Buyer", "Number", "Date"))

    private fun createToolBar(): JPanel {
        val tools = JPanel()
        tools.layout = BoxLayout(tools, X_AXIS)
        val load = JButton("Load")
        load.setMnemonic('L')
        load.addActionListener {
            db.load(db.WARE_PATH)
            db.load(db.MAKER_PATH)
            db.load(db.SELLER_PATH)
            db.load(db.BUYER_PATH)
            table = showTable()
            scroll.setViewportView(table)
        }
        tools.add(load)
        val add = JButton("Add")
        add.setMnemonic('A')
        add.addActionListener {
            val wares = db.getWares()
            val makers = db.getMakers()
            val sellers = db.getSellers()
            val buyers = db.getBuyers()
            val wid = JOptionPane.showInputDialog(this, "Select ware", "Add fact of sale",
                    JOptionPane.QUESTION_MESSAGE, null, wares, wares[0]) as String? ?: return@addActionListener
            val mid = JOptionPane.showInputDialog(this, "Select maker", "Add fact of sale",
                    JOptionPane.QUESTION_MESSAGE, null, makers, makers[0]) as String? ?: return@addActionListener
            val sid = JOptionPane.showInputDialog(this, "Select seller", "Add fact of sale",
                    JOptionPane.QUESTION_MESSAGE, null, sellers, sellers[0]) as String? ?: return@addActionListener
            val bid = JOptionPane.showInputDialog(this, "Select buyer", "Add fact of sale",
                    JOptionPane.QUESTION_MESSAGE,null,buyers,buyers[0]) as String? ?: return@addActionListener
            val amount = JOptionPane.showInputDialog(this, "Set number", "Add fact of sale",
                    JOptionPane.QUESTION_MESSAGE)?.toIntOrNull() ?: return@addActionListener
            db.addFact(bid.split(".")[0].toInt(),
                    sid.split(".")[0].toInt(),
                    mid.split(".")[0].toInt(),
                    wid.split(".")[0].toInt(),
                    amount)
            table = showTable()
            scroll.setViewportView(table)
        }
        tools.add(add)
        val search = JButton("Search")
        search.setMnemonic('S')
        search.addActionListener {
            val wares = db.getWares()
            val wid = JOptionPane.showInputDialog(this, "Select ware", "Search fact of sale",
                    JOptionPane.QUESTION_MESSAGE, null, wares, wares[0]) as String?
            table = if (wid == null) {
                showTable()
            } else {
                val result = db.findFact(wid.split(".")[0].toInt())
                showTable(result)
            }
            scroll.setViewportView(table)
        }
        tools.add(search)
        val delete = JButton("Delete")
        delete.setMnemonic('D')
        delete.addActionListener {
            val facts = db.getFactsNames()
            val fid = JOptionPane.showInputDialog(this, "Select fact of sale", "Delete fact of sale",
                    JOptionPane.QUESTION_MESSAGE, null, facts, facts[0]) as String? ?: return@addActionListener
            db.delete(fid.split(".")[0].toInt())
            table = showTable()
            scroll.setViewportView(table)
        }
        tools.add(delete)
        val update = JButton("Update")
        update.setMnemonic('U')
        update.addActionListener {
            val facts = db.getFactsNames()
            val wares = db.getWares()
            val makers = db.getMakers()
            val sellers = db.getSellers()
            val buyers = db.getBuyers()
            val fid = JOptionPane.showInputDialog(this, "Select fact of sale", "Update fact of sale",
                    JOptionPane.QUESTION_MESSAGE, null, facts, facts[0]) as String? ?: return@addActionListener
            val wid = JOptionPane.showInputDialog(this, "Select ware", "Update fact of sale",
                    JOptionPane.QUESTION_MESSAGE, null, wares, wares[0]) as String? ?: return@addActionListener
            val mid = JOptionPane.showInputDialog(this, "Select maker", "Update fact of sale",
                    JOptionPane.QUESTION_MESSAGE, null, makers, makers[0]) as String? ?: return@addActionListener
            val sid = JOptionPane.showInputDialog(this, "Select seller", "Update fact of sale",
                    JOptionPane.QUESTION_MESSAGE, null, sellers, sellers[0]) as String? ?: return@addActionListener
            val bid = JOptionPane.showInputDialog(this, "Select buyer", "Update fact of sale",
                    JOptionPane.QUESTION_MESSAGE,null,buyers,buyers[0]) as String? ?: return@addActionListener
            val amount = JOptionPane.showInputDialog(this, "Set number", "Update fact of sale",
                    JOptionPane.QUESTION_MESSAGE)?.toIntOrNull() ?: return@addActionListener
            db.updateFact(fid.split(".")[0].toInt(),
                    wid.split(".")[0].toInt(),
                    mid.split(".")[0].toInt(),
                    sid.split(".")[0].toInt(),
                    bid.split(".")[0].toInt(),
                    amount)
            table = showTable()
            scroll.setViewportView(table)
        }
        tools.add(update)
        val field = JTextField()
        tools.add(field)
        val text = JButton("Text search")
        text.setMnemonic('T')
        text.addActionListener {
            val str = field.text
            table = if (str.isEmpty())
                showTable()
            else
                showTable(db.findText(str))
            scroll.setViewportView(table)
        }
        tools.add(text)
        return tools
    }
}
