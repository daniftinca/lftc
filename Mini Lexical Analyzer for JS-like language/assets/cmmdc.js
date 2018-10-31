function cmmdc(a, b) {
    var aux = 0;
    if (a === 0) {
        return b;
    }
    if (b === 0) {
        return a;
    }

    while (b > 0) {
        aux = a;
        a = b;
        b = aux % b;
    }
    return a;
}