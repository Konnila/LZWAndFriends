Aiheena on datan pakkaus.

Ideana on toteuttaa Huffman koodaus sek� LZW tekstitiedostoille.
Huffman vaatii toteutukseensa ainakin listan, sek� oman implementaation 
prioriteettijonosta (Minimikeon). LZW enkoodaus vaatii ainakin 
key-value listan implementaation.

Datan pakkaus on sin�ns� mielenkiintoinen aihe. Valitsin sen osittain my�s 
sen takia, ett� se kuulostaa suhteellisen helpolta, eik� tarvitse mit��n
kovinkaan monimutkaisia tietorakenteita toteuttaa. Mutta niist�kin saa 
monimutkaisempia kun niit� valjastaa pakkauksen optimoinnin tarpeisiin.
Laajennettavaa siis on, jos aikaa j��.

Lopullinen ohjelma saa sy�tteekseen viitteen tekstitiedostoon, ja tiedon siit�
onko kyseess� enkoodaus- vai dekoodaus-operaatio. Tuloksena on joko dekoodattu
tai enkoodattu tekstitiedosto, riippuen sy�tteest�.

Aikavaativuustavoitteena on: Huffman enkoodaukselle n log n
			     LZW enkoodaukselle n



