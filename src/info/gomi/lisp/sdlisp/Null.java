package info.gomi.lisp.sdlisp;

public class Null extends List {
	public static Null Nil = new Null();
	
	/** シリアライズ */
	public String toString() {
		return "NIL"; 
	}
}