function getNoun(number, one = '', few = '', many = '') {
    let n = Math.abs(number)
    n %= 100
    if (n >= 5 && n <= 20) {
        return many
    }
    n %= 10
    if (n === 1) {
        return one
    }
    if (n >= 2 && n <= 4) {
        return few
    }
    return many
}

export default getNoun
