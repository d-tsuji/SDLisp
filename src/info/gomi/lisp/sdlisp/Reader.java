package info.gomi.lisp.sdlisp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** リスプリーダ Reader */
public class Reader {
	private final static int CharBuffSize = 256;	// 文字処理バッファのサイズ
	private static char[] charBuff = null;			// 文字処理バッファ
	private static char ch;							// 1文字バッファ
	private static String line;						// 1行入力バッファ
	private static int indexOfLine = 0;				// 1行内の位置
	private static int lineLength = 0;				// 1行の文字数
	private static BufferedReader br = null;		// java リーダ
	static {
		br = new BufferedReader(new InputStreamReader(System.in));
		charBuff = new char[CharBuffSize];		
	}

	/** S 式リーダ */
	public static T read() throws IOException{
		line = br.readLine(); // 1行読み込み
		prepare();
		return getSexp();
	}
	
	/** リーダの事前準備 */
	private static void prepare() {
		indexOfLine = 0;
		lineLength = line.length();
		// 効率化のために charArray へ格納する
		line.getChars(0, lineLength, charBuff, 0);
		charBuff[lineLength] = '\0'; // 終了マーク
		getChar();
	}

	/**
	* 配列から1文字読み込み ch に値をセットし、indexOfLine を進める
	*/
	private static void getChar() {
		ch = charBuff[indexOfLine++];
	}

	/**
	* 空白文字の読み飛ばし
	*/
	private static void skipSpace() {
		while (Character.isWhitespace(ch)) getChar();
	}

	/**
	* S式の読み込み getSexp
	*   ch は読み込んでいる状態で、このメソッドを呼び出すこと
	*/
	private static T getSexp(){
		while (true) {
			skipSpace();					// 空白の読み飛ばし
			switch (ch) {
				case '(' :	return makeList();
				case '\'':	return makeQuote();
				case '-' :	return makeMinusNumber();
				default :
					if (Character.isDigit(ch)) return makeNumber();
					return makeSymbol();
			}
		}
	}

	/** 数の読み込み makeNumber */
	private static T makeNumber(){
		StringBuilder str = new StringBuilder();
		if (ch == '-') {
			str = str.append(ch);
			getChar();
		}
		for (; indexOfLine <= lineLength; getChar()) {
			if (ch == '(' || ch == ')') break;
			if (Character.isWhitespace(ch)) break;			
			if (!Character.isDigit(ch)) {
				indexOfLine--;
				return makeSymbolInternal(str);
			}
			str.append(ch);
		}
		int value = new java.lang.Integer("" + str).intValue();
		return new Integer(value);
	}

	/** 負数の読み込み makeMinusNumber */
	private static T makeMinusNumber() {
		char nch = charBuff[indexOfLine]; // 次の文字
		// - (マイナス) の処理
		if (Character.isDigit(nch) == false) return makeSymbolInternal(new StringBuilder().append(ch));
		return makeNumber();
	}

	/** シンボルの読み込み makeSymbol */
	private static T makeSymbol() {
		ch = Character.toUpperCase(ch);
		StringBuilder str = new StringBuilder().append(ch);
		return makeSymbolInternal(str);
	}

	/**
	* 途中の文字列を渡してのシンボルの読み込み
	*   MakeSymbolInternal(StringBuffer)
	*/
	private static T makeSymbolInternal(StringBuilder str) {
		while (indexOfLine < lineLength) {
			getChar();
			if (ch == '(' || ch == ')') break;
			if (Character.isWhitespace(ch)) break;
			ch = Character.toUpperCase(ch);
			str.append(ch);
		}
		String symStr = "" + str;

		if (symStr.equals("NIL")) return Null.Nil; 		// NIL は特別に処理
		return Symbol.symbol(symStr); 
	}

	/** リストの読み込み makeList */
	private static T makeList() {
		getChar();
		skipSpace(); 				// 空白の読み飛ばし
		if (ch == ')') { 			// () のとき 
			getChar();
			return Null.Nil;
		};
		Cons top = new Cons(Null.Nil, Null.Nil);
		Cons list = top;
		while (true) {
			list.car = getSexp();   // car 部の読み込み
			skipSpace(); 			// 空白の読み飛ばし
			if (indexOfLine > lineLength) return Null.Nil; // 読み込み途中のときは NIL
			if (ch == ')') break; 	// close が来れば終了
			if (ch == '.') { 		// dot pair の読み込み
				getChar(); 			// dot の読み飛ばし
				list.cdr = getSexp();
				skipSpace(); 		// 空白の読み飛ばし
				getChar(); 			// close の読み飛ばし
				return top;
			}
			list.cdr = new Cons(Null.Nil, Null.Nil);
			list = (Cons)list.cdr;
		}
		getChar(); 					// close の読み飛ばし
		return top;
	}

	/** クォートの読み込み makeQuote */
	private static T makeQuote() {
		Cons top = new Cons();
		Cons list = top;
		list.car = Symbol.symbol("QUOTE");
		list.cdr = new Cons();
		list = (Cons)list.cdr;
		getChar();
		list.car = getSexp();
		return top;
	}
}