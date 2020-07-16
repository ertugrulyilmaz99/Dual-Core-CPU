import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {

		String filename = "word.txt";
		String file = "multi_word.txt";

		Single_Core(filename);
		Multi_Core(file);

	}

	public static void Single_Core(String filename) throws FileNotFoundException {
		ArrayList<String> rom = new ArrayList<String>(); // rom's content in hex
		// reading file
		File myFile = new File(filename);
		Scanner myInput = null;
		try {
			myInput = new Scanner(myFile);
		} catch (FileNotFoundException e) {
			System.out.println(filename + ": Input file can not be found!\nExiting program...");
			System.exit(1);
		}
		while (myInput.hasNext()) {
			String line = myInput.nextLine();
			String[] parts = line.split(" ");

			if (parts[0].equals("#")) { // It will pass with starting # lines
				continue;
			}

			for (int i = 0; i < parts.length; i++) { // travelling in a line
				if (parts[i].equals("#")) // if we see # in a line we will continue
					continue;
				String op = Op_Code(parts[i]);
				if (op != null) {
					String bin_op = op;
					String hex_op = binary_to_hex(bin_op); // op code in hex
					if (!parts[1].equals("r")) { // if there is no ret
						String im = decimal_to_hex(parts[1]); // immediate value = 12 bits
						String result = hex_op + im;
						rom.add(result); // add that instruction as hex in rom's content
						// System.out.println(result);

					} else if (parts[1].equals("r")) { // if there is ret
						String result = hex_op + "000"; // ret with instruction
						rom.add(result); // add that instruction as hex in rom's content

					}

				}

			}

		}
		// we give output in a txt which are rom's content as hex
		PrintStream single_out = new PrintStream("./single_out.txt");
		System.setOut(single_out);
		System.out.println("v2.0 " + "raw");
		rom.forEach((n) -> System.out.print(n + " "));

	}

	public static void Multi_Core(String file) throws FileNotFoundException {
		ArrayList<String> multi_rom0 = new ArrayList<String>(); // rom0's content
		ArrayList<String> multi_rom1 = new ArrayList<String>(); // rom1's content
		File myFile = new File(file);// file reading
		Scanner myInput = null;
		try {
			myInput = new Scanner(myFile);
		} catch (FileNotFoundException e) {
			System.out.println(file + ": Input file can not be found!\nExiting program...");
			System.exit(1);
		}
		while (myInput.hasNext()) {
			String line = myInput.nextLine();
			String[] parts = line.split(" ");

			if (parts[0].equals("#")) {
				continue;
			}
			for (int i = 0; i < parts.length; i++) { // travelling in a line
				if (parts[i].equals("#"))
					continue;
				String op = Op_Code(parts[i]);
				if (op != null) {
					String bin_op = op;
					String hex_op = binary_to_hex(bin_op);

					if (!parts[1].equals("r")) { // if there is not ret
						if (parts[1].equals("0")) { // if rom0

							String im = decimal_to_hex(parts[2]); // immediate value = 12 bits
							String result = hex_op + im;

							if (parts[0].equals("MOV")) { // if there is MOV
								im = decimal_to_hex(parts[3]); // for MOV another ýmmediate
								result = result + im;
							}
							multi_rom0.add(result);
							// System.out.println(result);
						} else if (parts[1].equals("1")) { // if rom1
							String im = decimal_to_hex(parts[2]); // immediate value = 12 bits
							String result = hex_op + im;

							if (parts[0].equals("MOV")) { // if there is MOV
								im = decimal_to_hex(parts[3]); // for MOV another immediate
								result = result + im;
							}
							multi_rom1.add(result);
							// System.out.println(result);

						} else if (parts[1].equals("X")) { // if rom0 and rom1
							String im = decimal_to_hex(parts[2]); // immediate value = 12 bits
							String result = hex_op + im;
							multi_rom0.add(result);
							multi_rom1.add(result);
							// System.out.println(result);
						}

					}

				}

			}

		}
		// gives the output of rom0 to in a txt
		PrintStream multi_rom0_out = new PrintStream("./multi_rom0_out.txt");
		System.setOut(multi_rom0_out);

		System.out.println("# Core 0 ROM");
		System.out.println("v2.0 " + "raw");
		multi_rom0.forEach((n) -> System.out.print(n + " "));
		// gives the output of rom1 to in a txt
		PrintStream multi_rom1_out = new PrintStream("./multi_rom1_out.txt");
		System.setOut(multi_rom1_out);
		System.out.println("# Core 1 ROM");
		System.out.println("v2.0 " + "raw");
		multi_rom1.forEach((n) -> System.out.print(n + " "));

	}

	public static String Op_Code(String inst) { // instructions op code as binary

		String newNumber = null;
		if (inst.equals("BRZ"))
			newNumber = "0000";
		else if (inst.equals("BRN"))
			newNumber = "0001";
		else if (inst.equals("LDI"))
			newNumber = "0010";
		else if (inst.equals("LDM"))
			newNumber = "0011";
		else if (inst.equals("STR"))
			newNumber = "0100";
		else if (inst.equals("XOR"))
			newNumber = "0101";
		else if (inst.equals("NOT"))
			newNumber = "0110";
		else if (inst.equals("AND"))
			newNumber = "0111";
		else if (inst.equals("OR"))
			newNumber = "1000";
		else if (inst.equals("ADD"))
			newNumber = "1001";
		else if (inst.equals("SUB"))
			newNumber = "1010";
		else if (inst.equals("MUL"))
			newNumber = "1011";
		else if (inst.equals("DIV"))
			newNumber = "1100";
		else if (inst.equals("NEG"))
			newNumber = "1101";
		else if (inst.equals("LSL"))
			newNumber = "1110";
		else if (inst.equals("LSR"))
			newNumber = "1111";
		else if (inst.equals("MOV"))
			newNumber = "1";
		else
			return null;
		return newNumber;
	}

	public static String decimal_to_hex(String number) { // convert decimal to hex

		int i = Integer.parseInt(number);
		String hex = Integer.toString(i, 16);
		if (hex.length() == 1) // convert into 3 bits
			hex = "00" + hex;
		else if (hex.length() == 2) // convert into 3 bits
			hex = "0" + hex;

		return hex;
	}

	public static String binary_to_hex(String newNumber) { // convert binary to hex

		String hex = new BigInteger(newNumber, 2).toString(16);
		return hex;

	}

}
