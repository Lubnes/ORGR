// /*
//  * 
//  * 
//  * 
// 	////// Desenvolvido por Gustavo Geyer, Tobias Trein, Douglas Schlaatter e Luiza Nunes \\\\\\\\
//  ////// Matrículas: 19102825-7, 18200257-6, 20101146-7, 20106324-5	
//  * 
//  *  
//  *  
// */
// //

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class processador {
	

	static ArrayList<String> tipoI = new ArrayList();
	static ArrayList<String> tipoR = new ArrayList();
	static ArrayList<String> tipoJ = new ArrayList();
	static ArrayList<String> exe = new ArrayList();
public static void populaLista(){
	tipoI.add("ori");
	tipoI.add("bne");
	tipoR.add("addu");
	tipoR.add("and");
	tipoR.add("slt");
	tipoR.add("srl");
	tipoJ.add("j");
	exe.add("lw");
	exe.add("lui");
	exe.add("sw");
	
}

public static void update()
{
	for (int i = 0; i<regis.length;i++ ) 
	{
		System.out.print("| Registrador "+i+": " + regis[i]+" | ");
		if(i%2 == 1)
		{
			System.out.println();
		}
	}
	System.out.print("Area de Dados: ");
	for (Integer i : data) 
	{
		System.out.print(i + " ");
	}
	System.out.print("\n");
}
	

	//Momento de inicialização de variaveis e registradores
	static ArrayList<String> hexCodes = new ArrayList<String>();	
	static Converte converte = new Converte();
	static ArrayList<Integer> data = new ArrayList<Integer>();
	static ArrayList<String> hexInst = new ArrayList<String>();
	static ArrayList<String> mipsInst = new ArrayList<String>();
	Map<String,ArrayList<Integer>> dictI = new HashMap<String,ArrayList<Integer>>();
	
	

	//Registradores
	static int [] regis = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; // <- funciona como um dicionario

		public static void main(String[] args) throws FileNotFoundException 
		{
			consome_arquivo_hex();
		}
		
	public static void consome_arquivo_hex() throws FileNotFoundException { // Consome o arquivo com cod-objetos
		File hexInput = new File("entrada.txt");
		Scanner input = new Scanner(hexInput);
		input.useDelimiter("\n");
		
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
		for (String s : hexInst) {
			System.out.println(s);
			s = decodifica(converte.hex_to_bin(s));
			mipsInst.add(s);
			System.out.println(s);
		}
		populaLista();
		String com;

		String inter2;
		ArrayList<String> arg= new ArrayList();
		for (int i = 0;i< mipsInst.size();i++ ) {
			System.out.println("------------------------------------------");
			System.out.println("Instrução executada: "+mipsInst.get(i));
			com = mipsInst.get(i).split(" ")[0];
			inter2 = mipsInst.get(i).split(" ")[1];
			for (String s : inter2.split(",")) 
			{
				arg.add(s);
			}
			
			//trabalhando com tipo
			if(tipoI.contains(com)){
				int rs= Integer.parseInt(arg.get(0).replace("$",""));;
				int rt= Integer.parseInt(arg.get(1).replace("$",""));;
				int offset= Integer.parseInt(arg.get(2));
				//System.out.println(rs+rt+offset);
				switch (com) {
					case "ori":
					 	regis[rt] = rs | offset;
						break;
				
					case "bne":
						if(rs!=rt)
							i = offset;
						break;
				}
			}
			else if(tipoR.contains(com)){
				int rs= Integer.parseInt(arg.get(1).replace("$",""));
				int rt= Integer.parseInt(arg.get(2).replace("$",""));;
				int rd= Integer.parseInt(arg.get(3).replace("$",""));
				//System.out.println(rs+rt+rd);
				
				switch (com) {
					case "addu":
						regis[rd] = rs + rt;
						break;

					case "and":
						regis[rd] = rs & rt;
						break;

					case "slt":
						regis[rd] = rs < rt ? 1 : 0;
						break;

					case "srl":
						regis[rd] = rt >> rs;
						break;
				}
			}
			else if(tipoJ.contains(com)){
				int target = Integer.parseInt(arg.get(1).replace("$",""));;
				i = target;
			}
			else{
				switch (com) {
					case "lw": 
						break;

					case "lui":
						break;

					case "sw":
						break;
				}
			}
			update();
		}
		
	}
	
	
	
	public static String decodifica(String binCode) { // Desmonta uma linha em binario e transforma em codigo mips
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
				
				endLine = "srl $"+regB+",$"+regA+","+var1;
				break;
			case 8: // jr
				regA = converte.bin_to_dec(binCode.substring(6, 11));
				
				endLine = "jr $"+regA;
				break;
				
			case 33: // addu
				regA = converte.bin_to_dec(binCode.substring(6, 11)); //rs
				regB = converte.bin_to_dec(binCode.substring(11, 16)); //rt
				var1 = converte.bin_to_dec(binCode.substring(16, 21)); //rd
				
				endLine = "addu $"+var1+",$"+regA+",$"+regB;
				break;
				
			case 36: // and
				regA = converte.bin_to_dec(binCode.substring(6, 11)); //rs
				regB = converte.bin_to_dec(binCode.substring(11, 16)); //rt
				var1 = converte.bin_to_dec(binCode.substring(16, 21)); //rd
				
				endLine = "and $"+var1+",$"+regA+",$"+regB;
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
			
			endLine = "j 0x"+conv;
			break;
			
		case 4: // beq
			regA = converte.bin_to_dec(binCode.substring(6, 11)); // rs
			regB = converte.bin_to_dec(binCode.substring(11, 16)); // rt
			conv = binCode.substring(16, 32); // offset
			
			conv = converte.bin_to_hex(converte.fill_hex(conv).concat(conv));
			
			endLine = "beq $"+regA+",$"+regB+",0x"+conv;
			break;
			
		case 5: // bne
			regA = converte.bin_to_dec(binCode.substring(6, 11)); // rs
			regB = converte.bin_to_dec(binCode.substring(11, 16)); // rt
			conv = binCode.substring(16, 32); // offset
			
			conv = converte.bin_to_hex(converte.fill_hex(conv).concat(conv));
			
			endLine = "bne $"+regA+",$"+regB+",0x"+conv;
			break;
			
		case 10: // slti 
			regA = converte.bin_to_dec(binCode.substring(6, 11));
			regB = converte.bin_to_dec(binCode.substring(11, 16));
			var1 = converte.bin_to_dec(binCode.substring(16, 32));
			
			endLine = "slti $"+regB+",$"+regA+","+var1;
			break;
			
		case 13: // ori
			regA = converte.bin_to_dec(binCode.substring(6, 11));
			regB = converte.bin_to_dec(binCode.substring(11, 16));
			var1 = converte.bin_to_dec(binCode.substring(16, 32));
			
			endLine = "ori $"+regB+",$"+regA+","+var1;
			break;
			
		case 35: // lw
			regA = converte.bin_to_dec(binCode.substring(6, 11)); // rs
			regB = converte.bin_to_dec(binCode.substring(11, 16)); // rt
			var1 = converte.bin_to_dec(binCode.substring(16, 32)); // offset
			
			endLine = "lw $"+regB+","+var1+"($"+regA+")";
		
		case 43: // sw
			regA = converte.bin_to_dec(binCode.substring(6, 11)); // rs
			regB = converte.bin_to_dec(binCode.substring(11, 16)); // rt
			var1 = converte.bin_to_dec(binCode.substring(16, 32)); // offset
		
			endLine = "sw $"+regB+","+var1+"($"+regA+")";
		}
		

		return endLine;
	}
	
}














