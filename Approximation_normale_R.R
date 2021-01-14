#on lit tout le set de données
library(readr)
raw_data <- read_delim("~/R/raw_data.csv",";", escape_double = FALSE, col_names = FALSE, locale = locale(decimal_mark = ","), trim_ws = TRUE)
View(raw_data)

#Création du tableau
TAB = cbind(raw_data)
View(TAB)

#nombre de set de données
N <- length(TAB[1,])

#nombre de données par set
n <- length(TAB[,1])


#on construit les vecteurs des moyennes & écart-type échantillonaux par ligne
vect.mu = c()
vect.sigma = c()
for (i in 1:n){
  xi <- c(as.numeric(TAB[i,])) #on récupère le vecteur de la ligne actuelle
  vect.mu[i] = mean(xi) #on calcule sa moyenne échantillonale
  vect.sigma[i] = sd(xi) #on calcule son écart-type
}
View(vect.mu)
View(vect.sigma)

#on prend l'ancien tableau TAB, et on ajoute dans un vecteur toutes les valeurs de TAB centrées et réduites
#en faisant attention à ce que les valeurs soient contrées avec leur bon mu et réduites avec leur bon sigma
vect <- c() #Le vecteur est initialisé vide
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

#quartiles : n/4, déciles : n/10, fractiles : n/d
d = 200
#on créé le vecteur des fractiles échantillonaux
fractileE <- c()
#on créé le vecteur des fractiles théoriques
fractileT <- c()

#on calcule les fractiles échantillonaux et théoriques et on les ajoute dans leur vecteur respectif
for (i in 1:d) {
  fractileE[i] = vect[(i/d)*ndata]
  fractileT[i] = qnorm(i/d)
}
plot(fractileE)
lines(fractileT,col="red")


#Test statistique de normalité : Moyenne ET écart-type
#H0: µ = µ0 && sd = sd0
#H1: µ != µ0 || sd != sd0
#test bilatéral (two-sided) à 5%
library(OneTwoSamples)

x1 = vect
x2 = rnorm(ndata,mean = 0, sd = 1)

#tests sur la loi normale : symétrique donc inutile de calculer le quantile inférieur en bilatéral.
#test moyenne
mean_test2(x1,x2,sigma = c(sd(x1),sd(x2)))$Z
val.critique = qnorm(0.975) ; val.critique
mean_test2(x1,x2,sigma = c(sd(x1),sd(x2)))$p_value

#test variance (donc écart-type)
var_test2(x1,x2,mu = c(mean(x1),mean(x2)))$F
val.critique = qnorm(0.975) ; val.critique
var_test2(x1,x2,mu = c(mean(x1),mean(x2)))$P_value





