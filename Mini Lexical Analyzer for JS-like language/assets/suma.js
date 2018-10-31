function sumaNNumere() {
    var suma = 0;
    var nextNumber = prompt();
    while (nextNumber != '') {

        suma = suma + parseInt(nextNumber);
        nextNumber = prompt();
    }
    return suma;
}


