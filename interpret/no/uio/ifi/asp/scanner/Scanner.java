// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
	private LineNumberReader sourceFile = null;
	private String curFileName;
	private ArrayList<Token> curLineTokens = new ArrayList<>();
	private Stack<Integer> indents = new Stack<>();
	private final int TABDIST = 4;

	public Scanner(String fileName) {
		curFileName = fileName;
		indents.push(0);

		try {
			sourceFile = new LineNumberReader(
					new InputStreamReader(
							new FileInputStream(fileName),
							"UTF-8"));
		} catch (IOException e) {
			scannerError("Cannot read " + fileName + "!");
		}
	}

	private void scannerError(String message) {
		String m = "Asp scanner error";
		if (curLineNum() > 0)
			m += " on line " + curLineNum();
		m += ": " + message;

		Main.error(m);
	}

	public Token curToken() {
		while (curLineTokens.isEmpty()) {
			readNextLine();
		}
		return curLineTokens.get(0);
	}

	public void readNextToken() {
		if (!curLineTokens.isEmpty())
			curLineTokens.remove(0);
	}

	private void readNextLine() {
		curLineTokens.clear();

		// Read the next line:
		String line = null;
		try {
			line = sourceFile.readLine();
			if (line == null) {
				sourceFile.close();
				sourceFile = null;
			} else {
				Main.log.noteSourceLine(curLineNum(), line);
			}
		} catch (IOException e) {
			sourceFile = null;
			scannerError("Unspecified I/O error!");
		}

		// -- Must be changed in part 1:
		if (line == null) {
			// we reached the end, and must correct the final indents if any
			while (indents.peek() > 0) {
				indents.pop();
				curLineTokens.add(new Token(dedentToken, curLineNum()));
			}
			curLineTokens.add(new Token(eofToken, curLineNum()));

			for (Token token : curLineTokens)
				Main.log.noteToken(token);
			return;
		} else {
			// transforms all leading tabs to blanks instead
			line = expandLeadingTabs(line);
		}

		if (line.length() == 0) {
			// curLineTokens.add(new Token(newLineToken, curLineNum()));
			return;
		}

		String[] lineList = line.split("");
		int index = 0;

		// iterate through the line and check if it only contains
		// blanks/comment/blanks+comment
		if (lineList[index].equals(" ") || lineList[index].equals("#")) {
			while (index < lineList.length && lineList[index].equals(" ")) {
				index++;
			}
			// if index is equal to lineList.length, the entire line is filled with
			// blanks/tabs
			if (index == lineList.length) {
				return;
			} else if (lineList[index].equals("#")) {
				return;
			} else {
				// reset index because we know the line contains more than just blanks/tabs
				index = 0;
			}
		}

		int leadingIndents = findIndent(line);
		if (leadingIndents > indents.peek()) {
			indents.push(leadingIndents);
			curLineTokens.add(new Token(indentToken, curLineNum()));
		} else if (leadingIndents < indents.peek()) {
			while (leadingIndents < indents.peek()) {
				indents.pop();
				curLineTokens.add(new Token(dedentToken, curLineNum()));
			}
		}

		while (index < lineList.length) {
			// Comment
			if (lineList[index].equals("#")) {
				boolean significantToken = false;
				for (Token token : curLineTokens) {
					if ((token.kind != indentToken) && (token.kind != dedentToken)) {
						// found at least 1 significant token before #, ignoring rest of line
						significantToken = true;
						break;
					}
				}
				if (!significantToken)
					// Line only contains dedent, indent or comment: (add nothing to curLine)
					return;
			}

			// Whitespace mid line (not indents)
			if (lineList[index].equals(" ")) {
				index++;
				continue;
			}

			// Double symbols:
			String potentialDoubleStrings = "=/><!";
			if (potentialDoubleStrings.contains(lineList[index]) &&
					index < lineList.length - 1) {

				if (lineList[index].equals("=") &&
						lineList[index + 1].equals("=")) {
					curLineTokens.add(new Token(doubleEqualToken, curLineNum()));
					index += 2;
					continue;

				} else if (lineList[index].equals("/") &&
						lineList[index + 1].equals("/")) {
					curLineTokens.add(new Token(doubleSlashToken, curLineNum()));
					index += 2;
					continue;

				} else if (lineList[index].equals(">") &&
						lineList[index + 1].equals("=")) {
					curLineTokens.add(new Token(greaterEqualToken, curLineNum()));
					index += 2;
					continue;

				} else if (lineList[index].equals("<") &&
						lineList[index + 1].equals("=")) {
					curLineTokens.add(new Token(lessEqualToken, curLineNum()));
					index += 2;
					continue;

				} else if (lineList[index].equals("!") &&
						lineList[index + 1].equals("=")) {
					curLineTokens.add(new Token(notEqualToken, curLineNum()));
					index += 2;
					continue;
				}
			}

			if (lineList[index].equals("=")) {
				curLineTokens.add(new Token(equalToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("(")) {
				curLineTokens.add(new Token(leftParToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals(")")) {
				curLineTokens.add(new Token(rightParToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("{")) {
				curLineTokens.add(new Token(leftBraceToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("}")) {
				curLineTokens.add(new Token(rightBraceToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("[")) {
				curLineTokens.add(new Token(leftBracketToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("]")) {
				curLineTokens.add(new Token(rightBracketToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals(",")) {
				curLineTokens.add(new Token(commaToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals(":")) {
				curLineTokens.add(new Token(colonToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals(";")) {
				curLineTokens.add(new Token(semicolonToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("+")) {
				curLineTokens.add(new Token(plusToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("-")) {
				curLineTokens.add(new Token(minusToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("%")) {
				curLineTokens.add(new Token(percentToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("/")) {
				curLineTokens.add(new Token(slashToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("<")) {
				curLineTokens.add(new Token(lessToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals(">")) {
				curLineTokens.add(new Token(greaterToken, curLineNum()));
				index++;
				continue;
			}

			if (lineList[index].equals("*")) {
				curLineTokens.add(new Token(astToken, curLineNum()));
				index++;
				continue;
			}

			// String token
			if (lineList[index].equals("'")) {
				String tokenStringData = "";
				int counter = 1;
				while (!(lineList[index + counter].equals("'"))) {
					tokenStringData += lineList[index + counter];
					counter++;
					if (counter + index >= lineList.length) {
						break;
					}
				}

				Token newToken = new Token(stringToken, curLineNum());
				newToken.stringLit = tokenStringData;
				curLineTokens.add(newToken);

				index += counter + 1;
				continue;
			}

			// Also string token
			if (lineList[index].equals("\"")) {
				String tokenStringData = "";
				int counter = 1;
				while ((index + counter) < lineList.length && !lineList[index + counter].equals("\"")) {
					tokenStringData += lineList[index + counter];
					counter++;
				}

				Token newToken = new Token(stringToken, curLineNum());
				newToken.stringLit = tokenStringData;
				curLineTokens.add(newToken);

				index += counter + 1;
				continue;
			}

			// Name token, must be far enough down lol remember for us:
			String legalLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			String legalDigits = "1234567890";
			if (legalLetters.contains(lineList[index]) ||
					lineList[index].equals("_")) {
				int counter = 1;
				String nameTokenData = lineList[index];

				// Litt rar if while her, evt optimaliser for boble.asp
				if (!(counter + index >= lineList.length)) {
					while (legalLetters.contains(lineList[index + counter]) ||
							lineList[index + counter].equals("_") ||
							legalDigits.contains(lineList[index + counter])) {
						nameTokenData += lineList[index + counter];
						counter++;
						if (counter + index >= lineList.length) {
							break;
						}
					}
				}

				// ASP Keywords:
				if (nameTokenData.equals("and")) {
					curLineTokens.add(new Token(andToken, curLineNum()));

				} else if (nameTokenData.equals("def")) {
					curLineTokens.add(new Token(defToken, curLineNum()));

				} else if (nameTokenData.equals("elif")) {
					curLineTokens.add(new Token(elifToken, curLineNum()));

				} else if (nameTokenData.equals("else")) {
					curLineTokens.add(new Token(elseToken, curLineNum()));

				} else if (nameTokenData.equals("False")) {
					curLineTokens.add(new Token(falseToken, curLineNum()));

				} else if (nameTokenData.equals("for")) {
					curLineTokens.add(new Token(forToken, curLineNum()));

				} else if (nameTokenData.equals("global")) {
					curLineTokens.add(new Token(globalToken, curLineNum()));

				} else if (nameTokenData.equals("if")) {
					curLineTokens.add(new Token(ifToken, curLineNum()));

				} else if (nameTokenData.equals("in")) {
					curLineTokens.add(new Token(inToken, curLineNum()));

				} else if (nameTokenData.equals("None")) {
					curLineTokens.add(new Token(noneToken, curLineNum()));

				} else if (nameTokenData.equals("not")) {
					curLineTokens.add(new Token(notToken, curLineNum()));

				} else if (nameTokenData.equals("or")) {
					curLineTokens.add(new Token(orToken, curLineNum()));

				} else if (nameTokenData.equals("pass")) {
					curLineTokens.add(new Token(passToken, curLineNum()));

				} else if (nameTokenData.equals("return")) {
					curLineTokens.add(new Token(returnToken, curLineNum()));

				} else if (nameTokenData.equals("True")) {
					curLineTokens.add(new Token(trueToken, curLineNum()));

				} else if (nameTokenData.equals("while")) {
					curLineTokens.add(new Token(whileToken, curLineNum()));

				} else {
					// create nameToken with nameTokenData as data
					Token newToken = new Token(nameToken, curLineNum());
					newToken.name = nameTokenData;
					curLineTokens.add(newToken);
				}
				index += counter;
				continue;
			}

			// Integer or float token
			String legalDigits2 = "1234567890.";
			if (legalDigits2.contains(lineList[index])) {
				int counter = 1;
				String numberTokenStringData = lineList[index];

				while (index + counter < lineList.length &&
						legalDigits2.contains(lineList[index + counter])) {
					numberTokenStringData += lineList[index + counter];
					counter++;

					if (lineList[index + counter - 1].equals(".") &&
							!(legalDigits.contains(lineList[index + counter]))) {
						scannerError("Illigal character: " + lineList[index + counter - 1]);
					}
				}

				if (numberTokenStringData.contains(".")) {
					try {
						Float floatTokenData = Float.parseFloat(numberTokenStringData);
						Token newToken = new Token(floatToken, curLineNum());
						newToken.floatLit = floatTokenData;
						curLineTokens.add(newToken);
					} catch (NumberFormatException e) {
						// Todo finne en bedre formatering
						scannerError("Could not convert float: " + e);
					}

				} else {
					try {
						Integer integerTokenData = Integer.parseInt(numberTokenStringData);
						Token newToken = new Token(integerToken, curLineNum());
						newToken.integerLit = integerTokenData;
						curLineTokens.add(newToken);
					} catch (NumberFormatException e) {
						// Todo finne en bedre formatering
						// Bemerk at tall 04 vil funke, men parseInt conv det til 4.
						// Skal scanner på noe vis huske at det stod 04 for 4?
						scannerError("Could not convert integer: " + e);
					}
				}
				index += counter;
				continue;
			}
		}

		// Terminate line:
		curLineTokens.add(new Token(newLineToken, curLineNum()));

		for (Token t : curLineTokens)
			Main.log.noteToken(t);
	}

	public int curLineNum() {
		return sourceFile != null ? sourceFile.getLineNumber() : 0;
	}

	private int findIndent(String s) {
		int indent = 0;

		while (indent < s.length() && s.charAt(indent) == ' ')
			indent++;
		return indent;
	}

	private String expandLeadingTabs(String s) {
		// -- Must be changed in part 1:
		int n = 0; // teller opp ' ' for å holde styr på hvor mange mellomrom vi har
		int spacesToAdd = 0; // teller opp hvor mange mellomrom vi skal legge til til slutt
		int indent = 0; // teller hvor vi har kommet til på linjen
		String copy = s;

		if (s.length() > 0) {
			while (indent < s.length() && (s.charAt(indent) == ' ' || s.charAt(indent) == '\t')) {
				if (s.charAt(indent) == ' ') {
					if (n < 3) {
						n++;
					}
					// hvis vi har telt opp 4 mellomrom på rad, legg til 4 mellomrom og reset n
					else {
						spacesToAdd += 4;
						n = 0;
					}
				}
				if (s.charAt(indent) == '\t') {
					spacesToAdd += 4;
					n = 0;
				}
				copy = copy.substring(1, copy.length());

				indent++;
			}
		} else {
			return copy;
		}
		// dersom det er avsluttende ' ' som vi til slutt må telle med:
		spacesToAdd += n;

		// lager en streng med mellomrommene som skal legges til først
		String mellomrom = "";
		for (int i = 0; i < spacesToAdd; i++) {
			mellomrom += " ";
		}
		// konkatinerer strengene sammen
		copy = mellomrom + copy;

		// returnerer den nye strengen
		return copy;
	}

	private boolean isLetterAZ(char c) {
		return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
	}

	private boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}

	public boolean isCompOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		if (k == TokenKind.lessToken || k == TokenKind.lessEqualToken || k == TokenKind.greaterToken ||
				k == TokenKind.greaterEqualToken || k == TokenKind.doubleEqualToken || k == TokenKind.notEqualToken) {
			return true;
		}
		return false;
	}

	public boolean isFactorPrefix() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		if (k == plusToken || k == minusToken)
			return true;
		return false;
	}

	public boolean isFactorOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		if (k == astToken || k == slashToken || k == doubleSlashToken || k == percentToken)
			return true;
		return false;
	}

	public boolean isTermOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		if (k == plusToken || k == minusToken)
			return true;
		return false;
	}

	public boolean anyEqualToken() {
		for (Token t : curLineTokens) {
			if (t.kind == equalToken)
				return true;
			if (t.kind == semicolonToken)
				return false;
		}
		return false;
	}
}
