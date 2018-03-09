/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PVAI.util;

import ai.core.AI;
import java.util.ArrayList;

/**
 *
 * @author rubens
 */
public class Permutation {
    //numero da permutacao atual
		private static int cont=0; 
		
		//armazena a permutacao corrente
		private static char[] p;
		
		//armazena as permutações calculadas
		private static ArrayList< ArrayList<Integer>> itens = new ArrayList<>();
		
		
		/**
		 * metodo principal: recebe o vetor cujos elementos que serao permutados
		 * @param vet
		 */
		private static void permutar(char [] vet, int len) {
			
			//p = new char[vet.length];
			p = new char[len];
			permuta(vet,0);
		}
				

		/**
		 * método recursivo que implementa as permutacoes
		 * @param vet
		 * @param n
		 */
		private static void permuta(char []vet, int n) {
			
			if (n==p.length) {
				cont++;
				//imprime();
				gravar();
								
			} else {
						
				for (int i=0; i < vet.length; i++) {
				
					boolean achou = false;
				
					for (int j = 0; j < n; j++) {
					
						if (p[j]==vet[i]) achou = true;
					}
				
					if (!achou) {
						
						p[n] = vet[i];
						permuta(vet,n+1);
					}
					
				} //--for
				
			} //--if/else
			
		} //--permuta
		
		
		
		/** grava a permutacao corrente  no arrayList de itens */
		private static void gravar() {
			ArrayList<Integer> temp = new ArrayList<>();
			
			for (int i=0; i < p.length; i++) {
				temp.add( Integer.decode( Character.toString(p[i])));
			}
			
			itens.add(temp);
		}
		
		
		/** imprime a permutacao corrente */
		private static void imprime() {
			
			System.out.println();
			System.out.print("(" + cont + ") : ");
			for (int i=0; i < p.length; i++) {
				System.out.print(p[i] + " ");
			}
			
		} //--imprime
		

		/**
                 * método que retorna qual os elementos que será utilizados para teste com base no indice.
                 * @param nArgChoose indice dos elementos selecionados
                 * @return 
                 */
		public static ArrayList<Integer> getPermutation(int nArgChoose) {
			
			char v[] = {'0','1','2', '3', '4', '5', '6', '7', '8'};
			Permutation.permutar(v, 1);
			Permutation.permutar(v, 3);
			Permutation.permutar(v, 4);
			Permutation.permutar(v, 5);
			
			
			//System.out.println("Total de elementos= "+ itens.size());
                        //System.out.println("Elemento desejado= "+ itens.get(nArgChoose));
                        
			/*
			for (ArrayList<Integer> t : itens) {
				System.out.println(t);
			}
			*/
                        
                        return itens.get(nArgChoose);
		}
}
