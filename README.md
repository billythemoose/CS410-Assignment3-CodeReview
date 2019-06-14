# CS410-Assignment3

## Code Smells

* Then entire class is an action listener
```
	24
	Change 
		public class SimpleNotePad extends JFrame implements ActionListener{
	To
		public class SimpleNotePad extends JFrame
```

* Split up the single action listener
```
  70
```

* Remove if else chain
```
	71
	87
	110
	112
	117
```
  
* Naming conventions for UI items
```
	24
	Change 
		JMenuBar mb = new JMenuBar();
	To
		JMenuBar uiMenuBar = new JMenuBar();
	25
	Change
    JMenu fm = new JMenu("File");
	To
		JMenu uiMenuFile = new JMenu("File");
	26
	Change 
		JMenu em = new JMenu("Edit");
	To
		JMenu uiMenuEdit = new JMenu("Edit");
	27 
	Change 
		JTextPane d = new JTextPane();
	To
		JTextPane uiTextPane = new JTextPane();
	28 
	Change 
		JMenuItem nf = new JMenuItem("New File");
	To
		JMenuItem uiMenuNew = new JMenuItem("New File");
	29 
	Change 
		JMenuItem sf = new JMenuItem("Save File");
	To
		JMenuItem uiMenuSave = new JMenuItem("Save File");
	30 
	Change 
		JMenuItem pf = new JMenuItem("Print File");
	To
		JMenuItem uiMenuPrint = new JMenuItem("Print File");
	32 
	Change 
		JMenuItem c = new JMenuItem("Copy");
	To
		JMenuItem uiMenuCopy = new JMenuItem("Copy");
	33
	Change 
		JMenuItem p = new JMenuItem("Paste");
	To
		JMenuItem uiMenuPaste = new JMenuItem("Paste");
```
	
* Remove unimplemented code
```
	31 remove JMenuItem u = new JMenuItem("Undo");
	42 remove uiMenuEdit.add(uiMenuUndo);
	56 remove uiMenuUndo.addActionListener(this);
	57 remove uiMenuUndo.setActionCommand("undo");
	118 "TODO: implement undo operation"
```
	
* Add logging to catch statements
	
* Spacing for readability
```
	Insert line break
	33
	62
	Remove line break
	51
```
	
* Add comments to methods
