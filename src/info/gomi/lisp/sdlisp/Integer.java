package info.gomi.lisp.sdlisp;

public class Integer extends Number {
	public int value;

	public Integer(int value){
		this.value = value;
	}
	
	public T add(Integer arg2){
		return new Integer(this.value + arg2.value);
	}
	
	public T sub(Integer arg2){
		return new Integer(this.value - arg2.value);
	}

	public T mul(Integer arg2){
		return new Integer(this.value * arg2.value);
	}

	public T div(Integer arg2){
		return new Integer(this.value / arg2.value);
	}

	public T ge(Integer arg2){
		return (this.value >= arg2.value) ? Symbol.symbolT : Null.Nil;
	}

	public T le(Integer arg2){
		return (this.value <= arg2.value) ? Symbol.symbolT : Null.Nil;
	}

	public T gt(Integer arg2){
		return (this.value > arg2.value) ? Symbol.symbolT : Null.Nil;
	}

	public T lt(Integer arg2){
		return (this.value < arg2.value) ? Symbol.symbolT : Null.Nil;
	}

	public T numberEqual(Integer arg2){
		return (this.value == arg2.value) ? Symbol.symbolT : Null.Nil;
	}
	
	/**　固定整数の文字列化（シリアライズ）　*/
	public String toString(){
		return "" + value;
	}
}
