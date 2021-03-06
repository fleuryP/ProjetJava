#on lit tout le set de donn�es
library(readr)
raw_data <- read_delim("~/R/raw_data.csv",";", escape_double = FALSE, col_names = FALSE, locale = locale(decimal_mark = ","), trim_ws = TRUE)
View(raw_data)

#Cr�ation du tableau
TAB = cbind(raw_data)
View(TAB)

#nombre de set de donn�es
N <- length(TAB[1,])

#nombre de donn�es par set
n <- length(TAB[,1])


#on construit les vecteurs des moyennes & �cart-type �chantillonaux par ligne
vect.mu = c()
vect.sigma = c()
for (i in 1:n){
  xi <- c(as.numeric(TAB[i,])) #on r�cup�re le vecteur de la ligne actuelle
  vect.mu[i] = mean(xi) #on calcule sa moyenne �chantillonale
  vect.sigma[i] = sd(xi) #on calcule son �cart-type
}
View(vect.mu)
View(vect.sigma)

#on prend l'ancien tableau TAB, et on ajoute dans un vecteur toutes les valeurs de TAB centr�es et r�duites
#en faisant attention � ce que les valeurs soient contr�es avec leur bon mu et r�duites avec leur bon sigma
vect <- c() #Le vecteur est initialis� vide
for(i in 1:n){
  for(j in 1:N){
    vect[(i-1)*N + j] <- ((TAB[i,j] - vect.mu[i]) / vect.sigma[i])
  }
}
View(vect)

#on trie par ordre croissant le vecteur (pour ensuite trouver les fractiles)
vect <- sort(vect)
ndata = length(vect)

hist(vect,nclass = 60, freq = FALSE)
curve(dnorm(x), from = -5, to = 5, col = "green",add=TRUE)

#quartiles : n/4, d�ciles : n/10, fractiles : n/d
d = 200
#on cr�� le vecteur des fractiles �chantillonaux
fractileE <- c()
#on cr�� le vecteur des fractiles th�oriques
fractileT <- c()

#on calcule les fractiles �chantillonaux et th�oriques et on les ajoute dans leur vecteur respectif
for (i in 1:d) {
  fractileE[i] = vect[(i/d)*ndata]
  fractileT[i] = qnorm(i/d)
}
plot(fractileE)
lines(fractileT,col="red")


#Test statistique de normalit� : Moyenne ET �cart-type
#H0: � = �0 && sd = sd0
#H1: � != �0 || sd != sd0
#test bilat�ral (two-sided) � 5%
library(OneTwoSamples)

x1 = vect
x2 = rnorm(ndata,mean = 0, sd = 1)

#tests sur la loi normale : sym�trique donc inutile de calculer le quantile inf�rieur en bilat�ral.
#test moyenne
mean_test2(x1,x2,sigma = c(sd(x1),sd(x2)))$Z
val.critique = qnorm(0.975) ; val.critique
mean_test2(x1,x2,sigma = c(sd(x1),sd(x2)))$p_value

#test variance (donc �cart-type)
var_test2(x1,x2,mu = c(mean(x1),mean(x2)))$F
val.critique = qnorm(0.975) ; val.critique
var_test2(x1,x2,mu = c(mean(x1),mean(x2)))$P_value





