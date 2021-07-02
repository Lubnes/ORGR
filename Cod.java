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

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
public class Cod {
	
	ArrayList<String> hexCodes = new ArrayList<String>();	
	Converte converte = new Converte();
	
	public void consome_arquivo_hex() throws FileNotFoundException { // Consome o arquivo com cod-objetos
		File hexInput = new File("C:\\Users\\Admin\\Documents\\.codeing\\T2 orgarq\\hex.txt");
		Scanner input = new Scanner(hexInput);
		input.useDelimiter("\n");
		
		if(hexInput.exists() && !hexInput.isDirectory()) {
			try {
				System.out.println("\n Lendo arquivo...");
				while(input.hasNextLine()) {
					hexCodes.add(input.nextLine().substring(2).replaceAll(" ", ""));
				}
			}
			catch(Exception e) {
				System.out.println("\n Erro na leitura do arquivo.");
				System.out.println(e);
			}
			finally {}
		}
		else {System.out.println("\n Arquivo nao encontrado."); }
		input.close();
	}
	
	
	
	public String decodifica(String binCode) { // Desmonta uma linha em binario e transforma em codigo mips
		String endLine = "";
		long opcodenum = converte.bin_to_dec(binCode.substring(0, 6));
		int opcode = (int) opcodenum;
		long regA;
		long regB;
		long var1;
		String conv;
		
		switch(opcode) {
		
		case 0:
			long funct = converte.bin_to_dec(binCode.substring(26, 32));
			int funcInt = (int) funct;
			switch(funcInt) {
			
			case 2: //srl
				regA = converte.bin_to_dec(binCode.substring(11, 16)); //rt
				regB = converte.bin_to_dec(binCode.substring(16, 21)); //rd
				var1 = converte.bin_to_dec(binCode.substring(21, 26)); //shamt
				
				endLine = "srl   $"+regB+", $"+regA+", "+var1;
				break;
			case 8: // jr
				regA = converte.bin_to_dec(binCode.substring(6, 11));
				
				endLine = "jr   $"+regA;
				break;
				
			case 32: // add
				regA = converte.bin_to_dec(binCode.substring(6, 11)); //rs
				regB = converte.bin_to_dec(binCode.substring(11, 16)); //rt
				var1 = converte.bin_to_dec(binCode.substring(16, 21)); //rd
				
				endLine = "add   $"+var1+", $"+regA+", $"+regB;
				break;
				
			case 36: // and
				regA = converte.bin_to_dec(binCode.substring(6, 11)); //rs
				regB = converte.bin_to_dec(binCode.substring(11, 16)); //rt
				var1 = converte.bin_to_dec(binCode.substring(16, 21)); //rd
				
				endLine = "and   $"+var1+", $"+regA+", $"+regB;
				break;
			}
			break;
		
		case 2: // j
			String pc = converte.hex_to_bin("00400000");
			conv = binCode.substring(6, 32);
			
			var1 = converte.bin_to_dec(conv);
			var1 = var1 << 2;
			
			conv = converte.dec_to_bin(var1, 28);
			
			conv.concat(pc.substring(0, 4));
			
			conv = converte.bin_to_hex(conv);
			
			endLine = "j   0x"+conv;
			break;
			
		case 4: // beq
			regA = converte.bin_to_dec(binCode.substring(6, 11)); // rs
			regB = converte.bin_to_dec(binCode.substring(11, 16)); // rt
			conv = binCode.substring(16, 32); // offset
			
			conv = converte.bin_to_hex(converte.fill_hex(conv).concat(conv));
			
			endLine = "beq   $"+regA+", $"+regB+", 0x"+conv;
			break;
			
		case 5: // bne
			regA = converte.bin_to_dec(binCode.substring(6, 11)); // rs
			regB = converte.bin_to_dec(binCode.substring(11, 16)); // rt
			conv = binCode.substring(16, 32); // offset
			
			conv = converte.bin_to_hex(converte.fill_hex(conv).concat(conv));
			
			endLine = "bne   $"+regA+", $"+regB+", 0x"+conv;
			break;
			
		case 10: // slti 
			regA = converte.bin_to_dec(binCode.substring(6, 11));
			regB = converte.bin_to_dec(binCode.substring(11, 16));
			var1 = converte.bin_to_dec(binCode.substring(16, 32));
			
			endLine = "slti   $"+regB+", $"+regA+", "+var1;
			break;
			
		case 13: // ori
			regA = converte.bin_to_dec(binCode.substring(6, 11));
			regB = converte.bin_to_dec(binCode.substring(11, 16));
			var1 = converte.bin_to_dec(binCode.substring(16, 32));
			
			endLine = "ori   $"+regB+", $"+regA+", "+var1;
			break;
			
		case 35: // lw
			regA = converte.bin_to_dec(binCode.substring(6, 11)); // rs
			regB = converte.bin_to_dec(binCode.substring(11, 16)); // rt
			var1 = converte.bin_to_dec(binCode.substring(16, 32)); // offset
			
			endLine = "lw   $"+regB+", "+var1+"($"+regA+")";
		}
		
		return endLine;
	}
	
}
