// /*
//  * 
//  * 
//  * 
// 	////// Desenvolvido por Gustavo Geyer e Luiza Nunes (ambos colorados) \\\\\\\\
//  * 
//  *  
//  *  
// */
// //esse é o que vamos usarrr
package orgarq;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Cod {
	
	ArrayList<String> hexCodes = new ArrayList<String>();	
	Converte converte = new Converte();
	ArrayList<Integer> data = new ArrayList<Integer>();
	ArrayList<String> hexaInst = new ArrayList<String>();
	Map<String,ArrayList<Integer>> dictI = new HashMap<String,ArrayList<Integer>>();
	/*
	#RegDest,WriteReg,ALUSrc,PCSrc,MemtoReg,Mem Read, Mem Write,
# branch, ALUOp1, ALUp0,Jump

#tipo i -> 0,0,0,1,mem write= 0,alu src =1,
sinais = {
    'j'   :  [0,0,0,0,0,0,,1],
    'srl' :  [1,1,0,0,0,0,1,0,0], #tipo r
    'slti':  [,,,,,,],
    'addu' : [1,1,0,0,0,0,1,0,0],#TODO conferir
    'lw'  :  [0,1,1,1,1,0,0,,0],
    'beq' :  [0,0,0,0,0,0,1,,0],
    'bne' :  [,,,,,,],
    'ori' :  [,,,,,,],
    'and' :  [1,1,0,0,0,0,1,0,0]#tipo r
}

tipoi = ['slti','ori','bne','beq']
tipor = ['addu','and','srl']
tipoj = ['j','jr']
exe  = ['lw',lui,sw]

	*/
	
	public void consome_arquivo_hex() throws FileNotFoundException { // Consome o arquivo com cod-objetos
		File hexInput = new File("C:\\Users\\T-Gamer\\Desktop\\Faculdade\\facul 3 sem\\Organização e Arquitetura de C. I\\Trab 3 Real\\ORGR\\hex.txt");
		Scanner input = new Scanner(hexInput);
		input.useDelimiter("\n");
		dictI.put(key, value);


		if(hexInput.exists() && !hexInput.isDirectory()) {
			try {
				System.out.println("\n Lendo arquivo...");
				hexCodes.add(input.nextLine());
				while(input.hasNextLine()) {
					hexCodes.add(input.nextLine().substring(2).replaceAll(" ", ""));
				}
				String[] aux = hexCodes.remove(0).split(" ");
				for (String e : aux){
					data.add(Integer.parseInt(e));
				}
				hexInst = hexCodes;
			}
			catch(Exception e) {
				System.out.println("\n Erro na leitura do arquivo.");
				System.out.println(e);
			}
			finally {}
		}
		else {System.out.println("\n Arquivo nao encontrado."); 
		}
		input.close();
		for (int i : data) {
			System.out.println(i);
		}
		for (String s : inst) {
			System.out.println(s);
			s = decodifica(converte.hex_to_bin(s));
			System.out.println(s);
			//dictI.put(key, value);
			//String[] s = dictI.get(key);
		}
		
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
