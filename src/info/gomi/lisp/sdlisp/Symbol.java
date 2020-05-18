package info.gomi.lisp.sdlisp;

import java.util.HashMap;

public class Symbol extends Atom {
	public String name;		// シンボルの名前
	public T value;			// シンボルの値
	public T function;		// シンボルの関数	
	private static HashMap<String, Symbol> symbolTable = new HashMap<String, Symbol>();	// シンボルテーブル
	public static Symbol symbolT = Symbol.symbol("T");			// シンボル T
	public static Symbol symbolQuit = Symbol.symbol("QUIT");	// シンボル QUIT
	static { symbolT.value = symbolT; }							// T の値は自分自身
	
  	/**　Symbol コンストラクタ （関数 symbol から呼ばれる）　*/
  	private Symbol(String name){ this.name = name; }

  	/**　シンボルの生成とインターン(新規のシンボルのときは生成してインターン、既存のシンボルのときはそれを返す)　*/
	public static Symbol symbol(java.lang.String name) {
		if (symbolTable.get(name) == null){
			Symbol symbol = new Symbol(name);
			symbolTable.put(name, symbol);
		}
		return symbolTable.get(name);
	}
	
	/**　シリアライズ　*/
	public String toString() { return name; }
}