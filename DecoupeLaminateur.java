import java.util.*;

public class DecoupeLaminateur {
	public static void main(String[] args) {
		int nbLaizeDiff = 0;
		int laizeBobineMere = 1860;
		int trimMinGauche = 30;
		int trimMinDroit = 30;
		int trimSecuGauche = 10;
		int trimSecuDroit = 10;
		int nbCouteaux = 15; /**15 par defaut*/
		int laizeProd = laizeBobineMere - (trimMinGauche + trimMinDroit);
		int laizeUtile = laizeProd;
		int countBobine = 1; //au moins 1 bobine sera utilisee
		String planDeCoupe = "";
		String planBobineMere = "";
		String pertes = "";
		int nbBobinotParBobine = nbCouteaux - 1;


		System.out.println("Calcul de patron de coupe");
		/*System.out.println(" Nombres de laize différentes ?");
		Scanner sc = new Scanner(System.in);
		nbLaizeDiff = sc.nextInt();
		int[] orderLaize = new int[nbLaizeDiff];
		int[] orderNbre = new int[nbLaizeDiff];
		for (int i=0; i<nbLaizeDiff; i++){
			System.out.println("Entrez laize bobinot");
			orderLaize[i] = sc.nextInt();
			System.out.println("Entrez nombre de bobinots pour : " + orderLaize[i]);
			orderNbre[i] = sc.nextInt();
		}*/
		int[] orderLaize = new int[]{92, 94, 112, 178, 189, 528};
		int[] orderNbre = new int[]{18, 15, 20, 13, 15, 14};
		nbLaizeDiff = 6;
		/**recupere nbre total de bobinot*/
		int nbBobinot = 0;
		for (int i = 0; i<nbLaizeDiff; i++){
			nbBobinot += orderNbre[i];
		}
		/**creer un tableau avec tous les bobinots*/
		int[] listOrderBobinot = new int[nbBobinot];
		int n = 0;/**sert a incrementer position orderTotale*/

		/**remplit le tableau listOrderBobinot*/
		for(int j=0; j<orderNbre.length; j++){
			int coeff = orderNbre[j];
			for(int k=0; k<coeff; k++){
				listOrderBobinot[n] = orderLaize[j];
				n++;/**incremente position liste orderTotale*/
			};
		};
		/**remplit un tableau pour utiliser ou non*/
		int[] usedBobinot = new int[nbBobinot];
		for(int k = 0; k<usedBobinot.length; k++){
			usedBobinot[k] = 0;
		};
		/**rangement sens decroissant*/
		int n1, n2, swap;
		for (n1 = 0; n1 < (nbBobinot - 1); n1++){
			for(n2 = 0; n2 < (nbBobinot - n1 - 1); n2++){
				if (listOrderBobinot[n2] < listOrderBobinot[n2 + 1]){ /**pour croissant utilise >*/
				swap = listOrderBobinot[n2];
				listOrderBobinot[n2] = listOrderBobinot[n2+1];
				listOrderBobinot[n2+1] = swap; 
				}
			}
		}
		int countBobinotParBobine = 0;
		int countBobinot = 0;
		int nbBobinotOrder = listOrderBobinot.length;
		//System.out.println("bobinot le plus petit utilisable " + minLaizeOrder(listOrderBobinot, usedBobinot));

		/**initialisation plan de coupe*/
		planDeCoupe += "\n" + "        bobine : " + countBobine + ")";
		planBobineMere += "bobine " + countBobine + "\n";
		for(int i=0; i<nbBobinot; i++){
			if(countBobinotParBobine < nbBobinotParBobine){
				if((laizeUtile - listOrderBobinot[i] > 0) && (usedBobinot[i] == 0)){
					laizeUtile -= listOrderBobinot[i];
					planDeCoupe += " " + listOrderBobinot[i];
					usedBobinot[i] = 1;
					countBobinot++;
					countBobinotParBobine++;
					for (int k = 0; k < nbBobinot; k++){
						if (countBobinotParBobine < nbBobinotParBobine){
							if ((laizeUtile - listOrderBobinot[k] > 0) && (usedBobinot[k] == 0)){
								laizeUtile -= listOrderBobinot[k];
								planDeCoupe += " " + listOrderBobinot[k];
								usedBobinot[k] = 1;
								countBobinot++;
								countBobinotParBobine++;
							}
						}
					}
				}
				else if ((laizeUtile - listOrderBobinot[i] > 0) && (usedBobinot[i] == 1)){
					continue;
				}
				else{/**si laize trop petite*/
					pertes += "de bobine " + countBobine + " : " + laizeUtile + ",\n";
					i--;
					countBobine++;
					laizeUtile = laizeProd;
					planBobineMere += "bobine " + countBobine + "\n";
					countBobinotParBobine = 0;
					planDeCoupe += "\n" + "        bobine : " + countBobine + ")";
				}
			}
			else if ((countBobinotParBobine == nbBobinotParBobine) && ((laizeUtile - trimSecuGauche) - minLaizeOrder(listOrderBobinot, usedBobinot) > 0)){
				/**on verifie que 14 bobinots ont été coupé et que la laize utile
				est superieur à un petit bobinot disponible*/
				//System.out.println("nb bobinot coupé " + countBobinotParBobine+ " sur bobine " + countBobine);
				//System.out.println("bobinot utilisable " + minLaizeOrder(listOrderBobinot, usedBobinot));
				/**si 14 bobinots, on refait une boucle sur la meme bobine*/
				planBobineMere += "bobine " + countBobine + "\n";
				countBobinotParBobine = 0;
				planDeCoupe += "\n" + "recoupe bobine : " + countBobine + ")";
				/**on rajoute la trim droite qui n'a pas été coupée*/
				laizeUtile += trimMinDroit;
				/**on enleve la trim gauche de secu pour la recoupe*/
				laizeUtile -= trimSecuGauche;
				/**on enleve la trim droite encollée*/
				laizeUtile -= trimMinDroit;
				for(int j = 0; j < nbBobinot; j++){
					if(countBobinotParBobine < nbBobinotParBobine){
						if ((laizeUtile - listOrderBobinot[j] > 0) && (usedBobinot[j] == 0)){
							laizeUtile -= listOrderBobinot[j];
							planDeCoupe += " " +listOrderBobinot[j];
							usedBobinot[j] = 1;
							countBobinot++;
							countBobinotParBobine++;
							for (int l = 0; l < nbBobinot; l++){
								if (countBobinotParBobine < nbBobinotParBobine){
									if ((laizeUtile - listOrderBobinot[l] > 0) && (usedBobinot[l] == 0)){
										laizeUtile -= listOrderBobinot[l];
										planDeCoupe += " " + listOrderBobinot[l];
										usedBobinot[l] = 1;
										countBobinot++;
										countBobinotParBobine++;
									}
								}
							}
						}
						else if (usedBobinot[j] == 1){
							continue;
						}
						else{/**si laize trop petite*/
							if(i==nbBobinot){
								/**condition obligatoire pour forcer la boucle a aller jusqu'au bout*/
								/**on rajoute la trim gauche de secu au perte à la redecoupe*/
								laizeUtile -= trimSecuGauche;
								pertes += "de bobine "+ countBobine + " : " + laizeUtile + " incluant trim gauche de "+ trimSecuGauche +" à la recoup,\n";
								i--;
								countBobine++;
								laizeUtile = laizeProd;
								planBobineMere += "bobine " + countBobine + "\n";
								countBobinotParBobine = 0;
								planDeCoupe += "\n" + "        bobine : " + countBobine + ")";
							}
						}
					}/**fin de verif nb bobinot*/
				}/**fin boucle for*/
			}
			else{/**si pas de bobinot utilisable et 14 bobinots*/
				//System.out.println(countBobinotParBobine + " bobinots sur run " + countBobine + " et plus de place");
				countBobinotParBobine = 0;
				pertes += "laize insuffisante pour la recoupe\nsachant qu'on enlève encore " + trimSecuGauche + " en\nlisiere ";
			}
		}/**fin boucle for generale*/
		/**recupere la derniere perte*/
		pertes += "de bobine " + countBobine + " : " + laizeUtile + ".";
		System.out.println("Nombre bobine mere : " + countBobine + " de laize " + laizeBobineMere);
		System.out.println("Total bobinots commandés " + nbBobinotOrder);
		System.out.println("Total bobinots produits " + countBobinot);
		System.out.println("\nPlan de coupe :\n" + planDeCoupe);
		System.out.println("\nBobines utilisees :\n" + planBobineMere);
		System.out.println("Chutes restantes (hors chutes gauche " + trimMinGauche + "\n et droite " + trimMinDroit + " :\n" + pertes);
		System.out.println("Nb de couteaux : \n" + nbCouteaux + " soit " + nbBobinotParBobine + " bobinots maxi par run.");
		//System.out.println("bobinot le plus petit utilisable " + minLaizeOrder(listOrderBobinot, usedBobinot));
	}
	public static int minLaizeOrder(int[] listOrderBobinot, int[] usedBobinot){
		int result = 0;
		int nbBobinot = listOrderBobinot.length;
		for(int i=0; i<nbBobinot; i++){
			for (int j=0; j<nbBobinot; j++){
				if((listOrderBobinot[i] < listOrderBobinot[j]) && (usedBobinot[i] == 0)){
					result = listOrderBobinot[i];
				}	
			}
		}
		return result;
	}
}
