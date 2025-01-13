import csv

for filename in ('birch3', 's1', 's3'):
    # Lire le fichier texte
    with open('data/2d/' + filename + '.txt', 'r') as f:
        lignes = f.readlines()

    # Écrire dans un fichier CSV
    with open('data/2d/' + filename + '.csv', 'w', newline='') as csvfile:
        csvwriter = csv.writer(csvfile)
        for ligne in lignes:
            # Supprimer les espaces multiples et diviser les colonnes
            csvwriter.writerow(ligne.split())
