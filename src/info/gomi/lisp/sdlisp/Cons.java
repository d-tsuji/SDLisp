package info.gomi.lisp.sdlisp;

public class Cons extends List{
	public T car;	// コンスセルの car 部
	public T cdr;	// コンスセルの cdr 部
	
	/**　Cons コンストラクタ 　*/
	public Cons() { this(Null.Nil, Null.Nil); }
	public Cons(T car, T cdr) {
		this.car = car;
		this.cdr = cdr;
	}

	/** Cons のシリアライズ */
	public String toString() {
		StringBuilder str = new StringBuilder();
		Cons list = this;
		str.append("("); 						// Open "("
		while (true) {
			str.append(list.car.toString());	// Car 部
			if (list.cdr == Null.Nil) {
				str.append(")");				// Close ")"
				break;
			} else if (list.cdr instanceof Atom) {
				str.append(" . ");				// ドット対
				str.append(list.cdr.toString()); // Cdr 部
				str.append(")");				// Close ")"
				break;
			} else {
				str.append(" ");				// 空白
				list = (Cons)(list.cdr);		// 次の Cdr 部へ
			}
		}
		return str.toString();
	}	
}