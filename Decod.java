/*
 * 
 * 
 * 
	////// Desenvolvido por Gustavo Geyer e Luiza Nunes (ambos colorados) \\\\\\\\
 * 
 *  
 *  
*/

package orgarq;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Decod {
	ArrayList<String> mipsLines = new ArrayList<String>();
	Converte converte = new Converte();

	int gPC = 0x00400000;

	public void consome_arquivo_asm() throws FileNotFoundException { // Consome o arquivo com codigo mips
		File asmInput = new File("C:\\Users\\Admin\\Documents\\.codeing\\T2 orgarq\\mips.asm");
		Scanner input = new Scanner(asmInput);
		input.useDelimiter("\n");

		if (asmInput.exists() && !asmInput.isDirectory()) {
			try {
				System.out.println("\n Lendo arquivo...");
				while (input.hasNextLine()) {
					mipsLines.add(input.nextLine().replaceAll(" ", ""));
				}
			} catch (Exception e) {
				System.out.println("\n Erro na leitura do arquivo.");
				System.out.println(e);
			} finally {
			}
		} else {
			System.out.println("\n Arquivo nao encontrado.");
		}
		input.close();
	}

	public long[] parse_regs(String line, int regs) { // Parsing dos registradores e valores de uma linha MIPS
		long[] vet = new long[regs];
		String lineAux = line;

		if (line.contains("beq") || line.contains("bne") || line.contains("ori") || line.contains("srl")
				|| line.contains("slti")) {

			String[] auxVet = lineAux.split(",");
			lineAux = auxVet[auxVet.length - 1];
			System.out.println(lineAux);

			String[] valores = line.replaceAll("[^0-9,]", "").split(",");
			for (int i = 0; i < regs - 1; i++) {
				vet[i] = Long.parseLong(valores[i]);
				System.out.println(valores[i]);
			}
			vet[regs - 1] = Long.decode(lineAux);
		} else if (line.contains("add") || line.contains("and") || line.contains("jr")) {
			String[] valores = line.replaceAll("[^0-9,]", "").split(",");
			for (int i = 0; i < regs; i++) {
				vet[i] = Integer.parseInt(valores[i]);
			}
		} else if (line.contains("lw")) {
			vet[0] = Integer.parseInt(line.replaceAll("[^0-9,abcdef]", "").split(",")[0]);
			int v = line.indexOf(",");
			vet[1] = converte.hex_to_dec(line.substring(v + 3, v + 10));
			vet[2] = Integer.parseInt(line.substring(v + 13, line.length() - 1));

		} else {
			vet[0] = converte.hex_to_dec(line.substring(5));
		}
		return vet;

	}

	public String codifica(int index) { // Pega uma linha de codigo MIPS e monta um cod. objeto
		String binLine;
		String endLine = "";
		String instr = mipsLines.get(index);

		gPC = gPC + 4;

		if (instr.contains("jr")) {
			long[] regs = parse_regs(instr, 1);

			binLine = converte.dec_to_bin(0, 6) + converte.dec_to_bin(regs[0], 5) + converte.dec_to_bin(0, 16)
					+ converte.dec_to_bin(8, 5);

			endLine = "0x" + converte.bin_to_hex(binLine);
		} else if (instr.contains("srl")) {
			long[] regs = parse_regs(instr, 3);

			binLine = converte.dec_to_bin(0, 6) // opcode
					+ converte.dec_to_bin(0, 5) // rs
					+ converte.dec_to_bin(regs[1], 5) // rt
					+ converte.dec_to_bin(regs[0], 5) // rd
					+ converte.dec_to_bin(regs[2], 5) // shamt
					+ converte.dec_to_bin(2, 6); // funct

			endLine = "0x" + converte.bin_to_hex(binLine);
		} else if (instr.contains("slti")) {
			long[] regs = parse_regs(instr, 3);

			binLine = converte.dec_to_bin(10, 6) + converte.dec_to_bin(regs[1], 5) + converte.dec_to_bin(regs[0], 5)
					+ converte.dec_to_bin(regs[2], 16);

			endLine = "0x" + converte.bin_to_hex(binLine);
		} else if (instr.contains("ori")) {
			long[] regs = parse_regs(instr, 3);

			binLine = converte.dec_to_bin(13, 6) + converte.dec_to_bin(regs[1], 5) + converte.dec_to_bin(regs[0], 5)
					+ converte.dec_to_bin(regs[2], 16);

			endLine = "0x" + converte.bin_to_hex(binLine);

		} else if (instr.contains("lw")) {
			long[] regs = parse_regs(instr, 3);

			binLine = converte.dec_to_bin(36, 6) + converte.dec_to_bin(regs[0], 5) + converte.dec_to_bin(regs[2], 5)
					+ converte.dec_to_bin(regs[1], 16);

			endLine = "0x" + converte.bin_to_hex(binLine);
		} else if (instr.contains("add")) {
			long[] regs = parse_regs(instr, 3);
			binLine = converte.dec_to_bin(0, 6) + converte.dec_to_bin(regs[1], 5) + converte.dec_to_bin(regs[2], 5)
					+ converte.dec_to_bin(regs[0], 5) + converte.dec_to_bin(0, 5) + converte.dec_to_bin(32, 6);

			endLine = "0x" + converte.bin_to_hex(binLine);
		} else if (instr.contains("and")) {
			long[] regs = parse_regs(instr, 3);

			binLine = converte.dec_to_bin(0, 6) + converte.dec_to_bin(regs[1], 5) + converte.dec_to_bin(regs[2], 5)
					+ converte.dec_to_bin(regs[0], 5) + converte.dec_to_bin(0, 5) + converte.dec_to_bin(36, 6);

			endLine = "0x" + converte.bin_to_hex(binLine);
		} else if (instr.contains("beq")) {
			long[] regs = parse_regs(instr, 3);

			String address = converte.dec_to_bin(regs[2], 32).substring(16);
			System.out.println(instr);
			System.out.println(address);

			binLine = converte.dec_to_bin(4, 6) + converte.dec_to_bin(regs[0], 5) + converte.dec_to_bin(regs[1], 5)
					+ address;

			endLine = "0x" + converte.bin_to_hex(binLine);
		} else if (instr.contains("bne")) {
			long[] regs = parse_regs(instr, 3);

			long pc = converte.hex_to_dec("00400000");
			pc = pc + 4;

			String address = converte.dec_to_bin(regs[2], 32);
			long convAddress = converte.bin_to_dec(address);
			convAddress = convAddress << 2;
			address = converte.dec_to_bin((convAddress + pc), 16);

			binLine = converte.dec_to_bin(5, 6) + converte.dec_to_bin(regs[0], 5) + converte.dec_to_bin(regs[1], 5)
					+ address;

			endLine = "0x" + converte.bin_to_hex(binLine);
		} else { // j

			// int instr_count = index+1 - label;
			long[] regs = parse_regs(instr, 1);

			long pc = converte.hex_to_dec("00400000");
			pc = pc + 4;

			regs[0] = regs[0] >> 2;

			String address = converte.dec_to_bin(regs[0], 28);
			String pcBin = converte.dec_to_bin(pc, 32);

			address.concat(pcBin.substring(0, 4));

			long finalAddress = converte.bin_to_dec(address);
			address = converte.dec_to_bin(finalAddress, 26);

			binLine = converte.dec_to_bin(2, 6) + address;

			endLine = "0x" + converte.bin_to_hex(binLine);
		}
		return endLine;
	}
}
